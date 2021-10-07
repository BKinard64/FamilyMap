package dao;

import model.AuthToken;

import java.sql.Connection;

public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) { this.conn = conn; }

    public AuthToken()

}
