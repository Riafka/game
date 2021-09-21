package com.game.service;

import com.game.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface PlayerService {
    Optional<Player> findPlayer(long id);
    void savePlayer(Player Player);
    void deletePlayer(Player Player);
    void updatePlayer(Player Player);
    Iterable<Player> findAllPlayers();
    List<Player> findAllPlayersByParams(Map<String,String> allParams, Optional<Integer> pageNumber, Optional<Integer> pageSize);
}
