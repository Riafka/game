package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlayerController {
    @Autowired
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {

        this.playerService = playerService;
    }
    @GetMapping(value = "/rest/players")
    public ResponseEntity<List<Player>> read(@RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> pageSize, @RequestParam Map<String,String> allParams) {
        final List<Player> players = playerService.findAllPlayersByParams(allParams,pageNumber,pageSize);
        return players != null
                ? new ResponseEntity<>(players, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<?> count(@RequestParam Map<String,String> allParams) {
        final List<Player> players = playerService.findAllPlayersByParams(allParams,Optional.of(0),Optional.of(Integer.MAX_VALUE));
        return players != null &&  !players.isEmpty()
                ? new ResponseEntity<>(players.size(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/rest/players/{id}")
    @ResponseBody
    public ResponseEntity<?> id(@PathVariable("id")String id) {
        if (id.matches("[0-9]+"))
        {
            Long idToLong = Long.valueOf(id);
            if (idToLong == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Player> player = playerService.findPlayer(idToLong);

            return player.isPresent()
                    ? new ResponseEntity<>(player.get(), HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/rest/players/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id")String id) {
        if (id.matches("[0-9]+"))
        {
            Long idToLong = Long.valueOf(id);
            if (idToLong == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Player> player = playerService.findPlayer(idToLong);
            if (player.isPresent()){
                playerService.deletePlayer(player.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/rest/players")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Map<String, String> allparams){
        String name = allparams.get("name");
        String title = allparams.get("title");
        String race = allparams.get("race");
        String profession = allparams.get("profession");
        String birthday = allparams.get("birthday");
        String banned = allparams.get("banned");
        String experience = allparams.get("experience");
        if (name == null || title == null || race == null || profession == null || birthday == null || experience == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player player = new Player();
        try {
            player.setNameFromFront(name);
            player.setTitleFromFront(title);
            player.setProfession(Profession.valueOf(profession));
            player.setRace(Race.valueOf(race));
            player.setBirthdayFromFront(birthday);
            player.setBanned( banned == null ? false: Boolean.parseBoolean(banned));
            player.setExperienceFromFront(experience);
            playerService.savePlayer(player);
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/rest/players/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("id")String id, @RequestBody Player newPlayer) {
        if (id.matches("[0-9]+"))
        {
            Long idToLong = Long.valueOf(id);
            if (idToLong == 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Player> player = playerService.findPlayer(idToLong);
            if (!player.isPresent()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else{
                Player oldPlayer = player.get();
                try {
                    if (newPlayer.getName() != null){
                        oldPlayer.setNameFromFront(newPlayer.getName());
                    }
                    if (newPlayer.getTitle() != null){
                        oldPlayer.setTitleFromFront(newPlayer.getTitle());
                    }
                    if (newPlayer.getRace() != null){
                        oldPlayer.setRace(newPlayer.getRace());
                    }
                    if (newPlayer.getProfession() != null){
                        oldPlayer.setProfession(newPlayer.getProfession());
                    }
                    if (newPlayer.getBirthday() != null){
                        oldPlayer.setBirthdayFromFront(String.valueOf(newPlayer.getBirthday().getTime()));
                    }
                    if (newPlayer.getBanned() != null){
                        oldPlayer.setBanned(newPlayer.getBanned());
                    }
                    if (newPlayer.getExperience() != null){
                        oldPlayer.setExperienceFromFront(newPlayer.getExperience().toString());
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (!newPlayer.playerIsNull()){
                    playerService.updatePlayer(oldPlayer);
                }
                return new ResponseEntity<>(oldPlayer, HttpStatus.OK);
            }

        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
