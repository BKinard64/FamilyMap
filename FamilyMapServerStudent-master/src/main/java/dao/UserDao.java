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

    public AuthToken login(User user) {return null;}
}
