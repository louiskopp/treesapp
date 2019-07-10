package edu.hope.cs.treesap2;

import org.junit.Test;

import edu.hope.cs.treesap2.model.GPSCoordinates;

import static org.junit.Assert.*;

public class TestGPSCoordinates {

    GPSCoordinates gps = new GPSCoordinates();
    GPSCoordinates compare = new GPSCoordinates(45.12345, -89.54321);
    GPSCoordinates compare2 = new GPSCoordinates(45.12345, 89.54321);

    @Test
    public void test1() {
        gps.parse("45.12345,-89.54321");
        assertTrue(gps.equals(compare));

        gps.parse("45.12345 W, 89.54321 N");
        assertTrue(gps.equals(compare2));

        gps.parse("45.12345w, 89.54321n");
        assertTrue(gps.equals(compare2));

        gps.parse("W45.12345, N89.54321");
        assertTrue(gps.equals(compare2));

        gps.parse("45 7 24.4201,-89 32 35.556");
        assertTrue(gps.equals(new GPSCoordinates(45.12345002777778, -89.54321)));
    }
}
