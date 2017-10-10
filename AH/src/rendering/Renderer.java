package rendering;

import java.applet.Applet;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.MemoryImageSource;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import physics.PuckPhysics;
import physics.ObjectPhysics;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.vecmath.Vector2d;
import physics.AIPhysics;
import physics.OpponentPaddlePhysics;

import static main.Const.*;

public class Renderer 
{
	public static Point2d getMouseMovement() { return MouseMovement; }

	private GameGraph Graphics;
	private MainFrame _Frame;
        	
	public static Point2d MouseMovement;
        
	public Renderer() 
        {
		_debuginfo = new Debugger();

		Graphics = new GameGraph();
		_Frame = new MainFrame(Graphics.getSceneGraph(),GameGraph.getScreenWidth(), GameGraph.getScreenHight());
		HideMouse(ShowMouse);
                _Frame.setTitle("AI-AIRHockey by Baha'a and friends");
		MouseMovement = new Point2d();
	}
        
	public Geometry getGeometry() {
		return (Graphics.getGeometry());
	}

	private void HideMouse(boolean status) 
        {
		if (!status) {
			_Frame.setResizable(false);
			int[] pixels = new int[16 * 16];
			Image image = Toolkit.getDefaultToolkit().createImage(
					new MemoryImageSource(16, 16, pixels, 0, 16));
			Cursor transparentCursor = Toolkit.getDefaultToolkit()
					.createCustomCursor(image, new Point(0, 0),
							"invisibleCursor");
			_Frame.setCursor(transparentCursor);
		} else {
			Cursor shown = Cursor.getDefaultCursor();
			_Frame.setCursor(shown);
		}
	}

	public void Render(ObjectPhysics obj) 
	{
            Graphics.Render(obj);
	}

	private static class GameGraph {

		public SceneGraph getSceneGraph() {
			return sceneGraph;
		}

		public static int getScreenHight() {
			return ScreenHight;
		}

		public static int getScreenWidth() {
			return ScreenWidth;
		}

		private Geometry geometry;

		public Geometry getGeometry() {
			return geometry;
		}

		private static int ScreenHight;
		private static int ScreenWidth;
		private SceneGraph sceneGraph;

		public GameGraph() {
			geometry = new Geometry();
			sceneGraph = new SceneGraph();
		}

		public void Render(ObjectPhysics obj) 
		{
			if (obj instanceof PuckPhysics) 
				this.sceneGraph.paintPuck(obj.getxPosition(),obj.getyPosition());
                        
			if (obj instanceof OpponentPaddlePhysics) 
				this.sceneGraph.paintOpponentPaddle(obj.getxPosition(),obj.getyPosition());
			
                        if (obj instanceof AIPhysics) 
				this.sceneGraph.paintAIPaddle(obj.getxPosition(),obj.getyPosition());
		}

		private class SceneGraph extends Applet implements KeyListener, MouseListener, MouseMotionListener 
		{
			private static final long serialVersionUID = 955904895157747080L;
			
			private Canvas3D canvas;
			private Transform3D movingOpponentPaddle;
			private Transform3D movingAIPaddle;
			private Transform3D movingPuck;
			private Matrix3d Tan;
			private PickCanvas pickCanvas;
			private Robot robot;
			private Point previousLocation;

			public SceneGraph() 
			{
				setLayout(new BorderLayout());
				GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
                                
				ScreenHight = config.getBounds().height;
				ScreenWidth = config.getBounds().width;

				canvas = new Canvas3D(config) 
				{
					private static final long serialVersionUID = 7144426579917281131L;

                                        @Override
					public void postRender() //  this  for Debug Mode (HUD)
					{	
                                            if (_showDebugInfo) 
                                                _debuginfo.PrintInfo(this.getGraphics2D());	
					}
				};

				add("Center", canvas);

				canvas.addKeyListener(this);

				// Create a simple scene and attach it to the virtual universe
				Tan = new Matrix3d();
				BranchGroup scene = createSceneGraph();
				SimpleUniverse simpleUniverse = new SimpleUniverse(canvas);
				
				// simpleUniverse.getViewingPlatform().setNominalViewingTransform();
				TransformGroup tg = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
				Transform3D translation = new Transform3D();
				translation.setTranslation(new Vector3f(0, 0, 35f));
				Transform3D rotationX = new Transform3D();
				
				rotationX.rotX(0);

				Transform3D trans = new Transform3D();
				trans.mul(rotationX, translation);
				tg.setTransform(trans);

				simpleUniverse.addBranchGraph(scene);

				pickCanvas = new PickCanvas(canvas, scene);
				pickCanvas.setMode(PickCanvas.GEOMETRY);
				
				// simpleUniverse.addBranchGraph(arg0);
				movingPuck = new Transform3D();
				movingAIPaddle = new Transform3D();
				movingOpponentPaddle = new Transform3D();

				canvas.addMouseListener(this);
				canvas.addMouseMotionListener(this);

				try { robot = new Robot(); } catch (AWTException e) { }
				
				previousLocation = new Point();
				previousLocation.setLocation(MouseInfo.getPointerInfo().getLocation());

			}

			@Override
			public void keyPressed(KeyEvent arg0) 
			{
                            main.Control.onKeyPress(arg0.getKeyChar());
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			public BranchGroup createSceneGraph() 
			{
				BranchGroup objRoot = new BranchGroup();
				
				geometry.X(objRoot, canvas); /////////////////////////////////////////////////////////////////////////
				
				
				objRoot.addChild(geometry.getTable());
				BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10);
				Color3f light1Color = new Color3f(0.8f, 0.8f, 0.8f);
				Vector3f light1Direction = new Vector3f(0f, -1f, -1f);
				DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
				light1.setInfluencingBounds(bounds);
				objRoot.addChild(light1);

				// Set up the ambient light
				Color3f ambientColor = new Color3f(0.3f, 0.4f, 0.7f);
				AmbientLight ambientLightNode = new AmbientLight(ambientColor);
				ambientLightNode.setInfluencingBounds(bounds);
				objRoot.addChild(ambientLightNode);
				Tan.rotX(+Math.PI / 2);
				Background background = new Background();
				background.setCapability(Background.ALLOW_IMAGE_WRITE);
				background.setCapability(Background.ALLOW_COLOR_WRITE);
				background.setColor(0.895f, 0.895f, 0.895f);
				background.setApplicationBounds(bounds);
				objRoot.addChild(background);
				
				return objRoot;
			}

			private void paintPuck(float x, float y) 
			{
				Vector3f s = new Vector3f(x, y, 2 * TABLE_THICK);
				movingPuck.setTranslation(s);
				
				PuckPhysics.AngularVelocity.rotZ(PuckPhysics.angle);
				PuckPhysics.AngularVelocity.mul(Tan);
				
				movingPuck.setRotation(PuckPhysics.AngularVelocity);
				geometry.getPuck().setTransform(movingPuck);
			}

			private void paintAIPaddle(float x, float y) 
			{
				Vector3f s = new Vector3f(x, y, 2 * TABLE_THICK);

				// Point2d currentPositionforAIPaddle = new Point2d(s.x, s.y);
				// GraphicsObject.AIPaddle.Update(currentPositionforAIPaddle);

				movingAIPaddle.setTranslation(s);
				movingAIPaddle.setRotation(Tan);
                                
				geometry.getAIPaddle().setTransform(movingAIPaddle);
			}

			private void paintOpponentPaddle(float x, float y) 
			{
				Vector3f s = new Vector3f(x, y, 2 * TABLE_THICK);

				movingOpponentPaddle.setTranslation(s);
				movingOpponentPaddle.setRotation(Tan);
				
				geometry.getOpponentPaddle().setTransform(movingOpponentPaddle);
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {

			}

			// it needs to edit control from physics not from here
			@Override
			public void mouseMoved(MouseEvent arg0) 
			{
                            for (int i = 0; i < 1; i++) 
                            {
                                int x = arg0.getXOnScreen();//((arg0.getXOnScreen() * i) /2) + (previousLocation.x * (4 - i) /4); // B
                                int y = arg0.getYOnScreen();//((arg0.getYOnScreen() * i) /2) + (previousLocation.y * (4 - i) /4);

                                robot.mouseMove(x, y);

                                Point3d mousePos = new Point3d();
                                canvas.getPixelLocationInImagePlate(x, y, mousePos);

                                Transform3D transform = new Transform3D();
                                canvas.getImagePlateToVworld(transform);
                                transform.transform(mousePos);
                                MouseMovement.set(mousePos.x, mousePos.y);
                            }
                            previousLocation.setLocation(arg0.getXOnScreen(),arg0.getYOnScreen());
			}

			@Override
			public void mouseClicked(MouseEvent arg0) 
                        {
                            main.Control.onClick();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		}
	}
}