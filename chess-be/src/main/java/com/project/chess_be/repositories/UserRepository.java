package com.project.chess_be.repositories;

import com.project.chess_be.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.userId FROM User u WHERE u.username = :username")
    List<Integer> getUserId(@Param("username") String name);

    @Query("SELECT u FROM User u WHERE u.game.gameId = :gameId")
    List<User> getUserIdByGameId(@Param("gameId") Integer gameId);

    User findByUsername(String username);
}
