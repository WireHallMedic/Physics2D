/*

   Interface for collision objects

*/

package Physics2D;

public abstract class BoundingShape
{
   public abstract boolean pointIsIn(int x, int y);
   
   public abstract boolean isColliding(BoundingShape that);
}