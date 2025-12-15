package com.example.hotelbackend.service;

import com.example.hotelbackend.model.WeddingPage;
import com.example.hotelbackend.repository.WeddingPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeddingPageService {

    private final WeddingPageRepository repo;

    public WeddingPageService(WeddingPageRepository repo) {
        this.repo = repo;
    }

    public WeddingPage save(WeddingPage page) {
        return repo.save(page);
    }

    public List<WeddingPage> getAll() {
        return repo.findAll();
    }

    public WeddingPage getOne(String id) {
        return repo.findById(id).orElse(null);
    }

    public WeddingPage update(String id, WeddingPage data) {
        data.setId(id);
        return repo.save(data);
    }

    public List<WeddingPage> saveAll(List<WeddingPage> pages) {
        return repo.saveAll(pages);
    }


    public void delete(String id) {
        repo.deleteById(id);
    }
}

