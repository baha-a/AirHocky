package physics;

import javax.vecmath.Vector2d;
import static main.Const.*;
import rendering.Renderer;

public class OpponentPaddlePhysics extends ObjectPhysics
{	
	@Override
	public void Update() 
	{
            UpdateVectors();
            super.Update();
	}

	public OpponentPaddlePhysics() 
	{
            super("Opponent Paddle");
            Location = new Vector2d(0, OPPONENT_PADDLE_POSITION_YaXIS);
            Velocity = new Vector2d(Location);
	}

	private void validateBoundaries()
	{
            Location.x = Math.min(Location.x, RIGHT_BOTTOM_ANGLE_POSITION_xAXIS - PADDLE_BASE_RADIUS);
            Location.x = Math.max(Location.x, LEFT_BOTTOM_ANGLE_POSITION_xAXIS + PADDLE_BASE_RADIUS);
            Location.y = Math.min(Location.y - PADDLE_BASE_RADIUS, 0);
            Location.y = Math.max(Location.y, RIGHT_BOTTOM_ANGLE_POSITION_yAXIS + PADDLE_BASE_RADIUS);
	}
	
	private void UpdateVectors() 
	{			
            PreviousLocation.set(Location);
            PreviousVelocity.set(Velocity);
            
            Location.set(Renderer.getMouseMovement());
            Location.scale(15);
            Velocity.set((Location.x - PreviousLocation.x) / 10, (Location.y - PreviousLocation.y) /10);
            validateBoundaries();
           
            //try  { Thread.sleep(10); } catch (InterruptedException e)  { }
	}
}
