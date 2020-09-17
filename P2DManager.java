/*

   A manager for physics2D objects. Holds constants, shared variables, etc. Static.
   Gravity values are in millitiles per tick.
   
   Lists of values are kept so that they can be prioritized; the higher the index, the
     higher the priority. Normal is in the middle of the list.

*/

package Physics2D;

public class P2DManager
{
   public static int DEFAULT_PHYSICS_INDEX = 5;
   
	private static int[] gravity = new int[11];
	private static int[] terminalVelocity = new int[11];
	private static double[] friction = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
	private static double[] speedMult = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
   private static int tileSize = 1;


	public static int getGravity(int g){return gravity[g];}
	public static int getGravity(){return getGravity(DEFAULT_PHYSICS_INDEX);}
	public static int getTerminalVelocity(int tv){return terminalVelocity[tv];}
	public static int getTerminalVelocity(){return getTerminalVelocity(DEFAULT_PHYSICS_INDEX);}
	public static double getFriction(int f){return friction[f];}
	public static double getFriction(){return getFriction(DEFAULT_PHYSICS_INDEX);}
	public static double getSpeedMult(int sm){return speedMult[sm];}
	public static double getSpeedMult(){return getSpeedMult(DEFAULT_PHYSICS_INDEX);}
   public static int getTileSize(){return tileSize;}


	public static void setGravity(int i, int g){gravity[i] = g;}
	public static void setGravity(int g){setGravity(DEFAULT_PHYSICS_INDEX, g);}
	public static void setTerminalVelocity(int i, int t){terminalVelocity[i] = t;}
	public static void setTerminalVelocity(int t){setTerminalVelocity(DEFAULT_PHYSICS_INDEX, t);}
	public static void setFriction(int i, double f){friction[i] = f;}
	public static void setFriction(double f){setFriction(DEFAULT_PHYSICS_INDEX, f);}
	public static void setSpeedMult(int i, double sm){speedMult[i] = sm;}
	public static void setSpeedMult(double sm){setSpeedMult(DEFAULT_PHYSICS_INDEX, sm);}
   public static void setTileSize(int t){tileSize = t;}
   
   public static int millitileToPixel(int mtLoc)
   {
      return mtLoc * tileSize / 1000;
   }
   
   public static void setArraySizes(int s)
   {
      gravity = new int[s];
      terminalVelocity = new int[s];
      friction = new double[s];
      speedMult = new double[s];
      DEFAULT_PHYSICS_INDEX = s / 2;
   }
   
   public static void setDefaultPhysicsValues(int grav, int termVel, double frict, double speedM)
   {
      setGravity(grav);
      setTerminalVelocity(termVel);
      setFriction(frict);
      setSpeedMult(speedM);
   }
   
   public static void initializeValues(int grav, int termVel, double frict, double speedMul)
   {
      for(int i = 0; i < gravity.length; i++)
      {
         gravity[i] = grav;
         terminalVelocity[i] = termVel;
         friction[i] = frict;
         speedMult[i] = speedMul;
      }
   }
   
}