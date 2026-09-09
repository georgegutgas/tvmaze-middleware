package com.tvmaze.middleware.repository;

import com.tvmaze.middleware.entity.ValidateShow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ValidateShow, Long> {
}
