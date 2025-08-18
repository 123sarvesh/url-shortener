package com.example.shortener.controller;

import com.example.shortener.model.UrlMapping;
import com.example.shortener.service.UrlService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@Validated
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/api/v1/shorten")
    public ResponseEntity<?> shorten(@RequestBody Map<String, String> body) {
        String longUrl = body.get("longUrl");
        UrlMapping mapping = service.createShortUrl(longUrl);
        String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        URI shortUri = URI.create(base + "/" + mapping.getCode());
        return ResponseEntity.ok(Map.of(
                "code", mapping.getCode(),
                "shortUrl", shortUri.toString(),
                "longUrl", mapping.getLongUrl()
        ));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> redirect(@PathVariable @NotBlank String code) {
        return service.resolve(code)
                .map(m -> {
                    service.incrementHits(m);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create(m.getLongUrl()));
                    return ResponseEntity.status(302).headers(headers).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
