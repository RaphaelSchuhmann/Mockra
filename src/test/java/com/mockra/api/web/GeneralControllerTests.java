package com.mockra.api.web;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.mockra.api.config.ConfigService;

@SpringBootTest(properties = {
    "mockra.config.autoload=false",
    "mockra.config.path=src/test/resources/controllerTests/config.yaml"
})
@AutoConfigureMockMvc
public class GeneralControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfigService configService;

    @BeforeEach
    void loadTestConfig() throws Exception {
        configService.load(true, false);
    }

    @Test
    public void testCatchAllHandlesBasicRequest() throws Exception {
        mockMvc.perform(get("/api/user")
               .header("X-Mockra-Variant", "success"))
               .andExpect(status().isOk())
               .andExpect(content().string("{\"username\":\"Random Name\"}"));
    }

    @Test
    public void testCatchAllHandlesNotFoundEndpoints() throws Exception {
        mockMvc.perform(get("/api/data")
               .header("X-Mockra-Variant", "succes"))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testCatchAllHandlesMissingVariant() throws Exception {
        mockMvc.perform(get("/api/user")
               .header("X-Mockra-Variant", "not-found"))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void testCatchAllHandlesEmptyHeader() throws Exception {
        mockMvc.perform(get("/api/user")
               .header("X-Mockra-Variant", ""))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Mockra: variant header value is invalid"));
    }

    @Test
    public void testCatchAllHandlesMissingHeader() throws Exception {
        mockMvc.perform(get("/api/user"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Mockra: missing X-Mockra-Variant header"));
    }

    @Test
    public void testCatchAllHandlesResponseDelay() throws Exception {
        long delayMs = 100;

        long start = System.currentTimeMillis();

        mockMvc.perform(get("/api/delayTest")
               .header("X-Mockra-Variant", "success"))
               .andExpect(status().isOk());
               
        long duration = System.currentTimeMillis() - start;

        assertTrue(duration >= delayMs, "Response was faster than expected delay");
    }
}
