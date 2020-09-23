/*

   Moving axis-aligned bounding box. Updates speeds at the end of each tick (after movement).
   All values are in millitiles

*/

package Physics2D;

public class MovingBC extends BoundingCircle implements MovingBS
{
   private int xSpeed;
	private int ySpeed;
	private boolean affectedByGravity;
	private boolean corporeal;


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
   	setAffectedByGravity(false);
   	setCorporeal(false);
   }
   
   
   public void applyImpulse(int x, int y)
   {
      xSpeed += x;
      ySpeed += y;
   }
   
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
}