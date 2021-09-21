package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizedPlayerImpl implements CustomizedPlayer {
    @Override
    public Optional<Player> findPlayer(Long id) {
        String queryString = "FROM Player Where id = " + id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Player> query = session.createQuery(queryString, Player.class);
        session.close();
        return query.uniqueResultOptional();
    }

    @Override
    public void savePlayer(Object Player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(Player);
        tx1.commit();
        session.close();
    }

    @Override
    public void deletePlayer(Object Player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(Player);
        tx1.commit();
        session.close();
    }

    @Override
    public void updatePlayer(Object Player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(Player);
        tx1.commit();
        session.close();
    }

    @Override
    public List findAllPlayers() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Player> players = session.createQuery("From Player").list();
        session.close();
        return players;
    }

    @Override
    public List findAllPlayersByParams(Map allParams, Optional pageNumber, Optional pageSize) {
        String queryString = buildQueryString(allParams);

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query<Player> query = session.createQuery(queryString, Player.class);

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

        List<Player> players = query.list();
        for (Player player: players) {
            player.setBirthdayTimezero();
        }
        session.close();
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
