package com.mockra.api.registry;

import java.util.List;
import java.util.Map;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mockra.api.config.MockraConfig;
import com.mockra.api.errorHandling.ErrorType;
import com.mockra.api.errorHandling.ValidationExceptions.InvalidDelayException;
import com.mockra.api.events.ConfigLoadedEvent;
import com.mockra.api.model.*;

@Component
public class EndpointRegistry {

    private final Map<String, Endpoint> endpoints = new HashMap<>();

    @EventListener
    public void onConfigLoaded(ConfigLoadedEvent event) {
        rebuildRegistry(event.config());
    }

    public synchronized void rebuildRegistry(MockraConfig config) {
        endpoints.clear();

        List<MockraConfig.EndpointConfig> cfgEndpoints = config.getEndpoints();
        List<Endpoint> rtEndpoints = convertToRuntimeEndpoints(cfgEndpoints);

        for (Endpoint endpoint : rtEndpoints) {
            endpoints.put(endpoint.getPath(), endpoint);
        }
        
        displayMessage("Endpoint registry rebuilt (" + endpoints.size() + " endpoints)", ErrorType.INFO);
    }

    private List<Endpoint> convertToRuntimeEndpoints(List<MockraConfig.EndpointConfig> configs) {
        List<Endpoint> result = new ArrayList<>();

        for (MockraConfig.EndpointConfig cfg : configs) {
            Endpoint rtEndpoint = new Endpoint(cfg.getId(), cfg.getPath(), cfg.getMethod());
            List<EndpointVariant> variants = new ArrayList<>();

            rtEndpoint.setBody(cfg.getRequest());

            for (MockraConfig.VariantConfig variantCfg : cfg.getResponses()) {
                ResponseDef response = new ResponseDef(variantCfg.getStatus());
                
                if (!variantCfg.getBody().isEmpty()) {
                    response.setBody(variantCfg.getBody());
                }

                if (variantCfg.getDelayMs() != null && variantCfg.getDelayMs() >= 0) {
                    try {
                        response.setDelay(variantCfg.getDelayMs(), false);
                    } catch (InvalidDelayException e) {
                        displayMessage(e.getMessage(), ErrorType.WARNING);
                    }
                }
                
                variants.add(new EndpointVariant(variantCfg.getVariant(), response));
            }

            rtEndpoint.setVariants(variants);
            result.add(rtEndpoint);
        }

        return result;
    }

    public Endpoint getEndpoint(String path) {
        return endpoints.get(path);
    }

    public int size() {
        return endpoints.size();
    }

    // TODO: Implement endpoint resolver which takes in a RequestContext and returns an Endpoint
}
