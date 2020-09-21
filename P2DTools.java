/*

   Circle for collision, etc. Dimensions are internally kept as millitiles.
   Origin is center.

*/

package Physics2D;

public class P2DTools
{
   public static int getDistanceMetric(int startX, int startY, int endX, int endY)
   {
		int a = Math.abs(endX - startX);
		int b = Math.abs(endY - startY);
		return (a * a) + (b * b);
   }
   
   public static boolean pointIsInBox(int x, int y, int boxOriginX, int boxOriginY, int boxWidth, int boxHeight)
   {
      return x >= boxOriginX && x <= boxOriginX + boxWidth && 
             y >= boxOriginY && y <= boxOriginY + boxHeight;
   }
   
   
   public static boolean pointIsInBox(int x, int y, AABB box)
   {
      return pointIsInBox(x, y, box.getOriginX(), box.getOriginY(), box.getWidth(), box.getHeight());
   }
   
   // box-to-box collison
   public static boolean boxesIntersect(int aOriginX, int aOriginY, int aHalfWidth, int aHalfHeight,
                                        int bOriginX, int bOriginY, int bHalfWidth, int bHalfHeight)
   {
      if(Math.abs((aOriginX + aHalfWidth) - (bOriginX + bHalfWidth)) > aHalfWidth + bHalfWidth) 
         return false;
      if(Math.abs((aOriginY + aHalfHeight) - (bOriginY + bHalfHeight)) > aHalfHeight + bHalfHeight) 
         return false;
      return true;
   }
}