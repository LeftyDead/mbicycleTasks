package bel.l4d;

import bel.l4d.task1.Calculator;
import bel.l4d.task2.EchoServer;
import bel.l4d.task3.SqlWorker;
import bel.l4d.task3.Book;

import static bel.l4d.ErrorHandler.exitWithMsg;

import java.util.ArrayList;
import java.util.Arrays;

public class TasksLauncher {
    static private String argumentSingleString = "";
    static private ArrayList<String> listOfArgs = null;
    static private int taskNumToLaunch = 0;
    static private String task1Expression = null;

    public static void launchTasks(String[] args) {

        if(args.length==0) {
            exitWithMsg("[TasksLauncher] Error: Invalid arguments: missing arguments. Exiting...");
        }

        // ||| START ||| Parsing Arguments.
        argumentSingleString = Arrays.toString(args);
        if(argumentSingleString.length() < 2 ) {
            exitWithMsg("[TasksLauncher] Error: Invalid arguments: too few arguments. Exiting...");
        }
        argumentSingleString = argumentSingleString.replaceAll("[,\\[|\\]]","");

        listOfArgs = new ArrayList<>();
        int fromLetterNum = 0;
        String subStr = null;
        int findOcc = -1;
        while( (findOcc = argumentSingleString.indexOf(";",fromLetterNum)) !=-1 ) {
            listOfArgs.add(argumentSingleString.substring(fromLetterNum,findOcc));
            fromLetterNum = findOcc+1;
        }
        listOfArgs.add(argumentSingleString.substring(fromLetterNum,argumentSingleString.length()));

        if(listOfArgs.size() == 1 && !listOfArgs.get(0).equals("2") || listOfArgs.size() > 6) {
            exitWithMsg("[TasksLauncher] Error: Invalid arguments: too few arguments. Exiting...");
        }

        try {
            taskNumToLaunch = Integer.parseInt(listOfArgs.get(0));
        } catch (NumberFormatException nfe) {
            exitWithMsg("[TasksLauncher] Error: First argument is not a number. Exiting...");
        }
        // ||| END ||| Parsing Arguments. Now execute needed task.

        if (taskNumToLaunch == 1 && listOfArgs.size() == 2) {
            double answer = 0;
            try {
                Calculator calc = new Calculator(listOfArgs.get(1));
                answer = calc.calculate();
            } catch (Exception e) {
                exitWithMsg("[Task 1] Error: " + e.getMessage() + " Exiting...");    //#FIX
            }
            System.out.println("The answer is: " + answer);
            return;
        }
        if (taskNumToLaunch == 2 && listOfArgs.size() == 1) {
            try {
                EchoServer es = new EchoServer();
                System.out.println("Server started on port 8020.");
                /*
                    //To close the Echo Server:

                 try {
                        es.closeServer();
                    } catch (Exception e) {
                        exitWithMsg("[task 2]: " + e.getMessage() + " Exiting...");
                    }
                */
                return;
            } catch (Exception e) {
                exitWithMsg("[Task 2] Error: " + e.getMessage() + "  Exiting...");
            }
            return;
        }
        if (taskNumToLaunch == 3 && listOfArgs.size() == 6 ) {
            try {
                SqlWorker sw = new SqlWorker(listOfArgs.get(1), listOfArgs.get(2), listOfArgs.get(3), listOfArgs.get(4));

                ArrayList<Book> books = new ArrayList<>();
                books = sw.getBooksByAuthor(listOfArgs.get(5));
                System.out.println("List of books by author "+ listOfArgs.get(5) + ":");
                for (Book book: books) {
                    System.out.println("    " + book.getTitle() + " (" + book.getYear() +")");
                }
                sw.closeConnection();
            } catch (Exception e) {
                exitWithMsg("[Task 3] Error: " + e.getMessage() + "  Exiting...");
            }
            return;
        }
        exitWithMsg("[TasksLauncher] Error: Invalid arguments. Exiting...");
    }
}