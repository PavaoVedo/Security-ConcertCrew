package hr.algebra.concertcrew.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HardeningIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    private String adminToken() throws Exception {
        String body = objectMapper.writeValueAsString(
            Map.of("username", "admin", "password", "admin123"));
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("accessToken").asText();
    }

    @Test
    void ssrfBlocksInternalMetadataAddress() throws Exception {
        mockMvc.perform(get("/api/tools/fetch-metadata")
                .param("url", "http://169.254.169.254/latest/meta-data/")
                .header("Authorization", "Bearer " + adminToken()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void ssrfBlocksLoopbackAddress() throws Exception {
        mockMvc.perform(get("/api/tools/fetch-metadata")
                .param("url", "http://127.0.0.1:8090/")
                .header("Authorization", "Bearer " + adminToken()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void ssrfAllowsPublicUrlFetch() throws Exception {
        mockMvc.perform(get("/api/tools/fetch-metadata")
                .param("url", "http://example.com/")
                .header("Authorization", "Bearer " + adminToken()))
            .andExpect(result -> {
                int s = result.getResponse().getStatus();
                assertTrue(s == 200 || s == 502, "expected 200 or 502 but got " + s);
            });
    }

    @Test
    void adminCanBackupDatabase() throws Exception {
        mockMvc.perform(post("/api/database/backup")
                .header("Authorization", "Bearer " + adminToken()))
            .andExpect(status().isOk());
    }

    @Test
    void adminCanRestoreDatabase() throws Exception {
        String token = adminToken();
        mockMvc.perform(post("/api/database/backup")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/database/restore")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
