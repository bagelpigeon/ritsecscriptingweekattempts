package com.logscraper.model;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Log
{
    //CONSTANTS
    //entries in auth.log that have to do with ssh are denoted sshd
    static private String SSH_LINE_ID = "sshd";
    //entry that denotes an attempt at using an invalid user for ssh
    static private String INVALID_USER = "Invalid user";
    final private MAX_AUTH_TRIES;

    private int numOfSuccessLogins = 0;
    private int numOfFailedAttempts = 0;
    private int numOfInvalidUsersAttempts = 0;
    private String mostCommonLoginName = "";
    private HashMap<String, LoginAttempt> loginAttemptsRecord = new Hashmap<>();

    public Log ( String fileName, int maxAuthTries )
    {
        this.parseFile(fileName);
        this.MAX_AUTH_TRIES = maxAuthTries;
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
                //only pay attention to ssh entries
                if (line.contains(SSH_LINE_ID))
                {
                    parseLine(line);
                }
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

    /**
     * Counts invalid users as invalid attempts
     * Number of failed login attempts, total
     * @return
     */
    public int getNumOfFailedAttempts ( )
    {
        //iterate userlist
        return numOfFailedAttempts;
    }

    /**
     * Counts invalid users as invalid attempts
     * Number of failed login attempts, by user/ip
     * @return
     */
    public int getNumOfFailedAttempts ( username )
    {
        return numOfFailedAttempts;
    }

    public void printLocationsByIP ( )
    {

    }

}