package com.landingpage.landingpage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vietduc")
public class VietDucController {

    @Value("${google.script.url}")
    private String googleScriptUrl; // URL Apps Script (/exec)


    @GetMapping("/chinh-sach-van-chuyen")
    public String chinhSachVanChuyen() {
        return "/footer/chinh-sach-van-chuyen";
    }

    @GetMapping("/dieu-khoan-su-dung")
    public String dieuKhoanSuDung() {
        return "/footer/dieu-khoan-su-dung";
    }

    @GetMapping("/chinh-sach-bao-mat")
    public String chinhSachBaoMat() {
        return "/footer/chinh-sach-bao-mat";
    }

    @GetMapping("/error")
    public String trangGuiLoi() {
        return "/vietduc/error";
    }




    @PostMapping("/submit")
    public String submitForm(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String combo,
            org.springframework.ui.Model model
    ) {

        String json = String.format(
                "{\"name\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\",\"combo\":\"%s\"}",
                name, phone, address, combo
        );

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

            if (response.statusCode() == 200 && response.body().contains("success")) {

                // truyền dữ liệu sang trang thank nếu muốn
                model.addAttribute("name", name);
                model.addAttribute("phone", phone);
                model.addAttribute("combo", combo);

                // 👉 templates/vietduc/thank.html
                return "vietduc/thank";
            } else {
                model.addAttribute("error", response.body());
                return "vietduc/error";
            }

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "vietduc/error";
        }
    }








}