package com.example.shortener;

import com.example.shortener.model.UrlMapping;
import com.example.shortener.repository.UrlMappingRepository;
import com.example.shortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UrlServiceTests {

    @Test
    void createShortUrl_generatesCode() {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);
        UrlService svc = new UrlService(repo);

        when(repo.save(any())).thenAnswer(inv -> {
            UrlMapping m = inv.getArgument(0);
            // simulate DB id assignment
            try {
                var f = UrlMapping.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(m, 123L);
            } catch (Exception ignored) {}
            return m;
        });

        UrlMapping out = svc.createShortUrl("https://example.com");
        assertNotNull(out.getCode());
        assertEquals("https://example.com", out.getLongUrl());
    }

    @Test
    void resolve_notFound() {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);
        when(repo.findByCode("abc")).thenReturn(Optional.empty());
        UrlService svc = new UrlService(repo);
        assertTrue(svc.resolve("abc").isEmpty());
    }
}
