package com.landingpage.landingpage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vietduc")
public class VietDucController {

    @Value("${google.script.url}")
    private String googleScriptUrl; // URL Apps Script (/exec)

    @GetMapping
    public String showForm() {
        return "vietduc/form"; // form nhập dữ liệu
    }

    @PostMapping("/submit")
    @ResponseBody
    public String submitForm(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String combo) {

        // TẠO JSON THỦ CÔNG
//        String json = String.format(
//                "{\"name\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\"}",
//                name, phone, address
//        );

        String json = String.format(
                "{\"name\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\",\"combo\":\"%s\"}",
                name, phone, address, combo
        );


        // Tạo HttpClient (Java 11+)
        java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                .build();

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(googleScriptUrl))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            java.net.http.HttpResponse<String> response = client.send(
                    request,
                    java.net.http.HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());

            if (response.statusCode() == 200 && response.body().contains("success")) {
                return "<h2 style='color:green;text-align:center;'>Gửi thành công!</h2>";
            } else {
                return "<h2 style='color:red;text-align:center;'>Gửi thất bại!<br>"
                        + response.body() + "</h2>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<h2 style='color:red;text-align:center;'>Lỗi: " + e.getMessage() + "</h2>";
        }
    }
}