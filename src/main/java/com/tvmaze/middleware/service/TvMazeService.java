package com.tvmaze.middleware.service;

import com.tvmaze.middleware.dto.CommentRequest;
import com.tvmaze.middleware.dto.CommentResponse;
import com.tvmaze.middleware.dto.SearchShowDto;
import com.tvmaze.middleware.entity.CommentEntity;
import com.tvmaze.middleware.entity.ValidateShow;
import com.tvmaze.middleware.repository.CommentRepository;
import com.tvmaze.middleware.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TvMazeService {
    private static final String API_URL = "http://api.tvmaze.com";

    @Autowired
    private RestTemplate restTemplate;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;


    public TvMazeService(RestTemplate restTemplate, ShowRepository showRepository, CommentRepository commentRepository) {
        this.restTemplate = restTemplate;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
    }

    public List<SearchShowDto> searchShows(String query) {
        String url = API_URL + "/search/shows?q=" + query;

        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        List<SearchShowDto> resultList = new ArrayList<>();

        if (response != null) {
            for (Map<String, Object> item : response) {
                Map<String, Object> showMap = (Map<String, Object>) item.get("show");

                if (showMap != null) {
                    SearchShowDto dto = new SearchShowDto();

                    if (showMap.get("id") != null) {
                        dto.setId(((Number) showMap.get("id")).longValue());
                    }

                    dto.setName((String) showMap.get("name"));
                    dto.setSummary((String) showMap.get("summary"));
                    dto.setGenres((List<String>) showMap.get("genres"));

                    dto.setChannel(ChannelName(showMap));

                    resultList.add(dto);
                }
            }
        }
        return resultList;
    }
    // Obtener el channel porque no se optiene directamente
    private String ChannelName(Map<String, Object> showMap) {

        Map<String, Object> network = (Map<String, Object>) showMap.get("network");
        if (network != null && network.get("name") != null) {
            return (String) network.get("name");
        }

        Map<String, Object> webChannel = (Map<String, Object>) showMap.get("webChannel");
        if (webChannel != null && webChannel.get("name") != null) {
            return (String) webChannel.get("name");
        }

        return null;
    }

    // Muestra info de un show por su ID
    public Map<String, Object> getShowById(Long showId) {
        String url = API_URL + "/shows/"+ showId;

        return restTemplate.getForObject(url, Map.class);
    }

    // Valida que el show este guardado en Mongo para retornarlo o consumirlo directamente de tvmaze
    public Map<String, Object> getShowByIdCache(Long showId) {
        Optional<ValidateShow> cachedShow = showRepository.findById(showId);

        if (cachedShow.isPresent()) {
            return cachedShow.get().getData();
        }

        String url = API_URL + "/shows/" + showId;
        Map<String, Object> showData = restTemplate.getForObject(url, Map.class);

        if (showData != null) {
            showRepository.save(new ValidateShow(showId, showData));
        }

        return showData;
    }

    public List<SearchShowDto> searchShowsComplete(String query) {
        String url = API_URL + "/search/shows?q=" + query;
        List<Map<String, Object>> rawResponse = restTemplate.getForObject(url, List.class);

        if (rawResponse == null) return Collections.emptyList();

        List<SearchShowDto> result = new ArrayList<>();

        for (Map<String, Object> item : rawResponse) {
            Map<String, Object> show = (Map<String, Object>) item.get("show");
            if (show == null) continue;

            Long showId = ((Number) show.get("id")).longValue();

            // Determinar canal (network_name o webchannel_name)
            String channelName = null;
            if (show.get("network") != null) {
                Map<String, Object> network = (Map<String, Object>) show.get("network");
                channelName = (String) network.get("name");
            } else if (show.get("webChannel") != null) {
                Map<String, Object> webChannel = (Map<String, Object>) show.get("webChannel");
                channelName = (String) webChannel.get("name");
            }

            // Consultar comentarios de la BD
            List<CommentEntity> commentsFromDb = commentRepository.findByShowId(showId);

            List<CommentResponse> commentsDto = commentsFromDb.stream()
                    .map(c -> CommentResponse.builder()
                            .comment(c.getComment())
                            .rating(c.getRating())
                            .build())
                    .collect(Collectors.toList());

            SearchShowDto responseDto = SearchShowDto.builder()
                    .id(showId)
                    .name((String) show.get("name"))
                    .channel(channelName)
                    .summary((String) show.get("summary"))
                    .genres((List<String>) show.get("genres"))
                    .comments(commentsDto)
                    .build();

            result.add(responseDto);
        }

        return result;
    }

}
