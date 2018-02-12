package com.michaeljoelphillips.spotifyhistory;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.michaeljoelphillips.spotifyhistory.AuthCredentials;
import com.michaeljoelphillips.spotifyhistory.Database;
import com.michaeljoelphillips.spotifyhistory.model.Item;
import com.michaeljoelphillips.spotifyhistory.model.RecentlyPlayed;

public class App {
  public static void main(String[] args) throws IOException, SQLException {
    AuthCredentials credentials = Auth.getAuthCredentials();
    SpotifyApi spotify = new SpotifyApi(credentials);
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
