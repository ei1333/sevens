
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

public class GameField extends JPanel implements IValueModel
{


	@SuppressWarnings("rawtypes")
	protected Vector listener = new Vector();


	// カードの画像
	private Card[][] img = new Card[4][13];

	private boolean[][] tapped = new boolean[4][13];

 	private int[] x = new int[13];
	private int[] y = new int[4];

	private int preUp;

	private static final int SHIFTLeft = 22;
	private static final int SHIFTHead = 25;
	private static final int Width = 72;
	private static final int Height = 96;

	private GameState information;
	private GameState model;

	private IValueModel.Listener modelListener =
	new IValueModel.Listener()
	{
		@Override
		public void valueChanged(IValueModel.Event e)
		{
			repaintView((GameState)e.getValue());
		}
	};

	private CommonImage images;

	GameField(GameState state)
	{
		this.preUp = -1;

		this.model = state;
		try {
			images = new CommonImage();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if(state instanceof IValueModel) {
			((IValueModel)state).addListener(this.modelListener);
		}

		this.setLayout(null);

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				this.tapped[i][j] = false;
				img[i][j] = new Card(i, j + 1);
				img[i][j].setLocate(j * GameField.Width + GameField.SHIFTLeft, i * GameField.Height + GameField.SHIFTHead);
			}
		}
		for(int i = 0; i < 4; i++) {
			y[i] = i * GameField.Height + GameField.SHIFTLeft;
		}
		for(int i = 0; i < 13; i++) {
			x[i] = i * GameField.Width + GameField.SHIFTLeft;
		}

		this.addMouseListener(new OnClick());
		this.addMouseMotionListener(new OnMove());
	}


	public void repaintView(GameState info)
	{
		this.information = info;
		this.repaint();
	}


	public void paintComponent(Graphics g)
	{
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if(this.information != null) {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 13; j++) {
					if(this.information.isused(i, j)) {
						img[i][j].draw(g);
					} else if(this.information.getNowPlayer() == 0 && this.information.CanPutCard(0, i, j)) {
						g.drawImage(images.getImg(4, 12), img[i][j].getX(), img[i][j].getY(), img[i][j].getWidth(), img[i][j].getHeight(), this);
						if(this.tapped[i][j]) {
							g.setColor(Color.BLUE);
							g.drawRect(img[i][j].getX(), img[i][j].getY(), img[i][j].getWidth() - 1, img[i][j].getHeight() - 1);
						}
					}
				}
			}
		}
	}

	class OnClick extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(information == null) return;
			if(information.getNowPlayer() != 0) return;

			int mx = e.getX();
			int my = e.getY();

			ArrayList< Integer > mycard = information.getPlayerCard(0);
			for(int i = 0; i < mycard.size(); i++) {
				int card = mycard.get(i);
				if(img[card / 13][card % 13].getContains(mx,  my)) {
					if(information.CanPutCard(0, card / 13, card % 13)) {
						model.PutCard(0, card / 13, card % 13);
						setValue(-1);
						return;
					}
				}
			}
		}
	}

	class OnMove extends MouseMotionAdapter
	{
		public void mouseMoved(MouseEvent e)
		{
			if(information == null) return;
			if(information.getNowPlayer() != 0) return;

			int mx = e.getX();
			int my = e.getY();
			ArrayList< Integer > mycard = information.getPlayerCard(0);
			int mark = -1, number = -1;
			Card c = null;
			for(int i = 0; i < mycard.size(); i++) {
				int card = mycard.get(i);
				if(img[card / 13][card % 13].getContains(mx,  my)) {
					c = img[card / 13][card % 13];
					mark = card / 13;
					number = card % 13;
				}
			}
			if(c != null && information.CanPutCard(0, mark, number)) {
				if(mark * 13 + number != preUp) {
					if(preUp != -1) {
						tapped[preUp / 13][preUp % 13] = false;
						setValue(preUp);
					}
					preUp = mark * 13 + number;
					setValue(preUp);
					tapped[mark][number] = true;
					repaint();
				}
			} else {
				if(preUp != -1) {
					tapped[preUp / 13][preUp % 13] = false;
					setValue(preUp);
					repaint();
				}
				preUp = -1;
			}
		}
	}


	@Override
	public void setValue(Object obj)
	{
		this.findValueChanged(obj);
	}

	@Override
	public Object getValue()
	{
		return(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addListener(IValueModel.Listener l)
	{
		this.listener.add(l);
	}

	@Override
	public void removeListener(IValueModel.Listener l)
	{
		this.listener.removeElement(l);
	}

	protected void findValueChanged(Object obj)
	{
		int size = this.listener.size();
		IValueModel.Event e = new IValueModel.Event(this, obj);
		for(int i = 0; i < size; i++) {
			((IValueModel.Listener) this.listener.elementAt(i)).valueChanged(e);
		}
	}



}
