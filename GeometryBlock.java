/*

   Struct for keeping geometry block data

*/

package Physics2D;

public class GeometryBlock
{
	private boolean passable;
	private int gravityIndex;       // which gravity is used
   private int terminalVelocityIndex;
	private int frictionIndex;      // divide changes in speed by this
   private int speedMultIndex;     // multiply top speed and speed changes by this (ie, low for water)
   
   public static final int DEFAULT_PHYSICS_INDEX = P2DManager.DEFAULT_PHYSICS_INDEX;


	public boolean isPassable(){return passable;}
	public int getGravityIndex(){return gravityIndex;}
   public int getTerminalVelocityIndex(){return terminalVelocityIndex;}
	public int getFrictionIndex(){return frictionIndex;}
   public int getSpeedMultIndex(){return speedMultIndex;}


	public void setPassable(boolean p){passable = p;}
	public void setGravityIndex(int g){gravityIndex = g;}
   public void setTerminalVelocityIndex(int tv){terminalVelocityIndex = tv;}
	public void setFrictionIndex(int f){frictionIndex = f;}
   public void setSpeedMult(int sm){speedMultIndex = sm;}


   public GeometryBlock(boolean p, int gi, int tv, int fi, int sm)
   {
      passable = p;
      gravityIndex = gi;
      terminalVelocityIndex = tv;
      frictionIndex = fi;
      speedMultIndex = sm;
   }
   
   public GeometryBlock(GeometryBlock that)
   {
      this.passable = that.passable;
      this.gravityIndex = that.gravityIndex;
      this.terminalVelocityIndex = that.terminalVelocityIndex;
      this.frictionIndex = that.frictionIndex;
      this.speedMultIndex = that.speedMultIndex;
   }
   
   public static GeometryBlock getSolidBlock()
   {
      return new GeometryBlock(false, DEFAULT_PHYSICS_INDEX, DEFAULT_PHYSICS_INDEX, 
                                      DEFAULT_PHYSICS_INDEX, DEFAULT_PHYSICS_INDEX);
   }
   
   public static GeometryBlock getOpenBlock()
   {
      return new GeometryBlock(true, DEFAULT_PHYSICS_INDEX, DEFAULT_PHYSICS_INDEX, 
                                     DEFAULT_PHYSICS_INDEX, DEFAULT_PHYSICS_INDEX);
   }
   
   public void setAllPhysicsIndices(int i)
   {
      gravityIndex = i;
      terminalVelocityIndex = i;
      frictionIndex = i;
      speedMultIndex = i;
   }
}