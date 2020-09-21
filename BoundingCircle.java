/*

   Circle for collision, etc. Dimensions are internally kept as millitiles.
   Origin is center.

*/

package Physics2D;

public class BoundingCircle extends BoundingShape
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
   
   public boolean isColliding(BoundingShape that)
   {
      if(that instanceof AABB)
         return isColliding((AABB)that);
      if(that instanceof BoundingCircle)
         return this.isColliding((BoundingCircle)that);
      return true;
   }
   
   // check if colliding with another BoundingCircle
   public boolean isColliding(BoundingCircle that)
   {
      int radiiSum = this.radius + that.radius;
      return P2DTools.getDistanceMetric(this.originX, this.originY, that.originX, that.originY) <= radiiSum * radiiSum;
   }
   
   // check if colliding with an AABB
   public boolean isColliding(AABB that)
   {
      return collisionCheck(that.getOriginX(), that.getOriginY(), that.getWidth(), that.getHeight());
   }
   
   // check if colliding with a geometry location
   public boolean isColliding(int thatOriginX, int thatOriginY, int thatWidth, int thatHeight)
   {
      return collisionCheck(thatOriginX, thatOriginY, thatWidth, thatHeight);
   }
   
   // base collision routine (circle to box)
   // if any of the circles orthoganal points are in the box, or any of the box's diagonal points are in the circle,
   // there exists a collision
   public boolean collisionCheck(int thatOriginX, int thatOriginY, int thatWidth, int thatHeight)
   {
      return this.pointIsIn(thatOriginX, thatOriginY) ||
             this.pointIsIn(thatOriginX + thatWidth, thatOriginY) ||
             this.pointIsIn(thatOriginX, thatOriginY + thatHeight) ||
             this.pointIsIn(thatOriginX + thatWidth, thatOriginY + thatHeight) ||
             P2DTools.pointIsInBox(originX + radius, originY + radius, thatOriginX, thatOriginY, thatWidth, thatHeight) ||
             P2DTools.pointIsInBox(originX + radius, originY - radius, thatOriginX, thatOriginY, thatWidth, thatHeight) ||
             P2DTools.pointIsInBox(originX - radius, originY + radius, thatOriginX, thatOriginY, thatWidth, thatHeight) ||
             P2DTools.pointIsInBox(originX - radius, originY - radius, thatOriginX, thatOriginY, thatWidth, thatHeight);
   }
   
}