
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Menu extends JMenuBar
{
    private GameState model;

	public Menu(GameState state)
	{
		this.model = state;

        JMenu menu1 = new JMenu("ゲーム");
        JMenu menu2 = new JMenu("ヘルプ");

        add(menu1);
        add(menu2);

        JMenuItem item1_1 = new JMenuItem("新しいゲーム");
        // JMenuItem item1_2 = new JMenuItem("成績表");
        JMenuItem item1_3 = new JMenuItem("オプション");
        JMenuItem item1_4 = new JMenuItem("終了");

        menu1.add(item1_1);
        menu1.addSeparator();
        //menu1.add(item1_2);
        menu1.add(item1_3);
        menu1.addSeparator();
        menu1.add(item1_4);

        item1_1.addActionListener(new NewGame());
       // item1_2.addActionListener(new FUGA()); 未実装
        item1_3.addActionListener(new HOGE());
        item1_4.addActionListener(new Exit());
        JMenuItem item2_1 = new JMenuItem("ヘルプ");
        JMenuItem item2_2 = new JMenuItem("バージョン情報");
        menu2.add(item2_1);
        menu2.add(item2_2);

        item2_1.addActionListener(new Help());
        item2_2.addActionListener(new Virsion());
    }


    class FUGA implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            new Statistics();
        }
    }

    class HOGE implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            new Option();
        }
    }

    class NewGame implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
        	model.Build();
        }
    }

    class Virsion implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO 自動生成されたメソッド・スタブ
            String msg = "最終更新日2016/10/10";
            JOptionPane.showMessageDialog(null, msg,"七並べ のバージョン情報",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class Help implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO 自動生成されたメソッド・スタブ

            String msg = ""
            		+ "1. ゲームを始めるには画面右下の Start か\n「ゲーム」から新規作成をクリックしてください\n"
            		+ "2. 「ゲーム」からオプションで難易度設定が可能です\n";

            JOptionPane.showMessageDialog(null, msg, "ヘルプ", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class Statistics extends JDialog implements ActionListener
    {
    	Statistics()
    	{
    		this.setSize(400, 400);
    		this.setModal(true);
    		this.setLayout(null);
    		this.setLocationRelativeTo(null);
    		this.setTitle("成績表");

    		JButton okbtn = new JButton("OK");
    		okbtn.setBounds(140, 260, 100, 30);
    		okbtn.setActionCommand("OK");
    		okbtn.addActionListener(this);

    		JButton rtbtn = new JButton("リセット");
    		rtbtn.setBounds(240, 200, 100, 30);
    		rtbtn.setActionCommand("RS");
    		rtbtn.addActionListener(this);

    		this.add(okbtn);
    		this.add(rtbtn);


            JLabel label1 = new JLabel("成績表");
            label1.setBounds(160, 10, 125, 23);

            JLabel label2 = new JLabel("Easy");
            label2.setBounds(80, 40, 125, 23);

            JLabel label3 = new JLabel("Medium");
            label3.setBounds(180, 40, 125, 23);

            JLabel label4 = new JLabel("Hard");
            label4.setBounds(280, 40, 125, 23);

            for(int i = 1; i <= 4; i++) {
            	JLabel label = new JLabel(i + "位");
            	label.setBounds(10, 40 + i * 30, 125, 23);
            	this.add(label);
            	for(int j = 1; j <= 3; j++) {
            		JLabel ptr = new JLabel(i == 3 && j == 1 ? "1" : "0");
            		ptr.setHorizontalAlignment(JLabel.TRAILING);
            		ptr.setBounds(80 + (j - 1) * 100, 40 + i * 30, 50, 23);
            		this.add(ptr);
            	}
            }

            this.add(label1);
            this.add(label2);
            this.add(label3);
            this.add(label4);


    		this.setVisible(true);

    	}

		@Override
		public void actionPerformed(ActionEvent e)
		{
            String s = e.getActionCommand();
            if(s == "OK") {
                this.setVisible(false);
             } else if(s == "RS") {

             }
		}
    }

    class Option extends JDialog implements ActionListener
    {
        private JRadioButton rb1, rb2, rb3;
        protected int level;

        Option()
        {

        	level = model.getAIlevel();

            this.setSize(400, 360);
            this.setModal(true);
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.setLayout(null);
            this.setLocationRelativeTo(null);
            this.setTitle("オプション");

            JButton okbtn = new JButton("OK");
            okbtn.setBounds(70, 230, 100, 30);
            okbtn.setActionCommand("OK");
            okbtn.addActionListener(this);

            JButton nobtn = new JButton("キャンセル");
            nobtn.setBounds(210, 230, 100, 30);
            nobtn.setActionCommand("NO");
            nobtn.addActionListener(this);

            JLabel label1 = new JLabel("難易度");
            label1.setBounds(10, 10, 125, 23);

            popsize pop = new popsize();
            rb1 = new JRadioButton("Easy", model.getAIlevel() == 1);
            rb1.setBounds(15, 40, 125, 23);
            rb2 = new JRadioButton("Medium", model.getAIlevel() == 2);
            rb2.setBounds(15, 80, 125, 23);
            rb3 = new JRadioButton("Hard", model.getAIlevel() == 3);
            rb3.setBounds(15, 120, 125, 23);
            rb1.addChangeListener(pop);
            rb2.addChangeListener(pop);


            ButtonGroup bg = new ButtonGroup();
            bg.add(rb1);
            bg.add(rb2);
            bg.add(rb3);

            this.add(okbtn);
            this.add(nobtn);
            this.add(label1);
            this.add(rb1);
            this.add(rb2);
            this.add(rb3);

            this.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String s = e.getActionCommand();
            if(s == "OK") {
                this.setVisible(false);
                if(model.getAIlevel() != level) {
                	model.setAIlevel(level);
                	model.Build();
                }
            } else if(s == "NO") {
                this.setVisible(false);
            }
        }
        class popsize implements ChangeListener
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                // TODO 自動生成されたメソッド・スタブ
                if(rb1.isSelected()) level = 1;
                else if(rb2.isSelected()) level = 2;
                else level = 3;
            }
        }
    }

    class Exit implements ActionListener
    {
      public void actionPerformed(ActionEvent e)
      {
        System.exit(0);
      }
    }

}
