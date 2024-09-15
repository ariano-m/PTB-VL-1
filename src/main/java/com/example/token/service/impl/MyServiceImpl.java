package com.example.token.service.impl;

import com.example.token.service.MyService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Service
public class MyServiceImpl implements MyService {
    @Override
    public String getToken() {
        String url = "http://auth-vivelibre:8080/token";
        String username = "auth-vivelibre";
        String password = "password";

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        String jsonData = "{}";
        try {
            jsonData = convertMapToString(data);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        String date = "";
        try {
            date = getCurrentTimestamp();
        } catch (ParseException pe) {
            System.out.println("ERROR: " + pe.getMessage());
        }
        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // set the request method, headers and data
            con.setRequestProperty("Accept", "application/json; charset=UTF-8");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            System.out.println(jsonData);
            OutputStream os = con.getOutputStream();
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.close();


            // read the response
            int responceCode = con.getResponseCode();
            System.out.println("Response: " + responceCode);
            if (responceCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
            } else {
                return "";
            }

        } catch (MalformedURLException mue) {
            System.out.println(mue.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.toString()
                .replace("token","auth-vivelibre-token")
                .replace("}",  ",\"date\": \"" + date + "\"}");
    }

    private String convertMapToString(Map<String, String> data) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String key : data.keySet()) {
            sb.append("\"")
                    .append(key)
                    .append("\":\"")
                    .append(data.get(key))
                    .append("\",");
        }
        return sb.substring(0, sb.length() - 1) + "}";
    }

    private String getCurrentTimestamp() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date currentDate = new Date();
        return formatter.format(currentDate);
    }

}
