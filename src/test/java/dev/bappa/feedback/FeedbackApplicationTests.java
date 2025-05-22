package dev.bappa.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bappa.feedback.services.FeedbackService;
import dev.bappa.feedback.models.Feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeedbackService feedbackService;

    @BeforeEach
    void clearStorage() {
        feedbackService.clearStorage();
    }

    @Test
    void testSubmitFeedback_ValidInput() throws Exception {
        Feedback request = new Feedback("001", "Great app!", 5);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testSubmitFeedback_InvalidRating() throws Exception {
        Feedback request = new Feedback("002", "Nice", 10);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitFeedback_MissingMessage() throws Exception {
        Feedback request = new Feedback("003", "", 3);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllFeedback_AsAdmin() throws Exception {
        feedbackService.saveFeedback(new Feedback("adminUser", "Looks good", 4));

        mockMvc.perform(get("/api/admin/feedback")
                .header("X-ADMIN", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].userId").exists())
                .andExpect(jsonPath("$.data[0].message").exists())
                .andExpect(jsonPath("$.data[0].rating").isNumber());
    }

    @Test
    void testGetAllFeedback_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/feedback"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Access denied: admins only"));
    }

    @Test
    void testAdminCanFilterFeedbackByRating() throws Exception {
			mockMvc.perform(post("/api/feedback")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new Feedback("user1", "Excellent", 5))));
			
			mockMvc.perform(post("/api/feedback")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new Feedback("user2", "Okay", 3))));	

			mockMvc.perform(post("/api/feedback")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new Feedback("user3", "Bad", 1))));

			mockMvc.perform(post("/api/feedback")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(new Feedback("user2", "Bad", 1))));	

			mockMvc.perform(get("/api/admin/feedback")
											.param("rating", "1")
											.header("X-ADMIN", "true"))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.status").value("success"))
							.andExpect(jsonPath("$.data.length()").value(2))
							.andExpect(jsonPath("$.data[0].rating").value(1))
							.andExpect(jsonPath("$.data[1].rating").value(1));
    }
}
