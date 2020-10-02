/*

   Struct for keeping geometry block data

*/

package Physics2D;

public class GeometryBlock
{
	private boolean passable;
	private int physicsIndex;
	public static final int DEFAULT_PHYSICS_INDEX = P2DManager.DEFAULT_PHYSICS_INDEX;


	public boolean isPassable(){return passable;}
	public int getPhysicsIndex(){return physicsIndex;}


	public void setPassable(boolean p){passable = p;}
	public void setPhysicsIndex(int p){physicsIndex = p;}



   public GeometryBlock(boolean p, int pi)
   {
      passable = p;
      physicsIndex = pi;
   }
   
   public GeometryBlock(GeometryBlock that)
   {
      this.passable = that.passable;
      this.physicsIndex = that.physicsIndex;
   }
   
   public static GeometryBlock getSolidBlock()
   {
      return new GeometryBlock(false, DEFAULT_PHYSICS_INDEX);
   }
   
   public static GeometryBlock getOpenBlock()
   {
      return new GeometryBlock(true, DEFAULT_PHYSICS_INDEX);
   }
}