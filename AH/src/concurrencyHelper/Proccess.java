package concurrencyHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static main.Const.*;

public class Proccess
{
    public final ExecutorService exec = Executors.newCachedThreadPool();
    
    public worker physics;

    public boolean Terminate = false;
    public void finish() { Terminate = true; }

    public Proccess(worker physics)
    {
        this.physics = physics;
                           
        exec.execute(new Runnable() {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                {
                    Proccess.this.physics.Update();
                    try { Thread.sleep(SleepTimeForProcess); } catch (InterruptedException ex) { }
                }
            }
        });
    }
}