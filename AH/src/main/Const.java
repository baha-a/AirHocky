package main;

import javax.vecmath.Point2d;
import physics.AIPhysics;
import physics.OpponentPaddlePhysics;
import physics.PuckPhysics;
import rendering.Debugger;

public class Const
{
    public static AIPhysics ai;
    public static OpponentPaddlePhysics op;
    public static PuckPhysics puck;
    
    
    
    
    public static enum GameLevel { Easy , Hard , Expert}
    
    // Parameters

    public static GameLevel level = GameLevel.Easy;
    
    public static boolean freezeAiPaddel = false;
    public static boolean freezePuck = false;
    public static boolean pausegame = false;

    public static float puckFriction = 0.004f;
    
    public static float puckSpeed = 0.5f;
    public static float AiSpeed = 0.2f;
    
    
    public static int UpPlayerScore = 0;
    public static int DownPlayerScore = 0;
    
    public static boolean EnableGoals = true;
    public static final float GoalLength = 1.4f; // need restart game
    
    
  
    public static final float TABLE_WIDTH = 4.80f * 2f; // need restart game
    public static final float TABLE_HIGH = 3.20f * 2f; // need restart game
    
    public static final float PUCK_RADIUS = 0.6f; // need restart game
    public static final float PADDLE_BASE_RADIUS = 0.6f; // need restart game
    
    
    
    
    
    
    // extra parameters
    
    public static final float OPPONENT_PADDLE_POSITION_YaXIS = -(TABLE_WIDTH * (0.75f));
    public static final float AI_PADDLE_POSITION_YaXIS = +(TABLE_WIDTH * (0.75f));
    public static final Point2d Puck_First_Position = new Point2d(0, 0);
    
    public static boolean isAiNormalStateAllowed = true;
    public static boolean isAiAttackStateAllowed = true;
    public static boolean isAiDefenceStateAllowed = true;
    public static boolean isAiWonderStateAllowed = true;
    

    
    
    
    // don't play with those Fields
    
    public static final float TABLE_THICK = 0.1f;
    public static final float PLAYER_WALL_WIDTH = TABLE_HIGH;
    public static final float PLAYER_WALL_HIGH = 0.1f;
    public static final float PLAYER_WALL_THICK = TABLE_THICK * 3;
    public static final float SIDE_WALL_WIDTH = TABLE_WIDTH;
    public static final float SIDE_WALL_HIGH = 0.1f;
    public static final float SIDE_WALL_THICK = TABLE_THICK * 3;

    /** The Constant SMALL_CONSTANT its value equal to Table High/10 . */
    public static final float SMALL_CONSTANT = TABLE_HIGH / 10;

    public static final float LEFT_BOTTOM_ANGLE_POSITION_xAXIS = -TABLE_HIGH;
    public static final float LEFT_BOTTOM_ANGLE_POSITION_yAXIS = -TABLE_WIDTH;
    public static final float RIGHT_BOTTOM_ANGLE_POSITION_xAXIS = +TABLE_HIGH;
    public static final float RIGHT_BOTTOM_ANGLE_POSITION_yAXIS = -TABLE_WIDTH;
    public static final float LEFT_UPPER_ANGLE_POSITION_xAXIS = -TABLE_HIGH;
    public static final float LEFT_UPPER_ANGLE_POSITION_yAXIS = +TABLE_WIDTH;
    public static final float RIGHT_UPPER_ANGLE_POSITION_xAXIS = +TABLE_HIGH;
    public static final float RIGHT_UPPER_ANGLE_POSITION_yAXIS = +TABLE_WIDTH;
    
    public static final float PUCK_THICK = 0.12f;
    public static final float PADDLE_BASE_THICK = 0.25f;
    public static final float PADDLE_HEAD_RADIUS = 0.30f;
    public static final float PADDLE_HEAD_THICK = 0.25f;
 
    
    
    public static int snooze = 30;
    public static int goNextFrame = 0;
    public static boolean _showDebugInfo = true;
    public static Debugger _debuginfo;
    
    public static boolean ShowMouse = true;
    
    public static long SleepTimeForProcess = 27;
    public static boolean DEBUGMODE = true;    
    
    // not used
    public static int NormalWeight  = 100;
    public static int WonderWeight  = 100;
    public static int AttackWeight  = 100;
    public static int DefenceWeight = 100;
}
