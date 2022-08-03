package ru.netology.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.diploma.entities.Token;

public interface TokenRepository extends CrudRepository<Token, String> {
}
