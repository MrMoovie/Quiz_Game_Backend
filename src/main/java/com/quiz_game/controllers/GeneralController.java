package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.quiz_game.utils.Errors.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.Transactional;

@RestController
public class GeneralController {
    @Autowired
    private Persist persist;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() {
        this.loadTestData();
    }

    @RequestMapping("get-user-type")
    public BasicResponse getUser(String token){
        BasicUser user = persist.getUserByToken(token);
        if(user!=null){
            return new UserTypeResponse(true, null ,user);
        }else{
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/test")
    public BasicResponse loadTestData() {
        try {
            // Make sure your file in src/main/resources is renamed to test-data.sql
            Resource resource = new ClassPathResource("test-data.sql");
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);

            // Execute the script against your database
            databasePopulator.execute(dataSource);

            return new BasicResponse(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, 1);
        }
    }
    @RequestMapping("/delete-race")
    public BasicResponse deleteRace(int raceId) {
        try {
            // בודקים קודם כל אם המירוץ אכן קיים במערכת
            RaceEntity race = persist.getRaceByRaceId(raceId);

            if (race != null) {
                // הפעלת פונקציית המחיקה המדורגת שבנינו בתוך Persist
                persist.deleteRaceAndComponents(raceId);
                return new BasicResponse(true, null);
            } else {
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }

        } catch (Exception e) {
            // הדפסת השגיאה ללוג במידה ומשהו נכשל
            e.printStackTrace();
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
    }


    @RequestMapping("/get-default-params")
    public BasicResponse getDefaultParams (String token, Integer raceId) {
        BasicUser basicUser = persist.getUserByToken(token);

        String entryCode = null;
        if(raceId!=null) {
            RaceEntity race = persist.getRaceByRaceId(raceId);

            if(race!=null){
                entryCode = race.getEntryCode();
            }
        }
        if (basicUser != null) {
            return new DefaultParamsResponse(true, null, basicUser, entryCode);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }





}
