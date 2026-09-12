package com.tvmaze.middleware.service;

import com.tvmaze.middleware.entity.CommentEntity;
import com.tvmaze.middleware.repository.CommentRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentsService {
    private final CommentRepository commentRepository;

    public CommentsService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    // Guarda el nuevo comentario y calificacion
    public CommentEntity addComment(Long showId, String commentText, Double rating) {
        if (rating != null && (rating < 1.0 || rating > 5.0)) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        // Sanitizar el texto para evitar etiquetas HTML y scripts
        String sComment = null;
        if (commentText != null) {
            sComment = Jsoup.clean(commentText.trim(), Safelist.none());
        }

        CommentEntity comment = CommentEntity.builder()
                .showId(showId)
                .comment(sComment)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    // Obtiene lista de comentarios para un show especifico.
    public List<CommentEntity> getCommentsByShowId(Long showId) {
        return commentRepository.findByShowId(showId);
    }
}
