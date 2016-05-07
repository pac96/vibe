package edu.brown.cs.cjps.vibetests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import edu.brown.cs.cjps.db.UserDBCreator;

public class dbCreatorTests {


    @Test
    public void invalidDBFile() {
        String db = "notARealDB";
        try {
            UserDBCreator userDB = new UserDBCreator(db);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("The db is invalid");
            assertTrue(true);
            e.printStackTrace();
        }
    }

    @Test
    public void validDBFileTableCreation() {
        String db = "vibe.sqlite3";
        try {
            UserDBCreator userDB = new UserDBCreator(db);
            System.out.println("file created");
            assertTrue(true);
        } catch (ClassNotFoundException | SQLException e) {
            assertTrue(1 == 0);
        }
    }



}
