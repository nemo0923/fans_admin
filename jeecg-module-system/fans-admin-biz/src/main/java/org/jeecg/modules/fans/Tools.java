package org.jeecg.modules.fans;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Tools {

    public static void FileDownloader(String fileUrl,String savePath) throws Exception{
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(savePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } else {
        }
    }

    public static JSONObject post(String url,JSONObject postData ) {
        RestTemplate client = new RestTemplate();
        return  client.postForEntity(url, postData, JSONObject.class).getBody();
    }
    public static void put(String url,JSONObject data ) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8);
     //   System.out.println("put:"+data.toString());
        HttpEntity<String> entity = new HttpEntity<String>(data.toString(), headers);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.PUT, entity, String.class);
     //   System.out.println("response:"+response.toString());
    }
    public static long subDay(Date date1, Date date2) {

        LocalDate localDate1 = LocalDate.of(date1.getYear(), date1.getMonth()+1, date1.getDate());
        LocalDate localDate2 = LocalDate.of(date2.getYear(), date2.getMonth()+1, date2.getDate());
        long days = ChronoUnit.DAYS.between(localDate2, localDate1);
        return days;
    }
    public static int randomN(int n) {
        Random random = new Random();
        return  random.nextInt(n);
    }
    public static void main(String[] args) {
        System.out.println(randomN(15));
        System.out.println(randomN(15));
        System.out.println(randomN(15));
        System.out.println(randomN(15));
        System.out.println(randomN(15));
        System.out.println(randomN(15));
        System.out.println(randomN(15));
    }
}
