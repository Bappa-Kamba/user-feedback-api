package dev.bappa.feedback.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Feedback {
    private String userId;
    private String message;
    private int rating;
    private LocalDateTime timestamp;

    public Feedback(String userId, String message, int rating) {
        this.userId = userId;
        this.message = message;
        this.rating = rating;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public int getRating() {
        return rating;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy, hh:mm a")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
