import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class HttpConnection {
    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void connectionJSon() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.gov.uk/bank-holidays.json"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        JSONObject englandAndWales = obj.getJSONObject("england-and-wales");
        JSONArray englandAndWalesHolidays = englandAndWales.getJSONArray("events");
        System.out.println("These are public holidays in England and Wales:");
        for (int i = 0; i < englandAndWalesHolidays.length(); i++) {
            JSONObject holiday = englandAndWalesHolidays.getJSONObject(i);
            System.out.println(holiday.getString("title") + " on " + holiday.getString("date"));
        }

        //Starting point for application in JOptionPane
        String message = "Choose a month to get information about holidays in the UK!";
        String title = "Hello";
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

        while (true) {
            String input = (String) JOptionPane.showInputDialog(null, "", "Choose a month", JOptionPane.QUESTION_MESSAGE, null, MONTHS, MONTHS[1]);
            System.out.println(input);

            Month selectedMonth = Month.valueOf(input.toUpperCase());

            boolean foundHoliday = false;
            for (int i = 0; i < englandAndWalesHolidays.length(); i++) {
                JSONObject holiday = englandAndWalesHolidays.getJSONObject(i);
                LocalDate date = LocalDate.parse(holiday.getString("date"), DATE_FORMAT);

                if (date.getMonth() == selectedMonth) {
                    JOptionPane.showMessageDialog(null, "Holiday in " + input + ": " + holiday.getString("title") + " on " + date.format(DATE_FORMAT), "Information", JOptionPane.INFORMATION_MESSAGE);
                    foundHoliday = true;
                }
            }

            if (!foundHoliday) {
                JOptionPane.showMessageDialog(null, "No holidays in " + input + ".", "Information", JOptionPane.OK_OPTION);
            }

            JOptionPane.showMessageDialog(null, "Switching to months options", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}