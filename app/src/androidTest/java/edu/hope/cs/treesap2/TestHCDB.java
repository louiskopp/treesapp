package edu.hope.cs.treesap2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.hope.cs.treesap2.datasource.HopeCollegeDataSource;
import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestHCDB {

    HopeCollegeDataSource hopeDB;
    Context appContext;

    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.hope.cs.treesap2", appContext.getPackageName());
    }

    @Test
    public void testInitialize() {
        useAppContext();

        hopeDB = new HopeCollegeDataSource(appContext);
        hopeDB.initialize(appContext,null);

        assertTrue(hopeDB.isUpToDate());
        assertFalse(hopeDB.isOutOfDate());
        assertFalse(hopeDB.checkForUpdate());
        assertTrue(hopeDB.isDownloadable());
    }

    @Test
    public void testSearch1() {
        useAppContext();

        hopeDB = new HopeCollegeDataSource(appContext);
        hopeDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.78823471);
        loc.setLongitude(-86.10377502);
        Tree t = hopeDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Maple-Red"));
        assertNull(t.getScientificName());
        assertTrue(t.getLocation().getLatitude() == 42.78823471);
        assertTrue(t.getLocation().getLongitude() == -86.10377502);
        assertTrue(t.getID().equals("12"));
        assertTrue(t.getAllInfo().equals("Condition: Good\n" +
                "Tree asset value: 10904.83\n" +
                "Age class: Mature\n" +
                "Root infringement: 25-50%"));
    }


    @Test
    public void testSearch2() {
        useAppContext();

        hopeDB = new HopeCollegeDataSource(appContext);
        hopeDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.78783417);
        loc.setLongitude(-86.10136414);
        Tree t = hopeDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Beech-European"));
        assertNull(t.getScientificName());
        assertTrue(t.getLocation().getLatitude() == 42.78783417);
        assertTrue(t.getLocation().getLongitude() == -86.10136414);
        assertTrue(t.getID().equals("69"));
        assertTrue(t.getAllInfo().equals("Condition: Good\n" +
                "Tree asset value: 399.91\n" +
                "Age class: Young\n" +
                "Root infringement: <25%"));
    }

    @Test
    public void testSearch3() {
        useAppContext();

        hopeDB = new HopeCollegeDataSource(appContext);
        hopeDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.78684998);
        loc.setLongitude(-86.1034317);
        Tree t = hopeDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Coffeetree-Kentucky"));
        assertNull(t.getScientificName());
        assertTrue(t.getLocation().getLatitude() == 42.78684998);
        assertTrue(t.getLocation().getLongitude() == -86.1034317);
        assertTrue(t.getID().equals("153"));
        assertTrue(t.getAllInfo().equals("Condition: Good\n" +
                "Tree asset value: 8516.71\n" +
                "Age class: Mature\n" +
                "Root infringement: 25-50%"));
    }

    @Test
    public void testSearchNotThere() {
        useAppContext();

        hopeDB = new HopeCollegeDataSource(appContext);
        hopeDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(3.14159);
        loc.setLongitude(0.0);
        Tree t = hopeDB.search(loc);
        assertNull(t);
    }


}
