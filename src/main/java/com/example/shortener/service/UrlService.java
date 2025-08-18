package com.example.shortener.service;

import com.example.shortener.model.UrlMapping;
import com.example.shortener.repository.UrlMappingRepository;
import com.example.shortener.util.Base62;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlMappingRepository repo;

    public UrlService(UrlMappingRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public UrlMapping createShortUrl(String longUrl) {
        validateUrl(longUrl);
        // Create a record to get an auto-increment id, then derive code from it.
        UrlMapping draft = new UrlMapping();
        draft.setLongUrl(longUrl);
        draft.setHits(0L);
        draft.setCode("tmp"); // placeholder
        UrlMapping saved = repo.save(draft);

        String code = Base62.encode(saved.getId());
        saved.setCode(code);
        return repo.save(saved);
    }

    @Cacheable(value = "codeToUrl", key = "#code")
    public Optional<UrlMapping> resolve(String code) {
        return repo.findByCode(code);
    }

    @Transactional
    public void incrementHits(UrlMapping mapping) {
        mapping.setHits(mapping.getHits() + 1);
        repo.save(mapping);
    }

    private void validateUrl(String url) {
        Assert.hasText(url, "URL must not be empty");
        try {
            URI u = new URI(url);
            if (u.getScheme() == null || u.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL: " + url);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }
}
