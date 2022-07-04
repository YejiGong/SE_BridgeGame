import java.util.Scanner;

public class ConsoleDisplay extends UIGenerator{
	@Override
	public void printStr(String input) {
		System.out.println(input);
	}

	@Override
	public void printMap(Map map, Player[] p) {
		int[] size = map.getSize(); int[] minValue = map.getMinValue();
		int realX=0; int realY=0;
		for(int i=0; i<size[1]+1; i++) {
			realY=(size[1]+minValue[1])-i;
			for(int j=0; j<size[0]+1; j++) {
				System.out.print("----- ");
			}
			System.out.println("");
			boolean BridgeAvailable = false;
			for(int j=0; j<size[0]+1; j++) {
				if(BridgeAvailable == true) {
					System.out.print("===== ");
					BridgeAvailable = false;
				} else {
					realX=minValue[0]+j;
					if(map.isThereCell(realX,realY)) {
						char cellType = map.getCellType(realX, realY);
						if(cellType=='C' || cellType=='B' || cellType=='b') {
							System.out.print("| + | ");
							if(cellType=='B') {
								BridgeAvailable = true;
							}
						}else {
							System.out.print("| "+cellType+" | ");
						}
					}else {
						System.out.print("      ");
					}
				}
			}
			System.out.println("");
			for(int j=0; j<size[0]+1; j++) {
				boolean IsPlayer = false;
				int num = 0; int[] position = new int[2];
				realX=minValue[0]+j;
				for(int k=0; k<p.length; k++) {
					position=p[k].getPosition();
					if(position[0]==realX && position[1]==realY) {
						System.out.print(p[k].getID().substring(0,1));
						IsPlayer = true;
						num+=1;
					}
				}
				if(IsPlayer == false) {
					System.out.print("      ");
				}else {
					for(int k=0; k<6-num; k++) {
						System.out.print(" ");
					}
				}
			}
			System.out.println("");
			for(int j=0; j<size[0]+1; j++) {
				System.out.print("----- ");
			}
			System.out.println("");
		}
	}

	@Override
	public void printPlayerInfo(Player[] p) {
		for(int i=0; i<p.length; i++) {
			System.out.println("플레이어 id: "+p[i].getID()+" 플레이어 카드 개수:"+p[i].getCardCount());
		}
	}

	@Override
	public void printResult(Player p) {
		System.out.println("플레이어 id: "+p.getID()+" 플레이어 점수: "+p.getScore());
	}

	@Override
	public String inputStr() {
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		return input;
	}

	@Override
	public int inputInt() {
		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		return input;
	}
	
}