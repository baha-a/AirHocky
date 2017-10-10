package aI;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import static main.Const.*;


public class AIHelper 
{	
    
    public static boolean isPuckBehindAI()
    {
        return (ai.getLocation().y<puck.getLocation().y);
    }
         
    public static boolean isPuckStuckInCorner() 
    {
        return (Math.abs(puck.getLocation().y) + (PUCK_RADIUS) >= TABLE_WIDTH * 0.8)
            && (Math.abs(puck.getLocation().x) + (PUCK_RADIUS) >= TABLE_HIGH * 0.8);
    }

    public static boolean isPuckInLeftSide()
    {
        return (puck.getLocation().x < 0);
    }
    public static boolean isPuckInAISide()
    {
         return (puck.getLocation().y >= 0);
    }
    public static boolean isAIPADDLEinFrontSide() // not tested yet but it's look great 
    {
            return (Math.abs(ai.getLocation().y) < TABLE_WIDTH / 2);
    }
    public static boolean isAIPADDLEinLeftSide() // not tested
    {		
            return (ai.getLocation().x < 0);
    }


    public static boolean isOPPOPADDLENearToSideWalls()
    {		
            return (Math.abs(op.getLocation().x) > TABLE_HIGH / 2);
    }
    public static boolean isOPPOPADDLEinFrontSide()
    {
            return (Math.abs(op.getLocation().y) < TABLE_WIDTH / 2);
    }
    public static boolean isOPPOPADDLEinLeftSide()
    {		
            return (op.getLocation().x < 0);
    }	
    public static boolean isPuckAwayFromOPPADDLe()
    {
            return ((GetLength(puck.getLocation(),op.getLocation())+PUCK_RADIUS+PADDLE_BASE_RADIUS) >= 5);
    }
    public static double GetLength(Vector2d v,Vector2d u)
    {
            return (int)Math.sqrt(Math.pow((v.x-u.x),2) + Math.pow((v.y-u.y),2)); 
    }


    //////////////////////////////////////////////////////////////////////






    public double getPuckDirection() {
            // still unimplemented yet!;
            return (0); // Radian
    }

    public double getOpponetDirection() {
            // still unimplemented yet!;
            return (0.0); // Radian
    }

    public Vector2d getDesiredVectortoAchiveGoal() {
            // still unimplemented yet!;
            return (null);
    }

    public boolean IsCapabletoMove(Point2d start, Point2d end, float period) {
            // still unimplemented yet!;
            return (false);
    }

    public Point2d FuturePuckLocationAfterTimePeriod(float period) {
            return (null);
    }

    public Point2d FutureopponentPaddleLocationAfterTimePeriod(float period) {
            return (null);
    }

    public boolean isPuckOnAISide() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean isPuckOnOpponentSide() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean isPuckBehindAIPlayer() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean isPuckbehindOpponentPlayer() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerGoingtoShoot() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentInDefensePosition() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerInAttackPosition() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerOnLeftSide() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerOnRightSide() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerinTheMiddle() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean areOpponentPlayerNeartheGoal() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean isPuckgoingFastEnough() {
            // still unimplemented yet!;
            return (false);
    }

    public boolean isPuckgoingSlowEnough() {
            // still unimplemented yet!;
            return (false);
    }
}
