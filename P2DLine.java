/*

   Lines with interactions for AABBs.
   X and Y values are in millitiles
   Since we're using millitiles, we can let m and b (of y=mx+b) be ints.
*/

package Physics2D;

import WidlerSuite.Coord;
import WidlerSuite.Vect;

public class P2DLine
{
   private Coord p1;
   private Coord p2;
   private boolean verticalLine;    // special case flag
   private boolean horizontalLine;  // special case flag
   private int m;                   // for slope-intercept form
   private int b;                   // for slope-intercept form
   private int leftBound;           // memoize for intersection calcs
   private int rightBound;          // memoize for intersection calcs
   private int topBound;            // memoize for intersection calcs
   private int bottomBound;         // memoize for intersection calcs
   
   public boolean isVerticalLine(){return verticalLine;}
   public boolean isHorizontalLine(){return horizontalLine;}
   
   public int getXTerminus(){return p2.x;}
   public int getYTerminus(){return p2.y;}
   
   public P2DLine(int x1, int y1, int x2, int y2)
   {
      p1 = new Coord(x1, y1);
      p2 = new Coord(x2, y2);
      verticalLine = false;
      horizontalLine = false;
      calcValues();
   }
   
   public P2DLine(Coord c1, Coord c2)
   {
      this(c1.x, c1.y, c2.x, c2.y);
   }
   
   private void calcValues()
   {
      topBound = Math.min(p1.y, p2.y);
      bottomBound = Math.max(p1.y, p2.y);
      leftBound = Math.min(p1.x, p2.x);
      rightBound = Math.max(p1.x, p2.x);
      
      // check for special cases
      if(p1.x == p2.x)
      {
         verticalLine = true;
         return;  // bail to avoid div0 err when calculating m
      }
      
      int rise = p2.y - p1.y;
      int run = p2.x - p1.x;
      m = rise / run;
      if(m == 0)
         horizontalLine = true;
      // y=mx+b -> y - mx = b
      b = p1.y - (m * p1.x);
   }
   
   public int getXAtY(int y)
   {
      // x = (y - b) / m
      return (y - b) / m;
   }
   
   public int getYAtX(int x)
   {
      // y = mx + b
      return (m * x) + b;
   }
   
   public boolean intersects(AABB box)
   {
      // are they far enough apart to not bother with precise checks
      if(rightBound < box.getOriginX() ||
         leftBound > box.getOriginX() + box.getWidth() ||
         bottomBound < box.getOriginY() ||
         topBound > box.getOriginY() + box.getHeight())
         return false;
      
      // a vertical or horizontal line which gets this far must intersect
      if(verticalLine || horizontalLine)
         return true;
      
      int leftBoxBound = box.getOriginX();
      int rightBoxBound = leftBoxBound + box.getWidth();
      int topBoxBound = box.getOriginY();
      int bottomBoxBound = topBoxBound + box.getHeight();
      
         // breaks left plane
      if((getYAtX(leftBoxBound) >= topBoxBound && getYAtX(leftBoxBound) <= bottomBoxBound) ||
         // breaks right plane
         (getYAtX(rightBoxBound) >= topBoxBound && getYAtX(rightBoxBound) <= bottomBoxBound) ||
         // breaks top plane
         (getXAtY(topBoxBound) >= leftBoxBound && getXAtY(topBoxBound) <= rightBoxBound) ||
         // breaks bottom plane
         (getXAtY(topBoxBound) >= leftBoxBound && getXAtY(topBoxBound) <= rightBoxBound))
         return true;
      
      return false;
   }
   
   // returns the square of the distance
   public int lineCompVal()
   {
      return lineCompVal(p1.x, p1.y, p2.x, p2.y);
   }
   
   // returns the square of the distance
   public static int lineCompVal(int x1, int y1, int x2, int y2)
   {
      int a = x2 - x1;
      int b = y2 - y1;
      return (a * a) + (b * b);
   }
   
   public static void main(String[] args)
   {
      P2DLine line = new P2DLine(0, 0, 100, 200);
      System.out.println(String.format("x val at y = 200: %d (expected 100)", line.getXAtY(200)));
      System.out.println(String.format("y val at x = 200: %d (expected 400)", line.getYAtX(200)));
      System.out.println(String.format("y val at x = 300: %d (expected 600)", line.getYAtX(300)));
      P2DLine line2 = new P2DLine(0, 0, 100, 100);
      P2DLine line3 = new P2DLine(0, 0, 20, 20);
      P2DLine line4 = new P2DLine(100, 100, 0, 0);
      P2DLine line5 = new P2DLine(0, 0, 0, 100);
      P2DLine line6 = new P2DLine(50, 0, 50, 100);
      P2DLine line7 = new P2DLine(50, 0, 50, 20);
      AABB box = new AABB(10, 10, 45, 45);
      
      System.out.println(String.format("Line intersects box : %b (expected false)", line.intersects(box)));
      System.out.println(String.format("Line2 intersects box : %b (expected true)", line2.intersects(box)));
      System.out.println(String.format("Line3 intersects box : %b (expected false)", line3.intersects(box)));
      System.out.println(String.format("Line4 intersects box : %b (expected true)", line4.intersects(box)));
      System.out.println(String.format("Line5 intersects box : %b (expected false)", line5.intersects(box)));
      System.out.println(String.format("Line6 intersects box : %b (expected true)", line6.intersects(box)));
      System.out.println(String.format("Line7 intersects box : %b (expected false)", line7.intersects(box)));
   }
}