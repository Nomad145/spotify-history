package com.michaeljoelphillips.spotifyhistory;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.michaeljoelphillips.spotifyhistory.ApiToken;
import com.michaeljoelphillips.spotifyhistory.Credentials;
import com.michaeljoelphillips.spotifyhistory.Database;
import com.michaeljoelphillips.spotifyhistory.model.Item;
import com.michaeljoelphillips.spotifyhistory.model.RecentlyPlayed;

public class Cli {
  public static void main(String[] args) throws IOException, SQLException {
    Credentials credentials = new Credentials("/home/nomad/.config/spotify/credentials");
    Auth auth = new Auth(credentials);
    ApiToken token = auth.getApiToken();

    SpotifyApi spotify = new SpotifyApi(token);
    RecentlyPlayed recentlyPlayed = spotify.getRecentlyPlayed();

    Database database = new Database();
    Optional<Date> lastPlayed = database.getLastPlayedTime();
    List<Item> newTracks;

    if (lastPlayed.isPresent()) {
      newTracks = recentlyPlayed.getNewTracksSinceLastPlayed(lastPlayed.get());
    } else {
      newTracks = recentlyPlayed.items;
    }

    if (newTracks.size() > 0) {
      database.saveNewTracks(newTracks);
    }

    return;
  }
}
