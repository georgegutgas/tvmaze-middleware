package com.tvmaze.middleware.controller;

import com.tvmaze.middleware.dto.SearchShowDto;
import com.tvmaze.middleware.service.TvMazeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final TvMazeService tvMazeService;

    public ShowController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchShowDto>> searchShows(@RequestParam("q") String query) {
        return ResponseEntity.ok(tvMazeService.searchShowsComplete(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getShowById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tvMazeService.getShowByIdComplete(id));
    }


}
