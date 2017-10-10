package aI;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import static main.Const.*;

public class APlayer implements AIPlayer 
{
    public enum State { Attack, Defense, Wonder, Normal }   
    private enum TargetSpot { LCorner, Mid, RCorner, LWall, RWall }

    State lastState;
    Vector2d target;

    public APlayer() 
    {
        lastState = State.Normal;
    }

    public void ChangeStateMenualy(State newState)
    {
        lastState = newState;
    }

    private void StatesHandler() 
    {        
        if(AIHelper.isPuckStuckInCorner() && AIHelper.isPuckInAISide())
        {
            target = new Vector2d(puck.getLocation().x, puck.getLocation().y - ((puck.getLocation().y > 0)? -(PUCK_RADIUS + PADDLE_BASE_RADIUS) : (PUCK_RADIUS + PADDLE_BASE_RADIUS)));
            return;
        }
        
        TargetSpot spt;
        switch (lastState) 
        {
            case Attack:
                if(AIHelper.isPuckBehindAI())
                {
                    target = new Vector2d(puck.Location);
                    break;
                }
                
                if (AIHelper.isOPPOPADDLEinFrontSide()) 
                {
                    if (AIHelper.isOPPOPADDLENearToSideWalls()) // hit the Puck to goal directly on straight line
                            spt = TargetSpot.Mid;
                    else // hit the Puck to wall to zigzag way
                    if (AIHelper.isOPPOPADDLEinLeftSide()) // then to right wall
                            spt = TargetSpot.RWall;
                    else
                            // else to left wall
                            spt = TargetSpot.LWall;
                }
                else 
                {
                    if (AIHelper.isOPPOPADDLEinLeftSide()) // hit the Puck to goal directly on straight
                                                 // line and to right a little bit (to Right Corner of goal)
                            spt = TargetSpot.RCorner;
                    else // hit the Puck to goal directly on straight line and to left a little bit (to Left Corner of goal)
                            spt = TargetSpot.LCorner;
                }
                target = FindPositionToAttack(spt);
                
                break;
            case Defense:
                target =(AIHelper.isPuckInLeftSide())
                      ? new Vector2d(-0.8 * Math.min(Math.abs(puck.getLocation().x), TABLE_HIGH * 0.4), TABLE_WIDTH * 0.8)
                      : new Vector2d(0.8 * Math.min(puck.getLocation().x, TABLE_HIGH * 0.4), TABLE_WIDTH * 0.8);
                break;
            case Wonder:
                if (!AIHelper.isPuckInAISide()) // i can't do "wonder-ing" if puck in my side
                    if (AIHelper.isOPPOPADDLEinLeftSide())
                            target = new Vector2d(TABLE_HIGH * 0.3,TABLE_WIDTH * 0.8);
                    else
                            target = new Vector2d(TABLE_HIGH * -0.3, TABLE_WIDTH * 0.8);
                else 
                {
                    if (AIHelper.isPuckInLeftSide())
                            target = new Vector2d(-0.8 * Math.min(Math.abs(puck.getLocation().x),TABLE_HIGH * 0.4), TABLE_WIDTH * 0.8);
                    else
                            target = new Vector2d(0.8 * Math.min(puck.getLocation().x, TABLE_HIGH * 0.4), TABLE_WIDTH * 0.8);
                    break;
                }
                break;
            case Normal:
                if (AIHelper.isPuckInAISide())
                        target = new Vector2d(puck.getLocation().x, puck.getLocation().y);
                else
                        target = new Vector2d(puck.getLocation().x * 0.6f, TABLE_WIDTH * 2 / 4);
                break;
        }
    }

    private Vector2d FindPositionToAttack(TargetSpot spot) 
    {
            double x = 0, y = 0;
            switch (spot) 
            {
                case LCorner:
                    y = TABLE_WIDTH / 2;
                    x = TABLE_HIGH / -4;
                    break;
                case RCorner:
                    y = TABLE_WIDTH / 2;
                    x = TABLE_HIGH / 4;
                    break;
                case Mid:
                    y = TABLE_WIDTH / 2;
                    x = 0;
                    break;
                case LWall:
                    y = TABLE_WIDTH / 10;
                    x = TABLE_HIGH / -2;
                    break;
                case RWall:
                    y = TABLE_WIDTH / 10;
                    x = TABLE_HIGH / 2;
                    break;
            }

            x = (puck.getLocation().x - x) + PUCK_RADIUS
                            + PADDLE_BASE_RADIUS;
            y = (puck.getLocation().y - y) + PUCK_RADIUS
                            + PADDLE_BASE_RADIUS;

            return new Vector2d(x, y);
    }

    private Vector2d moveTo(Vector2d o, Vector2d p) 
    {
        double speed = AiSpeed, dx = p.x - o.x, dy = p.y - o.y, distance = Math.sqrt((dx * dx + dy * dy));

        if(level == GameLevel.Easy) 
            speed /=2;
        else if(level == GameLevel.Expert)
            speed = aispeed;
        if (distance > speed) 
        {
                p.x = o.x + speed / distance * dx;
                p.y = o.y + speed / distance * dy;
        }

        return new Vector2d(p.x, p.y);
    }

    int counter = 0;
    public void UpdateAIplayerPosition(float delta) 
    {
        if (++counter % (delta * 2) == 0)
        {
            if (lastState == State.Attack || lastState == State.Defense)
            {
                if(isAiNormalStateAllowed)
                    lastState = State.Normal;
            }
            else if (lastState == State.Wonder)
            {
                if(isAiDefenceStateAllowed)
                    lastState = State.Defense;
            }
            else if (AIHelper.isPuckInAISide())
            {
                if(isAiAttackStateAllowed)
                    lastState = State.Attack;
            }
            else if (!AIHelper.isPuckInAISide())
                if (!AIHelper.isOPPOPADDLEinFrontSide() && !AIHelper.isPuckInAISide() && AIHelper.isPuckAwayFromOPPADDLe())
                {
                    if(isAiWonderStateAllowed)
                        lastState = State.Wonder;
                }
                else
                {
                    if(isAiDefenceStateAllowed)
                        lastState = State.Defense;
                }
        }
    }

       
    float aispeed = AiSpeed;
    void adaptiveSpeed()
    {
        if(UpPlayerScore < DownPlayerScore)
            aispeed = 0.5f;
        else if(UpPlayerScore > DownPlayerScore)
            aispeed = 0.1f;
        else
            aispeed = 0.2f;
    }
    
    
    @Override
    public Point2d getAIMovement() 
    {           
        if(level == GameLevel.Easy)
            lastState = State.Normal;
        else
            UpdateAIplayerPosition(30);
        
        StatesHandler();
        
        if(level == GameLevel.Expert)
            adaptiveSpeed();
        
        return new Point2d(moveTo(ai.getLocation(), target));
    }
}
