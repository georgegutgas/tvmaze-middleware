package com.tvmaze.middleware.service;

import com.tvmaze.middleware.dto.SearchShowDto;
import com.tvmaze.middleware.entity.ValidateShow;
import com.tvmaze.middleware.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TvMazeService {
    private static final String API_URL = "http://api.tvmaze.com";

    @Autowired
    private RestTemplate restTemplate;
    private final ShowRepository showRepository;

    public TvMazeService(RestTemplate restTemplate, ShowRepository showRepository) {
        this.restTemplate = restTemplate;
        this.showRepository = showRepository;
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
}
