/* Model
 * データそのものの管理や変更処理を担当
*/


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class GameState implements IValueModel
{

	protected Vector<Listener> listener = new Vector<Listener>();


	// 残りパス回数
	private int[] pass = new int[4];

	// プレイヤーが所持するカード
	@SuppressWarnings("unchecked")
	private ArrayList< Integer >[] card = new ArrayList[4];

	// 盤面に置かれているカード
	private boolean[][] used = new boolean[4][13];


	/* 現在のプレイヤー */
	private int nowPlayer;

	private ArrayList< Integer > lose = new ArrayList< Integer >();
	private ArrayList< Integer > win = new ArrayList< Integer >();

	/* 現在のゲームID */
	private int gameId;

	/* 微妙に遅延させたいので */
	private int playerBuffer;

	/* 最初の描画はこれを true にする */
	public boolean first;

	/* AI のレベル */
	private int AIlevel;

	GameState()
	{
		this.AIlevel = 2;
	}

	public void setAIlevel(int val)
	{
		this.AIlevel = val;
	}

	public int getAIlevel()
	{
		return(this.AIlevel);
	}


	void Build()
	{
		this.gameId = new Random().nextInt();
		this.lose.clear();
		this.win.clear();
		this.nowPlayer = -1;
		for(int i = 0; i < 4; i++) {
			this.pass[i] = 3;
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				this.used[i][j] = false;
			}
		}
		this.SetCard();

		this.first = true;
		setValue(this);
		this.first = false;
	}


	void SetCard()
	{
		Random random = new Random();
		int[] cardIdx = new int[52];
		for(int i = 0; i < 52; i++) {
			cardIdx[i] = i;
		}
		for(int i = 51; i > 0; i--) {
			int k = (i != 1 ? random.nextInt(i - 1) : 0);
			int buff = cardIdx[i];
			cardIdx[i] = cardIdx[k];
			cardIdx[k] = buff;
		}
		for(int i = 0; i < 4; i++) {
			this.card[i] = new ArrayList< Integer >();
			for(int j = 0; j < 13; j++) {
				this.card[i].add(cardIdx[i * 13 + j]);
			}
			Collections.sort(this.card[i]);
		}
	}

	public void DistributeCard()
	{
		setValue(this);
	}

	/* mark: [0, 5) の範囲で連続して呼び出す */
	public void BuildFirstCard(int mark)
	{
		if(mark == 4) {
			this.nowPlayer = this.playerBuffer;
			setValue(this);
			return;
		}
		this.used[mark][6] = true;
		for(int j = 0; j < card[mark].size(); j++) {
			if(this.card[mark].get(j) % 13 == 6) {
				if(this.card[mark].get(j) / 13 == Card.DIAMOND) this.playerBuffer = mark;
				this.card[mark].remove(j--);
			}
		}
		setValue(this);
	}

	public boolean CanPutCard(int player, int mark, int number)
	{
		if(!card[player].contains(mark * 13 + number)) return(false);
		return(this.DFS_Check(mark, number,  true));
	}

	public void PutCard(int player, int mark, int number)
	{
		this.AddFieldCard(player, mark, number);
	}

	public boolean Pass(int player)
	{
		if(pass[player] == 0) return(false);
		--pass[player];
		return(true);
	}

	public boolean GameClear(int player)
	{
		if(card[player].isEmpty()) {
			this.win.add(player);
			setValue(this);
			return(true);
		}
		return(false);
	}

	public void GameOver(int player)
	{
		for(int i = 0; i < card[player].size(); i++) {
			int row = card[player].get(i);
			used[row / 13][row % 13] = true;
		}
		--pass[player];
		card[player].clear();
		lose.add(player);
		setValue(this);
	}

	private void AddFieldCard(int player, int mark, int number)
	{
		used[mark][number] = true;
		for(int i = 0; i < card[player].size(); i++) {
			int row = card[player].get(i);
			if(row % 13 == number && row / 13 == mark) {
				card[player].remove(i);
				return;
			}
		}
	}

	public boolean Nextplayer()
	{
		for(int i = 0; i < 4; i++) {
			nowPlayer = (nowPlayer + 1) % 4;
			if(!this.card[nowPlayer].isEmpty()) {
				setValue(this);
				return(true);
			}
		}
		return(false);
	}

	private boolean DFS_Check(int mark, int number, boolean first)
	{
		if(number == 6) return(true);
		if(!first && !used[mark][number]) return(false);
		if(number < 6) return(this.DFS_Check(mark, number + 1, false));
		return(this.DFS_Check(mark, number - 1,  false));
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

	public boolean isused(int mark, int number)
	{
		return(this.used[mark][number]);
	}

	public int getNowPlayer()
	{
		return(this.nowPlayer);
	}

	public ArrayList< Integer > getPlayerCard(int player)
	{
		return(this.card[player]);
	}

	public ArrayList< Integer > getlose()
	{
		return(this.lose);
	}

	public ArrayList< Integer > getwin()
	{
		return(this.win);
	}

	public int getPass(int player) {
		return(this.pass[player]);
	}

	public int getId()
	{
		return(this.gameId);
	}

}
