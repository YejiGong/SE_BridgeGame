import java.util.*;

public class Player{
	private int score;
	private List<Card> cards;
	private List<BridgeCard> bridgecards;
	private int[] position;
	private boolean state;
	private String id;
	
	public Player(String id) {
		this.score=0;
		this.id=id;
		this.cards = new ArrayList<Card>();
		this.bridgecards = new ArrayList<BridgeCard>();
		this.position = new int[2];
		this.state=true; //if end:state will be false
	}
	
	public boolean move(String input, Map map, boolean backAvailable) {
		int[] originalPosition = new int[2];
		originalPosition[0]=this.position[0]; originalPosition[1]=this.position[1];
		List<Character> directions = new ArrayList<Character>();
		char before=' '; int BridgeCard=0;
		for(int i=0; i<input.length(); i++) {
			directions.clear();
			String curDirection = map.getPositionDirection(this.position[0], this.position[1]);
			boolean endFlag = map.isEnd(this.position[0], this.position[1]);
			if(curDirection.charAt(0)=='B') {
				directions.add('R');
			}else if(curDirection.charAt(0)=='b') {
				directions.add('L');
			}
			for(int j=2; j<curDirection.length(); j+=2) {
				directions.add(curDirection.charAt(j));
			}
			
			char curInput = input.toUpperCase().charAt(i);
			if(!endFlag) {
				before = map.getPositionDirection(this.position[0], this.position[1]).charAt(2);
			}
			if(directions.contains(curInput) || endFlag) {
				if(backAvailable==false
					&& ( (!endFlag && directions.size()>1 && curInput == directions.get(directions.size()-2))
					|| (endFlag && curInput == before) )) {
					this.position[0]=originalPosition[0]; this.position[1]=originalPosition[1];
					return false;
				}else {
					if( (!endFlag) || (endFlag && curInput == before)) {
						//if player arrived end, doesn't update position(accept back direction), fix position at end.
						if(curInput=='U') {
							this.position[1]+=1;
						}else if(curInput=='D') {
							this.position[1]-=1;
						}else if(curInput=='R') {
							this.position[0]+=1;
							if(curDirection.charAt(0)=='B') {
								this.position[0]+=1;
								BridgeCard+=1;
							}
						}else if(curInput=='L') {
							this.position[0]-=1;
							if(curDirection.charAt(0)=='b') {
								this.position[0]-=1;
								BridgeCard+=1;
							}
						}
					}
				}
			}else {
				this.position[0]=originalPosition[0]; this.position[1]=originalPosition[1];
				return false;
			}
		}
		for(int i=0; i<BridgeCard; i++) {
			this.addBridgeCard();
		}
		return true;
		}
	
	public String getID() {
		return this.id;
	}
	public int getScore() {
		return this.score;
	}
	
	public void setScore(int num) {
		this.score+=num;
	}
	public boolean getState() {
		return this.state;
	}
	
	public void setState(boolean state) {
		this.state=state;
	}
	
	public int[] getPosition() {
		return this.position;
	}
	
	public int getCardCount() {
		return this.cards.size();
	}
	
	public int getBridgeCardCount() {
		return this.bridgecards.size();
	}
	public void addCard(char type) {
		Card newCard = new Card(type);
		this.cards.add(newCard);
		setScore(newCard.getScore());
	}
	public void addBridgeCard() {
		BridgeCard newBridgeCard = new BridgeCard();
		this.bridgecards.add(newBridgeCard);
	}
	public void removeBridgeCard() {
		this.bridgecards.remove(0);
	}
}