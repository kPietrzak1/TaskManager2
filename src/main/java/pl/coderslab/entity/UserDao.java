package pl.coderslab.entity;

import java.sql.*;

public class UserDao {

    private static final String DB_URL_workshop = "jdbc:mysql://localhost:3306/workshop2?serverTimezone=Europe/Warsaw";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private Connection connection;



    public User[] findAll() throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        PreparedStatement countStatement = connection.prepareStatement(query);
        ResultSet countResultSet = countStatement.executeQuery();
        countResultSet.next();
        int count = countResultSet.getInt(1);
        User[] users = new User[count];

        query = "SELECT * FROM users";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        int index = 0;
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUserName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            users[index++] = user;
        }
        return users;
        }

        public void delete(int userId) throws SQLException {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            }
        }


    public void update(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        statement.setInt(3, user.getId());

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("An existing user was updated successfully!");
        }
    }

    public User read(int userId) {
        try (Connection conn = UserDao.connect();
             PreparedStatement ps = conn.prepareStatement(READ_USER_QUERY)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL_workshop, DB_USER, DB_PASS);
    }

    public static void insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String READ_USER_QUERY =
            "SELECT * FROM users WHERE id = ?";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    private static final String FIND_ALL_USERS_QUERY =
            "SELECT * FROM users";

    public User createUser(User user) {
        try (Connection connection = connect();
             PreparedStatement preStmt = connection.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, user.getPassword());
            preStmt.executeUpdate();

            ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                user.setId((int) id);
                System.out.println("Inserted ID: " + id);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
