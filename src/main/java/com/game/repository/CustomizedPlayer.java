package com.game.repository;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomizedPlayer<T> {
    Optional<Player> findPlayer(Long id);

    void savePlayer(T Player);

    void deletePlayer(T Player);

    void updatePlayer(T Player);

    List<T> findAllPlayers();

    List<T> findAllPlayersByParams(Map<String, String> allParams, Optional<Integer> pageNumber, Optional<Integer> pageSize);
}
