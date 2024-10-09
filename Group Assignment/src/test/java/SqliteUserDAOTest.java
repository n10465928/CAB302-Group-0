import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.services.PasswordHashingService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqliteUserDAOTest {

    private SqliteUserDAO userDAO;
    private Connection connection;
    private PasswordHashingService hashService;

    @BeforeEach
    public void setUp() throws Exception {
        // Set up in-memory SQLite database for testing
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        userDAO = new SqliteUserDAO();
        userDAO.connection = connection;
        userDAO.createTable();
        hashService = new PasswordHashingService();
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Close the connection after each test
        connection.close();
    }

    @Test
    public void testAddUser() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        User retrievedUser = userDAO.getUser(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
        assertEquals(hashService.getHash("password123"), retrievedUser.getPassword());
        assertEquals("test@example.com", retrievedUser.getEmail());
        assertEquals("1234567890", retrievedUser.getPhone());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        user.setUsername("updatedUser");
        user.setPassword(hashService.getHash("newPassword"));
        user.setEmail("updated@example.com");
        user.setPhone("0987654321");
        userDAO.updateUser(user);
        User retrievedUser = userDAO.getUser(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("updatedUser", retrievedUser.getUsername());
        assertEquals(hashService.getHash("newPassword"), retrievedUser.getPassword());
        assertEquals("updated@example.com", retrievedUser.getEmail());
        assertEquals("0987654321", retrievedUser.getPhone());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        userDAO.deleteUser(user);
        User retrievedUser = userDAO.getUser(user.getId());
        assertNull(retrievedUser);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("user1", hashService.getHash("password1"), "user1@example.com", "1111111111");
        User user2 = new User("user2", hashService.getHash("password2"), "user2@example.com", "2222222222");
        userDAO.addUser(user1);
        userDAO.addUser(user2);
        List<User> users = userDAO.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        User retrievedUser = userDAO.getUserByUsername("testUser");
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    public void testVerifyUser() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        assertTrue(userDAO.verifyUser("testUser", "password123"));
        assertFalse(userDAO.verifyUser("testUser", "wrongPassword"));
    }

    @Test
    public void testGetUserID() {
        User user = new User("testUser", hashService.getHash("password123"), "test@example.com", "1234567890");
        userDAO.addUser(user);
        int userId = userDAO.getUserID("testUser", hashService.getHash("password123"));
        assertEquals(user.getId(), userId);
    }
}
