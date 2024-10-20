package vpm.gui_prototype.models.DatabaseStuff.UserData;

import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.services.PasswordHashingService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class SqliteUserDAO implements IUserDAO {
    public Connection connection;
    public PasswordHashingService hashService;

    // DateTimeFormatter to parse string representations of timestamps
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Initialize connection and create table
    public SqliteUserDAO() {
        connection = UserSqliteConnection.getInstance();
        hashService = new PasswordHashingService();
        createTable();
    }

    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NULL,"
                    + "phone VARCHAR NULL,"
                    + "last_interaction_time DATETIME"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, password, email, phone, last_interaction_time) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getLastInteractionTime() != null ? user.getLastInteractionTime().format(FORMATTER) : null);

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, last_interaction_time = ? WHERE id = ?"
            );
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getLastInteractionTime() != null ? user.getLastInteractionTime().format(FORMATTER) : null);
            statement.setInt(6, user.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                User user = new User(username, password, email, phone);
                user.setId(id);
                user.setLastInteractionTime(lastInteractionTime);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                User user = new User(username, password, email, phone);
                user.setId(id);
                user.setLastInteractionTime(lastInteractionTime);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                User user = new User(username, password, email, phone);
                user.setId(id);
                user.setLastInteractionTime(lastInteractionTime);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean verifyUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) return false;
        return (user.getPassword().equals(hashService.getHash(password)));
    }

    @Override
    public int getUserID(String username, String password) {
        int userId = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM users WHERE username = ? AND password = ?"
            );
            statement.setString(1, username);
            statement.setString(2, password);
//            statement.setString(2, hashService.getHash(password));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public LocalDateTime getLastInteractionTime(int userId) {
        LocalDateTime lastInteractionTime = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT last_interaction_time FROM users WHERE id = ?"
            );
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");
                lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastInteractionTime;
    }

    @Override
    public void setLastInteractionTime(int userId, LocalDateTime lastInteractionTime) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET last_interaction_time = ? WHERE id = ?"
            );
            statement.setString(1, lastInteractionTime != null ? lastInteractionTime.format(FORMATTER) : null);
            statement.setInt(2, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
