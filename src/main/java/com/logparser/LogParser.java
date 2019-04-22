package com.logparser;
import com.logparser.model.Log;

/**
 * Hello world!
 *
 */
public class LogParser
{
    public static void main( String[] args )
    {
        Log newLog = new Log ( "auth.log", 6 );
        newLog.printSummaryOfAttempts();
        System.out.println ("The most common user was: " + newLog.getMostCommonLoginUser());
    }
}
