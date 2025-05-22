package dev.bappa.feedback.controllers;

import dev.bappa.feedback.models.Feedback;
import dev.bappa.feedback.models.FeedbackRequest;
import dev.bappa.feedback.models.ResponseWrapper;
import dev.bappa.feedback.services.FeedbackService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        Feedback feedback = new Feedback(
                request.getUserId(),
                request.getMessage(),
                request.getRating()
        );

        Feedback saved = feedbackService.saveFeedback(feedback);

        return ResponseEntity.ok(
            new ResponseWrapper<>("success", "Feedback submitted successfully.", saved)
        );
    }

    @GetMapping("/admin/feedback")
    public ResponseEntity<?> getAllFeedback(
            @RequestHeader(value = "x-admin", required = false) String isAdmin,
            @RequestParam(value = "rating", required = false) Integer rating  
    ) {
        if (!"true".equalsIgnoreCase(isAdmin)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseWrapper<>("error", "Access denied: admins only", null));
        }

        List<Feedback> filtered = feedbackService.getAllFeedback(Optional.ofNullable(rating));

        return ResponseEntity.ok(
            new ResponseWrapper<>("success", "Fetched feedbacks successfully", filtered)
        );
    }
}
