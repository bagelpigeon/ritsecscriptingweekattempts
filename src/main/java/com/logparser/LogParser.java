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
        System.out.println("Hello");
    }
}