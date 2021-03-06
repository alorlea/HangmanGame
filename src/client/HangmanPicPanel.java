/*
 * HangmanPicPanel.java
 *
 * Created on 09-nov-2011, 14:54:33
 */
package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;



/**
 *
 * @author Alberto Lorente and Fernando Garcia
 */
public class HangmanPicPanel
   extends javax.swing.JPanel
{
   private final int thickness = 3;
   private final Color color = Color.BLUE;
   private int drawingSteps = 10;
   
   /** Creates new form HangmanPicPanel */
   public HangmanPicPanel()
   {
      initComponents();
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

   public void setDrawingSteps(int steps)
   {
      drawingSteps = steps;
   }

   @Override
   protected void paintComponent(Graphics graphics)
   {
      super.paintComponent(graphics);

      if (drawingSteps < 10)
      {
         int w = getWidth();
         int h = getHeight();

         Graphics2D thickGraphics = (Graphics2D) graphics;
         thickGraphics.setColor(color);

         thickGraphics.setStroke(new BasicStroke(thickness));

         // Ground:
         thickGraphics.drawLine(thickness, h - thickness,
                                w - thickness, h - thickness);

         if (drawingSteps < 9)
         {
            // Vertical post:
            thickGraphics.drawLine(w / 10, h - thickness, w / 10, thickness);

            if (drawingSteps < 8)
            {
               // Horizontal post:
               thickGraphics.drawLine(w / 10, thickness, w - w / 10, thickness);

               if (drawingSteps < 7)
               {
                  // Crossed post:
                  thickGraphics.drawLine(w / 10, thickness + h / 10,
                                         2 * w / 10, thickness);

                  // Rope:
                  thickGraphics.drawLine(w - 4 * w / 10, thickness,
                                         w - 4 * w / 10, h / 10);

                  if (drawingSteps < 6)
                  {
                     // Head:
                     thickGraphics.drawOval(w - 4 * w / 10 - (w / 6) / 2, h / 10,
                                            w / 6, w / 6);

                     if (drawingSteps < 5)
                     {
                        // Body:
                        thickGraphics.drawLine(w - 4 * w / 10, h / 10 + w / 6,
                                               w - 4 * w / 10, h / 2);

                        if (drawingSteps < 4)
                        {
                           // Left arm:
                           thickGraphics.drawLine(w - 4 * w / 10, 
                                                  h / 10 + w / 6,
                                                  w - 4 * w / 10 - (w / 6) / 2, 
                                                  (int) (h / 2.2));

                           if (drawingSteps < 3)
                           {
                              // Right arm:
                              thickGraphics.drawLine(w - 4 * w / 10, 
                                                     h / 10 + w / 6,
                                                     w - 4 * w / 10 + (w / 6) / 2, 
                                                     (int) (h / 2.2));

                              if (drawingSteps < 2)
                              {
                                 // Left leg:
                                 thickGraphics.drawLine(w - 4 * w / 10, 
                                                        h / 2,
                                                        w - 4 * w / 10 - (w / 6) / 2, 
                                                        (int) (h / 1.5));

                                 if (drawingSteps < 1)
                                 {
                                    // Rightt leg:
                                    thickGraphics.drawLine(w - 4 * w / 10, 
                                                           h / 2,
                                                           w - 4 * w / 10 + (w / 6) / 2, 
                                                           (int) (h / 1.5));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
