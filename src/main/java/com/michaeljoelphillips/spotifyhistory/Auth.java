package com.michaeljoelphillips.spotifyhistory;

import java.util.Base64.Encoder;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.michaeljoelphillips.spotifyhistory.ApiToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

public class Auth {
  private String redirectUrl = "http://localhost:9000";

  private final NetHttpTransport client = new NetHttpTransport();

  private final JsonFactory jsonFactory = new JacksonFactory();

  private final File savedToken = new File("/home/nomad/.config/spotify/token.json");

  private Credentials credentials;

  public Auth(Credentials credentials) {
    this.credentials = credentials;
  }

  public ApiToken getApiToken() throws IOException {
    ApiToken token = refreshToken(readTokenFromFile());

    return token;
  }

  private ApiToken readTokenFromFile() throws FileNotFoundException, IOException {
    JsonObjectParser parser = new JsonObjectParser(jsonFactory);

    ApiToken token = parser.parseAndClose(
        new FileReader(savedToken),
        ApiToken.class
    );

    return token;
  }

  private ApiToken refreshToken(ApiToken token) throws IOException {
    HttpRequestFactory factory = getRequestFactory();
    HashMap<String, String> authPayload = buildContentPayload(token);

    HttpRequest authRequest = factory.buildPostRequest(
        new GenericUrl("https://accounts.spotify.com/api/token"),
        new UrlEncodedContent(authPayload)
    );

    HttpResponse authResponse = authRequest.execute();

    ApiToken refreshedToken = authResponse.parseAs(ApiToken.class);
    token.accessToken = refreshedToken.accessToken;

    writeTokenToFile(token);

    return token;
  }

  private void writeTokenToFile(ApiToken token) throws IOException {
    String json = jsonFactory.toPrettyString(token);

    FileWriter writer = new FileWriter(savedToken);

    writer.write(json, 0, json.length());
    writer.close();
  }

  private HttpRequestFactory getRequestFactory() {
    HttpRequestFactory factory = client.createRequestFactory(
        new HttpRequestInitializer() {
          public void initialize(HttpRequest request) {
            HttpHeaders headers = request.getHeaders();
            headers.setAuthorization("Basic " + credentials.getCredentials());

            request.setParser(new JsonObjectParser(jsonFactory));
          }
        }
    );

    return factory;
  }

  private HashMap<String, String> buildContentPayload(ApiToken token) {
    HashMap<String, String> authPayload = new HashMap<String, String>(3);

    authPayload.put("refresh_token", token.refreshToken);
    authPayload.put("grant_type", "refresh_token");

    return authPayload;
  }
}
