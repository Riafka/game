package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomizedPlayerImpl implements CustomizedPlayer {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Player> findPlayer(Long id) {
        String queryString = "FROM Player Where id = " + id;
        Player player = entityManager.find(Player.class,id);
        Optional<Player> result = Optional.of(player);
        return result;
    }

    @Override
    public void savePlayer(Object Player) {
        entityManager.getTransaction().begin();
        entityManager.persist(Player);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deletePlayer(Object Player) {
        entityManager.getTransaction().begin();
        entityManager.remove(Player);
        entityManager.getTransaction().commit();
    }

    @Override
    public void updatePlayer(Object Player) {
        entityManager.getTransaction().begin();
        entityManager.persist(Player);
        entityManager.getTransaction().commit();
    }

    @Override
    public List findAllPlayers() {
        List<Player> players = entityManager.createQuery("From Player").getResultList();
        return players;
    }

    @Override
    public List findAllPlayersByParams(Map allParams, Optional pageNumber, Optional pageSize) {
        String queryString = buildQueryString(allParams);
        Query query = entityManager.createQuery(queryString);

        int page = 0;
        if (pageNumber.isPresent()){
            page = (int) pageNumber.get();
        }
        int maxResult = 3;
        if (pageSize.isPresent()){
            maxResult = (int) pageSize.get();
        }
        query.setFirstResult(page*maxResult);
        query.setMaxResults(maxResult);

        List<Player> players = query.getResultList();
        return players;
    }
    private String buildQueryString(Map<String, String> allParams){
        String queryString = "Select P FROM " + Player.class.getName() + " P";
        String whereString = "";
        String orderString = "";
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for ( Map.Entry<String, String> entry: allParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(!key.equals("pageNumber") && !key.equals("pageSize")&&!key.equals("PlayerOrder")){
                if (!whereString.isEmpty()){
                    whereString = whereString + " AND ";
                }

                switch (key){
                    case "name" :
                    case "title" :
                        whereString += "P." + key + " LIKE '%" + value + "%'"; break;
                    case "race": whereString += " P.race = '" + value +"'"; break;
                    case "profession": whereString += " P.profession = '" + value +"'"; break;
                    case "after":
                        date = Instant.ofEpochMilli(Long.parseLong(value)).atZone(ZoneId.systemDefault()).toLocalDate();
                        whereString += " P.birthday >= '" + date.format(formatter)+ "'"; break;
                    case "before":
                        date = Instant.ofEpochMilli(Long.parseLong(value)).atZone(ZoneId.systemDefault()).toLocalDate();
                        whereString += " P.birthday <= '" + date.format(formatter)+ "'"; break;
                    case "minExperience": whereString += " P.experience >= " + value; break;
                    case "maxExperience": whereString += " P.experience <= " + value; break;
                    case "minLevel": whereString += " P.level >= " + value; break;
                    case "maxLevel": whereString += " P.level <= " + value; break;
                    case "banned": whereString += "P." + key + " = " + value; break;
                    default:      whereString += "P." + key + " = '" + value + "'";

                }
            }
            if (key.equals("PlayerOrder")){
                orderString = " ORDER BY P." + value.toLowerCase();
            }
        }
        if (!whereString.isEmpty()){
            whereString = " Where " + whereString;
            queryString += whereString;
        }
        if (orderString.isEmpty()){
            orderString = " ORDER BY P." + PlayerOrder.ID.toString().toLowerCase();
        }
        if (!orderString.isEmpty()){
            queryString += orderString;
        }
        return queryString;
    }
}
