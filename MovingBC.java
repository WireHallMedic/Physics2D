/*

   Moving bounding circle. Updates speeds at the end of each tick (after movement).
   All values are in millitiles
   Only indended for top-down (not side-on), because gravity gets ugly for circles on edges)

*/

package Physics2D;

public class MovingBC extends BoundingCircle implements MovingBS
{
	private int xSpeed;
	private int ySpeed;
	private boolean affectedByGravity;
	private boolean corporeal;
	private int stepOriginX;      // used to avoid moving into walls
	private int stepOriginY;
   public static final int sensorInset = 10;


	public int getXSpeed(){return xSpeed;}
	public int getYSpeed(){return ySpeed;}
	public boolean isAffectedByGravity(){return affectedByGravity;}
	public boolean isCorporeal(){return corporeal;}


	public void setXSpeed(int x){xSpeed = x;}
	public void setYSpeed(int y){ySpeed = y;}
   public void setSpeed(int x, int y){xSpeed = x; ySpeed = y;}
	public void setAffectedByGravity(boolean a){affectedByGravity = a;}
	public void setCorporeal(boolean c){corporeal = c;}

   
   public MovingBC()
   {
      this(1000, 0, 0);
   }

   public MovingBC(int d)
   {
      this(d, 0, 0);
   }

   public MovingBC(int d, int x, int y)
   {
      super(d, x, y);
   	setCorporeal(false);
   }
   
   public int getCenterX()
   {
      return getOriginX();
   }
   
   
	public int getCenterY()
   {
      return getOriginY();
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
   
   // move x, resolve x collisions, move y, resolve y collisions
   public void doPhysics(GeometryBlock[][] geoMap)
   {
      stepOriginX = originX + xSpeed;
      stepOriginY = originY + ySpeed;
      
      // corporeal objects check if they would collide with level geometry, stopping if they would.
      // if moving diagionally, one tile is checked twice, but this is fast enough it shouldn't be
      // a problem
      if(corporeal)
      {
         boolean yBump = false;
         boolean xBump = false;
         // check and resolve horizontal movement
         int snapLocX = getHorizSnapLoc(stepOriginX, originY, geoMap);
         if(snapLocX != -1)
         {
            stepOriginX = snapLocX;
            xBump = true;
         }

         // check and resolve vertical movement
         int snapLocY = getVertSnapLoc(stepOriginX, stepOriginY, geoMap);
         if(snapLocY != -1)
         {
            stepOriginY = snapLocY;
            yBump = true;
         }
         
         // stop movement if bumping into wall on that vector
         if(xBump)
            xSpeed = 0;
         if(yBump)
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
      int snapLoc = -1;
      int start;
      int end;
      
      // check down
      if(ySpeed > 0)
      {
         // bottom center
         tileX = xPos / 1000;
         tileY = (yPos + radius) / 1000;
         if(blocked(tileX, tileY, geoMap))
         {
            snapLoc = (tileY * 1000) - radius;
         }
         else // check for corners in circle
         {
            start = (xPos - radius + sensorInset) / 1000;
            end = (xPos + radius - sensorInset) / 1000;
            int cornersToCheck = end - start + 1;
            for(int i = 0; i < cornersToCheck; i++)
            {
               tileX = ((xPos - radius) / 1000) + i;
               if(blocked(tileX, tileY, geoMap) || blocked(tileX + 1, tileY, geoMap))
               {
                  if(pointIsIn((tileX + 1) * 1000, tileY * 1000)) // check upper right corner of tile
                  {
                     int offset = P2DTools.getPythag(Math.abs(xPos - ((tileX + 1) * 1000)), radius);
                     snapLoc = (tileY * 1000) - offset;
                     break;
                  }
               }
            }
         }
      }
      // check up
      else if(ySpeed < 0)
      {
         // up center
         tileX = xPos / 1000;
         tileY = (yPos - radius) / 1000;
         if(blocked(tileX, tileY, geoMap))
         {
            snapLoc = ((tileY + 1) * 1000) + radius;
         }
         else // check for corners in circle
         {
            start = (xPos - radius + sensorInset) / 1000;
            end = (xPos + radius - sensorInset) / 1000;
            int cornersToCheck = end - start + 1;
            for(int i = 0; i < cornersToCheck; i++)
            {
               tileX = ((xPos - radius) / 1000) + i;
               if(blocked(tileX, tileY, geoMap) || blocked(tileX + 1, tileY, geoMap))
               {
                  if(pointIsIn((tileX + 1) * 1000, (tileY + 1) * 1000)) // check lower right corner of tile
                  {
                     int offset = P2DTools.getPythag(Math.abs(xPos - ((tileX + 1) * 1000)), radius);
                     snapLoc = ((tileY + 1) * 1000) + offset;
                     break;
                  }
               }
            }
         }
      }
      return snapLoc;
   }
   
   // returns either the millitile x location to snap to, or -1
   private int getHorizSnapLoc(int xPos, int yPos, GeometryBlock[][] geoMap)
   {
      int tileX;
      int tileY;
      int start;
      int end;
      int snapLoc = -1;
      
      // check right
      if(xSpeed > 0)
      {
         // right center
         tileX = (xPos + radius) / 1000;
         tileY = yPos / 1000;
         if(blocked(tileX, tileY, geoMap))
         {
            snapLoc = (tileX * 1000) - radius;
         }  
         else // check for corners in circle
         {
            start = (yPos - radius + sensorInset) / 1000;
            end = (yPos + radius - sensorInset) / 1000;
            int cornersToCheck = end - start + 1;
            for(int i = 0; i < cornersToCheck; i++)
            {
               tileY = ((yPos - radius) / 1000) + i;
               if(blocked(tileX, tileY, geoMap) || blocked(tileX, tileY - 1, geoMap))
               {
                  if(pointIsIn(tileX * 1000, tileY * 1000)) // check upper left corner of tile
                  {
                     int offset = P2DTools.getPythag(Math.abs(yPos - (tileY * 1000)), radius);
                     snapLoc = (tileX * 1000) - offset;
                     break;
                  }
               }
            }
         }
      }
      // check left
      else if(xSpeed < 0)
      {
         // left center
         tileX = (xPos - radius) / 1000;
         tileY = yPos / 1000;
         if(blocked(tileX, tileY, geoMap))
         {
            snapLoc = ((tileX + 1) * 1000) + radius;
         }
         else // check for corners in circle
         {
            start = (yPos - radius + sensorInset) / 1000;
            end = (yPos + radius - sensorInset) / 1000;
            int cornersToCheck = end - start + 1;
            for(int i = 0; i < cornersToCheck; i++)
            {
               tileY = ((yPos - radius) / 1000) + i;
               if(blocked(tileX, tileY, geoMap) || blocked(tileX, tileY - 1, geoMap))
               {
                  if(pointIsIn((tileX + 1) * 1000, tileY * 1000)) // check upper right corner of tile
                  {
                     int offset = P2DTools.getPythag(Math.abs(yPos - (tileY * 1000)), radius);
                     snapLoc = ((tileX + 1) * 1000) + offset;
                     break;
                  }
               }
            }
         }
      }
      return snapLoc;
   }

   
   // top-down methods
   ///////////////////////////////////////////////////////////////////////////////////////
   
   // returns the highest indexed friction of tiles occupied by the AABB
   public double getTopDownFriction(GeometryBlock[][] geoMap)
   {
      int tileX1 = (originX - radius) / 1000;
      int tileY1 = (originY - radius) / 1000;
      int tileX2 = (originX + radius - 1) / 1000;
      int tileY2 = (originY + radius - 1) / 1000;
      int frict = 0;
      for(int x = tileX1; x <= tileX2; x++)
      for(int y = tileY1; y <= tileY2; y++)
         frict = Math.max(frict, geoMap[x][y].getFrictionIndex());
      return P2DManager.getFriction(frict);
   }
   // returns the highest indexed speedMult of tiles occupied by the AABB
   public double getTopDownSpeedMult(GeometryBlock[][] geoMap)
   {
      int tileX1 = (originX - radius) / 1000;
      int tileY1 = (originY - radius) / 1000;
      int tileX2 = (originX + radius - 1) / 1000;
      int tileY2 = (originY + radius - 1) / 1000;
      
      int smi = 0;
      for(int x = tileX1; x <= tileX2; x++)
      for(int y = tileY1; y <= tileY2; y++)
         smi = Math.max(smi, geoMap[x][y].getSpeedMultIndex());
      return P2DManager.getSpeedMult(smi);
   }
}