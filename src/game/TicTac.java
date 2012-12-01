package game;


public class TicTac {

	/*public static void main(String[] args) {
		TicTac ticTac = new TicTac();
		System.out.println(ticTac);
		Scanner input = new Scanner(System.in);
		int playerMove = -1;
		int compMove = 0;
		while (true) {
			if(ticTac.checkForWin(2))
				break;
			System.out.print(">> ");
			playerMove = Integer.parseInt(input.next());

			if (ticTac.play(1, playerMove)) {
				compMove = ticTac.getNextMove(2);
				if (compMove == -1)
					break;
				else {
					ticTac.play(2, compMove);
					System.out.println(ticTac);
				}
			}
		}
		System.out.println(ticTac);
		input.close();
	}
*/
	private int[] matrix = null;
	private MoveSuggester ms = null;

	public TicTac() {
		matrix = new int[9];
		ms = new MoveSuggester();
	}
	
	public boolean checkForWin(int player){
		return ms.isWinner(matrix, player);
	}

	public boolean play(int player, int pos) {
		if (!(player == 1 || player == 2))
			return false;

		if (pos < 0 || pos >= 9)
			return false;

		if (matrix[pos] != 0)
			return false;

		matrix[pos] = player;
		return true;
	}

	public int getNextMove(int player) {
		return ms.nextMove(matrix, player);
	}
	
	public int[] getGameMatrix(){
		return matrix;
	}
	
	public int getGameStatus(){
		return ms.isOver(matrix);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		char ch = 'x';
		sb.append("\n-------\n");
		for (int i = 0; i < 3; i++) {
			sb.append("|");
			for (int j = 0; j < 3; j++) {
				if (matrix[i * 3 + j] == 1)
					ch = 'o';
				else if(matrix[i * 3 + j] == 2)
					ch = 'x';
				else
					ch=' ';
				sb.append(ch);
				sb.append("|");
			}
			sb.append("\n-------\n");

		}
		return sb.toString();
	}
	
	public void restart(){
			matrix = new int[9];
	}
	
	public void setCompPlayer(int num){
		ms.setCompPlayer(num);
	}

}
