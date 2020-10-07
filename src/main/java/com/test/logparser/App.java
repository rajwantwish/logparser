package com.test.logparser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.test.logparser.json.model.LogEntry;
import com.test.logparser.json.model.Output;

/**
 * LogParser
 */
public final class App {
    private App() {

    }

    /**
     * @param args Should be a path to input file. 
     * Or specify the absolute path to input file.
     */
    public static void main(String[] args) throws SQLException {
        String filePath = args[0]; // "/Users/gj8bpn/Desktop/Code/Java/logfile.txt"; 
        System.out.println(filePath);
        List<Output> output = parseJsonLineByLine(filePath);
        processData(output);
    }

    private static List<Output> parseJsonLineByLine(String filePath) {
        Gson g = new Gson();

        List<LogEntry> logEntries = new ArrayList<LogEntry>();
        List<Output> output = new ArrayList<Output>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> logEntries.add(g.fromJson(s, LogEntry.class)));
            order(logEntries);
            System.out.println(logEntries);
            for (int i = 0; i < logEntries.size(); i += 2) {
                long timeDiff = ProcessPair(logEntries.get(i), logEntries.get(i + 1));
                System.out.println("TimeDiff: " + String.valueOf(timeDiff));
                Output o = new Output();
                o.setId(logEntries.get(i).getId());
                o.setDuration(timeDiff);
                o.setAlert(o.duration > 4);
                o.setType(logEntries.get(i).getType());
                o.setHost(logEntries.get(i).getHost());
                output.add(o);
            }
            System.out.println(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static long ProcessPair(LogEntry leStart, LogEntry leFinished) {
        long startTime = leStart.getTimeStamp();
        long endTime = leFinished.getTimeStamp();
        long diff = endTime - startTime;
        System.out.println(diff);
        return diff;
    }

    private static void order(List<LogEntry> logEntries) {

        // Need test for type safety implemented here
        Collections.sort(logEntries, new Comparator() {

            public int compare(Object o1, Object o2) {
                String x1 = ((LogEntry) o1).getId();
                String x2 = ((LogEntry) o2).getId();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                String s1 = ((LogEntry) o1).getState();
                String s2 = ((LogEntry) o2).getState();
                return s2.compareTo(s1);
            }
        });
    }

    private static boolean processData(List<Output> output) throws SQLException {

        // Write contents to db file format for http://hsqldb.org/
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            c = DriverManager.getConnection("jdbc:hsqldb:mem://localhost:9001/PUBLIC","SA","");
            System.out.println("Database Opened...\n");

            stmt = c.createStatement();//IF NOT EXISTS
            String sql = "CREATE TABLE  Output " +
                "(id VARCHAR(255) PRIMARY KEY," +
                " duration INTEGER NOT NULL, " +
                " type VARCHAR(255), " +
                " host VARCHAR(255), " +
                " alert INTEGER NOT NULL) " ;

            stmt.executeUpdate(sql);

            for (Output o : output) {
                sql = "INSERT INTO Output (id, duration, type, host, alert) " +
                    "VALUES ('" + o.getId() + "'," + o.getDuration() + ",'" +
                    o.getType() + "','" + o.getHost() + "'," + (o.getAlert() == true ? 1 : 0) + ")";
                stmt.executeUpdate(sql);
            }

            stmt.close();
            c.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }

        return true;

    /*    // Write contents to db file format for http://hsqldb.org/
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return true;
        }

        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem://localhost:9001/demodb;", "SA", "");

        return false;
*/
    }
}
