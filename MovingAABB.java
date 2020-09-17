/*

   Moving axis-aligned bounding box. Updates speeds at the end of each tick (after movement).
   All values are in millitiles

*/

package Physics2D;

public class MovingAABB extends AABB
{
	private int xSpeed;
	private int ySpeed;
	private boolean affectedByGravity;
	private boolean corporeal;
	private int stepOriginX;      // used to avoid moving into walls
	private int stepOriginY;
   public static final int impactSensorOffset = 100;
   private boolean bottomSensor = false;
   private boolean topSensor = false;
   private boolean rightSensor = false;
   private boolean leftSensor = false;
   private static final boolean oldStylePushing = false;


	public int getXSpeed(){return xSpeed;}
	public int getYSpeed(){return ySpeed;}
	public boolean isAffectedByGravity(){return affectedByGravity;}
	public boolean isCorporeal(){return corporeal;}
   public boolean bottomSensor(){return bottomSensor;}
   public boolean topSensor(){return topSensor;}
   public boolean leftSensor(){return leftSensor;}
   public boolean rightSensor(){return rightSensor;}


	public void setXSpeed(int x){xSpeed = x;}
	public void setYSpeed(int y){ySpeed = y;}
   public void setSpeed(int x, int y){xSpeed = x; ySpeed = y;}
	public void setAffectedByGravity(boolean a){affectedByGravity = a;}
	public void setCorporeal(boolean c){corporeal = c;}

   
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

   // x and y are in tiles
   private boolean blocked(int x, int y, GeometryBlock[][] geoMap)
   {
      if(x < 0 || y < 0 || x >= geoMap.length || y >= geoMap[0].length)
         return false;
      return !geoMap[x][y].isPassable();
   }
   
   // move x, resolve x collisions, move y, resolve y collisions
   public void doPhysics(GeometryBlock[][] geoMap)
   {
      int localGravity = getGravity(geoMap);
      // set prospective position
      if(affectedByGravity)
         ySpeed = Math.min(ySpeed + P2DManager.getGravity(localGravity), 
                           P2DManager.getTerminalVelocity(localGravity));
      stepOriginX = originX + xSpeed;
      stepOriginY = originY + ySpeed;
      
      boolean stopHoriz = false;
      boolean stopVert = false;
      
      // corporeal objects check if they would collide with level geometry, stopping if they would.
      // if moving diagionally, one tile is checked twice, but this is fast enough it shouldn't be
      // a problem
      if(corporeal)
      {
         // check and resolve horizontal movement
         int snapLocX = getHorizSnapLoc(stepOriginX, originY, geoMap);
         if(snapLocX != -1)
         {
            stepOriginX = snapLocX;
         }

         // check and resolve vertical movement
         int snapLocY = getVertSnapLoc(stepOriginX, stepOriginY, geoMap);
         if(snapLocY != -1)
         {
            stepOriginY = snapLocY;
         }
         
         // check if you're bumping into any walls
         updateSensors(stepOriginX, stepOriginY, geoMap);
         
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
      originX = stepOriginX;
      originY = stepOriginY;
   }
   
   // returns either the millitile y location to snap to, or -1
   private int getVertSnapLoc(int xPos, int yPos, GeometryBlock[][] geoMap)
   {
      int tileX;
      int tileY;
      int collision = 0;
      int snapLoc = -1;
      
      // check down
      if(ySpeed > 0)
      {
         // bottom left
         tileX = (xPos + impactSensorOffset) / 1000;
         tileY = (yPos + height) / 1000;
         if(blocked(tileX, tileY, geoMap))
            collision++;
         // bottom right
         tileX = (xPos + width - impactSensorOffset) / 1000;
         if((blocked(tileX, tileY, geoMap)))
            collision++;
         if(collision > 0)
         {
            snapLoc = (tileY * 1000) - height;
         }
      }
      // check up
      else if(ySpeed < 0)
      {
         // upper left
         tileX = (xPos + impactSensorOffset) / 1000;
         tileY = yPos / 1000;
         if(blocked(tileX, tileY, geoMap))
            collision++;
         // upper right
         tileX = (xPos + width - impactSensorOffset) / 1000;
         if((blocked(tileX, tileY, geoMap)))
            collision++;
         if(collision > 0)
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
      int tileY;
      int collision = 0;
      int snapLoc = -1;
      
      // check right
      if(xSpeed > 0)
      {
         // upper right
         tileX = (xPos + width) / 1000;
         tileY = (yPos + impactSensorOffset) / 1000;
         if(blocked(tileX, tileY, geoMap))
            collision++;
         // bottom right
         tileY = (yPos + height - impactSensorOffset) / 1000;
         if((blocked(tileX, tileY, geoMap)))
            collision++;
         if(collision > 0)
         {
            snapLoc = (tileX - 1) * 1000;
         }
      }
      // check left
      else if(xSpeed < 0)
      {
         // upper left
         tileX = xPos / 1000;
         tileY = (yPos + impactSensorOffset) / 1000;
         if(blocked(tileX, tileY, geoMap))
            collision++;
         // bottom left
         tileY = (yPos + height - impactSensorOffset) / 1000;
         if((blocked(tileX, tileY, geoMap)))
            collision++;
         if(collision > 0)
         {
            snapLoc = (tileX + 1) * 1000;
         }
      }
      return snapLoc;
   }
   
   private void updateSensors(int x, int y, GeometryBlock[][] geoMap)
   {
      // horizontal checks
      int tileX1 = (x + width + 1) / 1000;                    // right
      int tileX2 = (x - 1) / 1000;                            // left
      int tileY1 = (y + impactSensorOffset) / 1000;           // top
      int tileY2 = (y + height - impactSensorOffset) / 1000;  // bottom
      
      rightSensor = blocked(tileX1, tileY1, geoMap) || blocked(tileX1, tileY2, geoMap);
      leftSensor = blocked(tileX2, tileY1, geoMap) || blocked(tileX2, tileY2, geoMap);
      
      // vertical checks
      tileY1 = (y + height + 1) / 1000;                       // bottom
      tileY2 = (y - 1) / 1000;                                // top
      tileX1 = (x + impactSensorOffset) / 1000;               // left
      tileX2 = (x + width - impactSensorOffset) / 1000;       // right
      
      bottomSensor = blocked(tileX1, tileY1, geoMap) || blocked(tileX2, tileY1, geoMap);
      topSensor = blocked(tileX1, tileY2, geoMap) || blocked(tileX2, tileY2, geoMap);
   }
   
   
   // side-on methods
   ///////////////////////////////////////////////////////////////////////////////////////
   
   // if the actor is standing on a block, returns the highest indexed friction, else
   // returns 1.0
   public double getSideOnFriction(GeometryBlock[][] geoMap)
   {
      if(bottomSensor)
      {
         int index = -1;
         int tileY = (originY + height) / 1000;                        // bottom
         int tileX1 = (originX + impactSensorOffset) / 1000;               // left
         int tileX2 = (originX + width - impactSensorOffset) / 1000;       // right
         index = Math.max(geoMap[tileX1][tileY].getFrictionIndex(), 
                          geoMap[tileX2][tileY].getFrictionIndex());
         return P2DManager.getFriction(index);
      }
      return 1.0;
   }
   
   // returns the highest-indexed gravity used by one of the blocks in which this AABB has a corner
   public int getGravity(GeometryBlock[][] geoMap)
   {
      int tileX1 = originX / 1000;
      int tileY1 = originY / 1000;
      int tileX2 = (originX + width - 1) / 1000;
      int tileY2 = (originY + height - 1) / 1000;
      int grav =            geoMap[tileX1][tileY1].getGravityIndex();
      grav = Math.max(grav, geoMap[tileX2][tileY1].getGravityIndex());
      grav = Math.max(grav, geoMap[tileX1][tileY2].getGravityIndex());
      grav = Math.max(grav, geoMap[tileX2][tileY2].getGravityIndex());
      return grav;
   }
   
   // top-down methods
   ///////////////////////////////////////////////////////////////////////////////////////
   
   // returns the highest indexed friction of tiles occupied by the AABB
   public double getTopDownFriction(GeometryBlock[][] geoMap)
   {
      int tileX1 = originX / 1000;
      int tileY1 = originY / 1000;
      int tileX2 = (originX + width - 1) / 1000;
      int tileY2 = (originY + height - 1) / 1000;
      int frict =             geoMap[tileX1][tileY1].getFrictionIndex();
      frict = Math.max(frict, geoMap[tileX2][tileY1].getFrictionIndex());
      frict = Math.max(frict, geoMap[tileX1][tileY2].getFrictionIndex());
      frict = Math.max(frict, geoMap[tileX2][tileY2].getFrictionIndex());
      return P2DManager.getFriction(frict);
   }
   // returns the highest indexed speedMult of tiles occupied by the AABB
   public double getTopDownSpeedMult(GeometryBlock[][] geoMap)
   {
      int tileX1 = originX / 1000;
      int tileY1 = originY / 1000;
      int tileX2 = (originX + width - 1) / 1000;
      int tileY2 = (originY + height - 1) / 1000;
      int smi =           geoMap[tileX1][tileY1].getSpeedMultIndex();
      smi = Math.max(smi, geoMap[tileX2][tileY1].getSpeedMultIndex());
      smi = Math.max(smi, geoMap[tileX1][tileY2].getSpeedMultIndex());
      smi = Math.max(smi, geoMap[tileX2][tileY2].getSpeedMultIndex());
      return P2DManager.getSpeedMult(smi);
   }
}