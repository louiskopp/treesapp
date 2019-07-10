package edu.hope.cs.treesap2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.hope.cs.treesap2.datasource.CityOfHollandDataSource;
import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestCOHDB {

    CityOfHollandDataSource cityDB;
    Context appContext;

    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.hope.cs.treesap2", appContext.getPackageName());
    }

    @Test
    public void testInitialize() {
        useAppContext();

        cityDB = new CityOfHollandDataSource(appContext);
        cityDB.initialize(appContext, null);

        assertTrue(cityDB.isUpToDate());
        assertFalse(cityDB.isOutOfDate());
        assertFalse(cityDB.checkForUpdate());
        assertTrue(cityDB.isDownloadable());
    }

    @Test
    public void testSearch1() {
        useAppContext();;

        cityDB = new CityOfHollandDataSource(appContext);
        cityDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.788394625);
        loc.setLongitude(-86.107450718);
        Tree t = cityDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Sugar maple"));
        assertTrue(t.getScientificName().equals("Acer saccharum"));
        assertTrue(t.getLocation().getLatitude() == 42.788394625);
        assertTrue(t.getLocation().getLongitude() == -86.107450718);
        assertTrue(t.getID().equals("1"));
        assertTrue(t.getAllInfo().equals("Park: Centennial Park"));
    }


    @Test
    public void testSearch2() {
        useAppContext();

        cityDB = new CityOfHollandDataSource(appContext);
        cityDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.78829926100);
        loc.setLongitude(-86.10828459300);
        Tree t = cityDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Yellow poplar"));
        assertTrue(t.getScientificName().equals("Liriodendron tulipifera"));
        assertTrue(t.getLocation().getLatitude() == 42.78829926100);
        assertTrue(t.getLocation().getLongitude() == -86.10828459300);
        assertTrue(t.getID().equals("9"));
        assertTrue(t.getAllInfo().equals("Park: Centennial Park"));
    }

    @Test
    public void testSearch3() {
        useAppContext();

        cityDB = new CityOfHollandDataSource(appContext);
        cityDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(42.78763881500);
        loc.setLongitude(-86.06207519200);
        Tree t = cityDB.search(loc);
        assertNotNull(t);
        assertTrue(t.getCommonName().equals("Japanese flowering cherry"));
        assertTrue(t.getScientificName().length() == 0);
        assertTrue(t.getLocation().getLatitude() == 42.787638815);
        assertTrue(t.getLocation().getLongitude() == -86.062075192);
        assertTrue(t.getID().equals("1244"));
        assertNull(t.getAllInfo());
    }

    @Test
    public void testSearchNotThere() {
        useAppContext();

        cityDB = new CityOfHollandDataSource(appContext);
        cityDB.initialize(appContext,null);

        TreeLocation loc = new TreeLocation();
        loc.setLatitude(3.14159);
        loc.setLongitude(0.0);
        Tree t = cityDB.search(loc);
        assertNull(t);
    }


}
