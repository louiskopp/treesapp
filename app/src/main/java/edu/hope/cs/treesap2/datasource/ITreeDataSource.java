package edu.hope.cs.treesap2.datasource;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.hope.cs.treesap2.control.PrefManager;
import edu.hope.cs.treesap2.model.Tree;
import edu.hope.cs.treesap2.model.TreeLocation;

public class ITreeDataSource extends DataSource {

    String internetFileName = "iTreeExport_119_HopeTrees_7may2018.csv";
    String localFileName = "iTreeTreeData.csv";

    Reader in;
    Iterable<CSVRecord> records;
    CSVRecord closestRecord;

    Context parent;
    boolean downloaded, alreadyRead;

    public ITreeDataSource() {}

    public ITreeDataSource(Context aParent) {

        parent = aParent;

        downloaded = false;
        alreadyRead = false;
    }

    public String getInternetFileName() {
        return internetFileBase + "/" + internetFileName;
    }

    @Override
    public Boolean isDownloadable() {
        return true;
    }

    @Override
    public String getSourceName() {
        return "Hope College Tree Data from iTree";
    }

    @Override
    public String getDescription() {
        return "Tree Data from iTree about Pine Grove trees.";
    }

    private void download(String earl, String filename) {
        String str;

        downloaded = false;
        try {
            URL url = new URL(earl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));

            while ((str = in.readLine()) != null) {
                out.write(str);
                out.newLine();
            }

            in.close();
            downloaded = true;
            alreadyRead = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;

        if(!localFileName.contains("/")) {
            localFileName = parent.getFilesDir() + "/" + localFileName;
        }
        File HCfile = new File(localFileName);

        // Check to see if db is downloaded
        if (! HCfile.exists()) {
            updateDataSource();
        }

        return true;
    }

    private boolean readData() {

        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        alreadyRead = true;
        return true;
    }

    @Override
    public Tree search(TreeLocation location) {
        float[] results = new float[1];
        float closestDistance;
        float cap = PrefManager.getFloat("tree result", 10f);
        int entry, closestEntry;

        readData();
        closestDistance = 99999;
        closestEntry = 0;
        entry = -1;
        for (CSVRecord record : records) {
            entry++;
            try {
                Double lat = Double.valueOf(record.get(Tree.LATITUDE));
                Double longi = Double.valueOf(record.get(Tree.LONGITUDE));

                Location.distanceBetween(lat, longi,
                        location.getLatitude(), location.getLongitude(),
                        results);
//                if (results[0] > 2.0) {
                    if (results[0] < closestDistance) {
                        closestDistance = results[0];
                        closestRecord = record;
                    }
//                    continue;
//                }
//
//                //MATCH!  Build tree and return it.
//                Tree tree = new Tree();
//                tree.setCommonName(record.get(Tree.LONGITUDE));
//                tree.setLocation(new TreeLocation(lat, longi));
//                tree.setID(record.get(Tree.TREE_ID));
//                tree.setCurrentDBH(new Double(record.get(Tree.DBH)));
//                tree.addInfo("Age class", record.get(Tree.AGE_CLASS));
//                tree.addInfo("Condition", record.get(Tree.CONDITION));
//                tree.addInfo("Tree asset value", record.get(Tree.TREE_ASSET_VALUE));
//                tree.addInfo("Root infringement", record.get(Tree.ROOT_INFRINGEMENT));
//                tree.setFound(true);
//                tree.setIsClosest(true);
//
//                return tree;
            } catch (Exception e) {
                continue;
            }
        }

        if (closestDistance > cap)
            return null;
        else {
            //MATCH!  Build tree and return it.
            Tree tree = new Tree();
            Double lat = new Double(closestRecord.get(Tree.LATITUDE));
            Double longi = new Double(closestRecord.get(Tree.LONGITUDE));
            tree.setCommonName(closestRecord.get(Tree.NAME));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(new Double(closestRecord.get(Tree.DBH)));
            tree.addInfo("Age class", closestRecord.get(Tree.AGE_CLASS));
            tree.addInfo("Condition", closestRecord.get(Tree.CONDITION));
            tree.addInfo("Tree asset value", closestRecord.get(Tree.TREE_ASSET_VALUE));
            tree.addInfo("Root infringement", closestRecord.get(Tree.ROOT_INFRINGEMENT));
            tree.setFound(true);
            tree.setIsClosest(true);

            return tree;
        }

    }

    @Override
    public Boolean checkForUpdate() {
        return !isUpToDate();
    }

    @Override
    public Boolean isUpToDate() {

        try {
            File HCfile = new File(localFileName);
            if (HCfile.exists()) {
                URL url = new URL(internetFileName);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("HEAD");
                c.connect();
                InputStream in = c.getInputStream();
                return HCfile.lastModified() >= c.getLastModified();
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateDataSource() {
        Log.d("HCDataSource", "Downloading "+ getInternetFileName() +" to "+ localFileName);
        File HCfile = new File(localFileName);
        download(getInternetFileName(), localFileName);
        return true;
    }

    @Override
    public Iterable<CSVRecord> getCoordinates(Context context, String file) {
        localFileName = file;
        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

}
