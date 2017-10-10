package physics;

import javax.vecmath.Vector2d;
import aI.*;
import static main.Const.*;

public class AIPhysics extends ObjectPhysics
{
	public AIPhysics(AIPlayer aip) 
	{
            super("AI Paddle");
            Location = new Vector2d(0, AI_PADDLE_POSITION_YaXIS);
            aIPlayer = aip;
	}

	public AIPhysics() 
	{
            super("AI Paddle");
            Location = new Vector2d(0, AI_PADDLE_POSITION_YaXIS);    
            aIPlayer=new APlayer();
	}
	
	@Override
	public void Update() 
	{
            UpdateVectors();
            super.Update();
	}
	
        private void validationPosition()
        {
            Location.x = Math.min(Location.x, RIGHT_BOTTOM_ANGLE_POSITION_xAXIS - SMALL_CONSTANT);
            Location.x = Math.max(Location.x, LEFT_BOTTOM_ANGLE_POSITION_xAXIS + SMALL_CONSTANT);
            Location.y = Math.max(Location.y, 0);
            Location.y = Math.min(Location.y, RIGHT_UPPER_ANGLE_POSITION_yAXIS + SMALL_CONSTANT);
        }
            
	private void UpdateVectors() 
	{
            PreviousVelocity.set(Velocity);
            PreviousLocation.set(Location);
            
            Location.set(aIPlayer.getAIMovement());
            
            Velocity.set((Location.x - PreviousLocation.x) / 10, (Location.y - PreviousLocation.y) / 10); 
            validationPosition();
            //try { Thread.sleep(17); } catch (InterruptedException e)  { e.printStackTrace(); }
	}
	
	public static AIPlayer aIPlayer;
}
