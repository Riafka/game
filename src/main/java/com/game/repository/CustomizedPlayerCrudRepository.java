package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizedPlayerCrudRepository extends CustomizedPlayer<Player>, CrudRepository<Player, Long> {
}
