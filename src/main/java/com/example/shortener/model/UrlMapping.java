package com.example.shortener.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "url_mapping", indexes = {
        @Index(name="idx_code", columnList = "code", unique = true),
        @Index(name="idx_long_url", columnList = "longUrl")
})
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String code;

    @Column(nullable = false, length = 2048)
    private String longUrl;

    private Instant createdAt = Instant.now();

    private Long hits = 0L;

    public UrlMapping() {}

    public UrlMapping(String code, String longUrl) {
        this.code = code;
        this.longUrl = longUrl;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLongUrl() { return longUrl; }
    public void setLongUrl(String longUrl) { this.longUrl = longUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getHits() { return hits; }
    public void setHits(Long hits) { this.hits = hits; }
}
