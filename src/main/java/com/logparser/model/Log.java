package com.logparser.model;
import com.logparser.model.LoginAttempt;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Log
{
    //CONSTANTS
    //entries in auth.log that have to do with ssh are denoted sshd
    static private String SSH_LINE_ID = "sshd";
    static private String SEPARATOR = " ";
    static private String INVALID_USER = "Invalid user";
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
        if (line.contains(INVALID_USER))
        {
            String[] data = line.split(SEPARATOR);
            String userName = data[7];
            if (loginAttemptsRecord.containsKey(userName))
            {
                LoginAttempt attempt = loginAttemptsRecord.get(userName);
                attempt.addToFailedLogins(1);
            }
            else
            {//if new user
                LoginAttempt attempt = new LoginAttempt (data, false);
                attempt.addToFailedLogins(1);
                loginAttemptsRecord.put(userName, attempt);
            }
        }
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

    public void printSummaryOfAttempts ( )
    {
        Iterator it = loginAttemptsRecord.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry pair = (Map.Entry)it.next();
            LoginAttempt record = (LoginAttempt) pair.getValue();
            record.printSummary();
            it.remove();
        }
    }

}