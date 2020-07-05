
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainApp extends JFrame
{
	private GameState state;
	private GameField panel;
	private MyPanel player;
	private EnemyPanel[] enemys;
	private Menu menu;

	final private int pos[][] =
		{
				{0, 125, 125, 575},
				{0, 0, 1105, 125},
				{1105, 0, 130, 560}
		};


	MainApp(String title)
	{

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1235, 820);
		this.setPreferredSize(new Dimension(1235, 720));
		this.setLayout(null);
		this.setResizable(false);

		this.state = new GameState();
		this.panel = new GameField(this.state);

		this.player = new MyPanel(this.state, this.panel);

		this.panel.setBounds(125, 125, 980, 435);
		this.player.setBounds(125, 560, 1110, 140);

		this.menu = new Menu(this.state);
		this.setJMenuBar(this.menu);

		this.enemys = new EnemyPanel[4];
		for(int i = 0; i < 3; i++) {
			this.enemys[i] = new EnemyPanel(this.state, i + 1);
			this.enemys[i].setBounds(pos[i][0], pos[i][1], pos[i][2], pos[i][3]);
			this.add(this.enemys[i]);
		}

		this.state.Build();

		this.add(this.panel);
		this.add(this.player);
	}


	public static void main(String[] args)
	{
		// TODO 自動生成されたメソッド・スタブ
		new MainApp("7 並べ").setVisible(true);
	}
}
