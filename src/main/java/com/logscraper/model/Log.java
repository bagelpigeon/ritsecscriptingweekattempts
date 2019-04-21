package com.logscraper.model;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Log
{
    private int numOfSuccessLogins = 0;
    private int numOfFailedAttempts = 0;
    private String mostCommonLoginName = "";
    private HashMap<String, String> ipAddressLocation = new HashMap<>();

    public Log ( String fileName )
    {
        this.parseFile(fileName);
    }

    public void parseFile ( String fileName )
    {
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null)
            {
                line = reader.readLine();
                parseLine(line);
            }
            reader.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void parseLine(String line)
    {

    }

    public String getMostCommonLoginUser ( )
    {
        if (mostCommonLoginName.equals(""))
        {
           return "No logins were attempted.";
        }
        return mostCommonLoginName;
    }

    public int getNumOfSuccessLogins ( )
    {
        return numOfSuccessLogins;
    }

    public int getNumOfFailedAttempts ( )
    {
        return numOfFailedAttempts;
    }

    public void printLocationsByIP ( )
    {

    }

}