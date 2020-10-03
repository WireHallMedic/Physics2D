package Physics2D;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class PhysicsTest extends JPanel implements KeyListener, ActionListener
{
   public static final int TILE_SIZE = 50;   // in pixels
   public static final int MAP_SIZE = 14;
   public static final int JUMP = -400;
   public static final int MAX_SPEED = 300;
   public static final int HORIZ_ACCEL = 50;
   public static final int NORMAL_GRAVITY = 30;
   public static final int NORMAL_TERMINAL_VELOCITY = 1000;
   public static final int WATER_GRAVITY = 15;
   public static final int WATER_PHYSICS_INDEX = P2DManager.DEFAULT_PHYSICS_INDEX + 1;
   public static final int ICE_PHYSICS_INDEX = WATER_PHYSICS_INDEX + 1;
   private boolean leftHeld = false;
   private boolean rightHeld = false;
   
   private MovingAABB player;
   private MovingBC nad;
   private Zone zone;
   
   public PhysicsTest()
   {
      super();
      setSize(800, 800);
      setVisible(true);
      
      player = new MovingAABB(1000, 1000, 2000, 2000);
      player.setAffectedByGravity(true);
      player.setCorporeal(true);
      
      nad = new MovingBC(500, 0, -1000);
      nad.setLeader(player);
      
      zone = new Zone(NORMAL_GRAVITY, NORMAL_TERMINAL_VELOCITY, 1.0, 1.0);
      zone.setGeometry(genBlockMap());
      zone.setGravity(WATER_PHYSICS_INDEX, WATER_GRAVITY);
      zone.setTerminalVelocity(WATER_PHYSICS_INDEX, NORMAL_TERMINAL_VELOCITY / 4);
      zone.setFriction(ICE_PHYSICS_INDEX, 0.25);
      
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
   }
   
   public void keyPressed(KeyEvent ke)
   {
      if(ke.getKeyCode() == KeyEvent.VK_SPACE)
         player.setYSpeed(JUMP);
      if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
         rightHeld = true;
      if(ke.getKeyCode() == KeyEvent.VK_LEFT)
         leftHeld = true;
   }
   
   
   public void actionPerformed(ActionEvent ae)
   {
      int xSpd = player.getXSpeed();
      double friction = player.getSideOnFriction(zone);
      double accelMult = (.5 + friction) / 2.0;
      // horizontal movement
      
      if(rightHeld)
         xSpd += (int)(HORIZ_ACCEL * accelMult);
      else if(leftHeld)
         xSpd -= (int)(HORIZ_ACCEL * accelMult);
      else // nothing held
      {
         if(xSpd < 0)
            xSpd = Math.min(xSpd + (int)((HORIZ_ACCEL) * friction), 0);
         else
            xSpd = Math.max(xSpd - (int)((HORIZ_ACCEL) * friction), 0);
      }
      if(xSpd > MAX_SPEED)
         xSpd = MAX_SPEED;
      else if(xSpd < -MAX_SPEED)
         xSpd = -MAX_SPEED;
         
      player.setXSpeed(xSpd);
      player.doPhysics(zone);
      this.repaint();
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
      int[] xList;
      int[] yList;
      for(int x = 0; x < MAP_SIZE; x++)
      for(int y = 0; y < MAP_SIZE; y++)
      {
         if(!(zone.getBlock(x, y).isPassable()))
         {
            g.setColor(Color.BLACK);
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
         }
         else if(zone.getBlock(x, y).getPhysicsIndex() == WATER_PHYSICS_INDEX)
         {
            g.setColor(Color.BLUE);
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
         }
      }
      g.setColor(Color.YELLOW);
      g.fillRect(P2DTools.millitileToPixel(player.getOriginX(), TILE_SIZE), 
                 P2DTools.millitileToPixel(player.getOriginY(), TILE_SIZE), 
                 TILE_SIZE, TILE_SIZE);
      
      // the nad
      g.setColor(Color.ORANGE);
      int nadRad = P2DTools.millitileToPixel(nad.getRadius(), TILE_SIZE);
      g.fillOval(P2DTools.millitileToPixel(nad.getOriginX(), TILE_SIZE) - nadRad, 
                 P2DTools.millitileToPixel(nad.getOriginY(), TILE_SIZE) - nadRad, 
                        nadRad * 2, nadRad * 2);
                       
      // player sensors
      int sensorInset = P2DTools.millitileToPixel(player.sensorInset, TILE_SIZE);
      int sensorLength = TILE_SIZE - sensorInset - sensorInset;
      int playerXLoc = P2DTools.millitileToPixel(player.getOriginX(), TILE_SIZE);
      int playerYLoc = P2DTools.millitileToPixel(player.getOriginY(), TILE_SIZE);
      g.setColor(Color.RED);
      
      if(player.topSensor())
         g.setColor(Color.RED);
      else
         g.setColor(Color.GREEN.darker());
      g.fillRect(playerXLoc + sensorInset, playerYLoc, 
                 sensorLength, 4);
         
      if(player.bottomSensor())
         g.setColor(Color.RED);
      else
         g.setColor(Color.GREEN.darker());
      g.fillRect(playerXLoc + sensorInset, playerYLoc + TILE_SIZE - 4, 
                 sensorLength, 4);
         
      if(player.rightSensor())
         g.setColor(Color.RED);
      else
         g.setColor(Color.GREEN.darker());
      g.fillRect(playerXLoc + TILE_SIZE - 4, playerYLoc + sensorInset, 
                 4, sensorLength);
         
      if(player.leftSensor())
         g.setColor(Color.RED);
      else
         g.setColor(Color.GREEN.darker());
      g.fillRect(playerXLoc, playerYLoc + sensorInset, 
                 4, sensorLength);
   }
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(800, 800);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      PhysicsTest pt = new PhysicsTest();
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
      m[MAP_SIZE / 2][MAP_SIZE - 5] = false;
      m[(MAP_SIZE / 2) + 1][MAP_SIZE - 5] = false;
      m[(MAP_SIZE / 2) + 2][MAP_SIZE - 5] = false;
      m[(MAP_SIZE / 2) + 3][MAP_SIZE - 5] = false;
      m[2][4] = false;
      m[3][MAP_SIZE - 4] = false;
      m[4][MAP_SIZE - 4] = false;
      m[3][MAP_SIZE - 3] = false;
      return m;
   }
   
   public static GeometryBlock[][] genBlockMap()
   {
      GeometryBlock[][] bMap = new GeometryBlock[MAP_SIZE][MAP_SIZE];
      boolean[][] passMap = genMap();
      for(int x = 0; x < MAP_SIZE; x++)
      for(int y = 0; y < MAP_SIZE; y++)
      {
         if(passMap[x][y])
            bMap[x][y] = GeometryBlock.getOpenBlock();
         else
            bMap[x][y] = GeometryBlock.getSolidBlock();
      }
      
      bMap[MAP_SIZE / 2][MAP_SIZE - 4].setPhysicsIndex(ICE_PHYSICS_INDEX);
      bMap[(MAP_SIZE / 2) + 1][MAP_SIZE - 4].setPhysicsIndex(ICE_PHYSICS_INDEX);
      bMap[2][4].setPhysicsIndex(ICE_PHYSICS_INDEX);
      
      for(int y = 1; y < MAP_SIZE - 1; y++)
         bMap[MAP_SIZE - 2][y].setPhysicsIndex(WATER_PHYSICS_INDEX);
      
      return bMap;
   }
}