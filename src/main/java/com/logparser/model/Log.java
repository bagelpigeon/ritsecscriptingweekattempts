package com.logparser.model;
import com.logparser.model.LoginAttempt;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.net.InetAddress;
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
    static private String MAX_ATTEMPTS_EXCEEDED = "maximum authentication attempts";
    final private int MAX_AUTH_TRIES = 6;

    private int numOfSuccessLogins = 0;
    private int numOfFailedAttempts = 0;
    private int numOfInvalidUsersAttempts = 0;
    private String mostCommonLoginName = "";
    private HashMap<String, LoginAttempt> loginAttemptsRecord = new HashMap<>();
    private DatabaseReader cityReader;

    public Log ( String fileName, int maxAuthTries )
    {
        createDatabases();
        this.parseFile(fileName);
        //this.MAX_AUTH_TRIES = maxAuthTries;
    }

    //TODO: fix extra counts for invalid users that exceed max auth
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
        String[] data = line.split(SEPARATOR);
        if (line.contains(INVALID_USER))
        {
            String userName = data[7];
            String ip = data[9].trim();

            //fix for weird attempts where users try to authenticate with
            //a user and password on the same line
            if ( ip.equals("from"))
            {
                userName = data[7] + data[8];
                ip = data[10];
            }
            if (loginAttemptsRecord.containsKey(userName))
            {
                LoginAttempt attempt = loginAttemptsRecord.get(userName);
                attempt.addNewIP(ip, 1, false);
            }
            else
            {//if new user
                LoginAttempt attempt = new LoginAttempt (userName, false, cityReader);
                attempt.addNewIP(ip, 1, false);
                loginAttemptsRecord.put(userName, attempt);
            }
        }
        else if (line.contains(MAX_ATTEMPTS_EXCEEDED))
        {
            String userName;
            String ip;

            //check if its an invalid user
            //TODO: fix up the indexes to be constants
            //Turn into function too
            if (line.contains("invalid"))
            {
                userName = data[13];
                ip = data[15];
                if ( ip.equals("from"))
                {
                    userName = data[11] + data[12];
                    ip = data[16];
                }
            }
            else
            {
                userName = data[11];
                ip = data[13];
                if ( ip.equals("from"))
                {
                    userName = data[11] + data[12];
                    ip = data[14];
                }
            }

            if (loginAttemptsRecord.containsKey(userName))
            {
                LoginAttempt attempt = loginAttemptsRecord.get(userName);
                attempt.addNewIP(ip, MAX_AUTH_TRIES, false);
            }
            else
            {//if new user
                LoginAttempt attempt = new LoginAttempt (userName, false, cityReader);
                attempt.addNewIP(ip, MAX_AUTH_TRIES, false);
                loginAttemptsRecord.put(userName, attempt);
            }
        }
    }

    public String getMostCommonLoginUser ( )
    {
        int maxNumOfLoginAttempts = -1;
        Iterator it = loginAttemptsRecord.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry pair = (Map.Entry)it.next();
            LoginAttempt record = (LoginAttempt) pair.getValue();
            if (record.getTotalAttempts()> maxNumOfLoginAttempts)
            {
                maxNumOfLoginAttempts = record.getTotalAttempts();
                mostCommonLoginName = record.getUserName();
            }
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
        //iterate userlist + add all attempts
        return numOfFailedAttempts;
    }

    /**
     * Counts invalid users as invalid attempts
     * Number of failed login attempts, by username
     * @return
     */
    public int getNumOfFailedAttemptsByUser ( String userName )
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
        }
    }

    private void createDatabases ( )
    {
        try
        {
            File cityDatabase = new File (this.getClass().getResource( "/GeoLite2-City.mmdb" ).toURI());
            cityReader = new DatabaseReader.Builder(cityDatabase).build();
        }
        catch (java.net.URISyntaxException e)
        {
            System.out.println("Error in URI syntax");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println ("Error creating databases");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println ("Error getting response from database");
        }
    }
}