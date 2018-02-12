package com.michaeljoelphillips.spotifyhistory;

import static java.util.Base64.Encoder;

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
import com.michaeljoelphillips.spotifyhistory.AuthCredentials;
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
  private static String clientId;

  private static String secretKey;

  private static String authCode;

  private static String redirectUrl = "http://localhost:9000";

  private static final NetHttpTransport client = new NetHttpTransport();

  private static final JsonFactory jsonFactory = new JacksonFactory();

  private static final File savedCredentials = new File("/home/nomad/.config/spotify/token.json");

  public static AuthCredentials getAuthCredentials() throws IOException {
    try {
      return getCredentialsFromFile();
    } catch (Exception e) {
      AuthCredentials credentials = getCredentialsFromSpotify();
      writeCredentialsToFile(credentials);

      return credentials;
    }
  }

  private static AuthCredentials getCredentialsFromFile()
      throws FileNotFoundException, IOException {

    JsonObjectParser parser = new JsonObjectParser(jsonFactory);

    AuthCredentials credentials = parser.parseAndClose(
        new FileReader(savedCredentials),
        AuthCredentials.class
    );

    return credentials;
  }

  private static AuthCredentials getCredentialsFromSpotify() throws IOException {
    HttpRequestFactory factory = getRequestFactory();
    HashMap<String, String> authPayload = getContentPayload();

    HttpRequest authRequest = factory.buildPostRequest(
        new GenericUrl("https://accounts.spotify.com/api/token"),
        new UrlEncodedContent(authPayload)
    );

    HttpResponse authResponse = authRequest.execute();

    return authResponse.parseAs(AuthCredentials.class);
  }

  private static void writeCredentialsToFile(AuthCredentials credentials) throws IOException {
    String json = jsonFactory.toPrettyString(credentials);

    FileWriter writer = new FileWriter(savedCredentials);

    writer.write(json, 0, json.length());
    writer.close();
  }

  private static HttpRequestFactory getRequestFactory() {
    HttpRequestFactory factory = client.createRequestFactory(
        new HttpRequestInitializer() {
          public void initialize(HttpRequest request) {
            String encodedCredentials = encodeCredentials();

            HttpHeaders headers = request.getHeaders();
            headers.setAuthorization("Basic " + encodedCredentials);

            request.setParser(new JsonObjectParser(jsonFactory));
          }
        }
    );

    return factory;
  }

  private static String encodeCredentials() {
    String credentials = String.format("%s:%s", clientId, secretKey);

    Encoder encoder = Base64.getEncoder();

    return encoder.encodeToString(credentials.getBytes());
  }

  private static HashMap<String, String> getContentPayload(){
    HashMap<String, String> authPayload = new HashMap<String, String>(3);

    authPayload.put("code", authCode);
    authPayload.put("grant_type", "authorization_code");
    authPayload.put("redirect_uri", redirectUrl);

    return authPayload;
  }
}
