
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Card
{
	private Image imgReverse;

	public static final int SPADE = 0;
	public static final int DIAMOND = 1;
	public static final int CLUB = 2;
	public static final int HEART = 3;

	private int mark, number;
	private Image img;
	private int x, y, w, h;

	private boolean reverse;
	private boolean highlight;
	private boolean preup;

	public void SetReverseImage()
	{
		try {
			this.imgReverse = ImageIO.read(Card.class.getResourceAsStream("image/b1fv.png"));
		} catch(IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	Card(int mark, int number)
	{
		this.mark = mark;
		this.number = number;
		try {
			img = ImageIO.read(getClass().getResourceAsStream(String.format("image/%d.png",  mark + (number - 1) * 4 + 1)));

		} catch(IOException e) {
			System.out.println(e);
			System.exit(2);;
		}
		this.x = 0;
		this.y = 0;
		this.w = img.getWidth(null);
		this.h = img.getHeight(null);
		this.reverse = false;
		this.highlight = false;
		this.preup = false;
	}

	public int getMark()
	{
		return(this.mark);
	}

	public int getNumber()
	{
		return(this.number);
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

	public boolean getReverse()
	{
		return(this.reverse);
	}

	public void setReverse(boolean flag)
	{
		this.reverse = flag;
	}

	public void setLocate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public boolean getContains(int x, int y)
	{
		return(this.x < x && x < this.x + this.w && this.y < y && y < this.y + this.h);
	}

	public boolean getHighlight()
	{
		return(this.highlight);
	}

	public void setHighlight(boolean flag)
	{
		this.highlight = flag;
	}

	public void setPreup(boolean flag)
	{
		this.preup = flag;
	}

	public void draw(Graphics g)
	{
		if(this.reverse) g.drawImage(this.imgReverse, this.x, this.y, this.w, this.h, null);
		else if(this.preup) g.drawImage(this.img, this.x, this.y - 15, this.w, this.h, null);
		else g.drawImage(this.img, this.x, this.y, this.w, this.h, null);
		if(this.highlight) {
			g.setColor(Color.BLUE);
			if(preup) g.drawRect(this.x, this.y - 15, this.w - 1, this.h - 1);
			else g.drawRect(this.x, this.y, this.w - 1, this.h - 1);
		}
	}

	public boolean getPreup() {
		return(this.preup);
	}
}
