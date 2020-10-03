/*

   Moving axis-aligned bounding box. Updates speeds at the end of each tick (after movement).
   All values are in millitiles

*/

package Physics2D;

import java.util.*;
import WidlerSuite.*;

public class MovingAABB extends AABB implements MovingBS
{
	private int xSpeed;
	private int ySpeed;
	private boolean affectedByGravity;
	private boolean corporeal;
	private int stepOriginX;      // used to avoid moving into walls
	private int stepOriginY;
   public static final int sensorInset = 10;
   private boolean bottomSensor = false;
   private boolean topSensor = false;
   private boolean rightSensor = false;
   private boolean leftSensor = false;
   private MovingBS leader = null;


	public int getXSpeed(){return xSpeed;}
	public int getYSpeed(){return ySpeed;}
	public boolean isAffectedByGravity(){return affectedByGravity;}
	public boolean isCorporeal(){return corporeal;}
   public boolean bottomSensor(){return bottomSensor;}
   public boolean topSensor(){return topSensor;}
   public boolean leftSensor(){return leftSensor;}
   public boolean rightSensor(){return rightSensor;}
   public boolean isFollower(){return leader != null;}
   public MovingBS getLeader(){return leader;}


	public void setXSpeed(int x){xSpeed = x;}
	public void setYSpeed(int y){ySpeed = y;}
   public void setSpeed(int x, int y){xSpeed = x; ySpeed = y;}
	public void setAffectedByGravity(boolean a){affectedByGravity = a;}
	public void setCorporeal(boolean c){corporeal = c;}
   public void setLeader(MovingBS l){leader = l;}

   
   public MovingAABB()
   {
      this(1000, 1000, 0, 0);
   }

   public MovingAABB(int w, int h)
   {
      this(w, h, 0, 0);
   }

   public MovingAABB(int w, int h, int x, int y)
   {
      super(w, h, x, y);
   	setAffectedByGravity(false);
   	setCorporeal(false);
   }
   
   @Override
   public int getOriginX()
   {
      if(isFollower())
         return getLeader().getCenterX() + super.getOriginX();
      return super.getOriginX();
   }
   
   @Override
   public int getOriginY()
   {
      if(isFollower())
         return getLeader().getCenterY() + super.getOriginY();
      return super.getOriginY();
   }
   
   public int getCenterX()
   {
      return getOriginX() + getHalfWidth();
   }
   
   
	public int getCenterY()
   {
      return getOriginY() + getHalfHeight();
   }

   // x and y are in tiles
   private boolean blocked(int x, int y, GeometryBlock[][] geoMap)
   {
      if(x < 0 || y < 0 || x >= geoMap.length || y >= geoMap[0].length)
         return true;
      return !geoMap[x][y].isPassable();
   }
   
   public void applyImpulse(int x, int y)
   {
      xSpeed += x;
      ySpeed += y;
   }
   
   public Vector<Coord> getOccupiedTiles()
   {
      Vector<Coord> tileList = new Vector<Coord>();
      int tileX1 = getOriginX() / 1000;
      int tileY1 = getOriginY() / 1000;
      int tileX2 = (getOriginX() + getWidth() - 1) / 1000;
      int tileY2 = (getOriginY() + getHeight() - 1) / 1000;
      
      for(int x = tileX1; x <= tileX2; x++)
      for(int y = tileY1; y <= tileY2; y++)
         tileList.add(new Coord(x, y));
      return tileList;
   }
   
   // move x, resolve x collisions, move y, resolve y collisions
   public void doPhysics(Zone zone)
   {
      int localGravity = getGravity(zone);
      // set prospective position
      if(affectedByGravity)
         ySpeed = Math.min(ySpeed + localGravity, localGravity);
      stepOriginX = getOriginX() + xSpeed;
      stepOriginY = getOriginY() + ySpeed;
      
      boolean stopHoriz = false;
      boolean stopVert = false;
      
      // corporeal objects check if they would collide with level geometry, stopping if they would.
      // if moving diagionally, one tile is checked twice, but this is fast enough it shouldn't be
      // a problem
      if(corporeal)
      {
         // check and resolve horizontal movement
         int snapLocX = getHorizSnapLoc(stepOriginX, getOriginY(), zone.getGeometry());
         if(snapLocX != -1)
         {
            stepOriginX = snapLocX;
         }

         // check and resolve vertical movement
         int snapLocY = getVertSnapLoc(stepOriginX, stepOriginY, zone.getGeometry());
         if(snapLocY != -1)
         {
            stepOriginY = snapLocY;
         }
         
         // check if you're bumping into any walls
         updateSensors(stepOriginX, stepOriginY, zone.getGeometry());
         
         // stop movement if bumping into wall on that vector
         if(xSpeed > 0 && rightSensor)
            xSpeed = 0;
         else if(xSpeed < 0 && leftSensor)
            xSpeed = 0;
         if(ySpeed > 0 && bottomSensor)
            ySpeed = 0;
         else if(ySpeed < 0 && topSensor)
            ySpeed = 0;
      }
      
      // set to final position. Directly accessing because of tile/millitile conversion.
      setOriginX(stepOriginX);
      setOriginY(stepOriginY);
   }
   
   // returns either the millitile y location to snap to, or -1
   private int getVertSnapLoc(int xPos, int yPos, GeometryBlock[][] geoMap)
   {
      int tileXStart;
      int tileXEnd;
      int tileY;
      boolean collision = false;
      int snapLoc = -1;
      
      // check down
      if(ySpeed > 0)
      {
         tileXStart = (xPos + sensorInset) / 1000;         // bottom left
         tileXEnd = (xPos + getWidth() - sensorInset) / 1000;   // bottom right
         tileY = (yPos + getHeight()) / 1000;
         for(int x = tileXStart; x <= tileXEnd; x++)
            if(blocked(x, tileY, geoMap))
            {
               collision = true;
               break;
            }
         if(collision)
         {
            snapLoc = (tileY * 1000) - getHeight();
         }
      }
      // check up
      else if(ySpeed < 0)
      {
         tileXStart = (xPos + sensorInset) / 1000;         // upper left
         tileXEnd = (xPos + getWidth() - sensorInset) / 1000;   // upper right
         tileY = yPos / 1000;
         for(int x = tileXStart; x <= tileXEnd; x++)
            if(blocked(x, tileY, geoMap))
            {
               collision = true;
               break;
            }
         if(collision)
         {
            snapLoc = (tileY + 1) * 1000;
         }
      }
      return snapLoc;
   }
   
   // returns either the millitile x location to snap to, or -1
   private int getHorizSnapLoc(int xPos, int yPos, GeometryBlock[][] geoMap)
   {
      int tileX;
      int tileYStart;
      int tileYEnd;
      boolean collision = false;
      int snapLoc = -1;
      
      // check right
      if(xSpeed > 0)
      {
         tileX = (xPos + getWidth()) / 1000;
         tileYStart = (yPos + sensorInset) / 1000;         // top
         tileYEnd = (yPos + getHeight() - sensorInset) / 1000;  // bottom
         for(int y = tileYStart; y <= tileYEnd; y++)
            if(blocked(tileX, y, geoMap))
            {
               collision = true;
               break;
            }
         if(collision)
         {
            snapLoc = (tileX * 1000) - getWidth();
         }
      }
      // check left
      else if(xSpeed < 0)
      {
         tileX = xPos / 1000;
         tileYStart = (yPos + sensorInset) / 1000;         // top
         tileYEnd = (yPos + getHeight() - sensorInset) / 1000;  // bottom
         for(int y = tileYStart; y <= tileYEnd; y++)
            if(blocked(tileX, y, geoMap))
            {
               collision = true;
               break;
            }
         if(collision)
         {
            snapLoc = (tileX + 1) * 1000;
         }
      }
      return snapLoc;
   }
   
   private void updateSensors(int x, int y, GeometryBlock[][] geoMap)
   {
      // horizontal checks
      int tileX1 = (x + getWidth() + 1) / 1000;                    // right
      int tileX2 = (x - 1) / 1000;                            // left
      int tileY1 = (y + sensorInset) / 1000;           // top
      int tileY2 = (y + getHeight() - sensorInset) / 1000;  // bottom
      
      rightSensor = blocked(tileX1, tileY1, geoMap) || blocked(tileX1, tileY2, geoMap);
      leftSensor = blocked(tileX2, tileY1, geoMap) || blocked(tileX2, tileY2, geoMap);
      
      // vertical checks
      tileY1 = (y + getHeight() + 1) / 1000;                       // bottom
      tileY2 = (y - 1) / 1000;                                // top
      tileX1 = (x + sensorInset) / 1000;               // left
      tileX2 = (x + getWidth() - sensorInset) / 1000;       // right
      
      bottomSensor = blocked(tileX1, tileY1, geoMap) || blocked(tileX2, tileY1, geoMap);
      topSensor = blocked(tileX1, tileY2, geoMap) || blocked(tileX2, tileY2, geoMap);
   }
   
   
   // side-on methods
   ///////////////////////////////////////////////////////////////////////////////////////
   
   // if the actor is standing on a block, returns the highest indexed friction, else
   // returns 1.0
   public double getSideOnFriction(Zone zone)
   {
      GeometryBlock[][] geoMap = zone.getGeometry();
      if(bottomSensor)
      {
         int index = -1;
         int tileY = (getOriginY() + getHeight()) / 1000;                        // bottom
         int tileX1 = (getOriginX() + sensorInset) / 1000;               // left
         int tileX2 = (getOriginX() + getWidth() - sensorInset) / 1000;       // right
         for(int x = tileX1; x <= tileX2; x++)
            index = Math.max(index, geoMap[x][tileY].getPhysicsIndex());
         return zone.getFriction(index);
      }
      return 1.0;
   }
   
   // returns the highest-indexed gravity used by one of the blocks which contains part of this AABB
   public int getGravity(Zone zone)
   {
      GeometryBlock[][] geoMap = zone.getGeometry();
      Vector<Coord> tileList = getOccupiedTiles();
      int grav = 0;
      for(Coord c : tileList)
         grav = Math.max(grav, geoMap[c.x][c.y].getPhysicsIndex());
      return zone.getGravity(grav);
   }
   
   // top-down methods
   ///////////////////////////////////////////////////////////////////////////////////////
   
   // returns the highest indexed friction of tiles occupied by the AABB
   public double getTopDownFriction(Zone zone)
   {
      GeometryBlock[][] geoMap = zone.getGeometry();
         Vector<Coord> tileList = getOccupiedTiles();
      int frict = 0;
      for(Coord c : tileList)
         frict = Math.max(frict, geoMap[c.x][c.y].getPhysicsIndex());
      return zone.getFriction(frict);
   }
   
   // returns the highest indexed speedMult of tiles occupied by the AABB
   public double getTopDownSpeedMult(Zone zone)
   {
      GeometryBlock[][] geoMap = zone.getGeometry();
      Vector<Coord> tileList = getOccupiedTiles();
      int smi = 0;
      for(Coord c : tileList)
         smi = Math.max(smi, geoMap[c.x][c.y].getPhysicsIndex());
      return zone.getSpeedMult(smi);
   }
}