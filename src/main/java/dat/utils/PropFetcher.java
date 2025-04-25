package dat.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dtos.PropDTO;
import dat.exceptions.ApiException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PropFetcher {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<PropDTO> fetchPropsByGenre(String genre) {
        try {
            String apiUrl = "https://exam.showcode.dk/api/performance/props/genre/" + genre;
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new ApiException(502, "Failed to fetch props from external API");
            }

            try (InputStream is = conn.getInputStream()) {
                return mapper.readValue(is, new TypeReference<List<PropDTO>>() {});
            }

        } catch (Exception e) {
            throw new ApiException(502, "Error contacting external prop API: " + e.getMessage());
        }
    }
}
