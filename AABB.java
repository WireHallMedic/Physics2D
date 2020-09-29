/*

   Axis-aligned bounding box. Dimensions are internally kept as millitiles.
   Origin is top-left.

*/

package Physics2D;

public class AABB implements BoundingShape
{
	private int originX;
	private int originY;
	private int height;
	private int width;
	private int halfHeight;
	private int halfWidth;


	public int getOriginX(){return originX;}
	public int getOriginY(){return originY;}
	public int getHeight(){return height;}
	public int getWidth(){return width;}
	public int getHalfHeight(){return halfHeight;}
	public int getHalfWidth(){return halfWidth;}


	public void setOriginX(int c){originX = c;}
	public void setOriginY(int c){originY = c;}
   public void setOrigin(int x, int y){setOriginX(x); setOriginY(y);}
	public void setHeight(int h){height = h; halfHeight = height / 2;}
	public void setWidth(int w){width = w; halfWidth = width / 2;}

   public AABB()
   {
      this(1000, 1000, 0, 0);
   }

   public AABB(int w, int h)
   {
      this(w, h, 0, 0);
   }

   public AABB(int w, int h, int x, int y)
   {
      setHeight(h);
      setWidth(w);
      setOrigin(x, y);
   }
   
   public boolean pointIsIn(int x, int y)
   {
      return P2DTools.pointIsInBox(x, y, this);
   }
   
   // parent collision check
   public boolean isColliding(BoundingShape that)
   {
      if(that instanceof AABB)
         return isColliding((AABB)that);
      if(that instanceof BoundingCircle)
         return ((BoundingCircle)that).isColliding(this);
      return true;
   }
   
   // check if colliding with another AABB
   public boolean isColliding(AABB that)
   {
      return isColliding(that.originX, that.originY, that.halfWidth, that.halfHeight);
   }
   
   // check if colliding with a geometry location
   public boolean isColliding(int thatOriginX, int thatOriginY, int thatHalfWidth, int thatHalfHeight)
   {
      return P2DTools.boxesIntersect(this.originX, this.originY, this.halfWidth, this.halfHeight,
                                     thatOriginX, thatOriginY, thatHalfWidth, thatHalfHeight);
   }

}