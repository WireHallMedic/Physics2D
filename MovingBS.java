/*

   Interface for moving collision objects

*/

package Physics2D;

import java.util.*;
import WidlerSuite.*;

public interface MovingBS
{
   public int getXSpeed();
	public int getYSpeed();
	public boolean isAffectedByGravity();
	public boolean isCorporeal();
   public int getCenterX();
	public int getCenterY();
   public boolean isFollower();
   public MovingBS getLeader();

	public void setXSpeed(int x);
	public void setYSpeed(int y);
   public void setSpeed(int x, int y);
	public void setAffectedByGravity(boolean a);
	public void setCorporeal(boolean c);
   public void setLeader(MovingBS l);
   
   public void applyImpulse(int x, int y);
   public void doPhysics(Zone zone);
   public Vector<Coord> getOccupiedTiles();

}