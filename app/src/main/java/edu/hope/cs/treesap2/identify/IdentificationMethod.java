package edu.hope.cs.treesap2.identify;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import edu.hope.cs.treesap2.model.TreeLocation;

public abstract class IdentificationMethod {

    public interface IdentificationCallback {
        void idCompleted();
    }

    Context parent;
    ArrayList<String> preferences;

    public IdentificationMethod() { parent = null; }
    public IdentificationMethod(Context context) {
        parent = context;
    }
    public void setParent(Context context) { parent = context; }

    public abstract String getMethodName();
    public String getDescription() { return null; };

    public abstract void identify();
    public abstract TreeLocation getLocation();

    public class Callback implements IdentificationCallback {
        public void idCompleted () {
            Log.i("IdMethod", "ID Completed. Tree location = "+getLocation().toString());
        }
    }
    public IdentificationCallback callbackClass = new Callback();
    public void registerIDCallback(IdentificationCallback callbackClass) {
        this.callbackClass = callbackClass;
    }

    public ArrayList<String> getPreferences() {return preferences;}
}
