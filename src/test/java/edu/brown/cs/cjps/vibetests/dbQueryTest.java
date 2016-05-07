package edu.brown.cs.cjps.vibetests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import edu.brown.cs.cjps.db.DBQuerier;

public class dbQueryTest {
    @Test
    public void userTableInsertion() {
        String db = "";
        DBQuerier querier;
        try {
            querier = new DBQuerier(db);
            querier.insertUser("12", "John");
            System.out.println(querier.userIsInDatabase("12"));
            assertTrue(querier.userIsInDatabase("12"));
            System.out.println(querier.userIsInDatabase("15"));
            assertTrue(!querier.userIsInDatabase("15"));


        } catch (SQLException e) {
            System.out.println("Error in db creation");
            assertTrue(1 == 0);
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not found");
            assertTrue(1 == 0);
        }

    }

    @Test
    public void eventTableInsertion() {
        String db = "";


    }
}
