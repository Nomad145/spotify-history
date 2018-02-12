package com.michaeljoelphillips.spotifyhistory;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.michaeljoelphillips.spotifyhistory.model.Item;
import java.sql.*;

public class Database {
  private Connection connection;

  public Database() throws SQLException {
    connection = DriverManager.getConnection(
        "jdbc:mysql://localhost/spotify_history",
        "root",
        "root"
    );
  }

  public Optional<Date> getLastPlayedTime() throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet result = statement.executeQuery(
        "SELECT last_played FROM tracks ORDER BY id DESC LIMIT 1"
    );

    if (result.first()) {
      Date lastPlayed = result.getDate("last_played");

      return Optional.of(lastPlayed);
    }

    return Optional.empty();
  }

  public void saveNewTracks(List<Item> newTracks) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO tracks VALUES (?, ?, ?, ?, ?)"
    );

    for (Item item : newTracks) {
      statement.setString(1, item.track.id);
      statement.setString(2, item.track.name);
      statement.setString(3, item.track.artists.get(0).name);
      statement.setString(4, item.track.artists.get(0).id);
      statement.setTimestamp(5, new Timestamp(item.playedAt.getValue()));

      statement.execute();
      statement.clearParameters();
    }
  }
}
