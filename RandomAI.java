
import java.util.ArrayList;
import java.util.Random;

public class RandomAI
{
	public static final int PASS = -1;
	public static final int PUT  = 0;

	GameState state;
	private boolean[][] map;
	private int after;
	private Random rand;

	RandomAI()
	{
		map = new boolean[4][13];
		rand = new Random();
	}

	public void BuildState(GameState state, int player)
	{
		this.state = state;

		this.after = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				map[i][j] = state.CanPutCard(player, i, j);
				if(map[i][j]) ++after;
			}
		}
	}


	public ArrayList < Integer > GetDoing()
	{
		ArrayList< Integer > result = new ArrayList< Integer >();
		if(this.after == 0) {
			result.add(RandomAI.PASS);
			return(result);
		} else {
			result.add(RandomAI.PUT);
			int val = rand.nextInt(this.after);
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 13; j++) {
					if(map[i][j]) {
						if(val == 0) {
							result.add(i * 13 + j);
							return(result);
						}
						--val;
					}
				}
			}
		}
		return(null);
	}

}
