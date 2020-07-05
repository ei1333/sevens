
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;


public class RoundButton {
    private Image image, disableimage;
    private boolean highlight;
    private boolean active;
    private boolean able;
    private int x, y, w, h;

    public RoundButton(Image img, Image disableimg)
    {
        this.image = img;
        this.disableimage = disableimg;
        this.x = 0;
        this.y = 0;
        this.w = this.image.getWidth(null);
        this.h = this.image.getHeight(null);
        this.highlight = false;
        this.active = false;
        this.able = true;
    }

    public int getX()
    {
        return(this.x);
    }

    public int getY()
    {
        return(this.y);
    }

    public int getWidth()
    {
        return(this.w);
    }

    public int getHeight()
    {
        return(this.h);
    }

    public void draw(Graphics g)
    {
        if(!this.active) return;
        if(this.able) g.drawImage(this.image, this.x, this.y, this.w, this.h, null);
        else g.drawImage(this.disableimage, this.x, this.y, this.w, this.h, null);
        if(this.highlight) {
            Graphics2D gg = (Graphics2D) g;
            gg.setColor(Color.YELLOW);
            gg.setStroke(new BasicStroke(3.0f));
            Ellipse2D eclipse = new Ellipse2D.Double(this.x, this.y, 100, 100);
            gg.draw(eclipse);
        }
    }

    public void locate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public boolean contains(int x, int y)
    { /* 要修正 なんせ四角形で判定してるから(震え声)*/
        return(this.active && this.x < x && x < this.x + this.w && this.y < y && y < this.y + this.h);
    }
    public boolean getHighlight()
    {
        return(this.highlight);
    }
    public void setHighlight(boolean f)
    {
        this.highlight = f;
    }
    public void setActive(boolean f)
    {
        this.active = f;
    }
    public boolean getActive()
    {
        return(this.active);
    }
    public void setAble(boolean f)
    {
    	this.able = f;
    }
    public boolean getAble()
    {
    	return(this.able);
    }
}
