package com.logparser;
import com.logparser.model.Log;
import com.logparser.model.LoginAttempt;

/**
 * Driver class for accessing various info from the log
 *
 */
public class LogParser
{
    public static void main( String[] args )
    {
        Log newLog = new Log ( "auth.log", 6 );
        //newLog.printSummaryOfAttempts();
        LoginAttempt record = newLog.getUserRecords("root");
        record.printSummary();
        LoginAttempt record2 = newLog.getUserRecords("ubuntu");
        record2.printSummary();
        System.out.println ("The most common user was: " + newLog.getMostCommonLoginUser());
    }
}
