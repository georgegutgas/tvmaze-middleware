package com.tvmaze.middleware.controller;

import com.tvmaze.middleware.dto.SearchShowDto;
import com.tvmaze.middleware.service.TvMazeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final TvMazeService tvMazeService;

    public ShowController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchShowDto>> searchShows(@RequestParam("q") String query) {
        return ResponseEntity.ok(tvMazeService.searchShows(query));
    }
}
