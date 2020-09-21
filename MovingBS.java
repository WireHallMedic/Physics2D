/*

   Interface for moving collision objects

*/

package Physics2D;

public interface MovingBS
{
   public int getXSpeed();
	public int getYSpeed();
	public boolean isAffectedByGravity();
	public boolean isCorporeal();

	public void setXSpeed(int x);
	public void setYSpeed(int y);
   public void setSpeed(int x, int y);
	public void setAffectedByGravity(boolean a);
	public void setCorporeal(boolean c);
   
   public void applyImpulse(int x, int y);
   public void doPhysics(GeometryBlock[][] geoMap);

}