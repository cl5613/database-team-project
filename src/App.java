import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
This class deals with the user experience and commands.
 */
public abstract class App
{
    private static final String login = "login";
    private static final String register = "register";
    private static final String quit = "quit";
    private static String email;
    private static String User;
    private static String Password;
    private static String First_Name;
    private static String Last_Name;
    private static String Movie_Name;
    private static String Movie_Date;
    private static String Movie_Cast;
    private static String Movie_Studio;
    private static String Movie_Genre;
    private static String Collection_Name;
    private static Scanner scanner;
    private static boolean loggedin;

    private static void LOGIN(Connection conn)
    {
        System.out.println("Enter username: ");
        User = scanner.nextLine();
        System.out.println("Enter password: ");
        Password = scanner.nextLine();
        try {
            String selectQuery = "Select COUNT(*) from p320_26.users WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Password\" = '"+ Password + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count == 1)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String updateQuery = "Update p320_26.users Set \"LastAccessed\"='" +
                        formatter.format(date) +"' Where \"Username\"='" + User + "'";
                try{
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                    System.out.println("Login successful.");
                    loggedin = true;
                }
                catch(Exception e){
                    System.out.println(e);
                };
            }
            else if(count == 0)
            {
                System.out.println("Not found");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }

    private static void REGISTER(Connection conn)
    {
        System.out.println("Enter your email: ");
        email = scanner.nextLine();
        System.out.println("Enter your first name: ");
        First_Name = scanner.nextLine();
        System.out.println("Enter your last name: ");
        Last_Name = scanner.nextLine();
        System.out.println("Enter a username: ");
        User = scanner.nextLine();
        System.out.println("Enter a password: ");
        Password = scanner.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String v = "'" + User + "','" + Password + "','" + First_Name + "','" + Last_Name +
                "','" + email + "','" + formatter.format(date) + "','" + formatter.format(date) + "'" ;
        String insertQuery = "insert into p320_26.users VALUES ("+ v + ")";
        try{
            Statement insertStatement = conn.createStatement();
            insertStatement.executeUpdate(insertQuery);
            System.out.println("Registration Success!");
            // Automatically logs in on register
            loggedin = true;
        }
        catch(Exception e){
            System.out.println(e);
        };

    }

    private static void help(){
        //Please add a description of a user action when you add one
        System.out.println("'search name' will let you search for movies based on the name");
        System.out.println("'search date' will let you search for movies based on the release date");
        System.out.println("'search cast' will let you search for movies based on a cast member");
        System.out.println("'search studio' will let you search for movies based on the studio ");
        System.out.println("'search genre' will let you search for movies based on the genre ");
        System.out.println("'create' will let you create a collection to put movies in ");
        System.out.println("'add movie' will let you add a movie to a collection ");
        System.out.println("'delete movie' will let you delete a movie from a collection ");
        System.out.println("'rename' will let you rename a collection ");
        System.out.println("'delete collection' will allow you to delete a movie ");
        System.out.println("'show' will show information on your collections ");
        System.out.println("'rate' will let you rate a movie ");
        System.out.println("'watch movie' will let you add one to your watch count of a movie");
        System.out.println("'watch collection' will let you add one to your watch count of all the movies in a collection");
        System.out.println("'profile rating' will let you search for a user profile and see info including their top 10 rated movies.");
        System.out.println("'profile watches' will let you search for a user profile and see info including their top 10 most watched movies.");
        System.out.println("'recommend rating' will let you see recommendations for a movie based on the rating");
        System.out.println("'recommend history' will let you see recommendations for a movie based on your watch history");
        System.out.println("'recommend new' will let you see recommendations for a movie based on what's come out this month");
        System.out.println("'recommend follow' will let you see recommendations for a movie based on what who you follow likes");
        System.out.println("'add follow' will let you follow a user ");
        System.out.println("'delete follow' will allow you to unfollow a user ");
    }

    private static void searchName(Connection conn){
        System.out.print("Movie Name: ");
        Movie_Name = scanner.nextLine();

        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " m.\"Name\" like '%" + Movie_Name + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                    System.out.print("\t" + selectResult.getString( 6 ));
                }
                else{
                    System.out.print(", " + selectResult.getString( 6 ));
                }
                old_movie = selectResult.getString(1 );
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void searchDate(Connection conn){
        System.out.print("Movie Release Date: ");
        Movie_Date = scanner.nextLine();

        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " m.\"ReleaseDate\" like '%" + Movie_Date + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                    System.out.print("\t" + selectResult.getString( 6 ));
                }
                else{
                    System.out.print(", " + selectResult.getString( 6 ));
                }
                old_movie = selectResult.getString(1 );
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void searchCast(Connection conn){
        System.out.print("Cast Member: ");
        Movie_Cast = scanner.nextLine();

        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " a.\"ActorName\" like '%" + Movie_Cast + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            while(selectResult.next()){
                try{
                    String selectQueryMovie = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                            " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                            " m.\"Name\" like '%" + selectResult.getString(1 ) + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                            " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    String old_movie = "";
                    String new_movie = "";
                    while(selectResultMovie.next()){
                        new_movie = selectResultMovie.getString(1 );
                        if(!(old_movie.equals(new_movie))){
                            System.out.print("\n" + selectResultMovie.getString(1 ) + "\t");
                            System.out.print(selectResultMovie.getString(2 ) + "\t");
                            System.out.print(selectResultMovie.getInt(3 ) + " minutes\t");
                            System.out.print(selectResultMovie.getString(4 ) + "\t");
                            System.out.print(selectResultMovie.getDouble(5 ) + "\n");
                            System.out.print("\t" + selectResultMovie.getString( 6 ));
                        }
                        else{
                            System.out.print(", " + selectResultMovie.getString( 6 ));
                        }
                        old_movie = selectResultMovie.getString(1 );
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                };
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void searchStudio(Connection conn){
        System.out.print("Movie Studio: ");
        Movie_Studio = scanner.nextLine();

        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " m.\"Studio\" like '%" + Movie_Studio + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                    System.out.print("\t" + selectResult.getString( 6 ));
                }
                else{
                    System.out.print(", " + selectResult.getString( 6 ));
                }
                old_movie = selectResult.getString(1 );
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void searchGenre(Connection conn){
        System.out.print("Movie Genre: ");
        Movie_Genre = scanner.nextLine();

        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\"" +
                    " from p320_26.movie m, p320_26.actinmovie a, p320_26.moviegenre g WHERE" +
                    " g.\"GenreName\" like '%" + Movie_Genre + "%' AND a.\"MovieID\" = m.\"MovieID\" AND g.\"MovieID\" = m.\"MovieID\""  +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                try{
                    new_movie = selectResult.getString(1 );
                    if(!(old_movie.equals(new_movie))){
                        System.out.print("\n" + selectResult.getString(1 ) + "\t");
                        System.out.print(selectResult.getString(2 ) + "\t");
                        System.out.print(selectResult.getInt(3 ) + " minutes\t");
                        System.out.print(selectResult.getString(4 ) + "\t");
                        System.out.print(selectResult.getDouble(5 ) + "\n");
                        System.out.print("\t" + selectResult.getString( 6 ));
                    }
                    else{
                        System.out.print(", " + selectResult.getString( 6 ));
                    }
                    old_movie = selectResult.getString(1 );
                }
                catch(Exception e){
                    System.out.println(e);
                };
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void createCollection(Connection conn){
        System.out.print("Collection Name: ");
        Collection_Name = scanner.nextLine();
        try{
            // Check to make sure they don't already have a collection named that.
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count > 0){
                System.out.println("You already made a collection by that name.");
            }
            else{
                //We need to find the next available id
                int i = 0;
                while(true){
                    try{
                        String v = "'" + Collection_Name + "'," + i + ",'" + User + "'";
                        String insertQuery = "insert into p320_26.collection VALUES ("+ v + ")";
                        Statement insertStatement = conn.createStatement();
                        insertStatement.executeUpdate(insertQuery);
                        System.out.println("Creation Success!");
                        return;
                    }
                    catch(Exception e){

                    }
                    i++;
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static void addMovieCollection(Connection conn){
        System.out.print("Collection Name: ");
        Collection_Name = scanner.nextLine();
        try{
            //Check to see if they have that collection
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count > 0){
                // Need some spicy data
                selectQuery = "Select \"CollectionID\" from p320_26.collection WHERE" +
                        " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
                selectStatement = conn.createStatement();
                selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int CollID = selectResult.getInt(1);
                System.out.print("Movie Name: ");
                Movie_Name = scanner.nextLine();
                try {
                    selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\", m.\"MovieID\"" +
                            " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                            " m.\"Name\" like '%" + Movie_Name + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                            " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
                    selectStatement = conn.createStatement();
                    selectResult = selectStatement.executeQuery(selectQuery);
                    String old_movie = "";
                    String new_movie = "";
                    int MovID = 0;
                    //need to know if this is the first time the loop goes
                    boolean notfirst = false;
                    while(selectResult.next()){
                        new_movie = selectResult.getString(1 );
                        if(!(old_movie.equals(new_movie))){
                            if(notfirst){
                                System.out.print("\nIf that was movie you wanted to add, type 'y': ");
                                String addcheck = scanner.nextLine();
                                if(addcheck.equals("y")){
                                    try{
                                        String v =  CollID + "," + MovID;
                                        String insertQuery = "insert into p320_26.movieincollection VALUES ("+ v + ")";
                                        Statement insertStatement = conn.createStatement();
                                        insertStatement.executeUpdate(insertQuery);
                                        System.out.println("Addition Success!");
                                        return;
                                    }
                                    catch(Exception e){
                                        System.out.println(e);
                                    }
                                }
                            }
                            notfirst = true;
                            System.out.print("\n" + selectResult.getString(1 ) + "\t");
                            System.out.print(selectResult.getString(2 ) + "\t");
                            System.out.print(selectResult.getInt(3 ) + " minutes\t");
                            System.out.print(selectResult.getString(4 ) + "\t");
                            System.out.print(selectResult.getDouble(5 ) + "\n");
                            System.out.print("\t" + selectResult.getString( 6 ));
                            MovID = selectResult.getInt(7);
                        }
                        else{
                            System.out.print(", " + selectResult.getString( 6 ));
                        }
                        old_movie = selectResult.getString(1 );
                    }
                    System.out.print("\nIf that was movie you wanted to add, type 'y': ");
                    String addcheck = scanner.nextLine();
                    if(addcheck.equals("y")){
                        try{
                            String v =  CollID + "," + MovID;
                            String insertQuery = "insert into p320_26.movieincollection VALUES ("+ v + ")";
                            Statement insertStatement = conn.createStatement();
                            insertStatement.executeUpdate(insertQuery);
                            System.out.println("Addition Success!");
                            return;
                        }
                        catch(Exception e){
                            System.out.println(e);
                        }
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                };
            }
            else{
                System.out.println("You do not have a collection by that name.");
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private static void deleteMovieCollection(Connection conn){
        System.out.print("Collection Name: ");
        Collection_Name = scanner.nextLine();
        try{
            //Check to see if they have that collection
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count > 0){
                // Need some spicy data
                selectQuery = "Select \"CollectionID\" from p320_26.collection WHERE" +
                        " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
                selectStatement = conn.createStatement();
                selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int CollID = selectResult.getInt(1);
                try {
                    selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\", m.\"MovieID\"" +
                            " from p320_26.movie m, p320_26.actinmovie a, p320_26.movieincollection c WHERE" +
                            " c.\"CollectionID\" = " + CollID + " AND a.\"MovieID\" = m.\"MovieID\" AND c.\"MovieID\" = m.\"MovieID\" " +
                            " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
                    selectStatement = conn.createStatement();
                    selectResult = selectStatement.executeQuery(selectQuery);
                    String old_movie = "";
                    String new_movie = "";
                    int MovID = 0;
                    //need to know if this is the first time the loop goes
                    boolean notfirst = false;
                    while(selectResult.next()){
                        new_movie = selectResult.getString(1 );
                        if(!(old_movie.equals(new_movie))){
                            if(notfirst){
                                System.out.print("\nIf that was movie you wanted to delete, type 'y': ");
                                 String addcheck = scanner.nextLine();
                                if(addcheck.equals("y")){
                                    try{
                                        String deleteQuery = "delete from p320_26.movieincollection where \"MovieID\" = " + MovID + " and \"CollectionID\" = " + CollID;
                                        Statement deleteStatement = conn.createStatement();
                                        deleteStatement.executeUpdate(deleteQuery);
                                        System.out.println("Deletion Success!");
                                        return;
                                    }
                                    catch(Exception e){
                                        System.out.println(e);
                                    }
                                }
                            }
                            notfirst = true;
                            System.out.print("\n" + selectResult.getString(1 ) + "\t");
                            System.out.print(selectResult.getString(2 ) + "\t");
                            System.out.print(selectResult.getInt(3 ) + " minutes\t");
                            System.out.print(selectResult.getString(4 ) + "\t");
                            System.out.print(selectResult.getDouble(5 ) + "\n");
                            System.out.print("\t" + selectResult.getString( 6 ));
                            MovID = selectResult.getInt(7);
                        }
                        else{
                            System.out.print(", " + selectResult.getString( 6 ));
                        }
                        old_movie = selectResult.getString(1 );
                    }
                    System.out.print("\nIf that was movie you wanted to delete, type 'y': ");
                    String addcheck = scanner.nextLine();
                    if(addcheck.equals("y")){
                        try{
                            String deleteQuery = "delete from p320_26.movieincollection where \"MovieID\" = " + MovID + " and \"CollectionID\" = " + CollID;
                            Statement deleteStatement = conn.createStatement();
                            deleteStatement.executeUpdate(deleteQuery);
                            System.out.println("Deletion Success!");
                            return;
                        }
                        catch(Exception e){
                            System.out.println(e);
                        }
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                };
            }
            else{
                System.out.println("You do not have a collection by that name.");
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void renameCollection(Connection conn){
        System.out.print("Collection Name to change: ");
        Collection_Name = scanner.nextLine();
        try{
            // Check to make sure they  already have a collection named that.
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count <= 0){
                System.out.println("You don't have a collection by that name.");
            }
            else{
                System.out.print("New Collection Name: ");
                String New_Collection_Name = scanner.nextLine();
                String updateQuery = "Update p320_26.Collection Set \"Name\"='" +
                        New_Collection_Name + "' Where \"Username\"='" + User + "' and " + "\"Name\" = '"+ Collection_Name + "'";
                try{
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                    System.out.println("Rename successful.");
                }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static void deleteCollection(Connection conn){
        System.out.print("Collection Name: ");
        Collection_Name = scanner.nextLine();
        try{
            //Check to see if they have that collection
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count > 0) {
                // Need some spicy data
                selectQuery = "Select \"CollectionID\" from p320_26.collection WHERE" +
                        " \"Username\" = '" + User + "'AND " + "\"Name\" = '" + Collection_Name + "'";
                selectStatement = conn.createStatement();
                selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int CollID = selectResult.getInt(1);
                try {
                    String deleteQuery = "delete from p320_26.movieincollection where \"CollectionID\" = " + CollID;
                    Statement deleteStatement = conn.createStatement();
                    deleteStatement.executeUpdate(deleteQuery);
                    deleteQuery = "delete from p320_26.collection where \"CollectionID\" = " + CollID;
                    deleteStatement = conn.createStatement();
                    deleteStatement.executeUpdate(deleteQuery);
                    System.out.println("Deletion Success!");
                    return;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            else{
                System.out.println("You do not have a collection by that name.");
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void showCollections(Connection conn){
        try{

            String selectQuery = "Select \"Name\", \"CollectionID\" from p320_26.collection WHERE" +
                    " \"Username\" = '" + User  + "' order by  \"Name\" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            while(selectResult.next()){
                String Collection_Name = selectResult.getString(1);
                int CollId = selectResult.getInt(2);
                try{
                    String selectQueryCount = "Select Count(c.*) as Movie_Count, Sum(m.\"Duration \") as time" +
                            " from p320_26.movieincollection c, p320_26.movie m WHERE" +
                            " c.\"CollectionID\" = '" + CollId  + "' and m.\"MovieID\" = c.\"MovieID\"";
                    Statement selectStatementCount = conn.createStatement();
                    ResultSet selectResultCount =
                            selectStatementCount.executeQuery(selectQueryCount);
                    selectResultCount.next();
                    int count = selectResultCount.getInt(1);
                    int time_sum = selectResultCount.getInt(2);
                    int time_hours = time_sum / 60;
                    int time_minutes = time_sum % 60;
                    System.out.print("\n" + Collection_Name + "\t #Movies " + count + "\t" + time_hours
                    + "h " + time_minutes + "t");
                    try{
                        String selectQueryMovie = "Select m.\"Name\"" +
                                " from p320_26.movie m, p320_26.movieincollection c WHERE" +
                                " c.\"CollectionID\" = " + CollId + " AND c.\"MovieID\" = m.\"MovieID\" " +
                                " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
                        Statement selectStatementMovie = conn.createStatement();
                        ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                        while(selectResultMovie.next()){
                            System.out.print("\n\t" + selectResultMovie.getString(1 ));
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void rateMovie(Connection conn){
        System.out.print("Movie Name: ");
        Movie_Name = scanner.nextLine();
        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\", m.\"MovieID\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " m.\"Name\" like '%" + Movie_Name + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            int MovID = 0;
            //need to know if this is the first time the loop goes
            boolean notfirst = false;
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    if(notfirst){
                        System.out.print("\nIf that was movie you wanted to rate, type 'y': ");
                        String addcheck = scanner.nextLine();
                        if(addcheck.equals("y")){
                            try{
                                System.out.print("\nMovie Rating (decimal number from 0-5): ");
                                double rating = scanner.nextDouble();
                                String selectQueryRating = "select Count(*)" +
                                        " from p320_26.userratesmovie" +
                                        " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                                Statement selectStatementRating = conn.createStatement();
                                ResultSet selectResultRating = selectStatementRating.executeQuery(selectQueryRating);
                                selectResultRating.next();
                                if(selectResultRating.getInt(1) != 0){
                                    System.out.print("\nIf you want to override your old rating, type 'y': ");
                                    String addchecknew = scanner.next();
                                    if(addchecknew.equals("y")) {
                                        String updateQuery = "Update p320_26.userratesmovie Set \"User_Rating\"=" +
                                                rating + " Where \"Username\"='" + User + "' and " + "\"MovieID\" = "+ MovID;
                                        Statement updateStatement = conn.createStatement();
                                        updateStatement.executeUpdate(updateQuery);
                                    }
                                    else{
                                        return;
                                    }
                                }
                                else{
                                    String updateQuery = "insert into p320_26.userratesmovie values ('" +
                                             User + "'," + MovID + "," + rating + ")";
                                    Statement updateStatement = conn.createStatement();
                                    updateStatement.executeUpdate(updateQuery);
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
                                System.out.println("Rating Success!");
                                return;
                            }
                            catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    }
                    notfirst = true;
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                    System.out.print("\t" + selectResult.getString( 6 ));
                    MovID = selectResult.getInt(7);
                }
                else{
                    System.out.print(", " + selectResult.getString( 6 ));
                }
                old_movie = selectResult.getString(1 );
            }
            System.out.print("\nIf that was movie you wanted to add, type 'y': ");
            String addcheck = scanner.nextLine();
            if(addcheck.equals("y")){
                try{
                    System.out.print("\nMovie Rating (decimal number from 0-5): ");
                    double rating = scanner.nextDouble();
                    String selectQueryRating = "select Count(*)" +
                            " from p320_26.userratesmovie" +
                            " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                    Statement selectStatementRating = conn.createStatement();
                    ResultSet selectResultRating = selectStatementRating.executeQuery(selectQueryRating);
                    selectResultRating.next();
                    if(selectResultRating.getInt(1) != 0){
                        System.out.print("\nIf you want to override your old rating, type 'y': ");
                        String addchecknew = scanner.nextLine();
                        if(addchecknew.equals("y")) {
                            String updateQuery = "Update p320_26.userratesmovie Set \"User_Rating\"=" +
                                    rating + " Where \"Username\"='" + User + "' and " + "\"MovieID\" = "+ MovID;
                            Statement updateStatement = conn.createStatement();
                            updateStatement.executeUpdate(updateQuery);
                        }
                        else{
                            return;
                        }
                    }
                    else{
                        String updateQuery = "insert into p320_26.userratesmovie values ('" +
                                User + "'," + MovID + "," + rating + ")";
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                    }
                    String selectQueryMovie = "select avg(\"User_Rating\")" +
                            " from p320_26.userratesmovie" +
                            " where \"MovieID\" = "+ MovID;
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    selectResultMovie.next();
                    double old_rating = selectResultMovie.getDouble(1);
                    String updateQuery = "Update p320_26.movie Set \"UserAvgRating\"= " +
                            old_rating+ " Where " +  "\"MovieID\" = "+ MovID;
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                    System.out.println("Rating Success!");
                    return;
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        catch(Exception e){
            System.out.print(e);
        }
    }

    private static void watchMovie(Connection conn){
        System.out.print("Movie Name: ");
        Movie_Name = scanner.nextLine();
        try {
            String selectQuery = "Select m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\", a.\"ActorName\", m.\"MovieID\"" +
                    " from p320_26.movie m, p320_26.actinmovie a WHERE" +
                    " m.\"Name\" like '%" + Movie_Name + "%' AND a.\"MovieID\" = m.\"MovieID\"" +
                    " ORDER BY m.\"Name\" asc, m.\"Duration \" asc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            int MovID = 0;
            //need to know if this is the first time the loop goes
            boolean notfirst = false;
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    if(notfirst){
                        System.out.print("\nIf that was movie you wanted to watch, type 'y': ");
                        String addcheck = scanner.nextLine();
                        if(addcheck.equals("y")){
                            try{
                                String selectQueryWatch = "select Count(*)" +
                                        " from p320_26.userwatchesmovie" +
                                        " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                                Statement selectStatementWatch = conn.createStatement();
                                ResultSet selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                                selectResultWatch.next();
                                if(selectResultWatch.getInt(1) != 0){
                                    selectQueryWatch = "select \"Play_Count\"" +
                                            " from p320_26.userwatchesmovie" +
                                            " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                                    selectStatementWatch = conn.createStatement();
                                    selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                                    selectResultWatch.next();
                                    int watches = selectResultWatch.getInt(1) + 1;
                                        String updateQuery = "Update p320_26.userwatchesmovie Set \"Play_Count\"=" +
                                                watches + " Where \"Username\"='" + User + "' and " + "\"MovieID\" = "+ MovID;
                                        Statement updateStatement = conn.createStatement();
                                        updateStatement.executeUpdate(updateQuery);

                                }
                                else{
                                    String updateQuery = "insert into p320_26.userwatchesmovie values ('" +
                                            User + "'," + MovID + "," + 1 + ")";
                                    Statement updateStatement = conn.createStatement();
                                    updateStatement.executeUpdate(updateQuery);
                                }
                                String selectQueryMovie = "select \"Play Count\"" +
                                        " from p320_26.movie" +
                                        " where \"MovieID\" = "+ MovID;
                                Statement selectStatementMovie = conn.createStatement();
                                ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                                selectResultMovie.next();
                                int old_watches = selectResultMovie.getInt(1) +1;
                                String updateQuery = "Update p320_26.movie Set \"Play Count\" = " +
                                        old_watches+ " Where " +  "\"MovieID\" = "+ MovID;
                                Statement updateStatement = conn.createStatement();
                                updateStatement.executeUpdate(updateQuery);
                                System.out.println("Watch Success!");
                                return;
                            }
                            catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    }
                    notfirst = true;
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                    System.out.print("\t" + selectResult.getString( 6 ));
                    MovID = selectResult.getInt(7);
                }
                else{
                    System.out.print(", " + selectResult.getString( 6 ));
                }
                old_movie = selectResult.getString(1 );
            }
            System.out.print("\nIf that was movie you wanted to add, type 'y': ");
            String addcheck = scanner.nextLine();
            if(addcheck.equals("y")){
                try{
                    String selectQueryWatch = "select Count(*)" +
                            " from p320_26.userwatchesmovie" +
                            " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                    Statement selectStatementWatch = conn.createStatement();
                    ResultSet selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                    selectResultWatch.next();
                    if(selectResultWatch.getInt(1) != 0){
                        selectQueryWatch = "select \"Play_Count\"" +
                                " from p320_26.userwatchesmovie" +
                                " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                        selectStatementWatch = conn.createStatement();
                        selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                        selectResultWatch.next();
                        int watches = selectResultWatch.getInt(1) + 1;
                        String updateQuery = "Update p320_26.userwatchesmovie Set \"Play_Count\"=" +
                                watches + " Where \"Username\"='" + User + "' and " + "\"MovieID\" = "+ MovID;
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);

                    }
                    else{
                        String updateQuery = "insert into p320_26.userwatchesmovie values ('" +
                                User + "'," + MovID + "," + 1 + ")";
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                    }
                    String selectQueryMovie = "select \"Play Count\"" +
                            " from p320_26.movie" +
                            " where \"MovieID\" = "+ MovID;
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    selectResultMovie.next();
                    int old_watches = selectResultMovie.getInt(1) + 1;
                    String updateQuery = "Update p320_26.movie Set \"Play Count\" = " +
                            old_watches+ " Where " +  "\"MovieID\" = "+ MovID;
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                    System.out.println("Watch Success!");
                    return;
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        catch(Exception e){
            System.out.print(e);
        }
    }

    private static void watchCollection(Connection conn){
        try{
            System.out.print("Collection Name: ");
            Collection_Name = scanner.nextLine();
            //Check to see if they have that collection
            String selectQuery = "Select COUNT(*) from p320_26.collection WHERE" +
                    " \"Username\" = '" + User + "'AND " + "\"Name\" = '"+ Collection_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count > 0) {
                // Need some spicy data
                selectQuery = "Select \"CollectionID\" from p320_26.collection WHERE" +
                        " \"Username\" = '" + User + "'AND " + "\"Name\" = '" + Collection_Name + "'";
                selectStatement = conn.createStatement();
                selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int CollID = selectResult.getInt(1);
                selectQuery = "Select \"MovieID\" from p320_26.movieincollection WHERE" +
                        " \"CollectionID\" = " + CollID;
                selectStatement = conn.createStatement();
                selectResult =
                        selectStatement.executeQuery(selectQuery);
                while(selectResult.next()){
                    int MovID = selectResult.getInt(1);
                    String selectQueryWatch = "select Count(*)" +
                            " from p320_26.userwatchesmovie" +
                            " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                    Statement selectStatementWatch = conn.createStatement();
                    ResultSet selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                    selectResultWatch.next();
                    if(selectResultWatch.getInt(1) != 0){
                        selectQueryWatch = "select \"Play_Count\"" +
                                " from p320_26.userwatchesmovie" +
                                " where \"Username\" = '" + User + "' and \"MovieID\" = "+ MovID;
                        selectStatementWatch = conn.createStatement();
                        selectResultWatch = selectStatementWatch.executeQuery(selectQueryWatch);
                        selectResultWatch.next();
                        int watches = selectResultWatch.getInt(1) + 1;
                        String updateQuery = "Update p320_26.userwatchesmovie Set \"Play_Count\"=" +
                                watches + " Where \"Username\"='" + User + "' and " + "\"MovieID\" = "+ MovID;
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);

                    }
                    else{
                        String updateQuery = "insert into p320_26.userwatchesmovie values ('" +
                                User + "'," + MovID + "," + 1 + ")";
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                    }
                    String selectQueryMovie = "select \"Play Count\"" +
                            " from p320_26.movie" +
                            " where \"MovieID\" = "+ MovID;
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    selectResultMovie.next();
                    int old_watches = selectResultMovie.getInt(1) + 1;
                    String updateQuery = "Update p320_26.movie Set \"Play Count\" = " +
                            old_watches+ " Where " +  "\"MovieID\" = "+ MovID;
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate(updateQuery);
                }
                System.out.println("Watch Success!");
                return;
            }
            else{
                System.out.println("You do not have a collection by that name.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private static void profileSearchRate(Connection conn){
        System.out.print("Name of the profile you want to see (leave blank for yourself) : ");
        String Profile_Name = scanner.nextLine();
        if(Profile_Name.equals("")){
            System.out.println( "\n" + User + "'s Profile");
            try{
                String selectQueryCount = "Select Count(*) as Collection_Count" +
                        " from p320_26.collection WHERE" +
                        " \"Username\" = '" + User  + "'";
                Statement selectStatementCount = conn.createStatement();
                ResultSet selectResultCount =
                        selectStatementCount.executeQuery(selectQueryCount);
                selectResultCount.next();
                int count = selectResultCount.getInt(1);
                System.out.println(count + " collections");
                String selectQueryFollow = "Select \"Followers\", \"Following\"" +
                        " from p320_26.users WHERE" +
                        " \"Username\" = '" + User  + "'";
                Statement selectStatementFollow = conn.createStatement();
                ResultSet selectResultFollow =
                        selectStatementFollow.executeQuery(selectQueryFollow);
                selectResultFollow.next();
                System.out.println(selectResultFollow.getInt(1) + " Followers");
                System.out.println(selectResultFollow.getInt(2) + " Following");
                System.out.println("List of top 10 rated movies\n");
                String selectQueryRating = "Select m.\"Name\", u.\"User_Rating\"" +
                        " from p320_26.userratesmovie u, p320_26.movie m WHERE" +
                        " u.\"Username\" = '" + User  + "' and m.\"MovieID\" = u.\"MovieID\"" +
                        " ORDER BY u.\"User_Rating\" desc" +
                        " LIMIT 10";
                Statement selectStatementRating = conn.createStatement();
                ResultSet selectResultRating =
                        selectStatementRating.executeQuery(selectQueryRating);
                while(selectResultRating.next()){
                    System.out.println("\t" + selectResultRating.getString(1)+ "\t" + selectResultRating.getDouble(2) );
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            try{
                String selectQuery = "Select COUNT(*) from p320_26.users WHERE" +
                        " \"Username\" = '" + Profile_Name + "'";
                Statement selectStatement = conn.createStatement();
                ResultSet selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int count = selectResult.getInt(1);
                if(count == 1){
                    System.out.println( "\n" + Profile_Name + "'s Profile");
                    try{
                        String selectQueryCount = "Select Count(*) as Collection_Count" +
                                " from p320_26.collection WHERE" +
                                " \"Username\" = '" + Profile_Name  + "'";
                        Statement selectStatementCount = conn.createStatement();
                        ResultSet selectResultCount =
                                selectStatementCount.executeQuery(selectQueryCount);
                        selectResultCount.next();
                        count = selectResultCount.getInt(1);
                        System.out.println(count + " collections");
                        String selectQueryFollow = "Select \"Followers\", \"Following\"" +
                                " from p320_26.users WHERE" +
                                " \"Username\" = '" + Profile_Name  + "'";
                        Statement selectStatementFollow = conn.createStatement();
                        ResultSet selectResultFollow =
                                selectStatementFollow.executeQuery(selectQueryFollow);
                        selectResultFollow.next();
                        System.out.println(selectResultFollow.getInt(1) + " Followers");
                        System.out.println(selectResultFollow.getInt(2) + " Following");
                        System.out.println("List of top 10 rated movies\n");
                        String selectQueryRating = "Select m.\"Name\", u.\"User_Rating\"" +
                                " from p320_26.userratesmovie u, p320_26.movie m WHERE" +
                                " u.\"Username\" = '" + Profile_Name  + "' and m.\"MovieID\" = u.\"MovieID\"" +
                                " ORDER BY u.\"User_Rating\" desc" +
                                " LIMIT 10";
                        Statement selectStatementRating = conn.createStatement();
                        ResultSet selectResultRating =
                                selectStatementRating.executeQuery(selectQueryRating);
                        while(selectResultRating.next()){
                            System.out.println("\t" + selectResultRating.getString(1)+ "\t" + selectResultRating.getDouble(2) );
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
                else{
                    System.out.println("Not found");
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    private static void recommendRating(Connection conn){
        try {
            String selectQuery = "Select \"Name\", \"Director\", \"Duration \", \"mpaa\", \"UserAvgRating\"" +
                    " from p320_26.movie" +
                    " ORDER BY \"UserAvgRating\" desc" +
                    " LIMIT 20";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                }
                old_movie = selectResult.getString(1 );
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void recommendHistory(Connection conn){
        try {
            // Get Genres of most watched movies
            String selectQuery = "Select g.\"GenreName\", u.\"Play_Count\"" +
                    " from p320_26.userwatchesmovie u, p320_26.moviegenre g" +
                    " ORDER BY u.\"Play_Count\" desc";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            int limit = 0;
                while(selectResult.next()) {
                    String genre = selectResult.getString(1);
                    String selectQueryMovie = "Select distinct m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", m.\"UserAvgRating\"" +
                            " from p320_26.movie m, p320_26.moviegenre g, p320_26.userwatchesmovie u WHERE" +
                            " g.\"GenreName\" like '%" + genre + "%' AND g.\"MovieID\" = m.\"MovieID\" AND u.\"Username\" = '" + User + "' AND m.\"MovieID\" != u.\"MovieID\"" +
                            " ORDER BY m.\"UserAvgRating\" desc" +
                            " LIMIT 20";
                    Statement selectStatementMovie = conn.createStatement();
                    ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                    while(selectResultMovie.next()){
                        if(limit < 20){
                            System.out.print("\n" + selectResultMovie.getString(1 ) + "\t");
                            System.out.print(selectResultMovie.getString(2 ) + "\t");
                            System.out.print(selectResultMovie.getInt(3 ) + " minutes\t");
                            System.out.print(selectResultMovie.getString(4 ) + "\t");
                            System.out.print(selectResultMovie.getDouble(5 ) + "\n");
                            limit++;
                        }
                        else{
                            return;
                        }
                    }
                }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void recommendNew(Connection conn){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String date_string = formatter.format(date);
        String Year = date_string.substring(0,4);
        String Month = date_string.substring(5,7);
        if(Month.equals("01")){
            Month = "Jan";
        }
        else if(Month.equals("02")){
            Month = "Feb";
        }
        else if(Month.equals("03")){
            Month = "Mar";
        }
        else if(Month.equals("04")){
            Month = "Apr";
        }
        else if(Month.equals("05")){
            Month = "May";
        }
        else if(Month.equals("06")){
            Month = "Jun";
        }
        else if(Month.equals("07")){
            Month = "Jul";
        }
        else if(Month.equals("08")){
            Month = "Aug";
        }
        else if(Month.equals("09")){
            Month = "Sep";
        }
        else if(Month.equals("10")){
            Month = "Oct";
        }
        else if(Month.equals("11")){
            Month = "Nov";
        }
        else if(Month.equals("12")){
            Month = "Dec";
        }
        try {
            String selectQuery = "Select \"Name\", \"Director\", \"Duration \", \"mpaa\", \"UserAvgRating\"" +
                    " from p320_26.movie" +
                    " Where \"ReleaseDate\" like '%" + Month + "%" + Year + "%'" +
                    " ORDER BY \"UserAvgRating\" desc" +
                    " LIMIT 5";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            String old_movie = "";
            String new_movie = "";
            while(selectResult.next()){
                new_movie = selectResult.getString(1 );
                if(!(old_movie.equals(new_movie))){
                    System.out.print("\n" + selectResult.getString(1 ) + "\t");
                    System.out.print(selectResult.getString(2 ) + "\t");
                    System.out.print(selectResult.getInt(3 ) + " minutes\t");
                    System.out.print(selectResult.getString(4 ) + "\t");
                    System.out.print(selectResult.getDouble(5 ) + "\n");
                }
                old_movie = selectResult.getString(1 );
            }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void addFriendUsername(Connection conn){
        System.out.println("Type in user you want to follow: ");
        String Profile_Name = scanner.nextLine();
        try{
            String selectQuery = "Select COUNT(*) from p320_26.users WHERE" +
                    " \"Username\" = '" + Profile_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count == 1){
                try{
                    selectQuery = "Select COUNT(*) from p320_26.friends WHERE" +
                            " \"Followed\" = '" + Profile_Name + "' AND \"Follower\" = '" + User + "'";
                    selectStatement = conn.createStatement();
                    selectResult =
                            selectStatement.executeQuery(selectQuery);
                    selectResult.next();
                    count = selectResult.getInt(1);
                    if(count == 0){
                        String v = "'" + User + "','" + Profile_Name + "'" ;
                        String insertQuery = "insert into p320_26.friends VALUES ("+ v + ")";
                            Statement insertStatement = conn.createStatement();
                            insertStatement.executeUpdate(insertQuery);
                        selectQuery = "Select \"Followers\" from p320_26.users WHERE" +
                                " \"Username\" = '" + Profile_Name + "'";
                        selectStatement = conn.createStatement();
                        selectResult =
                                selectStatement.executeQuery(selectQuery);
                        selectResult.next();
                        count = selectResult.getInt(1) + 1;
                        String updateQuery = "Update p320_26.users Set \"Followers\"='" +
                                count +"' Where \"Username\"='" + Profile_Name + "'";
                            Statement updateStatement = conn.createStatement();
                            updateStatement.executeUpdate(updateQuery);
                        selectQuery = "Select \"Following\" from p320_26.users WHERE" +
                                " \"Username\" = '" + User + "'";
                        selectStatement = conn.createStatement();
                        selectResult =
                                selectStatement.executeQuery(selectQuery);
                        selectResult.next();
                        count = selectResult.getInt(1) + 1;
                        updateQuery = "Update p320_26.users Set \"Following\"='" +
                                count +"' Where \"Username\"='" + User + "'";
                        updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                        System.out.println("Follow Success!");
                    }
                    else{
                        System.out.println("You already follow this user");
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
            else{
                System.out.println("Not found");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private static void recommendFriend(Connection conn){
        try {

                String selectQueryMovie = "Select distinct m.\"Name\", m.\"Director\", m.\"Duration \", m.\"mpaa\", u.\"User_Rating\", f.\"Followed\"" +
                        " from p320_26.movie m, p320_26.friends f, p320_26.userratesmovie u WHERE" +
                        " u.\"MovieID\" = m.\"MovieID\" AND u.\"Username\" = f.\"Followed\" and f.\"Follower\" = '" + User + "'" +
                        " ORDER BY u.\"User_Rating\" desc" +
                        " LIMIT 20";
                Statement selectStatementMovie = conn.createStatement();
                ResultSet selectResultMovie = selectStatementMovie.executeQuery(selectQueryMovie);
                while(selectResultMovie.next()){
                        System.out.print("\n" + selectResultMovie.getString(6) + " recommends \t");
                        System.out.print(selectResultMovie.getString(1 ) + "\t");
                        System.out.print(selectResultMovie.getString(2 ) + "\t");
                        System.out.print(selectResultMovie.getInt(3 ) + " minutes\t");
                        System.out.print(selectResultMovie.getString(4 ) + "\t");
                        System.out.print(selectResultMovie.getDouble(5 ) + "\n");
                    }
        }
        catch(Exception e){
            System.out.println(e);
        };
    }

    private static void profileSearchWatch(Connection conn){
        System.out.print("Name of the profile you want to see (leave blank for yourself) : ");
        String Profile_Name = scanner.nextLine();
        if(Profile_Name.equals("")){
            System.out.println( "\n" + User + "'s Profile");
            try{
                String selectQueryCount = "Select Count(*) as Collection_Count" +
                        " from p320_26.collection WHERE" +
                        " \"Username\" = '" + User  + "'";
                Statement selectStatementCount = conn.createStatement();
                ResultSet selectResultCount =
                        selectStatementCount.executeQuery(selectQueryCount);
                selectResultCount.next();
                int count = selectResultCount.getInt(1);
                System.out.println(count + " collections");
                String selectQueryFollow = "Select \"Followers\", \"Following\"" +
                        " from p320_26.users WHERE" +
                        " \"Username\" = '" + User  + "'";
                Statement selectStatementFollow = conn.createStatement();
                ResultSet selectResultFollow =
                        selectStatementFollow.executeQuery(selectQueryFollow);
                selectResultFollow.next();
                System.out.println(selectResultFollow.getInt(1) + " Followers");
                System.out.println(selectResultFollow.getInt(2) + " Following");
                System.out.println("List of top 10 most watched movies\n");
                String selectQueryRating = "Select m.\"Name\", u.\"Play_Count\"" +
                        " from p320_26.userwatchesmovie u, p320_26.movie m WHERE" +
                        " u.\"Username\" = '" + User  + "' and m.\"MovieID\" = u.\"MovieID\"" +
                        " ORDER BY u.\"Play_Count\" desc" +
                        " LIMIT 10";
                Statement selectStatementRating = conn.createStatement();
                ResultSet selectResultRating =
                        selectStatementRating.executeQuery(selectQueryRating);
                while(selectResultRating.next()){
                    System.out.println("\t" + selectResultRating.getString(1)+ "\t" + selectResultRating.getInt(2) );
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            try{
                String selectQuery = "Select COUNT(*) from p320_26.users WHERE" +
                        " \"Username\" = '" + Profile_Name + "'";
                Statement selectStatement = conn.createStatement();
                ResultSet selectResult =
                        selectStatement.executeQuery(selectQuery);
                selectResult.next();
                int count = selectResult.getInt(1);
                if(count == 1){
                    System.out.println( "\n" + Profile_Name + "'s Profile");
                    try{
                        String selectQueryCount = "Select Count(*) as Collection_Count" +
                                " from p320_26.collection WHERE" +
                                " \"Username\" = '" + Profile_Name  + "'";
                        Statement selectStatementCount = conn.createStatement();
                        ResultSet selectResultCount =
                                selectStatementCount.executeQuery(selectQueryCount);
                        selectResultCount.next();
                        count = selectResultCount.getInt(1);
                        System.out.println(count + " collections");
                        String selectQueryFollow = "Select \"Followers\", \"Following\"" +
                                " from p320_26.users WHERE" +
                                " \"Username\" = '" + Profile_Name  + "'";
                        Statement selectStatementFollow = conn.createStatement();
                        ResultSet selectResultFollow =
                                selectStatementFollow.executeQuery(selectQueryFollow);
                        selectResultFollow.next();
                        System.out.println(selectResultFollow.getInt(1) + " Followers");
                        System.out.println(selectResultFollow.getInt(2) + " Following");
                        System.out.println("List of top 10 watched movies\n");
                        String selectQueryRating = "Select m.\"Name\", u.\"Play_Count\"" +
                                " from p320_26.userwatchesmovie u, p320_26.movie m WHERE" +
                                " u.\"Username\" = '" + Profile_Name  + "' and m.\"MovieID\" = u.\"MovieID\"" +
                                " ORDER BY u.\"Play_Count\" desc" +
                                " LIMIT 10";
                        Statement selectStatementRating = conn.createStatement();
                        ResultSet selectResultRating =
                                selectStatementRating.executeQuery(selectQueryRating);
                        while(selectResultRating.next()){
                            System.out.println("\t" + selectResultRating.getString(1)+ "\t" + selectResultRating.getInt(2) );
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
                else{
                    System.out.println("Not found");
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    private static void deleteFriendUsername(Connection conn){
        System.out.println("Type in user you want to stop following: ");
        String Profile_Name = scanner.nextLine();
        try{
            String selectQuery = "Select COUNT(*) from p320_26.users WHERE" +
                    " \"Username\" = '" + Profile_Name + "'";
            Statement selectStatement = conn.createStatement();
            ResultSet selectResult =
                    selectStatement.executeQuery(selectQuery);
            selectResult.next();
            int count = selectResult.getInt(1);
            if(count == 1){
                try{
                    selectQuery = "Select COUNT(*) from p320_26.friends WHERE" +
                            " \"Followed\" = '" + Profile_Name + "' AND \"Follower\" = '" + User + "'";
                    selectStatement = conn.createStatement();
                    selectResult =
                            selectStatement.executeQuery(selectQuery);
                    selectResult.next();
                    count = selectResult.getInt(1);
                    if(count == 1){
                        String insertQuery = "delete from p320_26.friends where \"Followed\" = '" + Profile_Name + "' and \"Follower\" = '" + User + "'";
                        Statement insertStatement = conn.createStatement();
                        insertStatement.executeUpdate(insertQuery);
                        selectQuery = "Select \"Followers\" from p320_26.users WHERE" +
                                " \"Username\" = '" + Profile_Name + "'";
                        selectStatement = conn.createStatement();
                        selectResult =
                                selectStatement.executeQuery(selectQuery);
                        selectResult.next();
                        count = selectResult.getInt(1) - 1;
                        String updateQuery = "Update p320_26.users Set \"Followers\"='" +
                                count +"' Where \"Username\"='" + Profile_Name + "'";
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                        selectQuery = "Select \"Following\" from p320_26.users WHERE" +
                                " \"Username\" = '" + User + "'";
                        selectStatement = conn.createStatement();
                        selectResult =
                                selectStatement.executeQuery(selectQuery);
                        selectResult.next();
                        count = selectResult.getInt(1) - 1;
                        updateQuery = "Update p320_26.users Set \"Following\"='" +
                                count +"' Where \"Username\"='" + User + "'";
                        updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                        System.out.println("Unfollow Success!");
                    }
                    else{
                        System.out.println("You do not follow this user");
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
            else{
                System.out.println("Not found");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static void UserStart(Connection conn)
    {
        scanner = new Scanner(System.in);
        loggedin = false;
        while(!loggedin)
        {
            System.out.println("Please enter: 'login'" +
                    " or 'register' or 'quit'" );
            String line = scanner.nextLine();
            if(line.equals(login))
            {
                LOGIN(conn);
            }
            else if(line.equals(register))
            {
                REGISTER(conn);
            }
            else if (line.equals(quit))
            {
                return;
            }
        }
        while(true)
        {
            System.out.println("\nPlease enter: 'help'" +  " or 'quit' or an action described by help" );
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if(tokens[0].equals("search"))
            {
                if(tokens[1].equals("name")){
                    searchName(conn);
                }
                else if(tokens[1].equals("date")){
                    searchDate(conn);
                }
                else if(tokens[1].equals("cast")){
                    searchCast(conn);
                }
                else if(tokens[1].equals("studio")){
                    searchStudio(conn);
                }
                else if(tokens[1].equals("genre")){
                    searchGenre(conn);
                }
            }
            else if( tokens[0].equals("create")){
                createCollection(conn);
            }

            else if(tokens[0].equals("add")){
                if(tokens[1].equals("movie")){
                    addMovieCollection(conn);
                }
                else if(tokens[1].equals("follow")){
                    addFriendUsername(conn);
                }
            }
            else if(tokens[0].equals("delete")){
                if(tokens[1].equals("movie")){
                    deleteMovieCollection(conn);
                }
                else if(tokens[1].equals("collection")){
                    deleteCollection(conn);
                }
                else if (tokens[1].equals("follow")) {
                    deleteFriendUsername(conn);
                }
            }
            else if(tokens[0].equals("rename")){
                renameCollection(conn);
            }
            else if(tokens[0].equals("show")){
                showCollections(conn);
            }
            else if(tokens[0].equals("rate")){
                rateMovie(conn);
            }
            else if(tokens[0].equals("watch")){
                if(tokens[1].equals("movie")){
                    watchMovie(conn);
                }
                if(tokens[1].equals("collection")){
                    watchCollection(conn);
                }
            }
            else if(tokens[0].equals("profile")){
                if(tokens[1].equals("rating")){
                    profileSearchRate(conn);
                }
                else if(tokens[1].equals("watches")){
                    profileSearchWatch(conn);
                }
            }
            else if(tokens[0].equals("recommend")){
                if(tokens[1].equals("rating")){
                    recommendRating(conn);
                }
                else if(tokens[1].equals("history")){
                    recommendHistory(conn);
                }
                else if(tokens[1].equals("new")){
                    recommendNew(conn);
                }
                else if(tokens[1].equals("follow")){
                    recommendFriend(conn);
                }
            }
            else if (tokens[0].equals("help")){
                help();
            }
            else if (tokens[0].equals(quit))
            {
                return;
            }
        }
    }
}
