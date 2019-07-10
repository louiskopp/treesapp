package edu.hope.cs.treesap2.datasource;

import android.content.Context;


import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;

import edu.hope.cs.treesap2.model.SettingsOption;
import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

public abstract class DataSource {

    protected Context parent;
    protected String internetFileBase = "https://www.cs.hope.edu/~jipping/treesap";
    String internetFileName;
    String localFileName;

    Iterable<CSVRecord> container;
    HashMap<String, SettingsOption> preferences;


    public String getSourceName() {
        return null;
    }
    public String getDescription() { return null; };
    public void setParent(Context context) { parent = context; }

    public abstract Boolean initialize(Context context, String initString);
    public abstract Tree search(TreeLocation location);

    public abstract Boolean isDownloadable();
    public abstract Boolean checkForUpdate();
    public abstract Boolean isUpToDate();
    public Boolean isOutOfDate() {return !isUpToDate();}
    public abstract Boolean updateDataSource();

    public String getInternetFileBase() { return internetFileBase; }
    public String getInternetFileName() { return internetFileBase + "/" + internetFileName; }
    public String getLocalFileName() { return localFileName; }

    public boolean equals(DataSource ds) {
        return this.getSourceName().equals(ds.getSourceName());
    }

    public Iterable<CSVRecord> getCoordinates(Context context, String file) {
        return container;
    }

    public HashMap<String, SettingsOption> getPreferences(){return preferences;}


    // Flexible field name assignment
    public enum FieldID {
        TREE_ID
    };
    //abstract public String map(FieldID fid);
    //abstract public FieldID map(String fieldName);
}
