package com.muaz;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LottoDao {
    private static final String INSERT_SQL =
        "INSERT INTO lotto_runs(run_no, min_num, max_num, qty, numbers) VALUES(?,?,?,?,?)";

    public void insertRun(int runNo, int min, int max, int qty, String numbersCsv) {
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(INSERT_SQL)) {
            ps.setInt(1, runNo);
            ps.setInt(2, min);
            ps.setInt(3, max);
            ps.setInt(4, qty);
            ps.setString(5, numbersCsv);
            ps.executeUpdate();
        } catch (Exception e) {
            // For the lab demo we’ll just print; in prod log it.
            System.err.println("DB insert failed: " + e.getMessage());
        }
    }
}
