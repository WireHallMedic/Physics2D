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
}