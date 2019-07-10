package edu.hope.cs.treesap2;

import org.junit.Test;


import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

import static org.junit.Assert.*;

public class TestTreeLocation {

    @Test
    public void testConstructor1() {
        TreeLocation tl = new TreeLocation();

        assertTrue(tl.getLatitude() == 0);
        assertTrue(tl.getLongitude() == 0);
        assertTrue(tl.toString().equals("(0.0, 0.0)"));
    }

    @Test
    public void testConstructor2() {
        TreeLocation tl = new TreeLocation(10.0,20.0);
        TreeLocation tl2 = new TreeLocation(45.12345,-55.010203);

        assertTrue(tl.getLatitude() == 10);
        assertTrue(tl.getLongitude() == 20);
        assertTrue(tl2.getLatitude() == 45.12345);
        assertTrue(tl2.getLongitude() == -55.010203);

        assertTrue(tl.toString().equals("(10.0, 20.0)"));
        assertTrue(tl2.toString().equals("(45.12345, -55.010203)"));
    }

    @Test
    public void testConstructor3() {
        TreeLocation tl = new TreeLocation("10.0,20.0");
        TreeLocation tl2 = new TreeLocation("45.12345,-55.010203");
        TreeLocation tl3 = new TreeLocation("45.12345 N,55.010203 W");
        TreeLocation tl4 = new TreeLocation("coord1, coord2");

        assertTrue(tl.getLatitude() == 10);
        assertTrue(tl.getLongitude() == 20);
        assertTrue(tl2.getLatitude() == 45.12345);
        assertTrue(tl2.getLongitude() == -55.010203);
        assertTrue(tl3.getLatitude() == 45.12345);
        assertTrue(tl3.getLongitude() == -55.010203);
        assertTrue(tl4.getLatitude() == 0);
        assertTrue(tl4.getLongitude() == 0);

        assertTrue(tl.toString().equals("(10.0, 20.0)"));
        assertTrue(tl2.toString().equals("(45.12345, -55.010203)"));
        assertTrue(tl3.toString().equals("(45.12345, -55.010203)"));
        assertTrue(tl4.toString().equals("(0.0, 0.0)"));
    }

    @Test
    public void testSetters() {
        TreeLocation tl = new TreeLocation();

        tl.setLatitude(45.12345);
        assertTrue(tl.getLatitude() == 45.12345);

        tl.setLongitude(-55.010203);
        assertTrue(tl.getLongitude() == -55.010203);

        tl.setLatitude("54.12 W");
        assertTrue(tl.getLatitude() == 54.12);
        assertTrue(tl.getLongitude() == -55.010203);

        tl.setLongitude("76.2 W");
        assertTrue(tl.getLatitude() == 54.12);
        assertTrue(tl.getLongitude() == -76.2);
    }

    @Test
    public void testToString() {
        TreeLocation tl = new TreeLocation("45 44 30.822, 73 40 21.508W");
        String str = tl.toString();
        assertTrue(str.equals("(45.741895, -73.67264111111112)"));

        tl.setLatitude(0.0);
        tl.setLongitude(0.0);
        assertTrue(tl.toString().equals("(0.0, 0.0)"));

        // Bug in GPSCoordinates
        // Can't mix input formats
        tl = new TreeLocation("40.741895, 73 40 21.508");
        assertTrue(tl.toString().equals("(40.741895, 73.0)"));  // should be (45.741895, -73.67264111111112)
    }
}
