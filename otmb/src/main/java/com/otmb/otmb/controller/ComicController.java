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
@RequestMapping("/api/v1/comics")
public class ComicController {

    private final ComicRepository comicRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ComicController(ComicRepository comicRepository, BookRepository bookRepository) {
        this.comicRepository = comicRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public ResponseEntity<Comic> create(@Valid @RequestBody Comic comic) {
        Comic savedComic = comicRepository.save(comic);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedComic.getId()).toUri();

        return ResponseEntity.created(location).body(savedComic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comic> update(@PathVariable Integer id, @Valid @RequestBody Comic comic) {
        Optional<Comic> optionalLibrary = comicRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comic.setId(optionalLibrary.get().getId());
        comicRepository.save(comic);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comic> delete(@PathVariable Integer id) {
        Optional<Comic> optionalLibrary = comicRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        comicRepository.delete(optionalLibrary.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comic> getById(@PathVariable Integer id) {
        Optional<Comic> optionalLibrary = comicRepository.findById(id);
        if (!optionalLibrary.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalLibrary.get());
    }

    @GetMapping
    public ResponseEntity<Page<Comic>> getAll(Pageable pageable) {
        return ResponseEntity.ok(comicRepository.findAll(pageable));
    }
}
