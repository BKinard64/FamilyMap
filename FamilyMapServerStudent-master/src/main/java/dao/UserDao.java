package dao;

import model.AuthToken;
import model.User;

import java.sql.Connection;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(User user) {}

    public User find(String username) {return null;}

    public void delete(User user) {}
}
