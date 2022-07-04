import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Random;

public class BridgeGame{
	private Player[] p;
	private Player currentPlayer;
	private int curPlayerNum;
	private int wholePlayerNum;
	private int curPlayerOrder;
	private Map map;
	private UIGenerator UI;
	private Dice dice;
	
	class Dice{
		private int result;
		public int getDiceResult() {
			Random rand = new Random();
			result = rand.nextInt(6)+1;
			return result;
		}
	}
	
	public void setPlayerNum(int num) {
		this.curPlayerNum = num;
		this.wholePlayerNum = num;
		this.curPlayerOrder=-1;
	}
	
	public void playGame() {
		String str;
		this.UI.printPlayerInfo(this.p);
		this.UI.printMap(this.map, this.p); //ui : player, mapCells
		selectPlayer();
		str = "현재 플레이어는"+this.currentPlayer.getID()+"입니다";
		this.UI.printStr(str); //현재 플레이어 정보
		boolean moveSelect = true;
		if(this.currentPlayer.getBridgeCardCount()>0) {
			moveSelect = selectMove();
		}
		if(moveSelect) {
			int result = this.dice.getDiceResult();
			str = "주사위 던지기 결과입니다: " + Integer.toString(result);
			this.UI.printStr(str);
			str = "플레이어 " + this.currentPlayer.getID()+ "의 다리 카드 개수입니다: " + this.currentPlayer.getBridgeCardCount();
			this.UI.printStr(str);
			result -= this.currentPlayer.getBridgeCardCount();
			str = "이동가능한 거리입니다: " + Integer.toString(result);
			this.UI.printStr(str);
			if(result > 0) {
				String input;
				boolean moveAvailable = true;
				do {
					input = inputMove(result);
					boolean backAvailable = true;
					if(this.wholePlayerNum>this.curPlayerNum) {
						backAvailable = false;
					}
					moveAvailable = this.currentPlayer.move(input, this.map, backAvailable);
					if(moveAvailable==false) {
						this.UI.printStr("잘못된 이동경로입니다.");
					}
				}while(moveAvailable == false);
				this.UI.printMap(this.map, this.p);
				this.UI.printStr("이동하였습니다.");
			}
			checkendGame();
		}else {
			this.currentPlayer.removeBridgeCard();
		}
	}
	
	public void checkendGame() {
		int[] position = this.currentPlayer.getPosition();
		char cellType = map.getCellType(position[0], position[1]);
		if(cellType=='H'||(cellType=='S'&&!this.map.isStart(position[0], position[1]))||cellType=='P') {
			this.currentPlayer.addCard(cellType);
			this.UI.printStr(cellType+" 카드를 획득하였습니다.");
		}
		if(this.map.isEnd(position[0], position[1])) {
			this.curPlayerNum-=1;
			this.currentPlayer.setState(false);
			int diff = this.wholePlayerNum - this.curPlayerNum;
			if(diff==1) {
				this.currentPlayer.setScore(7);
			}else if (diff==2) {
				this.currentPlayer.setScore(3);
			}else if (diff==3) {
				this.currentPlayer.setScore(1);
			}
			this.UI.printStr("플레이어 "+this.currentPlayer.getID()+"가 도착 위치에 도착했습니다.");
			this.UI.printResult(this.currentPlayer); //종료한 플레이어 점수
		}
	}
	public void selectPlayer() {
		int order = (this.curPlayerOrder+1)%(this.wholePlayerNum);
		while(true) {
			if(this.p[order].getState()==true) {
				this.curPlayerOrder=order;
				this.currentPlayer=this.p[this.curPlayerOrder];
				break;
			}else {
				order = (order+1)%this.wholePlayerNum;
			}
		}
	}
	public boolean selectMove() {
		String input="";
		do{
			this.UI.printStr("이동할 거라면 Y, 이동하지 않을 거라면 N을 입력하세요."); //이동경로를 입력하세요
			input = this.UI.inputStr();
			input=input.toUpperCase();
		}while(!input.equals("Y") && !input.equals("N"));
		if(input.equals("Y")) {
			return true;
		}else {
			return false;
		}
	}
	public String inputMove(int num) {
		String input = "";
		do {
			this.UI.printStr("이동경로를 입력하세요. 이동 가능한 거리만큼만 입력해야합니다."); //이동경로를 선택하세요.
			input = this.UI.inputStr();
			if(input.length()!=num) {
				this.UI.printStr("입력한 경로가 이동 가능한 거리 미만이거나 초과하였습니다.\n이동 가능 거리는 "+num+"입니다.");
			}
		}while(input.length()!=(num));
		return input;
	}
	public void startGame() {
		this.dice = new Dice();
		int num = 0;
		do {
			this.UI.printStr("플레이어의 수를 입력하세요. 최소 2명, 최대 4명 입니다."); //플레이어 입력하세요
			num = this.UI.inputInt();
		}while(num<2 || num>4);
		this.p = new Player[num];
		List<String> ids = new ArrayList<>();
		
		for(int i=0; i<num; i++) {
			this.UI.printStr(Integer.toString(i+1)+"번 째 플레이어의 id를 입력하세요.");
			String id = this.UI.inputStr(); //id 입력받기
			while(ids.contains(id)){
				this.UI.printStr("같은 id의 플레이어가 있습니다. 다른 id를 입력하세요.");
				id = this.UI.inputStr();
			}
			ids.add(id);
			p[i]=new Player(id);
		}
		setPlayerNum(num);
		
	}
	public void endGame() {
		this.UI.printStr("게임이 종료되었습니다."); //게임이 종료하였습니다.
		int maxScore = 0; int maxScorePlayer=0;
		for(int i=0; i<this.wholePlayerNum; i++) {
			if(this.p[i].getScore()>maxScore) {
				maxScore = this.p[i].getScore();
				maxScorePlayer = i;
			}
			this.UI.printResult(this.p[i]); //각 플레이어 점수 출력
		}
		this.UI.printStr("승리한 플레이어는 플레이어 "+this.p[maxScorePlayer].getID()+"입니다.");
		
	}
	
	public static void main(String[] args) throws IOException{
		BridgeGame game = new BridgeGame();
		Scanner sc = new Scanner(System.in);
		String Method;
		do{
			System.out.println("플레이 방식을 선택하세요: 콘솔 방식은 C, GUI 방식은 G입니다.");
			Method = sc.next().toUpperCase();
		}while(!Method.equals("C") && !Method.equals("G"));
		if(Method.equals("C")) {
			game.UI=new ConsoleDisplay();
		}else {
			game.UI = new GUIDisplay();
		}
		File dir = new File("..\\data");
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File f, String name) {
				return name.contains("map");
			}
		};
		String[] files = dir.list(filter);
		if(files.length>0) {
			int map=0;
			game.UI.printStr("직접 지도를 선택하시겠습니까? \n 선택하려면 Y를 입력하세요.");
			String flag = game.UI.inputStr().toUpperCase();
			if (flag.equals("Y")) {
				do{
					game.UI.printStr("map을 선택하세요: 정확한 번호를 입력하세요 \n");
					for(int i=0; i<files.length; i++) {
						game.UI.printStr(i+"번 : "+files[i]+"\n");
					}
					map = game.UI.inputInt();
					if(map>=0 && map<files.length) {
						game.UI.printStr("map "+map+"으로 결정하시겠습니까? \n 다시 선택하려면 N을 입력해주세요.");
						flag = game.UI.inputStr().toUpperCase();
					}
				}while((map<0 && map>=files.length) || (flag.equals("N")));
				game.map = new Map("..\\data\\"+files[map]);
			}
			else{
				game.map = new Map("..\\data\\default.map");
			}
			do {
				game.UI.printStr("게임을 플레이하겠습니까? \n플레이하려면 Y, 그렇지 않다면 N을 입력해주세요.");
				flag = game.UI.inputStr().toUpperCase();
			}while(!flag.equals("Y") && !flag.equals("N"));
			if(flag.equals("Y")) {
				game.startGame();
				while(game.curPlayerNum>1) {
					game.playGame();
				}
				game.endGame();
			}else {
				game.UI.printStr("게임을 종료합니다.");
			}
		}else {
			game.UI.printStr("map이 존재하지 않아 게임을 플레이할 수 없습니다.");
		}
		sc.close();
	}
}