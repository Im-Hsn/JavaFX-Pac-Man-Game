package com.example.oop2project;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;

public class DB {

    public Connection connect() {

        Connection conn = null;

        try {
            String dbURL = "jdbc:mysql://localhost:3306/javafxProject";
            String user = "root";
            String pass = "root";
            conn = DriverManager.getConnection(dbURL, user, pass);

            if (conn != null) {
                DatabaseMetaData dm = conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed successfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Player authenticatePlayer(Connection cnx, String username, String password) {
        String selectSql = "SELECT * FROM player WHERE username = ?";
        Player authenticatedPlayer = null;

        try (PreparedStatement prepsSelect = cnx.prepareStatement(selectSql)) {
            prepsSelect.setString(1, username);
            ResultSet resultSet = prepsSelect.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");

                // Hash the provided password for comparison
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes("UTF-8"));
                StringBuilder hashedPassword = new StringBuilder();

                for (byte b : hash) {
                    hashedPassword.append(String.format("%02x", b));
                }

                // Compare the stored hashed password with the hashed provided password
                boolean isAuthenticated = storedHashedPassword.equals(hashedPassword.toString());

                // If authenticated, create a Player object with information
                if (isAuthenticated) {
                    authenticatedPlayer = new Player(resultSet.getInt("idplayer"),
                            resultSet.getInt("highscore"), resultSet.getString("username"),
                            resultSet.getString("email"), resultSet.getString("role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authenticatedPlayer;
    }


    public boolean createPlayer(Connection cnx, String username, String email, String password) {
        String selectSql = "SELECT * FROM player WHERE username = ? OR email = ?";
        String insertSql = "INSERT INTO player (username, email, password, highscore, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement prepsSelect = cnx.prepareStatement(selectSql);
             PreparedStatement prepsInsert = cnx.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            // Check if username or email already exists
            prepsSelect.setString(1, username);
            prepsSelect.setString(2, email);
            ResultSet resultSet = prepsSelect.executeQuery();

            if (resultSet.next()) {
                // Username or email already exists, return false
                return false;
            }

            // Encrypt the password using SHA-256 hash algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hashedPassword = new StringBuilder();

            for (byte b : hash) {
                hashedPassword.append(String.format("%02x", b));
            }

            // Set parameters for insertion
            prepsInsert.setString(1, username);
            prepsInsert.setString(2, email);
            prepsInsert.setString(3, hashedPassword.toString()); // Set the hashed password
            prepsInsert.setInt(4, 0); // Set highScore to 0
            prepsInsert.setString(5, "Player");

            // Execute the insertion
            int rowsAffected = prepsInsert.executeUpdate();

            // Check if insertion was successful
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveGame(Connection cnx, Game game) {
        String insertSql = "INSERT INTO game (playerid, score, levelreached, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement prepsInsert = cnx.prepareStatement(insertSql)) {
            prepsInsert.setInt(1, game.getPlayerId());
            prepsInsert.setInt(2, game.getScore());
            prepsInsert.setInt(3, game.getLevelReached());
            prepsInsert.setObject(4, game.getDateTime());

            int rowsAffected = prepsInsert.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ghost createGhostFromResultSet(ResultSet resultSet, Level level) throws SQLException {
        int id = resultSet.getInt("idGhost");
        int speed = resultSet.getInt("speed");
        String color = resultSet.getString("color");
        ImageView gifLeft = new ImageView(new Image("File:"+resultSet.getString("gif-left")));
        ImageView gifRight = new ImageView(new Image("File:"+resultSet.getString("gif-right")));
        return new Ghost(id, speed, color, gifLeft, gifRight, level);
    }

    public Level loadLevel(Connection cnx, int levelReached) {
        String selectLevelSql = "SELECT * FROM level WHERE idlevel = ?";
        String selectGhostsSql = "SELECT * FROM ghost_level gl JOIN ghost g ON gl.ghostid = g.idGhost WHERE gl.levelid = ?";

        Level level = null;

        try (PreparedStatement prepsSelectLevel = cnx.prepareStatement(selectLevelSql)) {
            prepsSelectLevel.setInt(1, levelReached);
            ResultSet levelResultSet = prepsSelectLevel.executeQuery();

            String mapLayout = null;

            if (levelResultSet.next()) {
                mapLayout = levelResultSet.getString("mapLayout");
            }

            if (mapLayout != null) {
                ImageView imageView = new ImageView(new Image("File:" + mapLayout));
                imageView.setFitWidth(800);
                imageView.setFitHeight(950);
                imageView.setLayoutX(460);
                level = new Level(levelReached, imageView, new ArrayList<>());

                try (PreparedStatement prepsSelectGhosts = cnx.prepareStatement(selectGhostsSql)) {
                    prepsSelectGhosts.setInt(1, levelReached);
                    ResultSet ghostsResultSet = prepsSelectGhosts.executeQuery();

                    while (ghostsResultSet.next()) {
                        Ghost ghost = createGhostFromResultSet(ghostsResultSet, level);
                        level.addGhost(ghost);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return level;
    }

    public boolean addGhost(Connection connection, int levelId, int ghostId) {
        String sql = "INSERT INTO ghost_level (levelid, ghostid) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, levelId);
            pstmt.setInt(2, ghostId);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean deleteGhost(Connection connection, int levelId, int ghostId) {
        String sql = "DELETE FROM ghost_level WHERE levelid = ? AND ghostid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, levelId);
            pstmt.setInt(2, ghostId);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public TableView<ObservableList<String>> createTableView(Connection connection) {
        TableView<ObservableList<String>> tableView = new TableView<>();

        TableColumn<ObservableList<String>, String> levelColumn = new TableColumn<>("Level");
        levelColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));

        TableColumn<ObservableList<String>, String> colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));

        tableView.getColumns().add(levelColumn);
        tableView.getColumns().add(colorColumn);

        String sql = "SELECT ghost_level.levelid, Ghost.color FROM ghost_level JOIN Ghost ON ghost_level.ghostid = Ghost.idghost";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("levelid")));
                row.add(rs.getString("color"));

                tableView.getItems().add(row);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        tableView.setPrefHeight(600);
        tableView.setPrefWidth(400);
        levelColumn.setPrefWidth(tableView.getPrefWidth() / 2);
        colorColumn.setPrefWidth(tableView.getPrefWidth() / 2);

        return tableView;
    }

}
