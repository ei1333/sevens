
import java.util.ArrayList;
import java.util.Random;

public class GrundyAI
{

	GameState state;
	private boolean[][] map;
	private int after, player;
	private Random rand;
	ArrayList< Integer > result;

	GrundyAI()
	{
		map = new boolean[4][13];
		rand = new Random();
	}

	private boolean endCard(final int mark, int number, int limit, boolean ok)
	{
		if(limit == -1) return(false);
		if(number < 0 || number > 12) return(true);
		if(ok) return(number < 6 ? endCard(mark, number - 1, limit, false) : endCard(mark, number + 1, limit, false));
		int ret = this.state.isused(mark, number) ? 0 : 1;
		return(number < 6 ? endCard(mark, number - 1, limit - ret, ok) : endCard(mark, number + 1, limit - ret, ok));
	}

	private boolean back(int limit)
	{
		ArrayList< Integer > list = new ArrayList< Integer >();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 6; j++) {
				if(map[i][j]) {
					if(endCard(i, j, limit, true)) {
						list.add(i * 13 + j);
					}
					break;
				}
			}
			for(int j = 12; j > 6; j--) {
				if(map[i][j]) {
					if(endCard(i, j, limit, true)){
						list.add(i * 13 + j);
					}
					break;
				}
			}
		}
		if(list.isEmpty()) return(false);
		result.add(list.get(rand.nextInt(list.size())));
		return(true);
	}


	public void BuildState(GameState state, int player)
	{
		this.state = state;
		this.player = player;
		this.after = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				map[i][j] = state.CanPutCard(player, i, j);
				if(map[i][j]) ++after;
			}
		}
	}

	private boolean ctne(int limit)
	{
		ArrayList< Integer > mycard = state.getPlayerCard(player);
		ArrayList< Integer > list = new ArrayList< Integer > ();
		for(int i = 0; i < 4; i++) {
			for(int j = 7; j < 12 - limit; j++) {
				if(map[i][j]) {
					if(mycard.contains(i * 13 + j + limit + 1)) {
						list.add(i * 13 + j);
					}
					break;
				}
			}
			for(int j = 5; j > limit; j--) {
				if(map[i][j]) {
					if(mycard.contains(i * 13 + j - limit - 1)) {
						list.add(i * 13 + j);
					}
					break;
				}
			}
		}
		if(list.isEmpty()) return(false);
		result.add(list.get(rand.nextInt(list.size())));
		return(true);
	}

	public ArrayList < Integer > GetDoing()
	{
		result = new ArrayList< Integer >();
		if(this.after == 0) {
			result.add(RandomAI.PASS);
			return(result);
		}
		result.add(RandomAI.PUT);
		if(back(0)) return(result);
		if(ctne(0)) return(result);
		if(ctne(1)) return(result);
		if(back(1)) return(result);
		if(state.getPass(player) > 0) {
			result.clear();
			result.add(RandomAI.PASS);
			return(result);
		}
		if(back(2)) return(result);
		if(ctne(2)) return(result);
		if(back(3)) return(result);
		if(ctne(4)) return(result);
		if(back(4)) return(result);
		if(back(5)) return(result);
		if(back(6)) return(result);
		result.clear();
		result.add(RandomAI.PASS);
		return(result);
	}

}
