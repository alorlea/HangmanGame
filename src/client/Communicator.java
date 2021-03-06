package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import javax.swing.Timer;



/**
 *
 * @author Alberto Lorente and Fernando Garcia
 */
public class Communicator
   extends Thread
{
   private static class TimeoutHandler
      implements ActionListener
   {
      Communicator parent;
      
      public TimeoutHandler(Communicator parent)
      {
         this.parent = parent;
      }
      
      @Override
      public void actionPerformed(ActionEvent ae)
      {
         parent.timeout();
      }
   }
   
   
   private final int TIMEOUT = 30000;
   
   GameClient game;
   Socket clientSocket;
   Timer timer;
   BufferedReader reader;
   BufferedWriter writer;
   String eol = System.getProperty("line.separator");
   boolean communicating = true;
   boolean messageSent = false;
   Semaphore semaphore = new Semaphore(1, true);
   
   
   public Communicator(GameClient game, Socket s)
   {
      this.game = game;
      clientSocket = s;
      
      timer = new Timer(TIMEOUT, new TimeoutHandler(this));
   }

   @Override
   public void run()
   {
      try
      {
         reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
         writer = new BufferedWriter(
            new OutputStreamWriter(clientSocket.getOutputStream()));
         
         game.setStatus("Ready to send a message to the server.");
         
         sendMessage("STARTGAME");
         
         waitForReply();
         
         while (communicating)
         {
            waitForReply();
         }
         
         reader.close();
         writer.close();
         clientSocket.close();
      }
      catch (IOException ex)
      {
         System.err.println("Error when creating writers and readers over the "
            + "client socket.");
      }
   }


   public void sendMessage(String outMsg)
   {
//      try
//      {
//         semaphore.acquire();
//         
         try
         {
            writer.write(outMsg + eol);
            writer.flush();
            
            messageSent = true;
            
            if (timer.isRunning() == false)
            {
               timer.start();
            }
            
            if (outMsg.equals("NEXTGAME"))  // In this case, we do not expect reply.
            {
//               semaphore.release();
            }
         }
         catch (IOException ex)
         {
            System.err.println("Error writing over the client socket.");
//            semaphore.release();
         }
//      }
//      catch (InterruptedException ex)
//      {
//      }
   }


   private void processReceivedMessages(String[] list)
   {  
      if (list[0].equals("WINNER"))
      {
         int score = Integer.parseInt(list[1]);
         String codedWord = list[2];
         game.deactivateButtons();
         game.updateScore(score);
         game.updateWord(codedWord);
         game.showVictoryMsg();
      }
      else if (list[0].equals("GAMEOVER"))
      {
         int attempts = Integer.parseInt(list[1]);
         int score = Integer.parseInt(list[2]);
         String word = list[3];
         game.deactivateButtons();
         game.updateAttempts(attempts);
         game.updateScore(score);
         game.updateWord(word);
         game.showDefeatMsg();
      }
      else
      {
         String codedWord = list[0];
         int attempts = Integer.parseInt(list[1]);
         game.updateAttempts(attempts);
         game.updateWord(codedWord);
      }
   }
   
   public void stopCommunication()
   {
      communicating = false;
      timer.stop();
   }
   
   
   private void timeout()
   {
      stopCommunication();
      game.onServerDown();
   }


   private void waitForReply()
   {
      if (messageSent)
      {
         game.setStatus("Message sent to the server. Waiting for reply...");

         String data = null;
         try
         {
            data = reader.readLine();
         }
         catch (IOException ex)
         {
            System.err.println("Error when reading a message from the server.");
         }

         if (data != null)
         {
            String[] list = data.split(",");

            if (list != null)
            {
               game.setStatus("Message received from the server. Processing...");
               timer.stop();

               processReceivedMessages(list);

               game.setStatus("Ready to send another message to the server.");
            }
         }

         messageSent = false;
         
//         semaphore.release();
      }
   }
}
