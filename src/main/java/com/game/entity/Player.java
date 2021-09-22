package com.game.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {
    public final static int MAX_EXPERIENCE = 10000000;
    public final static int MIN_YEAR = 2000;
    public final static int MAX_YEAR = 3000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;

    private Date birthday;
    private Boolean banned;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setNameFromFront(String name) throws Exception{
        if (name == null|| name.isEmpty() || name.length() > 12){
            throw new Exception();
        }
        setName(name);
    }
    public void setTitleFromFront(String title) throws Exception{
        if (title == null|| title.length() > 30){
            throw new Exception();
        }
        setTitle(title);
    }
    public void setExperienceFromFront(String exp) throws Exception{
        if (exp == null){
            throw new Exception();
        }
        Integer expInt = Integer.parseInt(exp);
        if (expInt < 0 || expInt > MAX_EXPERIENCE){
            throw new Exception();
        }
        setExperience(expInt);
    }
    public void setBirthdayFromFront(String birthdayFromFront) throws Exception{
        Long birthdayLong = Long.parseLong(birthdayFromFront);
        if (birthdayFromFront.isEmpty() || birthdayLong < 0){
            throw new Exception();
        }
        Date birthday = new Date(birthdayLong);
        setBirthday(birthday);

        Integer year = getBirthdayYear();
        if (year < MIN_YEAR || year > MAX_YEAR){
            throw new Exception();
        }

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }


    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, birthday, banned, experience, level, untilNextLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player that = (Player) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(title, that.title) &&
                race == that.race &&
                profession == that.profession &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(banned, that.banned) &&
                Objects.equals(experience, that.experience) &&
                Objects.equals(level, that.level) &&
                Objects.equals(untilNextLevel, that.untilNextLevel);
    }

    @Override
    public String toString() {
        LocalDate birthday = Instant.ofEpochMilli(this.birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        return "PlayerInfoTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday.toEpochDay() +
                '}';
    }
    public void calcRating(){
        double level = (Math.sqrt(2500 + 200*experience) - 50)/100;
        BigDecimal bd = BigDecimal.valueOf(level);
        bd = bd.setScale(2, RoundingMode.DOWN);
        this.level = bd.intValue();

        this.untilNextLevel = 50 * (this.level + 1) * (this.level + 2) - experience;
    }
    public int getBirthdayYear() {
        Calendar calendarThis = Calendar.getInstance();
        calendarThis.setTimeInMillis(birthday.getTime());
        return calendarThis.get(Calendar.YEAR);
    }
    public boolean playerIsNull(){
        if (name == null && title == null && race == null && profession == null && birthday == null && banned == null && experience == null){
            return true;
        }
        return false;
    }
}
