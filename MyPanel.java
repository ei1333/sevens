
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class MyPanel extends JPanel
{

	private RoundButton[] PassButton = new RoundButton[4];
	private RoundButton StartButton;
    static private Card[][] img = new Card[4][13];
    private CommonImage cimg;

    private GameState information;

    private GameState model;

    private CommonImage images;

	private IValueModel.Listener modelListener =
	new IValueModel.Listener()
	{
		@Override
		public void valueChanged(IValueModel.Event e)
		{
			repaintView((GameState)e.getValue());
		}
	};


	RandomAI AI = new RandomAI();
	GrundyAI GI = new GrundyAI();

	private IValueModel.Listener preupListener =
	new IValueModel.Listener() {

		@Override
		public void valueChanged(IValueModel.Event e) {
			// TODO 自動生成されたメソッド・スタブ
			int num = (int)e.getValue();
			if(num == -1) {
				PassButton[3 - model.getPass(0)].setAble(false);
				if(model.GameClear(0)) PassButton[3 - model.getPass(0)].setActive(false);
				if(!model.Nextplayer()) return;
				new NextTernWorker().execute();
			} else {
				img[num / 13][num % 13].setHighlight(!img[num / 13][num % 13].getHighlight());
				img[num / 13][num % 13].setPreup(!img[num / 13][num % 13].getPreup());
			}
			repaint();
		}
	};

	int mx, my;

	MyPanel(GameState state, GameField field)
	{
		this.mx = this.my = -1;
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

		if(field instanceof IValueModel) {
			((IValueModel)field).addListener(this.preupListener);
		}

		this.GetImage();
	}


	void GetImage()
	{
		try {
			cimg = new CommonImage();
		} catch(IOException e)
		{
			e.getStackTrace();
		}

		this.setLayout(null);

        StartButton = new RoundButton(cimg.getImg(4, 3), cimg.getImg(4, 4));
        PassButton[0] = new RoundButton(cimg.getImg(4, 5), cimg.getImg(4, 9));
        PassButton[1] = new RoundButton(cimg.getImg(4, 6), cimg.getImg(4, 10));
        PassButton[2] = new RoundButton(cimg.getImg(4, 7), cimg.getImg(4, 11));
        PassButton[3] = new RoundButton(cimg.getImg(4, 8), cimg.getImg(4, 8));

        StartButton.locate(950, 20);
        for(int i = 0 ; i < 4 ; i++) {
            PassButton[i].locate(950, 20);
        }

        this.addMouseMotionListener(new ButtonOnMove());
        this.addMouseListener(new ButtonOnClick());
        this.addMouseListener(new OnClick());
        this.addMouseMotionListener(new OnMove());


        for(int i = 0 ; i < 4 ; i++ ){
            for(int j = 0 ; j < 13 ; j++){
                img[i][j] = new Card(i, j + 1);
            }
        }
	}

	void Build()
	{
        for(int i = 0 ; i < 4 ; i++) {
        	PassButton[i].setActive(false);
        }
        for(int i = 0 ; i < 4 ; i++) {
        	for(int j = 0 ; j < 13 ; j++) {
        		img[i][j].setHighlight(false);
        		img[i][j].setPreup(false);
        	}
        }
        StartButton.setActive(true);
	}

	public void repaintView(GameState info)
	{

		if(info.first) {
			Build();
			this.information = null;
		}
		else{
			this.information = info;
		}
		this.repaint();
	}

	public void paintComponent(Graphics g)
	{
	   super.paintComponent(g);

	   StartButton.draw(g);
	   for(int i = 0; i < 4; i++) {
		   PassButton[i].draw(g);
	   }

	   if(this.information == null) return;

	   if(this.information.getlose().contains(0)){
		   g.drawImage(images.getImg(5, 3 - this.information.getlose().indexOf(0)), 300, 40, images.getImg(5, 0).getWidth(this), images.getImg(5, 0).getHeight(this), this);
	   } else if(this.information.getwin().contains(0)){
	       g.drawImage(images.getImg(5, this.information.getwin().indexOf(0)), 300, 40, images.getImg(5, 0).getWidth(this), images.getImg(5, 0).getHeight(this), this);
	   } else {
		   if(this.information.getNowPlayer() == 0) {
			   g.setColor(Color.RED);
			   g.fillOval(900, 50, 30, 30);
		   }
		   ArrayList< Integer > mycard = this.information.getPlayerCard(0);
		   for(int i = 0 ; i < mycard.size(); i++) {
			   int card = mycard.get(i);
			   img[card / 13][card % 13].setLocate(i * 60 + 20, 20);
			   img[card / 13][card % 13].draw(g);
		   }
	   }

	}


	class OnClick extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(information == null) return;
			if(model.getNowPlayer() != 0) return;

			int mx = e.getX();
			int my = e.getY();

			ArrayList< Integer > mycard = model.getPlayerCard(0);

			for(int i = 0; i < mycard.size(); i++) {
				int card = mycard.get(i);
				if(img[card / 13][card % 13].getContains(mx, my)) {
					if(model.CanPutCard(0, card / 13, card % 13)) {

						model.PutCard(0, card / 13, card % 13);
						PassButton[3 - model.getPass(0)].setAble(false);
						if(model.GameClear(0)) PassButton[3 - model.getPass(0)].setActive(false);
						repaint();
						if(!model.Nextplayer()) return;
						if(model.getNowPlayer() != 0) new NextTernWorker().execute();
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

			Card c = null;
			int mark = -1, number = -1;
			ArrayList < Integer > mycard = information.getPlayerCard(0);
			for(int i = 0; i < mycard.size(); i++) {
				int card = mycard.get(i);
				img[card / 13][card % 13].setHighlight(false);
				img[card / 13][card % 13].setPreup(false);
				if(img[card / 13][card % 13].getContains(mx, my)) {
					c = img[card / 13][card % 13];
					mark = card / 13;
					number = card % 13;
				}
			}

			if(c != null) {
				if(information.CanPutCard(0, mark, number)) {
					c.setPreup(true);
				}
				c.setHighlight(true);
				repaint();
			}
		}
	}
	class ButtonOnMove extends MouseMotionAdapter
	{
		public void mouseMoved(MouseEvent e)
		{
			mx = e.getX();
			my = e.getY();

			RoundButton c = null;

			for(int i = 0; i < 4; i++) {
				PassButton[i].setHighlight(false);
				if(PassButton[i].contains(mx, my) && PassButton[i].getAble()) c = PassButton[i];
			}
			StartButton.setHighlight(false);

			if(StartButton.contains(mx, my)) {
				c = StartButton;
			}
			if(c != null) {
				c.setHighlight(true);
			}
			repaint();
		}
	}
	class ButtonOnClick extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			int mmx = e.getX();
			int mmy = e.getY();

			for(int i = 0; i < 4; i++) {
				if(PassButton[i].contains(mmx,  mmy) && PassButton[i].getAble()) {
					PassButton[i].setActive(false);
					boolean flag = false;
					if(!model.Pass(0)) {
						model.GameOver(0);
						flag = true;
					}
					else PassButton[i + 1].setActive(true);

					if(!model.Nextplayer()) {
						repaint();
						return;
					}
					if(model.getNowPlayer() != 0) {
						new NextTernWorker().execute();
						if(!flag) PassButton[i + 1].setAble(false);
					}
					repaint();
					return;
				}
			}

			if(StartButton.contains(mmx,  mmy)) {
				StartButton.setActive(false);
				PassButton[0].setActive(true);
				PassButton[0].setAble(false);
				model.DistributeCard();
				repaint();
				new DistributeWorker(0).execute();
			}
		}
	}

	class DistributeWorker extends SwingWorker< Object, Object >
	{
		private int mark, gameId;

		DistributeWorker(int mark)
		{
				this.mark = mark;
				this.gameId = model.getId();
		}

		@Override
		protected Object doInBackground()
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			// TODO 自動生成されたメソッド・スタブ
			return(null);
		}

		@Override
		protected void done()
		{
			if(model.getId() != this.gameId) return; // new game
			model.BuildFirstCard(this.mark);
			if(this.mark == 4) { /* ゲームの開始 */
				if(model.getNowPlayer() != 0) {
					new NextTernWorker().execute();
				} else {
					PassButton[3 - model.getPass(0)].setAble(true);
					repaint();
				}
			} else {
				new DistributeWorker(mark + 1).execute();
			}
		}
	}


	class NextTernWorker extends SwingWorker< Object, Object >
	{
		private int GameId;
		NextTernWorker()
		{
			this.GameId = model.getId();
		}
		@Override
		protected Object doInBackground()
		{
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			// TODO 自動生成されたメソッド・スタブ
			return(null);
		}

		@Override
		protected void done()
		{
			if(model.getId() == this.GameId) DoDo();
		}
	}

	public void DoDo()
	{

		int player = model.getNowPlayer();
		ArrayList< Integer > action;
		if(model.getAIlevel() == 1) {
			AI.BuildState(model, player);
			action = AI.GetDoing();
		} else {
			GI.BuildState(model, player);
			action = GI.GetDoing();
		}
		if(action.get(0) == RandomAI.PASS) {
			if(!model.Pass(player)) model.GameOver(player);
		} else if(action.get(0) == RandomAI.PUT) {
			int val = action.get(1);
			model.PutCard(player, val / 13, val % 13);
			model.GameClear(player);
		}
		if(!model.Nextplayer()) { /* ゲーム終了 */
			return;
		} else if(model.getNowPlayer() == 0) { /* 自分のターン */
			PassButton[3 - model.getPass(0)].setAble(true);
			PassButton[3 - model.getPass(0)].setHighlight(PassButton[3 - model.getPass(0)].contains(mx, my));
			repaint();
			return;
		} else {
			new NextTernWorker().execute();
		}
	}
}

