public class Card{
	private char type;
	private int score;
	public Card(char type) {
		this.type=type;
		if(this.type=='P') {
			this.score=1;
		}else if(this.type=='H') {
			this.score=2;
		}else if(this.type=='S') {
			this.score=3;
		}
	}
	public int getScore() {
		return this.score;
	}
}