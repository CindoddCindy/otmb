package com.otmb.otmb.repository;
import com.otmb.otmb.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookRepository  extends JpaRepository<Book, Integer>{
}
