package ru.netology.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import ru.netology.diploma.entities.User;

public interface UserRepository extends CrudRepository<User, String> {
}
