package physics;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import rendering.SoundEffect;
import static main.Const.*;
import main.Control;

public class PuckPhysics extends ObjectPhysics 
{
	public static int CollisionResult;

	public static float angle = 0;
	public static Matrix3d AngularVelocity;
	
	
	@Override
	public void Update() 
	{   
            PreviousVelocity.set(Velocity);
            PreviousLocation.set(Location);
            
            GetUpdate();            
            super.Update();
	}

	public PuckPhysics() 
	{   
            super("Puck");
            
            Location = new Vector2d(0, 0);
            Velocity = new Vector2d(0, 0);
            
            Mass = 10;
            AngularVelocity = new Matrix3d();

            _debuginfo.Names[4]= "AI ";
            _debuginfo.Values[4]= UpPlayerScore;
            _debuginfo.Names[5] = "You";
            _debuginfo.Values[5]= DownPlayerScore;
	}
	
	private boolean CheckForCollisionWithPaddle(boolean IsItAiPaddle) 
	{
		Point2d firstBall = getPosition(), secondBall = IsItAiPaddle ? ai.getPosition() : op.getPosition();
				
		float tmp = PUCK_RADIUS + PADDLE_BASE_RADIUS + 0.1f,
                        
                      rp =  PUCK_RADIUS + 0.1f,
                      rb =  PADDLE_BASE_RADIUS + 0.1f;
		
		//if (!(firstBall.x - rb < secondBall.x + rp && firstBall.x + rb < secondBall.x - rp &&  firstBall.y - rb < secondBall.y + rp && firstBall.y + rb < secondBall.y - rp)) 
		{	
                    if(distance(Location,(IsItAiPaddle?ai.getLocation(): op.getLocation())) <= tmp)
                    {
                        try  
                        {
                            this.CollisionWith(IsItAiPaddle ? ai : op);
                            
                        } catch (Exception e)  { }

                        correctingLocation(IsItAiPaddle);
                        
                        return true;
                    }
		}
		return false;
	}
        
	private boolean CheckForCollisionwithWall() 
	{
            float shorty = 0.1f;
		
            if ((RIGHT_BOTTOM_ANGLE_POSITION_xAXIS) - (getxPosition() + PUCK_RADIUS) < shorty)
            {
                    Velocity.setX(-1 * Velocity.getX());
                    return true;
            }
            if ((Math.abs(LEFT_BOTTOM_ANGLE_POSITION_xAXIS)) - (Math.abs(getxPosition()) + PUCK_RADIUS) < shorty) 
            {
                    Velocity.setX(-1 * Velocity.getX());
                    return true;
            }
            if ((RIGHT_UPPER_ANGLE_POSITION_yAXIS) - (Math.abs(getyPosition()) + PUCK_RADIUS) < shorty) 
            {
                    Velocity.setY(-1 * Velocity.getY());
                    return true;
            }
            if (Math.abs(RIGHT_BOTTOM_ANGLE_POSITION_yAXIS) - (Math.abs(getyPosition()) + PUCK_RADIUS) < shorty) 
            {
                    Velocity.setY(-1 * Velocity.getY());
                    return true;
            }

            Momentum.set(Velocity);
            Momentum.scale(Mass);
            return false;
	}	
	
	private void validateBoundaries()
	{
		if ( Location.x > ((TABLE_HIGH) - PUCK_RADIUS) )		
			Location.x = TABLE_HIGH - PUCK_RADIUS;
		if ( Location.x < (-(TABLE_HIGH) + PUCK_RADIUS) )
			Location.x = -TABLE_HIGH+PUCK_RADIUS;
		if ( Location.y > ((TABLE_WIDTH) - PUCK_RADIUS) )
			Location.y = TABLE_WIDTH-PUCK_RADIUS;
		if ( Location.y < (-(TABLE_WIDTH) + PUCK_RADIUS) )
			Location.y = -TABLE_WIDTH+PUCK_RADIUS;
	}
	
	private boolean CheckGoals()
	{
            if(!EnableGoals)
                return false;
            
            if(Location.x <= GoalLength / 1.5 && Location.x >= -GoalLength / 1.5)
                if(Location.y >= TABLE_WIDTH - PUCK_RADIUS -0.1)
                {
                    Control.AddGoal(true);
                    return true;
                }
                else if(Location.y <= -TABLE_WIDTH + PUCK_RADIUS+0.1)
                {
                    Control.AddGoal(false);    
                    return true;
                }
            
            return false;
	}

        private void correctingLocation(boolean IsForAiPaddle)
        {    
            ObjectPhysics f = IsForAiPaddle ? ai: op;
            
            float tmp = PUCK_RADIUS + PADDLE_BASE_RADIUS + 0.5f;
            float dist = (float) distance(Location, f.getLocation());
            
            if(dist <= tmp)
            {        
                double 
                        tmp2 = (tmp - dist) + 0.1,
                        CollisionAngle = Math.atan2(Location.y - f.Location.y, Location.x - f.Location.x),
                        tmp2x = tmp2 * Math.cos(CollisionAngle),
                        tmp2y = tmp2 * Math.sin(CollisionAngle);
                
                Velocity.x += tmp2x;
                Velocity.y += tmp2y;
                
                PuckRefreshOnOverlay();
            }
        }
        
        private void PuckRefresh()
	{
            Acceleration.clamp(-puckFriction, puckFriction);
            Velocity.sub(Acceleration);
            Velocity.clamp(-puckSpeed, puckSpeed);
            Location.add(Velocity);
	}
                
        private void PuckRefreshOnOverlay()
	{
            Acceleration.clamp(-puckFriction, puckFriction);
            Velocity.sub(Acceleration);
            Velocity.clamp(-Math.max(puckSpeed,AiSpeed), Math.max(puckSpeed,AiSpeed));
            Location.add(Velocity);
	}

                
	private void GetUpdate() 
	{	
            if(CheckForCollisionwithWall())
                    SoundEffect.HitBorder.play();

            if(CheckGoals())
                    SoundEffect.HitGoal.play();
            
            if(CheckForCollisionWithPaddle(false))
                    SoundEffect.Collid.play();
            
            if(CheckForCollisionWithPaddle(true))
                    SoundEffect.Collid.play();

            HandleAngle();
            
            PuckRefresh();
            
            validateBoundaries();
	}
        
        private void HandleAngle()
        {
            //source : 
            // http://stackoverflow.com/questions/9760950/calculate-direction-and-velocity-based-horizontal-and-vertical-velocity
            //
            //http://www.real-world-physics-problems.com/physics-of-billiards.html
            //
           
            angle -= clamp((float) Math.toDegrees(Math.atan2(Acceleration.y, Acceleration.x)) , puckFriction*10);
            angle += clamp((float) Math.toDegrees(Math.atan2(Velocity.y, Velocity.x)) , puckSpeed);
        }
        private float clamp(float x,float max) { return ( x > max ) ? max : ( ( x < -max) ? -max : x ); }
        private float clamp2(float x,float max) 
        {
            return ( x > max ) ? max : (( x < 0) ? 0 : x ); }
	
        private double distance(Vector2d v1,Vector2d v2) 
        {
            return Math.sqrt(Math.pow(Math.abs(v1.x-v2.x), 2) + Math.pow(Math.abs(v1.y-v2.y), 2)); 
        }        
        

        public void recenter()
        {        
            Velocity=new Vector2d(0,0);
            Location=new Vector2d(0,0);
            Update();
        }
        
        public void putPuckInAICornerForTesting(boolean LeftCorner)
        {
            puck.Velocity = new Vector2d(0,0);
            if(!LeftCorner)
                puck.Location = new Vector2d(TABLE_HIGH - PUCK_RADIUS -0.5, TABLE_WIDTH - PUCK_RADIUS - 0.5);
            else
                puck.Location = new Vector2d(-TABLE_HIGH + PUCK_RADIUS + 0.5, TABLE_WIDTH - PUCK_RADIUS - 0.5);
        }
}
