package physics;

import javax.vecmath.Vector2d;
import javax.vecmath.Point2d;
import rendering.Renderer;
import concurrencyHelper.worker;
import static main.Const.puckFriction;

public class ObjectPhysics implements worker 
{
	public Vector2d Location;
	protected Vector2d PreviousLocation=new Vector2d(0,0);
	public Vector2d Velocity = new Vector2d(0,0);
	protected Vector2d PreviousVelocity = new Vector2d(0, 0);
	protected Vector2d Acceleration = new Vector2d(0, 0);
	protected int Mass;
	protected Vector2d Momentum = new Vector2d(0, 0);
	protected Vector2d Impulse = new Vector2d(0, 0);
	protected double KineticEnergy = 0;
	protected long delta = 1000; // for calculating elapsed time for physics work in mills second

	protected String Name = "";
	
	public ObjectPhysics(String Name) { this.Name=Name; }
	
	public Vector2d getLocation() { return Location; }
	public Vector2d getVelocity() { return Velocity; }
	public void setVelocity(Vector2d v) { Velocity = v; }
	
	
	public void CollisionWith(ObjectPhysics object) throws Exception 
	{
            if(Velocity.length() == 0)
            {
                Velocity = new Vector2d(object.Velocity);
                return;
            }
            
            double dx = this.Location.x - object.Location.x;
            double dy = this.Location.y - object.Location.y;

            
            double CollisionAngle = Math.atan2(dy, dx);
            double DirectionOfThis = Math.atan2(this.Velocity.getY(), this.Velocity.getX());
            double DirectionOfObject = Math.atan2(object.Velocity.getY(), object.Velocity.getX());
            
            double NewVelocityXaxisOfThis = this.Velocity.length() * Math.cos(DirectionOfThis - CollisionAngle);
            double NewVelocityYaxisOfThis = this.Velocity.length() * Math.sin(DirectionOfThis - CollisionAngle);

            double NewVelocityXaxisOfObject = object.Velocity.length() * Math.cos(DirectionOfObject - CollisionAngle);
            double NewVelocityYaxisOfObject = object.Velocity.length() * Math.sin(DirectionOfObject - CollisionAngle);

            double FinalVelocityXaxisOfThis = ((this.Mass + object.Mass) * NewVelocityXaxisOfThis + (object.Mass + object.Mass)* NewVelocityXaxisOfObject) / (this.Mass + object.Mass);

            double FinalVelocityXaxisOfObject = ((this.Mass + this.Mass) * NewVelocityXaxisOfThis + (object.Mass + this.Mass)* NewVelocityXaxisOfObject) / (this.Mass + object.Mass);

            double FinalVelocityYaxisofThis = NewVelocityYaxisOfThis;
            double FinalVelocityYaxisofObject = NewVelocityYaxisOfObject;

            Vector2d PreviousMomentum = Momentum;// save the value for calculating the Impulse of the collision

            // set the new Velocity after the collision
            Velocity.set(            
                    Math.cos(CollisionAngle) * FinalVelocityXaxisOfThis + 
                    Math.cos(CollisionAngle + Math.PI / 2) * FinalVelocityYaxisofThis,
                    
                    Math.sin(CollisionAngle) * FinalVelocityXaxisOfThis +
                    Math.sin(CollisionAngle + Math.PI / 2) * FinalVelocityXaxisOfThis
            );
            
            
            Vector2d ignoredVelocity = new Vector2d();
            // we ignore the new Velocity of the object as it's controlled by a  human or AI
            ignoredVelocity.set(
                            Math.cos(CollisionAngle) * FinalVelocityXaxisOfObject
                                            + Math.cos(CollisionAngle + Math.PI / 2)
                                            * FinalVelocityYaxisofObject,
                            Math.sin(CollisionAngle) * FinalVelocityXaxisOfObject
                                            + Math.sin(CollisionAngle + Math.PI / 2)
                                            * FinalVelocityXaxisOfObject);

            
            // calculate the Impulse
            Impulse.set(ImpulseCollision(PreviousMomentum));

            double acceleration = (this.Impulse.length() - puckFriction) / (this.Mass);
            Vector2d NormalizedVelocity = new Vector2d();
            NormalizedVelocity.normalize(this.Velocity);
            Acceleration.scale(acceleration, NormalizedVelocity);
	}	
	
	
	// this Method is private because it's just required for the collision so
	// the Momentum is going to change in the Object and the impulse is the
	// derivation of the difference of Momentum before collision and Momentum
	// after collision to delta time which collision was happened;
	private Vector2d ImpulseCollision(Vector2d PreviousMomentum) 
	{
		Momentum.scale(Mass, Velocity);
		return (new Vector2d(Momentum.getX() - PreviousMomentum.getX() / delta, Momentum.getY() - PreviousMomentum.getY() / delta));
	}
	
        
        	
        public static Renderer renderer;

        public Point2d getPosition() 
	{
		return new Point2d(Location.x,Location.y);
	}
	public float getxPosition() 
	{
		return (float)Location.x;
	}
	public float getyPosition() 
	{
		return (float)Location.y;
	}
        
	@Override
	public void Update() 
	{
            KineticEnergy = (1 / 2) * (Mass) * (Velocity.length() * Velocity.length());
            Momentum.scale(Mass, Velocity);
            Acceleration.set(Velocity.getX() - PreviousVelocity.getX() / delta, Velocity.getY() - PreviousVelocity.getY() / delta);

            renderer.Render(this);
	}
}