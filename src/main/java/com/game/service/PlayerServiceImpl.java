package com.game.service;

import com.game.entity.Player;
import com.game.repository.CustomizedPlayerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private CustomizedPlayerCrudRepository playerCrudRepository;
    @Override
    public Optional<Player> findPlayer(long id) {
        return playerCrudRepository.findById(id);
    }

    @Override
    public void savePlayer(Player Player) {
        Player.calcRating();
        playerCrudRepository.save(Player);
    }

    @Override
    public void deletePlayer(Player Player) {
        playerCrudRepository.delete(Player);
    }

    @Override
    public void updatePlayer(Player Player) {
        Player.calcRating();
        playerCrudRepository.save(Player);
    }

    @Override
    public Iterable<Player> findAllPlayers() {
        return playerCrudRepository.findAllPlayers();
    }

    @Override
    public List<Player> findAllPlayersByParams(Map<String, String> allParams, Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        return playerCrudRepository.findAllPlayersByParams(allParams,pageNumber,pageSize);
    }
}
