package ru.netology.diploma.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.diploma.entities.File;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, String> {
    @Query(value = "SELECT * FROM FILES LIMIT :limit", nativeQuery = true)
    List<File> getFiles(int limit);
}
