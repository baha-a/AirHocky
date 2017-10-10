package main;

import TesterWindow.TesterForm;
import concurrencyHelper.*;
import physics.*;
import rendering.Renderer;
import rendering.SoundEffect;
import static main.Const.*;

public class Control 
{	
    public Control() 
    {
        ObjectPhysics.renderer = new Renderer();
        
        op = new OpponentPaddlePhysics();
        ai = new AIPhysics();
        puck = new PuckPhysics();	
    }

    private void HeartGame() 
    {
        if(DEBUGMODE)
        {
                // this is debug mode
                //
            puck.Update();
            
            // wait for Loading game complete
            try { Thread.sleep(2000); } catch (InterruptedException e)  { }
           
            long time = System.currentTimeMillis();

            while(true)        
            {           
                if(pausegame && goNextFrame <= 0) 
                {
                    _debuginfo.Values[0] = "                        PAUSE";
                    try { Thread.sleep(100); } catch (InterruptedException e)  { }
                    continue;
                }
            
                frameCounter++;
                if((System.currentTimeMillis() - time)/1000 >= 1)
                {
                    _debuginfo.Values[0] = "                        fps = "+ frameCounter;
                    gametime++;
                    _debuginfo.Values[1] = "                        time = "+ gametime;
                    time = System.currentTimeMillis();
                    frameCounter = 0;
                }


                if(goNextFrame>0)
                    goNextFrame--;

                if(!freezeAiPaddel)
                {
                    ai.Update();
                }

                op.Update();

                if(!freezePuck)
                {
                    puck.Update();
                }		                    

                try { Thread.sleep(Const.snooze); } catch (InterruptedException e)  { }
            }
        }
        else
        {
            // this is normal mode
            //

            @SuppressWarnings("unused")
            Proccess OP = new Proccess(op);

            @SuppressWarnings("unused")
            Proccess AI = new Proccess(ai);		

            @SuppressWarnings("unused")
            Proccess PU = new Proccess(puck);
        }
    }
    private int frameCounter = 0; 
    private int gametime = 0;

     
    public static void AddGoal(boolean upplayer)
    {
        if(upplayer)
            DownPlayerScore++;
        else
            UpPlayerScore++;
        
        
        tf.updatespinners();
        
        puck.recenter();
        
        _debuginfo.Names[4]= "AI ";
        _debuginfo.Values[4]= UpPlayerScore;

        _debuginfo.Names[5] = "You";
        _debuginfo.Values[5]=DownPlayerScore;
    }
    
    static boolean b=false;
    public static void onClick()
    {
        //puck.putPuckInAICornerForTesting(b = !b);
    }

    public static void onKeyPress(char keyChar)
    {
        switch (Character.toLowerCase(keyChar)) 
        {	
            case 'q': UpPlayerScore++; break;
            case 'a': UpPlayerScore--; break;
                            
            case 'w': DownPlayerScore++; break;
            case 's': DownPlayerScore--; break;
            
            case 'g': EnableGoals = !EnableGoals; break;
                
            case '1': level=GameLevel.Easy; break;
            case '2': level=GameLevel.Hard; break;
            case '3': level=GameLevel.Expert; break;
                
           
            case 'f': puckFriction+=0.001f; break;
            case 'v': puckFriction-=0.001f; break;
                
            case '7': puck.putPuckInAICornerForTesting(true); break;
            case '9': puck.putPuckInAICornerForTesting(false); break;
                
            case 'x': tf.setVisible(true); break;
        }    
    }
    
    public static void main(String[] a) 
    {   
        SoundEffect.init(); 
        tf = new TesterForm();
        tf.setVisible(true);
        new Control().HeartGame();
    }
    static TesterForm tf;
}