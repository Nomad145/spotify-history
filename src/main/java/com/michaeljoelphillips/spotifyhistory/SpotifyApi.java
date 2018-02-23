package com.michaeljoelphillips.spotifyhistory;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;

import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import com.michaeljoelphillips.spotifyhistory.ApiToken;
import com.michaeljoelphillips.spotifyhistory.model.RecentlyPlayed;
import com.michaeljoelphillips.spotifyhistory.request.RecentlyPlayedUrl;

public class SpotifyApi {
  private ApiToken credentials;

  private HttpRequestFactory requestFactory;

  private final NetHttpTransport client = new NetHttpTransport();

  private final JsonFactory jsonFactory = new JacksonFactory();

  public SpotifyApi(ApiToken credentials) {
    this.credentials = credentials;
    requestFactory = buildRequestFactory();
  }

  private HttpRequestFactory buildRequestFactory() {
    HttpRequestFactory requestFactory = client.createRequestFactory(
        new HttpRequestInitializer() {
          public void initialize(HttpRequest request) {
            HttpHeaders headers = request.getHeaders();
            headers.setAuthorization("Bearer " + credentials.accessToken);

            request.setParser(new JsonObjectParser(jsonFactory));
          }
        }
    );

    return requestFactory;
  }

  public RecentlyPlayed getRecentlyPlayed() throws IOException {
    HttpRequest recentlyPlayed = requestFactory.buildGetRequest(
        new RecentlyPlayedUrl()
    );

    return recentlyPlayed
      .execute()
      .parseAs(RecentlyPlayed.class);
  }
}
