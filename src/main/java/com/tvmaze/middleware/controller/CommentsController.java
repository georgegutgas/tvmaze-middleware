package com.tvmaze.middleware.controller;

import com.tvmaze.middleware.entity.CommentEntity;
import com.tvmaze.middleware.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class CommentsController {
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentService) {
        this.commentsService = commentService;
    }

    @PostMapping("/{showId}/comments")
    public ResponseEntity<CommentEntity> addComment(
            @PathVariable Long showId,
            @RequestBody Map<String, Object> payload) {

        String comment = (String) payload.get("comment");
        Number ratingNum = (Number) payload.get("rating");
        Double rating = ratingNum != null ? ratingNum.doubleValue() : null;

        CommentEntity savedComment = commentsService.addComment(showId, comment, rating);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("/{showId}/comments")
    public ResponseEntity<List<CommentEntity>> getComments(@PathVariable Long showId) {
        return ResponseEntity.ok(commentsService.getCommentsByShowId(showId));
    }
}
