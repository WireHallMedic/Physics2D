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


   public Zone()
   {
      this(DEFAULT_ZONE_WIDTH, DEFAULT_ZONE_HEIGHT);
   }
   
   public Zone(int w, int h)
   {
      physicsManager = new P2DManager();
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
      if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
         return oobBlock;
      return geometry[x][y];
   }
   
   public void setBlock(int x, int y, GeometryBlock block)
   {
      if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
         return;
      geometry[x][y] = block;
   }
}