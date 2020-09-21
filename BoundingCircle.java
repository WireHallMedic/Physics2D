/*

   Circle for collision, etc. Dimensions are internally kept as millitiles.
   Origin is center.

*/

package Physics2D;

public class BoundingCircle
{
	private int originX;
	private int originY;
	private int radius;
   private int radiusSquared;    // memoization


	public int getOriginX(){return originX;}
	public int getOriginY(){return originY;}
	public int getRadius(){return radius;}
	public int getDiameter(){return radius * 2;}


	public void setOriginX(int o){originX = o;}
	public void setOriginY(int o){originY = o;}
	public void setRadius(int r){radius = r; radiusSquared = r * r;}
	public void setDiameter(int d){radius = d / 2; radiusSquared = radius * radius;}
   public void setOrigin(int x, int y){setOriginX(x); setOriginY(y);}
   

   public BoundingCircle()
   {
      this(1000, 0, 0);
   }

   public BoundingCircle(int d)
   {
      this(d, 0, 0);
   }

   public BoundingCircle(int d, int x, int y)
   {
      setDiameter(d);
      setOrigin(x, y);
   }
   
   public boolean pointIsIn(int x, int y)
   {
      return P2DTools.getDistanceMetric(this.originX, this.originY, x, y) <= radiusSquared;
   }
   
   // check if colliding with another BoundingCircle
   public boolean isColliding(BoundingCircle that)
   {
      int radiiSum = this.radius + that.radius;
      return P2DTools.getDistanceMetric(this.originX, this.originY, that.originX, that.originY) <= radiiSum * radiiSum;
   }
   /*
   // check if colliding with another BoundingCircle
   public boolean isColliding(BoundingCircle that)
   {
      return isColliding(that.originX, that.originY, that.halfWidth, that.halfHeight);
   }
   
   // check if colliding with a geometry location
   public boolean isColliding(int thatOriginX, int thatOriginY, int thatHalfWidth, int thatHalfHeight)
   {
      return collisionCheck(this.originX, this.originY, this.halfWidth, this.halfHeight,
                            thatOriginX, thatOriginY, thatHalfWidth, thatHalfHeight);
   }
   
   // base collision routine. Removed from isColliding(BoundingCircle) to be used for grid tiles and impending collision too.
   public boolean collisionCheckRect(int aOriginX, int aOriginY, int aHalfWidth, int aHalfHeight,
                                 int bOriginX, int bOriginY, int bHalfWidth, int bHalfHeight)
   {
      if(Math.abs((aOriginX + aHalfWidth) - (bOriginX + bHalfWidth)) > aHalfWidth + bHalfWidth) 
         return false;
      if(Math.abs((aOriginY + aHalfHeight) - (bOriginY + bHalfHeight)) > aHalfHeight + bHalfHeight) 
         return false;
      return true;
   }
   */
}