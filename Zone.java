/*

   Container for an area of geometry and its related values

*/

package Physics2D;

import java.util.*;

public class Zone
{

	private P2DManager physicsManager;
	private GeometryBlock[][] geometry;
	private Vector<MovingBS> movingShapes;
   private GeometryBlock oobBlock;
   public static final int DEFAULT_ZONE_WIDTH = 25;
   public static final int DEFAULT_ZONE_HEIGHT = 25;


	public P2DManager getPhysicsManager(){return physicsManager;}
	public GeometryBlock[][] getGeometry(){return geometry;}
	public Vector<MovingBS> getMovingShapes(){return movingShapes;}
   public GeometryBlock getOOBBlock(){return oobBlock;}
   public int getWidth(){return geometry.length;}
   public int getHeight(){return geometry[0].length;}


	public void setPhysicsManager(P2DManager p){physicsManager = p;}
	public void setGeometry(GeometryBlock[][] g){geometry = g;}
	public void setMovingShapes(Vector<MovingBS> m){movingShapes = m;}
   public void setOOBBlock(GeometryBlock b){oobBlock = b;}


   public Zone(int grav, int termVel, double frict, double speedM)
   {
      this(DEFAULT_ZONE_WIDTH, DEFAULT_ZONE_HEIGHT, grav, termVel, frict, speedM);
   }
   
   public Zone(int w, int h, int grav, int termVel, double frict, double speedM)
   {
      physicsManager = new P2DManager(grav, termVel, frict, speedM);
      geometry = new GeometryBlock[w][h];
      oobBlock = GeometryBlock.getSolidBlock();
      fillWithOpenBlocks();
   }
   
   public void fillWithSolidBlocks()
   {
      for(int x = 0; x < getWidth(); x++)
      for(int y = 0; y < getHeight(); y++)
      {
         setBlock(x, y, GeometryBlock.getSolidBlock());
      }
   }
   
   public void fillWithOpenBlocks()
   {
      for(int x = 0; x < getWidth(); x++)
      for(int y = 0; y < getHeight(); y++)
      {
         setBlock(x, y, GeometryBlock.getOpenBlock());
      }
   }
   
   public GeometryBlock getBlock(int x, int y)
   {
      if(isOutOfBounds(x, y))
         return oobBlock;
      return geometry[x][y];
   }
   
   public void setBlock(int x, int y, GeometryBlock block)
   {
      if(isOutOfBounds(x, y))
         return;
      geometry[x][y] = block;
   }
   
   public boolean isInBounds(int x, int y)
   {
      return !isInBounds(x, y);
   }
   
   public boolean isOutOfBounds(int x, int y)
   {
      return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
   }
   
   // physics manager stuff
   public void setGravity(int i, int g){physicsManager.setGravity(i, g);}
	public void setGravity(int g){physicsManager.setGravity(g);}
	public void setTerminalVelocity(int i, int t){physicsManager.setTerminalVelocity(i, t);}
	public void setTerminalVelocity(int t){physicsManager.setTerminalVelocity(t);}
	public void setFriction(int i, double f){physicsManager.setFriction(i, f);}
	public void setFriction(double f){physicsManager.setFriction(f);}
	public void setSpeedMult(int i, double sm){physicsManager.setSpeedMult(i, sm);}
	public void setSpeedMult(double sm){physicsManager.setSpeedMult(sm);}
   
   // get the gravity of the passed geoBlock
   public int getGravity(int x, int y)
   {
      return physicsManager.getGravity(getBlock(x, y).getPhysicsIndex());
   }
   
   // get the gravity from index
   public int getGravity(int i)
   {
      return physicsManager.getGravity(i);
   }
   
   // get the terminal velocity of the passed geoBlock
   public int getTerminalVelocity(int x, int y)
   {
      return physicsManager.getTerminalVelocity(getBlock(x, y).getPhysicsIndex());
   }
   
   // get the terminal velocity from index
   public int getTerminalVelocity(int i)
   {
      return physicsManager.getTerminalVelocity(i);
   }
   
   // get the friction of the passed geoBlock
   public double getFriction(int x, int y)
   {
      return physicsManager.getFriction(getBlock(x, y).getPhysicsIndex());
   }
   
   // get the friction from index
   public double getFriction(int i)
   {
      return physicsManager.getFriction(i);
   }
   
   // get the speed multiplier of the passed geoBlock
   public double getSpeedMult(int x, int y)
   {
      return physicsManager.getSpeedMult(getBlock(x, y).getPhysicsIndex());
   }
   
   // get the speed multiplier from index
   public double getSpeedMult(int i)
   {
      return physicsManager.getSpeedMult(i);
   }
   
}