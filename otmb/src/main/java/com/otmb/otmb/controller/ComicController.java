package com.otmb.otmb.controller;

import com.otmb.otmb.model.Book;
import com.otmb.otmb.model.Comic;
import com.otmb.otmb.model.Library;
import com.otmb.otmb.repository.BookRepository;
import com.otmb.otmb.repository.ComicRepository;
import com.otmb.otmb.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class ComicController {

    private final ComicRepository comicRepository;
    private final LibraryRepository libraryRepository;

    @Autowired
    public ComicController(ComicRepository comicRepository, LibraryRepository libraryRepository) {
        this.comicRepository = comicRepository;
        this.libraryRepository = libraryRepository;
    }

    @PostMapping
    public ResponseEntity<Comic> create(@RequestBody @Valid Comic comic) {
        Optional<Library> optionalLibrary = libraryRepository.findById(comic.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comic.setLibrary(optionalLibrary.get());

        Comic saveComic = comicRepository.save(comic);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saveComic.getId()).toUri();

        return ResponseEntity.created(location).body(saveComic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comic> update(@RequestBody @Valid Comic comic, @PathVariable Integer id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(comic.getLibrary().getId());
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Comic> optionalComic = comicRepository.findById(id);
        if (!optionalComic.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comic.setLibrary(optionalLibrary.get());
        comic.setId(optionalComic.get().getId());
        comicRepository.save(comic);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comic> delete(@PathVariable Integer id) {
        Optional<Comic> optionalComic = comicRepository.findById(id);
        if (!optionalComic.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comicRepository.delete(optionalComic.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Comic>> getAll(Pageable pageable) {
        return ResponseEntity.ok(comicRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comic> getById(@PathVariable Integer id) {
        Optional<Comic> optionalComic = comicRepository.findById(id);
        if (!optionalComic.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalComic.get());
    }
}
