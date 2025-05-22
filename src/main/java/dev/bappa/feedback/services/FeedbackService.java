package dev.bappa.feedback.services;

import dev.bappa.feedback.models.Feedback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final List<Feedback> feedbackStorage = new ArrayList<>();

    public Feedback saveFeedback(Feedback feedback) {
        feedbackStorage.add(feedback);
        return feedback;
    }

    public List<Feedback> getAllFeedback(Optional<Integer> ratingFilter) {
        if (ratingFilter.isPresent()) {
            return feedbackStorage.stream()
                    .filter(f -> f.getRating() == ratingFilter.get())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(feedbackStorage);
    }

    public void clearStorage() {
        feedbackStorage.clear();
    }
}
