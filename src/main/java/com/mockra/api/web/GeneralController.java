package com.mockra.api.web;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mockra.api.util.RequestContext;
import com.mockra.api.util.Result;
import com.mockra.api.model.EndpointVariant;
import com.mockra.api.model.HttpMethod;
import com.mockra.api.model.ResponseDef;
import com.mockra.api.resolver.EndpointResolutionError;
import com.mockra.api.resolver.EndpointResolver;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GeneralController {

    private final EndpointResolver resolver;

    public GeneralController(EndpointResolver resolver) {
        this.resolver = resolver;
    }

    @RequestMapping(
        path = "/**",
        method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
        }
    )
    public ResponseEntity<?> handle(HttpServletRequest request) {
        String reqMethod = request.getMethod();
        String reqPath = request.getRequestURI();

        HttpMethod method = HttpMethod.GET;

        switch (reqMethod) {
            case "GET" -> method = HttpMethod.GET;
            case "POST" -> method = HttpMethod.POST;
            case "PUT" -> method = HttpMethod.PUT;
            case "PATCH" -> method = HttpMethod.PATCH;
            case "DELETE" -> method = HttpMethod.DELETE;
            default -> method = HttpMethod.INVALID;
        }

        if (method == HttpMethod.INVALID) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mockra: invalid request, method '" + reqMethod + "' is not allowed.");
        }

        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(Function.identity(), request::getHeader));

        RequestContext context = new RequestContext(method, reqPath, headers);

        Result<EndpointVariant, EndpointResolutionError> res = resolver.resolve(context);

        return toResponseEntity(res);
    }

    private ResponseEntity<?> toResponseEntity(Result<EndpointVariant, EndpointResolutionError> result) {
        if (result instanceof Result.Ok<EndpointVariant, EndpointResolutionError> ok) {
            return buildSuccessResponse(ok.value());
        }

        Result.Err<EndpointVariant, EndpointResolutionError> err = (Result.Err<EndpointVariant, EndpointResolutionError>) result;

        return buildErrorResponse(err.error());
    }

    private ResponseEntity<?> buildSuccessResponse(EndpointVariant variant) {
        ResponseDef response = variant.getResponse();

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(response.getStatus());

        if (response.getDelay() > 0) {
            try {
                Thread.sleep(response.getDelay());
            } catch (InterruptedException ignored) {}
        }

        return builder.body(response.getBody());
    }

    private ResponseEntity<?> buildErrorResponse(EndpointResolutionError error) {
        if (error instanceof EndpointResolutionError.EndpointNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mockra: endpoint not found");
        }

        if (error instanceof EndpointResolutionError.VariantError ve) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mockra: " + ve.cause().getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mockra: invalid request");
    }
}
