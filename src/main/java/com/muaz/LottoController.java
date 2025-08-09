package com.muaz;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.SecureRandom;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LottoController {

    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private TextField qtyField;
    @FXML private TextArea outputArea;

    private final ExecutorService pool = Executors.newFixedThreadPool(5);
    private final SecureRandom rng = new SecureRandom();
    private final LottoDao dao = new LottoDao();

    @FXML
    public void onGenerate() {
        int min, max, qty;
        try {
            min = Integer.parseInt(minField.getText().trim());
            max = Integer.parseInt(maxField.getText().trim());
            qty = Integer.parseInt(qtyField.getText().trim());
        } catch (Exception e) {
            append("Please enter valid integers.");
            return;
        }

        // Spec: numbers within 0–100
        if (min < 0 || max > 100) {
            append("Numbers must be within 0–100.");
            return;
        }
        if (min >= max) {
            append("Minimum must be less than maximum.");
            return;
        }
        if (qty <= 0 || qty > (max - min + 1)) {
            append("Quantity must be >0 and ≤ range size.");
            return;
        }

        outputArea.clear();
        int runs = Math.min(5, 5); // up to 5 runs by spec

        for (int run = 1; run <= runs; run++) {
            final int runNo = run;
            pool.submit(() -> {
                var nums = generateUnique(min, max, qty);        // HashSet for uniqueness
                var asText = joinSorted(nums);                    // pretty string
                dao.insertRun(runNo, min, max, qty, asText);      // save to DB

                Platform.runLater(() -> {
                    // Show each run; last generated ends up as the last line (as required)
                    append(String.format("Run %d → %s", runNo, asText));
                });
            });
        }
    }

    @FXML
    public void onClear() {
        outputArea.clear();
    }

    private ArrayList<Integer> generateUnique(int min, int max, int qty) {
        var set = new HashSet<Integer>();
        while (set.size() < qty) {
            set.add(rng.nextInt(max - min + 1) + min);
        }
        return new ArrayList<>(set);
    }

    private String joinSorted(ArrayList<Integer> values) {
        values.sort(Integer::compareTo);
        var sj = new StringJoiner(", ");
        for (int v : values) sj.add(Integer.toString(v));
        return sj.toString();
    }

    private void append(String line) {
        outputArea.appendText(line + System.lineSeparator());
    }
}
