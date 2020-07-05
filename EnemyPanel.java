
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class EnemyPanel extends JPanel
{
	private int player;
	private GameState information;
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

	public void repaintView(GameState info)
	{
		if(info.first) this.information = null;
		else this.information = info;
		this.repaint();
	}

	public EnemyPanel(GameState state, int player)
	{
		try {
			images = new CommonImage();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if(state instanceof IValueModel) {
			((IValueModel)state).addListener(this.modelListener);
		}
		this.player = player;
	}

	public void paintComponent(Graphics g)
	{
		int x = this.player == 2 ? 400 : (this.player == 1 ? 20 : 22);
		int y = this.player == 2 ? 30 : (this.player == 1 ? 80 : 120);
		int base = this.player == 2 ? 0 : 4;
		Image result = null;

		super.paintComponent(g);
		if(this.information == null) return;

		if(this.information.getlose().contains(this.player)) {
			result = images.getImg(5, (base + 3) - this.information.getlose().indexOf(this.player));
  		} else if(this.information.getwin().contains(this.player)) {
			result = images.getImg(5, base + this.information.getwin().indexOf(this.player));
  		}
		if(result != null) {
			g.drawImage(result, x, y, result.getWidth(this), result.getHeight(this), this);
		} else {
			if(this.information.getNowPlayer() == this.player) {
				g.setColor(Color.RED);

				if(this.player == 1) g.fillOval(45, 505, 30, 30);
				if(this.player == 2) g.fillOval(205, 65, 30, 30);
				if(this.player == 3) g.fillOval(45, 55, 30, 30);
			}
			ArrayList< Integer > mycard = this.information.getPlayerCard(this.player);
			for(int i = 0; i < mycard.size(); i++) {
				Image img = images.getImg(4, player == 2 ? 1 : 2);
				int height = img.getHeight(this);
				int width = img.getWidth(this);

                if(this.player == 1) g.drawImage(img, 12, i * 32 + 10, width, height, this);
                if(this.player == 2) g.drawImage(img, this.getWidth() - i * 60 - 100, 12, width, height, this);
                if(this.player == 3) g.drawImage(img, 18, this.getHeight() - i * 32 - 97, width, height, this);
			}
			g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN, 20));
            if(this.player == 1) g.drawString(String.format("PASS %d", this.information.getPass(this.player)), 25, 500);
            if(this.player == 2) g.drawString(String.format("PASS %d", this.information.getPass(this.player)), 180, 60);
            if(this.player == 3) g.drawString(String.format("PASS %d", this.information.getPass(this.player)), 30, 50);
		}
	}
}
