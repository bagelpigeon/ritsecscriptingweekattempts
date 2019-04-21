package com.logparser.model;
import com.logparser.model.LoginAttempt;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Log
{
    //CONSTANTS
    //entries in auth.log that have to do with ssh are denoted sshd
    static private String SSH_LINE_ID = "sshd";
    static private String SEPARATOR = " ";
    final private int MAX_AUTH_TRIES;

    private int numOfSuccessLogins = 0;
    private int numOfFailedAttempts = 0;
    private int numOfInvalidUsersAttempts = 0;
    private String mostCommonLoginName = "";
    private HashMap<String, LoginAttempt> loginAttemptsRecord = new HashMap<>();

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
                //only pay attention to ssh entries
                if (line.contains(SSH_LINE_ID))
                {
                    parseLine(line);
                }
                line = reader.readLine();
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
        String[] lineList = line.split(SEPARATOR);

        //if new user
        LoginAttempt loginAttempt = new LoginAttempt (lineList);
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
        //iterate userlist + add all attempts
        return numOfSuccessLogins;
    }

    /**
     * Counts invalid users as invalid attempts
     * Number of failed login attempts, total
     * @return
     */
    public int getNumOfFailedAttempts ( )
    {
        //iterate userlist + add all attempts
        return numOfFailedAttempts;
    }

    /**
     * Counts invalid users as invalid attempts
     * Number of failed login attempts, by username
     * @return
     */
    public int getNumOfFailedAttempts ( String userName )
    {
        //change to get from hashmap
        return numOfFailedAttempts;
    }

    public void printLocationsByIP ( )
    {

    }

}