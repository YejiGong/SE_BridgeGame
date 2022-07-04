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
		str = "���� �÷��̾��"+this.currentPlayer.getID()+"�Դϴ�";
		this.UI.printStr(str); //���� �÷��̾� ����
		boolean moveSelect = true;
		if(this.currentPlayer.getBridgeCardCount()>0) {
			moveSelect = selectMove();
		}
		if(moveSelect) {
			int result = this.dice.getDiceResult();
			str = "�ֻ��� ������ ����Դϴ�: " + Integer.toString(result);
			this.UI.printStr(str);
			str = "�÷��̾� " + this.currentPlayer.getID()+ "�� �ٸ� ī�� �����Դϴ�: " + this.currentPlayer.getBridgeCardCount();
			this.UI.printStr(str);
			result -= this.currentPlayer.getBridgeCardCount();
			str = "�̵������� �Ÿ��Դϴ�: " + Integer.toString(result);
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
						this.UI.printStr("�߸��� �̵�����Դϴ�.");
					}
				}while(moveAvailable == false);
				this.UI.printMap(this.map, this.p);
				this.UI.printStr("�̵��Ͽ����ϴ�.");
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
			this.UI.printStr(cellType+" ī�带 ȹ���Ͽ����ϴ�.");
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
			this.UI.printStr("�÷��̾� "+this.currentPlayer.getID()+"�� ���� ��ġ�� �����߽��ϴ�.");
			this.UI.printResult(this.currentPlayer); //������ �÷��̾� ����
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
			this.UI.printStr("�̵��� �Ŷ�� Y, �̵����� ���� �Ŷ�� N�� �Է��ϼ���."); //�̵���θ� �Է��ϼ���
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
			this.UI.printStr("�̵���θ� �Է��ϼ���. �̵� ������ �Ÿ���ŭ�� �Է��ؾ��մϴ�."); //�̵���θ� �����ϼ���.
			input = this.UI.inputStr();
			if(input.length()!=num) {
				this.UI.printStr("�Է��� ��ΰ� �̵� ������ �Ÿ� �̸��̰ų� �ʰ��Ͽ����ϴ�.\n�̵� ���� �Ÿ��� "+num+"�Դϴ�.");
			}
		}while(input.length()!=(num));
		return input;
	}
	public void startGame() {
		this.dice = new Dice();
		int num = 0;
		do {
			this.UI.printStr("�÷��̾��� ���� �Է��ϼ���. �ּ� 2��, �ִ� 4�� �Դϴ�."); //�÷��̾� �Է��ϼ���
			num = this.UI.inputInt();
		}while(num<2 || num>4);
		this.p = new Player[num];
		List<String> ids = new ArrayList<>();
		
		for(int i=0; i<num; i++) {
			this.UI.printStr(Integer.toString(i+1)+"�� ° �÷��̾��� id�� �Է��ϼ���.");
			String id = this.UI.inputStr(); //id �Է¹ޱ�
			while(ids.contains(id)){
				this.UI.printStr("���� id�� �÷��̾ �ֽ��ϴ�. �ٸ� id�� �Է��ϼ���.");
				id = this.UI.inputStr();
			}
			ids.add(id);
			p[i]=new Player(id);
		}
		setPlayerNum(num);
		
	}
	public void endGame() {
		this.UI.printStr("������ ����Ǿ����ϴ�."); //������ �����Ͽ����ϴ�.
		int maxScore = 0; int maxScorePlayer=0;
		for(int i=0; i<this.wholePlayerNum; i++) {
			if(this.p[i].getScore()>maxScore) {
				maxScore = this.p[i].getScore();
				maxScorePlayer = i;
			}
			this.UI.printResult(this.p[i]); //�� �÷��̾� ���� ���
		}
		this.UI.printStr("�¸��� �÷��̾�� �÷��̾� "+this.p[maxScorePlayer].getID()+"�Դϴ�.");
		
	}
	
	public static void main(String[] args) throws IOException{
		BridgeGame game = new BridgeGame();
		Scanner sc = new Scanner(System.in);
		String Method;
		do{
			System.out.println("�÷��� ����� �����ϼ���: �ܼ� ����� C, GUI ����� G�Դϴ�.");
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
			game.UI.printStr("���� ������ �����Ͻðڽ��ϱ�? \n �����Ϸ��� Y�� �Է��ϼ���.");
			String flag = game.UI.inputStr().toUpperCase();
			if (flag.equals("Y")) {
				do{
					game.UI.printStr("map�� �����ϼ���: ��Ȯ�� ��ȣ�� �Է��ϼ��� \n");
					for(int i=0; i<files.length; i++) {
						game.UI.printStr(i+"�� : "+files[i]+"\n");
					}
					map = game.UI.inputInt();
					if(map>=0 && map<files.length) {
						game.UI.printStr("map "+map+"���� �����Ͻðڽ��ϱ�? \n �ٽ� �����Ϸ��� N�� �Է����ּ���.");
						flag = game.UI.inputStr().toUpperCase();
					}
				}while((map<0 && map>=files.length) || (flag.equals("N")));
				game.map = new Map("..\\data\\"+files[map]);
			}
			else{
				game.map = new Map("..\\data\\default.map");
			}
			do {
				game.UI.printStr("������ �÷����ϰڽ��ϱ�? \n�÷����Ϸ��� Y, �׷��� �ʴٸ� N�� �Է����ּ���.");
				flag = game.UI.inputStr().toUpperCase();
			}while(!flag.equals("Y") && !flag.equals("N"));
			if(flag.equals("Y")) {
				game.startGame();
				while(game.curPlayerNum>1) {
					game.playGame();
				}
				game.endGame();
			}else {
				game.UI.printStr("������ �����մϴ�.");
			}
		}else {
			game.UI.printStr("map�� �������� �ʾ� ������ �÷����� �� �����ϴ�.");
		}
		sc.close();
	}
}