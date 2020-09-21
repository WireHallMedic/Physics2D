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
      return;
   }
}