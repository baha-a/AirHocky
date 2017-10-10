package rendering;

import java.awt.Container;
import java.net.URL;
import java.util.Enumeration;

import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;

import static main.Const.*;

public class Geometry 
{
	public TransformGroup getPuck() { return Puck; }
	public TransformGroup getAIPaddle()  { return AIPaddle; }
	public TransformGroup getOpponentPaddle()  { return OpponentPaddle; }

	private TransformGroup Puck;
	private TransformGroup AIPaddle;
	private TransformGroup OpponentPaddle;
	private TransformGroup Table;

	static TransformGroup tableBorders;
	
	public Geometry() 
	{
		Puck = new TransformGroup();
		AIPaddle = new TransformGroup();
		OpponentPaddle = new TransformGroup();
		tableBorders = new TransformGroup();
		Table = AirHockeyTable();
		
		Puck.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Puck.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		Puck.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		
		AIPaddle.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		AIPaddle.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		AIPaddle.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		
		OpponentPaddle.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		OpponentPaddle.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		OpponentPaddle.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		
		tableBorders.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tableBorders.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		Puck.setUserData("Puck");
		AIPaddle.setUserData("AIPaddle");
		OpponentPaddle.setUserData("OpponentPaddle");
		tableBorders.setUserData("Borders");
	}
	
        public TransformGroup ReTable()
        {
            return AirHockeyTable();
        }
        
	BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,0.0),0.0001); //////////////////////////////////

	public void X(BranchGroup x,Canvas3D c)
	{
	    x.addChild(new PickTranslateBehavior(x, c, bounds));
		
		x.addChild(new CollisionDetector(AIPaddle, bounds));
	    x.addChild(new CollisionDetector(OpponentPaddle, bounds));
	    x.addChild(new CollisionDetector(tableBorders, bounds));
	}
	public Group getTable() 
	{ 
            return Table;
	}

	private TransformGroup AirHockeyTable() 
	{
		TransformGroup airHockeyTable = new TransformGroup();
		TransformGroup Opponentpaddle = Geometry.Paddle(new Vector3f(0f,OPPONENT_PADDLE_POSITION_YaXIS, 2 * TABLE_THICK));
		TransformGroup AIpaddle = Geometry.Paddle(new Vector3f(0f,AI_PADDLE_POSITION_YaXIS, 2 * TABLE_THICK));

		TransformGroup puck = Geometry.puck();
		
		airHockeyTable.addChild(AIpaddle);
		airHockeyTable.addChild(Opponentpaddle);
		airHockeyTable.addChild(puck);
		airHockeyTable.addChild(Geometry.Table());
		
		TransformGroup Horizon = new TransformGroup();
		Transform3D horizon = new Transform3D();
		Matrix3d Tan = new Matrix3d();
		Tan.rotX(-Math.PI / 3);
		horizon.setRotation(Tan);
		Horizon.setTransform(horizon);
		Horizon.addChild(airHockeyTable);

		this.AIPaddle = AIpaddle;
		this.OpponentPaddle = Opponentpaddle;
		this.Puck = puck;
		
		return Horizon;
	}

	private static TransformGroup puck() {
		return (PuckCylinderShape(PUCK_RADIUS, PUCK_THICK, new Vector3f(
				(float) Puck_First_Position.x, (float) Puck_First_Position.y,
				TABLE_THICK-2.1f),
				Geometry.class.getClassLoader()
						.getResource("textures/Puck.png")));
	}

	private static TransformGroup Paddle(Vector3f vector) 
	{
		TextureLoader loader = new TextureLoader(Geometry.class .getClassLoader().getResource("textures/Paddle.png"), new Container());
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);

		int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

		Appearance ap = setUpAppearance(new Material());
		ap.setTexture(texture);

		Cylinder cylinderBase = new Cylinder(PADDLE_BASE_RADIUS,PADDLE_BASE_THICK, primflags, ap);
		cylinderBase.setAppearance(Cylinder.TOP, ap);

		TransformGroup cylinderHead = CylinderShape(PADDLE_HEAD_RADIUS,PADDLE_HEAD_THICK, new Vector3f(0f, PADDLE_BASE_THICK, 0f));

		cylinderHead.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		TransformGroup complete = new TransformGroup();
		complete.addChild(cylinderHead);
		complete.addChild(cylinderBase);

		complete.setCapability(TransformGroup.ALLOW_PICKABLE_READ);
		complete.setCapability(TransformGroup.ALLOW_PICKABLE_WRITE);		
				
		Matrix3d Tan = new Matrix3d();
		Tan.rotX(+Math.PI / 2);

		Transform3D PaddlePosition = new Transform3D();
		PaddlePosition.setTranslation(vector);
		PaddlePosition.setRotation(Tan);
		complete.setTransform(PaddlePosition);

		return (complete);
	}

	private static Group Table() 
	{
		TransformGroup Table = new TransformGroup();
		Box _Table = setUpTextureBox(Geometry.class.getClassLoader().getResource("textures/Table.jpg"), TABLE_HIGH, TABLE_WIDTH,TABLE_THICK);

		Appearance appearance = setUpAppearance(new Material());

		TransformGroup _RightSideWall = buildBox(new Vector3f(TABLE_HIGH, 0f, 0f), SIDE_WALL_HIGH, SIDE_WALL_WIDTH, SIDE_WALL_THICK, appearance);
		TransformGroup _LeftSideWall = buildBox(new Vector3f(-TABLE_HIGH, 0f, 0f), SIDE_WALL_HIGH, SIDE_WALL_WIDTH, SIDE_WALL_THICK, appearance);
		TransformGroup _BottomSideWall = buildBox(new Vector3f(0f, -TABLE_WIDTH, 0f), PLAYER_WALL_WIDTH, PLAYER_WALL_HIGH, PLAYER_WALL_THICK, appearance);
		TransformGroup _UpperSideWall = buildBox(new Vector3f(0f, +TABLE_WIDTH, 0f), PLAYER_WALL_WIDTH, PLAYER_WALL_HIGH, PLAYER_WALL_THICK, appearance);
		
		TransformGroup Opponent_Goal= Goal(new Vector3f(0,+TABLE_WIDTH,0));
		TransformGroup AI_Goal= Goal(new Vector3f(0,-TABLE_WIDTH,0));

		Table.addChild(_Table);
		Table.addChild(Opponent_Goal);
		Table.addChild(AI_Goal);

		tableBorders.addChild(_LeftSideWall);
		tableBorders.addChild(_RightSideWall);
		tableBorders.addChild(_UpperSideWall);
		tableBorders.addChild(_BottomSideWall);
		
		Table.addChild(tableBorders);
		
		return (Table);
	}
	
	

	private static final Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

	private static final Color3f green = new Color3f(.15f, 0.7f, .15f);
	private static final Color3f blue = new Color3f(.15f, .15f, 0.7f);
	private static final Color3f Red = new Color3f(1f, 0f, 0f);	
	
	
	private static TransformGroup buildBox(Vector3f vector, float height, float width, float thick, Appearance appearance) 
	{
		Transform3D boxPosition = new Transform3D();
		boxPosition.setTranslation(vector);

		Box boxShape = new Box(height, width, thick, appearance);

		TransformGroup boxBuild = new TransformGroup();
		boxBuild.addChild(boxShape);
		boxBuild.setTransform(boxPosition);

		return (boxBuild);
	}

	private static TransformGroup Goal(Vector3f vector) 
	{
		Box goal = setUpTextureBox( Geometry.class.getClassLoader() .getResource("textures/goal.jpg"), GoalLength, 2 * PLAYER_WALL_HIGH, PLAYER_WALL_THICK);
		 
		Transform3D boxPosition = new Transform3D();
		boxPosition.setTranslation(vector);

		TransformGroup boxBuild = new TransformGroup();
		boxBuild.addChild(goal);
		boxBuild.setTransform(boxPosition);
		return (boxBuild);
	}

	private static Appearance setUpAppearance(Material material) 
	{
		Appearance appearance = new Appearance();
		material.setAmbientColor(0.31f, 0.3f, 0.5f);
		appearance.setMaterial(material);
		return (appearance);
	}

	private static Box setUpTextureBox(URL URL, float height, float width,
			float thick) {

		TextureLoader loader = new TextureLoader(URL, new Container());
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);

		Appearance ap = setUpAppearance(new Material(black, green, blue, black,
				1f));
	
		int primflags = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Box _Table = new Box(height, width, thick, primflags, ap);
		ap.setTexture(texture);

		_Table.setAppearance(Box.TOP, ap);

		return (_Table);
	}

	private static TransformGroup CylinderShape(float radius, float thick, Vector3f vector) 
	{
		Material m = new Material(Red, Red, Red, black, 1f);
		Appearance ap = setUpAppearance(m);

		Cylinder cylinder = new Cylinder(radius, thick, ap);

		TransformGroup Completecylinder = new TransformGroup();
		Completecylinder.addChild(cylinder);
		Transform3D cylinderRotation = new Transform3D();

		cylinderRotation.setTranslation(vector);
		Completecylinder.setTransform(cylinderRotation);
		return (Completecylinder);
	}

	private static TransformGroup PuckCylinderShape(float radius, float thick, Vector3f vector, URL URL) 
	{		
		TextureLoader loader = new TextureLoader(URL, new Container());
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);

		int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;

		Appearance ap = setUpAppearance(new Material(black, green, blue, black,1f));
		ap.setTexture(texture);

		Cylinder cylinder = new Cylinder(radius, thick, primflags,  ap);
		cylinder.setAppearance(Cylinder.TOP, ap);
		cylinder.setCollidable(true);
		
		TransformGroup Completecylinder = new TransformGroup();
		Completecylinder.addChild(cylinder);
		Transform3D cylinderRotation = new Transform3D();

		cylinderRotation.setTranslation(vector);
		Completecylinder.setTransform(cylinderRotation);
		return (Completecylinder);
	}
	
	
	
	
	
	
	
	class CollisionDetector extends Behavior 
	{
	  protected TransformGroup collidingShape;
	  protected WakeupCriterion[] theCriteria;
	  protected WakeupOr oredCriteria;

	  public CollisionDetector(TransformGroup theShape, Bounds theBounds) 
	  {
	    collidingShape = theShape;
	    setSchedulingBounds(theBounds);
	  }
	  
	     @Override
	  public void initialize() 
	  {
	    theCriteria = new WakeupCriterion[3];
	    theCriteria[0] = new WakeupOnCollisionEntry(collidingShape);
	    theCriteria[1] =  new WakeupOnCollisionExit(collidingShape);
	    theCriteria[2] = new WakeupOnCollisionMovement(collidingShape);
	    
	    oredCriteria = new WakeupOr(theCriteria);
	    wakeupOn(oredCriteria);
	  }

	     @Override
	  public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria) 
	  {
	    while (true)
	    {
	    	if(criteria.hasMoreElements())
	    	{
	    		WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();	      
		   	     if (theCriterion instanceof WakeupOnCollisionEntry)
		   	      {
		   	    	  //SoundEffect.Collid.play();
		   	      }
		   	      else if (theCriterion instanceof WakeupOnCollisionExit)
		   	      {  	    	   
		   	      }
		   	      else
		   	      {
		   	      }
	   	    }
	    	else
	    	{
	    		break;
	    	}
	    }
	    wakeupOn(oredCriteria);

	  }
 	}

}
