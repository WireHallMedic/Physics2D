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
   
	private  int[] gravity = new int[11];
	private  int[] terminalVelocity = new int[11];
	private  double[] friction = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
	private  double[] speedMult = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};


	public  int getGravity(int g){return gravity[g];}
	public  int getGravity(){return getGravity(DEFAULT_PHYSICS_INDEX);}
	public  int getTerminalVelocity(int tv){return terminalVelocity[tv];}
	public  int getTerminalVelocity(){return getTerminalVelocity(DEFAULT_PHYSICS_INDEX);}
	public  double getFriction(int f){return friction[f];}
	public  double getFriction(){return getFriction(DEFAULT_PHYSICS_INDEX);}
	public  double getSpeedMult(int sm){return speedMult[sm];}
	public  double getSpeedMult(){return getSpeedMult(DEFAULT_PHYSICS_INDEX);}


	public  void setGravity(int i, int g){gravity[i] = g;}
	public  void setGravity(int g){setGravity(DEFAULT_PHYSICS_INDEX, g);}
	public  void setTerminalVelocity(int i, int t){terminalVelocity[i] = t;}
	public  void setTerminalVelocity(int t){setTerminalVelocity(DEFAULT_PHYSICS_INDEX, t);}
	public  void setFriction(int i, double f){friction[i] = f;}
	public  void setFriction(double f){setFriction(DEFAULT_PHYSICS_INDEX, f);}
	public  void setSpeedMult(int i, double sm){speedMult[i] = sm;}
	public  void setSpeedMult(double sm){setSpeedMult(DEFAULT_PHYSICS_INDEX, sm);}
   
   public P2DManager(int g, int tv, double f, double sm)
   {
      setDefaultPhysicsValues(g, tv, f, sm);
   }
   
   public void setArraySizes(int s)
   {
      gravity = new int[s];
      terminalVelocity = new int[s];
      friction = new double[s];
      speedMult = new double[s];
      DEFAULT_PHYSICS_INDEX = s / 2;
   }
   
   public void setDefaultPhysicsValues(int grav, int termVel, double frict, double speedM)
   {
      setGravity(grav);
      setTerminalVelocity(termVel);
      setFriction(frict);
      setSpeedMult(speedM);
   }
   
   public void initializeValues(int grav, int termVel, double frict, double speedMul)
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