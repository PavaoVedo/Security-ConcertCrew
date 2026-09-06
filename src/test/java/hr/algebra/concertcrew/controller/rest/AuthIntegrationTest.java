package hr.algebra.concertcrew.controller.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${app.jwt.secret}")
    String secret;

    private String json(Map<String, String> body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    private String accessToken(String username, String password) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("username", username, "password", password))))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("accessToken").asText();
    }

    @Test
    void unauthenticatedApiRequestReturns401() throws Exception {
        mockMvc.perform(get("/api/concerts"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void userCanReadConcerts() throws Exception {
        mockMvc.perform(get("/api/concerts")
                .header("Authorization", "Bearer " + accessToken("user", "user123")))
            .andExpect(status().isOk());
    }

    @Test
    void userCannotCreateConcertReturns403() throws Exception {
        mockMvc.perform(post("/api/concerts")
                .header("Authorization", "Bearer " + accessToken("user", "user123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateConcertReturns201() throws Exception {
        String concert = "{\"mainArtist\":\"Integration Band\",\"venue\":\"Test Hall\","
            + "\"city\":\"Zagreb\",\"genre\":\"POP\",\"showType\":\"ARENA_TOUR\","
            + "\"dateAttended\":\"2024-01-01\"}";

        mockMvc.perform(post("/api/concerts")
                .header("Authorization", "Bearer " + accessToken("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(concert))
            .andExpect(status().isCreated());
    }

    @Test
    void loginWithWrongPasswordIsRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("username", "admin", "password", "wrong-password"))))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void registerCreatesNewUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of(
                    "username", "brandnew",
                    "email", "brandnew@example.com",
                    "password", "password1"))))
            .andExpect(status().isOk());
    }

    @Test
    void expiredAccessTokenReturns401() throws Exception {
        String expired = JWT.create()
            .withIssuer("concertcrew")
            .withSubject("user")
            .withClaim("role", "ROLE_USER")
            .withIssuedAt(new Date(System.currentTimeMillis() - 20_000))
            .withExpiresAt(new Date(System.currentTimeMillis() - 10_000))
            .sign(Algorithm.HMAC256(secret));

        mockMvc.perform(get("/api/concerts")
                .header("Authorization", "Bearer " + expired))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshRotatesTokenAndDetectsReuse() throws Exception {
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("username", "user", "password", "user123"))))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        String firstRefresh = objectMapper.readTree(loginResponse).get("refreshToken").asText();

        String refreshResponse = mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("refreshToken", firstRefresh))))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        String secondRefresh = objectMapper.readTree(refreshResponse).get("refreshToken").asText();

        assertNotEquals(firstRefresh, secondRefresh);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("refreshToken", firstRefresh))))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(Map.of("refreshToken", secondRefresh))))
            .andExpect(status().isUnauthorized());
    }
}
