package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Listens to a socket and, for every client connection, takes a thread from a
 * pool which executes the GameHandler class.
 * 
 * @author Alberto Lorente and Fernando Garcia
 * @see GameHandler
 */
public class GameServer
{
   private final boolean DEBUG;
   
   public static final int DEFAULT_MAX_CONN = 100;
   public static final int DEFAULT_PORT = 22569;
   protected ServerSocket serv;
   protected ExecutorService threadPool;
   protected ArrayList<String> dictionary;
   protected BufferedReader words;


   /**
    * Private constructor.
    * We don't want anyone outside the class to construct instances of this server.
    */
   private GameServer(int port, int maxConn, boolean deb)
   {
      DEBUG = deb;
      
      try
      {
         serv = new ServerSocket(port);
         
         System.out.println("Hangman Server 1.0 at " + port);
         if (DEBUG)
         {
            System.out.println("Debug mode.");
         }
         
         threadPool = Executors.newFixedThreadPool(maxConn);
      }
      catch (IOException e)
      {
         System.err.println("Unable to start server at port: " + port);
         System.exit(1);
      }
      catch (IllegalArgumentException iae)
      {
         System.err.println("Illegal number of threads");
         System.exit(1);
      }

      dictionary = this.FileToList();


      while (true)
      {
         try
         {
            Socket conn = serv.accept();
            threadPool.execute(new GameHandler(conn, dictionary, DEBUG));
            
            if (DEBUG)
            {
               System.out.println("Connection accepted from the client: " + 
                                  conn.getInetAddress().toString());
            }
         }
         catch (IOException ex)
         {
            System.err.println("Error creating the connexion with the client.");
         }
      }
   }

   /**
    * Method for passing the file into an array list for accessing the words 
    * in memory for all the threads.
    */
   private ArrayList<String> FileToList()
   {
      ArrayList<String> list = new ArrayList<String>();

      String sep = File.separator;
      String path = "dictionaries" + sep + "words";
      InputStream inputStream = getClass().getResourceAsStream(path);
      words = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      
      try
      {
         while ((line = words.readLine()) != null)
         {
            String newLine = line.toUpperCase();
            list.add(newLine);
         }
      }
      catch (IOException ioe)
      {
         System.err.println("Error reading file");
         System.exit(1);
      }
      
      System.out.println("Dictionary successfully loaded.");
      return list;
   }


   /**
    * File Server based in a Pooled Server.
    * Usage: HangmanServer <port> <maximum_number_of_connections>.
    * If not specified, the default values are:
    * - port: 22569
    * - maximum_number_of_connections: 100
    * @param args - args[0] = server port, args[1] = maximum number of 
    * conections, args[2] = debug?
    */
   public static void main(String args[])
   {
      int maxConn = 0, port = 0;
      boolean debug = false;

      switch (args.length)
      {
         case 3:
            if (args[2].equals("-v"))
               debug = true;
            else
            {
               System.out.println("Usage: HangmanServer [<port> <maximum number of "
                                  + "connections>] [-v]");
               System.exit(1);
            }
            maxConn = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[0]);
            break;
            
         case 2:
            maxConn = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[0]);
            break;

         case 1:
            if (args[0].equals("-v"))
            {
               debug = true;
               port = DEFAULT_PORT;
            }
            else
               port = Integer.parseInt(args[0]);
            
            maxConn = DEFAULT_MAX_CONN;
            break;

         case 0:
            maxConn = DEFAULT_MAX_CONN;
            port = DEFAULT_PORT;
            break;

         default:
            System.out.println("Usage: HangmanServer <port> <maximum number of "
                               + "connections> [-v]");
            System.exit(1);
      }

      GameServer server = new GameServer(port, maxConn, debug);
   }
}
