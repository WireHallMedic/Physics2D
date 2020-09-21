/*

   Interface for collision objects

*/

package Physics2D;

public interface BoundingShape
{
   public boolean pointIsIn(int x, int y);
   
   public boolean isColliding(BoundingShape that);
}