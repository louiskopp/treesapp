package edu.hope.cs.treesap2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.hope.cs.treesap2.identify.SimpleIdentifier;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestSimpleIdentifier {

    Context appContext;

    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.hope.cs.treesap2", appContext.getPackageName());
    }

    @Test
    public void testGetMethodName() {
        useAppContext();

        SimpleIdentifier si = new SimpleIdentifier();
        si.setParent(appContext);

        assertTrue(si.getMethodName().equals("Simple Identification by Manual Coordinate Entry"));

    }

    @Test
    public void testGetDescription() {
        useAppContext();

        SimpleIdentifier si = new SimpleIdentifier();
        si.setParent(appContext);

        assertNull(si.getDescription());
    }

    @Test
    public void testIdentify() {
        useAppContext();

        SimpleIdentifier si = new SimpleIdentifier();
        si.setParent(appContext);

        si.identify();
    }

}
