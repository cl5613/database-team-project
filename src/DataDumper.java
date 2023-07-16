import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Object.*;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;

/*
Name: DataDumper
Authors:
Description: Dumps data from comma seperated files into
             the database
 */

abstract class DataDumper
{
    static private String[] Genres;
    static private String[] Studios;
    static private String[] Actors;
    static private String[] Directors;
    static private String[] ReleaseDate;
    static private String MovieName;
    static private String MovieID;
    static private String MPAA;
    static private String duration;
    static private int durationInt;
    static private int a;
    static private int ActID;
    static private int MovieGenreID;
    static private final String quotes = "\"";

    static private String[] Group(int i, String[] stuff)
    {
        int beg = a;
        while(a < stuff.length)
        {
            if(stuff[a].endsWith(quotes))
            {
                break;
            }
            a++;
        }
        String[] list = new String[a-beg+1];
        for(int j = beg; j<a+1; j++)
        {
            list[j-beg] = stuff[j];
        }
        // Getting rid of the quotes at the start and end
        list[0] = list[0].substring(1);
        list[list.length - 1] = list[list.length - 1].substring(0,list[list.length - 1].length() - 1);
        return list;
    }


    static private String fixMPAA(String broken)
    {
        // Gets rid of the details in the the MPAA rating
        // Keeping only the main part ex. G, PG, etc.
        for(int i = 0; i<broken.length();i++){
            if(broken.charAt(i) == ' '){
                return broken.substring(0,i);
            }
        }
        return broken;
    }

    static private String fixDate(String[] broken)
    {
        // Combines the parts of the date and gets rid
        // of format things
        for(int i = 1; i<broken[1].length();i++){
            if(broken[1].charAt(i) == ' '){
                broken[1] = broken[1].substring(0,i);
            }
        }

        return broken[0]  + broken[1];
    }

    static private int fixDuration(String broken)
    {
        int hours = 0;
        int new_start = 0;
        int minutes = 0;
        for(int i = 0; i<broken.length();i++){
            if(broken.charAt(i) == 'h'){
                hours = Integer.parseInt(broken.substring(0,i));
                new_start = i;
            }
        }
        for(int i = new_start + 2; i<broken.length();i++){
            if(broken.charAt(i) == 'm'){
                minutes = Integer.parseInt(broken.substring(new_start + 2,i));
            }
        }
        return hours*60 + minutes;
    }

    static private String fixString(String broken)
    {
        // Combines the parts of the date and gets rid
        // of format things
        for(int i = 1; i<broken.length();i++){
            if(broken.charAt(i) == "'".charAt(0))
        {
                broken = broken.substring(0,i) + "'" + broken.substring(i);
                i++;
            }
        }
        return broken;
    }


    public static void ratingDump(Connection conn){
        int bot_num = 0;
        int CollID = 3;
        String Last_Name = "Robotnik";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String date_string = formatter.format(date);
        String Best_Name = "Best Movies";
        String Worst_Name = "Worst Movies";
        Random rand = new Random();
        for(bot_num = 0; bot_num<50; bot_num++){
            // Add bot to system
            String First_Name = "Robot" + bot_num;
            String email = First_Name + "@fakebot.com";
            String Password = "Password" + bot_num;
            String User_Name = "Username" + bot_num;
            try{
                String v = "'" + User_Name + "','" + Password + "','" + First_Name + "','" + Last_Name +
                        "','" + email + "','" + date_string + "','" + date_string + "'" ;
                String insertQuery = "insert into p320_26.users VALUES ("+ v + ")";
                Statement insertStatement = conn.createStatement();
                insertStatement.executeUpdate(insertQuery);
                // Add collections to the system.
                v = "'" + Best_Name + "'," + CollID + ",'" + User_Name + "'";
                insertQuery = "insert into p320_26.collection VALUES ("+ v + ")";
                insertStatement = conn.createStatement();
                insertStatement.executeUpdate(insertQuery);
                int Best_CollID = CollID;
                CollID++;
                v = "'" + Worst_Name + "'," + CollID + ",'" + User_Name + "'";
                insertQuery = "insert into p320_26.collection VALUES ("+ v + ")";
                insertStatement = conn.createStatement();
                insertStatement.executeUpdate(insertQuery);
                int Worst_CollID = CollID;
                CollID++;
                for(int i = 0; i<25; i++){
                    // Rate a movie
                    int MovID = rand.nextInt(500);
                    double rating = rand.nextDouble() * 5.0;
                    String selectQueryRating = "select Count(*)" +
                            " from p320_26.userratesmovie" +
                            " where \"Username\" = '" + User_Name + "' and \"MovieID\" = "+ 169;
                    Statement selectStatementRating = conn.createStatement();
                    ResultSet selectResultRating = selectStatementRating.executeQuery(selectQueryRating);
                    selectResultRating.next();
                    if(selectResultRating.getInt(1) != 0){
                            String updateQuery = "Update p320_26.userratesmovie Set \"User_Rating\"=" +
                                    rating + " Where \"Username\"='" + User_Name + "' and " + "\"MovieID\" = "+ 169;
                            Statement updateStatement = conn.createStatement();
                            updateStatement.executeUpdate(updateQuery);
                        String selectQueryWatch = "select \"Play_Count\"" +
                                " from p320_26.userwatchesmovie" +
                                " where \"Username\" = '" + User_Name + "' and \"MovieID\" = "+ 169;
                        Statement selectStatementWatch = conn.createStatement();
                        ResultSet selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                        selectResultWatch.next();
                        int watches = selectResultWatch.getInt(1) + 1;
                        updateQuery = "Update p320_26.userwatchesmovie Set \"Play_Count\"=" +
                                watches + " Where \"Username\"='" + User_Name + "' and " + "\"MovieID\" = "+ MovID;
                        updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                    }
                    else{
                        String updateQuery = "insert into p320_26.userratesmovie values ('" +
                                User_Name + "'," + MovID + "," + rating + ")";
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                        updateQuery = "insert into p320_26.userwatchesmovie values ('" +
                                User_Name + "'," + MovID + "," + 1 + ")";
                        updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                        if(rating >= 4){
                            v =  Best_CollID + "," + MovID;
                            insertQuery = "insert into p320_26.movieincollection VALUES ("+ v + ")";
                            insertStatement = conn.createStatement();
                            insertStatement.executeUpdate(insertQuery);
                        }
                        else if(rating <= 2){
                            v =  Worst_CollID + "," + MovID;
                            insertQuery = "insert into p320_26.movieincollection VALUES ("+ v + ")";
                            insertStatement = conn.createStatement();
                            insertStatement.executeUpdate(insertQuery);
                        }
                    }
                    String selectQueryMovie = "select avg(\"User_Rating\")" +
                            " from p320_26.userratesmovie" +
                            " where \"MovieID\" = "+ MovID;
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    selectResultMovie.next();
                    double old_rating = selectResultMovie.getDouble(1);
                    String updateQuery = "Update p320_26.movie Set \"UserAvgRating\" = " +
                            old_rating+ " Where " +  "\"MovieID\" = "+ MovID;
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                    selectQueryMovie = "select \"Play Count\"" +
                            " from p320_26.movie" +
                            " where \"MovieID\" = "+ MovID;
                    selectStatementMovie = conn.createStatement();
                    selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    selectResultMovie.next();
                    int old_watches = selectResultMovie.getInt(1) +1;
                    updateQuery = "Update p320_26.movie Set \"Play Count\" = " +
                            old_watches+ " Where " +  "\"MovieID\" = "+ MovID;
                    updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                }
                System.out.println("Bots are done rating :)");
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public static boolean MovieTransfer(Connection conn)
    {
        ActID=0;
        MovieGenreID = 0;
        BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("rotten_tomatoes_top_movies.csv"));
                reader.readLine();
            }
            catch(Exception e){
                System.out.print(e);
            };
            for(int i =0; i<500; i++)
            {
                String line = "";
                try{
                    line = reader.readLine();
                }
                catch(Exception e){
                  System.out.print(e);
                };
                String[] tokens = line.split(",");
                a = 0;
                MovieID = tokens[a];
                a++;
                if(tokens[a].startsWith(quotes))
                {
                    Studios = Group(a, tokens);
                }
                else
                {
                    Studios = new String[1];
                    Studios[0] = tokens[a];
                }
                Studios[0] = fixString(Studios[0]);
                a++;
                if(tokens[a].startsWith(quotes))
                {
                    Genres = Group(a, tokens);
                }
                else
                {
                    Genres = new String[1];
                    Genres[0] = tokens[a];
                }
                Genres[0] = fixString(Genres[0]);
                a++;
                MovieName = tokens[a];
                MovieName = fixString(MovieName);
                a++;
                MPAA = fixMPAA(tokens[a]);
                a++;
                if(tokens[a].startsWith(quotes))
                {
                    ReleaseDate = Group(a, tokens);
                }
                a++;
                ReleaseDate[0] = fixDate(ReleaseDate);
                duration = tokens[a];
                durationInt = fixDuration(duration);
                a++;
                if(tokens[a].startsWith(quotes))
                {
                    Directors = Group(a, tokens);
                }
                else
                {
                    Directors = new String[1];
                    Directors[0] = tokens[a];
                }
                Directors[0] = fixString(Directors[0]);
                a++;
                if(tokens[a].startsWith(quotes))
                {
                    Actors = Group(a, tokens);
                }
                else
                {
                    Actors = new String[1];
                    Actors[0] = tokens[a];
                }
                String v = MovieID + ",'" + Studios[0] + "'," + 0.0 + ","
                        + 0 + ",'" + Genres[0] + "','" + MovieName + "','"
                        + MPAA + "','" + ReleaseDate[0] + "'," + durationInt
                        + ",'" + Directors[0]+"'," + 0;
                String insertQuery;
                // Got some movies in but not the actors. Don't want a duplicate of a movie
                if(Integer.parseInt(MovieID) > 1){
                    insertQuery = "insert into p320_26.movie VALUES ("+ v + ")";
                    try{
                        Statement insertStatement = conn.createStatement();
                        insertStatement.executeUpdate(insertQuery);
                    }
                    catch(Exception e){
                        System.out.print(e);
                    };
                }
                for(String actor : Actors)
                {
                    if ( ActID > 35 ){
                        // Get different actors in
                        actor = fixString(actor);
                        if( ActID > 36)
                        {
                            insertQuery = "insert into p320_26.actor VALUES ('" + actor + "')";
                            try{
                                Statement actorState = conn.createStatement();
                                actorState.executeUpdate(insertQuery);
                            }
                            catch(Exception e){
                                System.out.print(e);
                            };
                        }
                        insertQuery = "insert into p320_26.actinmovie VALUES " +
                                "(" + ActID + ", '" + actor + "', " + MovieID+ ")";
                        try{
                            Statement actInMovie = conn.createStatement();
                            actInMovie.executeUpdate(insertQuery);
                        }
                        catch(Exception e){
                            System.out.print(e);
                        };

                    }
                    ActID++;
                }
                for(String genre: Genres)
                {
                    if(MovieGenreID > 4) {
                        genre = fixString(genre);
                        if(MovieGenreID > 5){
                            insertQuery = "insert into p320_26.genre VALUES ('" + genre + "')";
                            try{
                                Statement GenreInsert = conn.createStatement();
                                GenreInsert.executeUpdate(insertQuery);
                            }
                            catch(Exception e){
                                System.out.print(e);
                            };
                        }
                        insertQuery = "insert into p320_26.moviegenre VALUES"
                                + " (" + MovieGenreID + ",'" + genre + "', "
                                + MovieID + ")";
                        try{
                            Statement MGenreInsert = conn.createStatement();
                            MGenreInsert.executeUpdate(insertQuery);
                        }
                        catch(Exception e){
                            System.out.print(e);
                        };
                    }

                    MovieGenreID++;
                }

            }
        return true;
    }
}
