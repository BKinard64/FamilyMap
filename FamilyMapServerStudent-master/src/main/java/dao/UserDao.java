package dao;

import model.AuthToken;
import model.User;

import java.sql.Connection;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public AuthToken register(User user) {return null;}

    public User find(String username) {return null;}

    public void delete(User user) {}

    public AuthToken login(User user) {return null;}
}
