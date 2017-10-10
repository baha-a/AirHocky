package rendering;

import java.awt.Color;
import java.awt.Font;
import javax.media.j3d.J3DGraphics2D;
import main.Const;


/////////////////////////////////////////////////////////////////////////////
///// 9:22pm 19-11-2014  -   B    ///////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
/// how use this :
//          Renderer._debuginfo.Names[2] = "Y";					
///         Renderer._debuginfo.Value[3] = p.y + "" ;				
/////////////////////////////////////////////////////////////////////////////


public class Debugger
{
    public final int Length = 10;
    public String[] Names = new String[Length] ;	
    public Object[] Values = new Object[Length];

    public void PrintInfo(J3DGraphics2D  g)
    {
        g.setColor(Color.BLACK);
        Font f = g.getFont();
        
        g.setFont(new Font("Times New Rome", Font.BOLD, 25));
        g.drawString("AI ( " + Const.UpPlayerScore + " )  VS  Human ( "+ Const.DownPlayerScore + " )", 550, 50);
        g.setFont(f);
        
        g.setColor(Color.YELLOW);
        g.drawString("Debug Mode ", 40, 50);

        for (int xpos = 70 , i = 0; i < Length; i++ , xpos += 20)
        {
            g.setColor(Color.red);
            if(Names[i] != null)
                    g.drawString(Names[i] + " : " , 50, xpos);

            g.setColor(Color.black);
            if(Values[i] != null)
                    g.drawString(Values[i].toString() , 150, xpos);
        }
        g.flush(false);
    }
}
