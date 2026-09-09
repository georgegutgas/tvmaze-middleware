package com.tvmaze.middleware.repository;

import com.tvmaze.middleware.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    List<CommentEntity> findByShowId(Long showId);
}
