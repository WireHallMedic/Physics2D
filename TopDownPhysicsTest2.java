package Physics2D;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class TopDownPhysicsTest2 extends JPanel implements KeyListener, ActionListener
{
   public static final int TILE_SIZE = 50;   // in pixels
   public static final int MAP_SIZE = 12;
   public static final int MAX_SPEED = 200;
   public static final int WALKING_ACCEL = 50;
   public static final int WALKING_DECEL = 50;
   public static final int NORMAL_GRAVITY = 25;
   public static final int WATER_GRAVITY = 15;
   public static final int WATER_PHYSICS_INDEX = P2DManager.DEFAULT_PHYSICS_INDEX + 1;
   public static boolean[][] passMap;
   public static GeometryBlock[][] blockMap;
   private boolean leftHeld = false;
   private boolean rightHeld = false;
   private boolean upHeld = false;
   private boolean downHeld = false;
   private BoundingCircle circle;
   
   private MovingBC player;
   
   public TopDownPhysicsTest2()
   {
      super();
      setSize(800, 800);
      setVisible(true);
      
      player = new MovingBC(1000, 2000, 2000);
      player.setAffectedByGravity(false);
      player.setCorporeal(true);
      
      passMap = genMap();
      blockMap = genBlockMap();
      passMap = null;
      
      P2DManager.setTileSize(TILE_SIZE);
      
	   P2DManager.setSpeedMult(WATER_PHYSICS_INDEX, .5);
      
      // remember circles are drawn from the center
      circle = new BoundingCircle(1000,5500, 5500);
      
      javax.swing.Timer timer = new javax.swing.Timer(1000/30, null);
      timer.addActionListener(this);
      timer.start();
   }
   
   public void keyTyped(KeyEvent ke){}
   
   public void keyReleased(KeyEvent ke)
   {
      if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
         rightHeld = false;
      if(ke.getKeyCode() == KeyEvent.VK_LEFT)
         leftHeld = false;
      if(ke.getKeyCode() == KeyEvent.VK_UP)
         upHeld = false;
      if(ke.getKeyCode() == KeyEvent.VK_DOWN)
         downHeld = false;
   }
   
   public void keyPressed(KeyEvent ke)
   {/*
      if(ke.getKeyCode() == KeyEvent.VK_SPACE)
         player.setYSpeed(JUMP);*/
      if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
         rightHeld = true;
      if(ke.getKeyCode() == KeyEvent.VK_LEFT)
         leftHeld = true;
      if(ke.getKeyCode() == KeyEvent.VK_UP)
         upHeld = true;
      if(ke.getKeyCode() == KeyEvent.VK_DOWN)
         downHeld = true;
   }
   
   
   public void actionPerformed(ActionEvent ae)
   {
      int xSpd = player.getXSpeed();
      int ySpd = player.getYSpeed();
      double speedMult = player.getTopDownSpeedMult(blockMap);
      
      // horizontal movement
      if(rightHeld)
         xSpd += (int)(WALKING_ACCEL * speedMult);
      else if(leftHeld)
         xSpd -= (int)(WALKING_ACCEL * speedMult);
      else // no horiz held
      {
         if(xSpd < 0)
            xSpd = Math.min(xSpd + (int)((WALKING_DECEL) * speedMult), 0);
         else
            xSpd = Math.max(xSpd - (int)((WALKING_DECEL) * speedMult), 0);
      }
      if(xSpd > (int)(MAX_SPEED * speedMult))
         xSpd = (int)(MAX_SPEED * speedMult);
      else if(xSpd < -(int)(MAX_SPEED * speedMult))
         xSpd = -(int)(MAX_SPEED * speedMult);
         
      // vertical movement
      if(downHeld)
         ySpd += (int)(WALKING_ACCEL * speedMult);
      else if(upHeld)
         ySpd -= (int)(WALKING_ACCEL * speedMult);
      else // no vert held
      {
         if(ySpd < 0)
            ySpd = Math.min(ySpd + (int)((WALKING_DECEL) * speedMult), 0);
         else
            ySpd = Math.max(ySpd - (int)((WALKING_DECEL) * speedMult), 0);
      }
      if(ySpd > (int)(MAX_SPEED * speedMult))
         ySpd = (int)(MAX_SPEED * speedMult);
      else if(ySpd < -(int)(MAX_SPEED * speedMult))
         ySpd = -(int)(MAX_SPEED * speedMult);
         
      player.setXSpeed(xSpd);
      player.setYSpeed(ySpd);
      player.doPhysics(blockMap);
      this.repaint();
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
      for(int x = 0; x < MAP_SIZE; x++)
      for(int y = 0; y < MAP_SIZE; y++)
      {
         if(!blockMap[x][y].isPassable())
         {
            g.setColor(Color.BLACK);
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
         }
         else if(blockMap[x][y].getGravityIndex() == WATER_PHYSICS_INDEX)
         {
            g.setColor(Color.BLUE);
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
         }
      }
      
      g.setColor(Color.ORANGE);
      if(player.isColliding(circle))
         g.setColor(Color.RED);
      int r = circle.getRadius();
      g.fillOval(P2DManager.millitileToPixel(circle.getOriginX() - r), 
                 P2DManager.millitileToPixel(circle.getOriginY() - r), 
                 P2DManager.millitileToPixel(r + r), 
                 P2DManager.millitileToPixel(r + r));
      
      g.setColor(Color.YELLOW);
      g.fillOval(P2DManager.millitileToPixel(player.getOriginX() - 500), P2DManager.millitileToPixel(player.getOriginY() - 500), 
                       TILE_SIZE, TILE_SIZE);
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(800, 800);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      TopDownPhysicsTest2 pt = new TopDownPhysicsTest2();
      frame.addKeyListener(pt);
      frame.add(pt);
      frame.setVisible(true);
   }
   
   public static boolean[][] genMap()
   {
      boolean[][] m = new boolean[MAP_SIZE][MAP_SIZE];
      for(int x = 0; x < MAP_SIZE; x++)
      for(int y = 0; y < MAP_SIZE; y++)
      {
         if(x == 0 || x == MAP_SIZE - 1 || y == 0 || y == MAP_SIZE - 1)
            m[x][y] = false;
         else
            m[x][y] = true;
      }
      m[MAP_SIZE - 4][1] = false;
      m[MAP_SIZE / 2][MAP_SIZE - 4] = false;
      m[(MAP_SIZE / 2) + 1][MAP_SIZE - 4] = false;
      m[2][4] = false;
      return m;
   }
   
   public static GeometryBlock[][] genBlockMap()
   {
      GeometryBlock[][] bMap = new GeometryBlock[MAP_SIZE][MAP_SIZE];
      for(int x = 0; x < MAP_SIZE; x++)
      for(int y = 0; y < MAP_SIZE; y++)
      {
         if(passMap[x][y])
            bMap[x][y] = GeometryBlock.getOpenBlock();
         else
            bMap[x][y] = GeometryBlock.getSolidBlock();
      }
      for(int y = 1; y < MAP_SIZE - 1; y++)
      {
         bMap[MAP_SIZE - 2][y].setAllPhysicsIndices(WATER_PHYSICS_INDEX);
      }
      return bMap;
   }
}