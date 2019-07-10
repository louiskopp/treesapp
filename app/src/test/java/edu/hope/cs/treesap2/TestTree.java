package edu.hope.cs.treesap2;

import org.junit.Test;


import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

import static org.junit.Assert.*;

public class TestTree {

    @Test
    public void testTreeConstructors() {
        Tree tree = new Tree();

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());
    }

    @Test
    public void testTree1() {
        Tree tree = new Tree();

        tree.setCommonName("JipTree");
        assertNotNull(tree.getCommonName());
        assertTrue(tree.getCommonName().equals("JipTree"));

        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());
    }

    @Test
    public void testTree2() {
        Tree tree = new Tree();

        tree.setScientificName("Jippingus Treeingus");
        assertNotNull(tree.getScientificName());
        assertTrue(tree.getScientificName().equals("Jippingus Treeingus"));

        assertNull(tree.getCommonName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());
    }

    @Test
    public void testTree3() {
        Tree tree = new Tree();

        TreeLocation treeLocation = new TreeLocation(45.0, 52.0);
        tree.setLocation(treeLocation);
        assertTrue(tree.getLocation().getLatitude() == 45);
        assertTrue(tree.getLocation().getLongitude() == 52);

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());

    }

    @Test
    public void testTree4() {
        Tree tree = new Tree();

        tree.setFound(true);
        assertTrue(tree.isFound());
        tree.setFound(false);
        assertFalse(tree.isFound());

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());

    }

    @Test
    public void testTree5() {
        Tree tree = new Tree();

        tree.setIsClosest(true);
        assertTrue(tree.isClosest());
        tree.setIsClosest(false);
        assertFalse(tree.isClosest());

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());

    }

    @Test
    public void testTree6() {
        Tree tree = new Tree();

        tree.setID("Jip-1018");
        assertNotNull(tree.getID());
        assertTrue(tree.getID().equals("Jip-1018"));
        tree.setID(1018);
        assertNotNull(tree.getID());
        assertTrue(tree.getID().equals("1018"));

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getCurrentDBH());
        assertFalse(tree.isOtherInfoPresent());
    }

    @Test
    public void testTree7() {
        Tree tree = new Tree();

        tree.setCurrentDBH(12.5);
        assertNotNull(tree.getCurrentDBH());
        assertTrue(tree.getCurrentDBH() == 12.5);
        tree.setCurrentDBH(-2.15);
        assertNotNull(tree.getCurrentDBH());
        assertTrue(tree.getCurrentDBH() == -2.15);

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertFalse(tree.isOtherInfoPresent());
    }


    @Test
    public void testTree8() {
        Tree tree = new Tree();

        String dump = tree.getAllInfo();
        assertNull(dump);

        tree.addInfo("Old DBH", "45");
        assertTrue(tree.isOtherInfoPresent());
        tree.addInfo("Older DBH", "52");
        assertTrue(tree.isOtherInfoPresent());
        assertTrue(tree.getInfo("Older DBH").equals("52"));
        assertTrue(tree.getInfo("Old DBH").equals("45"));
        tree.addInfo("DBH-1", 53);
        tree.addInfo("DBH-2", 10.18);
        assertTrue(((Integer)tree.getInfo("DBH-1")) == 53);
        assertTrue(((Double)tree.getInfo("DBH-2")) == 10.18);
        tree.addInfo("Park", "Smallenberg Park");
        assertTrue(((String)tree.getInfo("Park")).equals("Smallenberg Park"));

        dump = tree.getAllInfo();
        assertTrue(dump.equals("DBH-2: 10.18\nDBH-1: 53\nOlder DBH: 52\nOld DBH: 45\nPark: Smallenberg Park"));

        assertNull(tree.getCommonName());
        assertNull(tree.getScientificName());
        assertNull(tree.getLocation());
        assertFalse(tree.isFound());
        assertFalse(tree.isClosest());
        assertNull(tree.getID());
        assertNull(tree.getCurrentDBH());
    }


}