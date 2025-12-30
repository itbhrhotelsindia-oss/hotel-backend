package com.example.hotelbackend.service;

import com.example.hotelbackend.model.WeddingPage;
import com.example.hotelbackend.repository.WeddingPageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public WeddingPage addFestivityItem(String pageId, WeddingPage.FestivityItem item) {

        WeddingPage page = repo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Wedding page not found"));

        if (page.getFestivities() == null) {
            page.setFestivities(new WeddingPage.Festivities());
        }

        if (page.getFestivities().getFestivitiesList() == null) {
            page.getFestivities().setFestivitiesList(new ArrayList<>());
        }

        page.getFestivities().getFestivitiesList().add(item);
        return repo.save(page);
    }

    public WeddingPage deleteFestivityItem(String pageId, int index) {

        WeddingPage page = repo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Wedding page not found"));

        if (page.getFestivities() != null &&
                page.getFestivities().getFestivitiesList() != null &&
                index >= 0 &&
                index < page.getFestivities().getFestivitiesList().size()) {

            page.getFestivities().getFestivitiesList().remove(index);
        }

        return repo.save(page);
    }

    public WeddingPage addWeddingTypeItem(String pageId, WeddingPage.WeddingItem item) {

        WeddingPage page = repo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Wedding page not found"));

        if (page.getTypeOfWeddings() == null) {
            page.setTypeOfWeddings(new WeddingPage.TypeOfWeddings());
        }

        if (page.getTypeOfWeddings().getWeddingList() == null) {
            page.getTypeOfWeddings().setWeddingList(new ArrayList<>());
        }

        page.getTypeOfWeddings().getWeddingList().add(item);
        return repo.save(page);
    }

    public WeddingPage deleteWeddingTypeItem(String pageId, int index) {

        WeddingPage page = repo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Wedding page not found"));

        if (page.getTypeOfWeddings() != null &&
                page.getTypeOfWeddings().getWeddingList() != null &&
                index >= 0 &&
                index < page.getTypeOfWeddings().getWeddingList().size()) {

            page.getTypeOfWeddings().getWeddingList().remove(index);
        }

        return repo.save(page);
    }

}

