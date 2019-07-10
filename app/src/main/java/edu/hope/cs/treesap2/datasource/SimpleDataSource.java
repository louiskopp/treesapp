package edu.hope.cs.treesap2.datasource;

import android.content.Context;

import java.io.File;

import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

public class SimpleDataSource extends DataSource {

    File dataFile;

    public SimpleDataSource() {}

    public String getSourceName() {
        return "Simple File Data Source";
    }

    public String getDescription() {
        return "Tree information stored in a local file.";
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {
        try {
            dataFile = new File(initString);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Tree search(TreeLocation location) {
        return null;
    }

    @Override
    public Boolean isDownloadable() {
        return false;
    }

    @Override
    public Boolean checkForUpdate() {
        return null;
    }

    @Override
    public Boolean updateDataSource() {
        return null;
    }

    @Override
    public Boolean isUpToDate() {
        return null;
    }
}
