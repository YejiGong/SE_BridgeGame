import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUIDisplay extends UIGenerator{
	public String inputValue;
	JFrame frame;
	JButton InputButton;
	JTextField inputBox;
	JTextArea inputMap;
	JScrollPane roll;
	JTextArea PlayerInfo;
	JTextArea print;
	JPanel infoPannel;
	JPanel mapPannel;
	JPanel printPannel;
	JPanel inputPannel;
	public GUIDisplay() {
		this.inputValue="";
		this.createAndShowGUI();
	}
	
	public void createAndShowGUI() {
		frame = new JFrame("Bridge Game");
		InputButton = new JButton("enter");
		InputButton.setActionCommand("enter");
		buttonClickAction actionHandler = new buttonClickAction();
		InputButton.addActionListener(actionHandler);
		inputBox = new JTextField(50);
		inputBox.setEnabled(false);
		inputMap = new JTextArea("  ");
		inputMap.setEnabled(false);
		roll = new JScrollPane();
		roll.setViewportView(inputMap);
		roll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		roll.setPreferredSize(new Dimension(400,400));
		PlayerInfo = new JTextArea("  ");
		PlayerInfo.setEnabled(false);
		PlayerInfo.setPreferredSize(new Dimension(400,100));
		print = new JTextArea("  ");
		print.setEnabled(false);
		print.setPreferredSize(new Dimension(400,200));
		infoPannel = new JPanel();
		infoPannel.add(PlayerInfo);
		mapPannel = new JPanel();
		mapPannel.add(roll);
		printPannel = new JPanel();
		printPannel.add(print);
		inputPannel = new JPanel();
		inputPannel.add(inputBox);
		inputPannel.add(InputButton);
		
		frame.setSize(700,700);
		frame.setLayout(new java.awt.BorderLayout());
		frame.add(infoPannel, java.awt.BorderLayout.NORTH);
		frame.add(mapPannel, java.awt.BorderLayout.WEST);
		frame.add(printPannel, java.awt.BorderLayout.EAST);
		frame.add(inputPannel, java.awt.BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	class buttonClickAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand()=="enter") {
				inputValue = String.valueOf(inputBox.getText());
			}
		}
	}
	@Override
	public void printStr(String input) {
		this.print.append(input+"\n");
	}

	@Override
	public void printMap(Map map, Player[] p) {
		this.inputMap.setText("");
		int[] size = map.getSize(); int[] minValue = map.getMinValue();
		int realX=0; int realY=0;
		for(int i=0; i<size[1]+1; i++) {
			realY=(size[1]+minValue[1])-i;
			for(int j=0; j<size[0]+1; j++) {
				this.inputMap.append("----- ");
			}
			this.inputMap.append("\n");
			boolean BridgeAvailable = false;
			for(int j=0; j<size[0]+1; j++) {
				if(BridgeAvailable == true) {
					this.inputMap.append("=== ");
					BridgeAvailable = false;
				} else {
					realX=minValue[0]+j;
					if(map.isThereCell(realX,realY)) {
						char cellType = map.getCellType(realX, realY);
						if(cellType=='C' || cellType=='B' || cellType=='b') {
							this.inputMap.append("| + | ");
							if(cellType=='B') {
								BridgeAvailable = true;
							}
						}else {
							this.inputMap.append("| "+cellType+" | ");
						}
					}else {
						this.inputMap.append("       ");
					}
				}
			}
			this.inputMap.append("\n");
			for(int j=0; j<size[0]+1; j++) {
				boolean IsPlayer = false;
				int num = 0; int[] position = new int[2];
				realX=minValue[0]+j;
				for(int k=0; k<p.length; k++) {
					position=p[k].getPosition();
					if(position[0]==realX && position[1]==realY) {
						this.inputMap.append(p[k].getID().substring(0, 1));
						IsPlayer = true;
						num+=1;
					}
				}
				if(IsPlayer == false) {
					this.inputMap.append("       ");
				}else {
					for(int k=0; k<6-num; k++) {
						this.inputMap.append(" ");
					}
				}
			}
			this.inputMap.append("\n");
			for(int j=0; j<size[0]+1; j++) {
				this.inputMap.append("----- ");
			}
			this.inputMap.append("\n");
		}
		
	}
	@Override
	public void printPlayerInfo(Player[] p) {
		this.PlayerInfo.setText("");
		for(int i=0; i<p.length; i++) {
			this.PlayerInfo.append("플레이어 id: "+p[i].getID()+" 플레이어 카드 개수:"+p[i].getCardCount()+"\n");
		}
	}

	@Override
	public void printResult(Player p) {
		this.print.append("플레이어 id: "+p.getID()+" 플레이어 점수: "+p.getScore()+"\n");
	}

	@Override
	public String inputStr() {
		this.inputBox.setEnabled(true);
		while(this.inputValue.isBlank()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!this.inputValue.isBlank()) {
				break;
			}
		}
		String result = this.inputValue;
		this.inputBox.setEnabled(false);
		this.inputBox.setText("");
		this.inputValue="";
		this.print.setText("");
		return result;
	}

	@Override
	public int inputInt() {
		this.inputBox.setEnabled(true);
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!this.inputValue.isBlank()) {
				break;
			}
		}
		int result = Integer.parseInt(this.inputValue);
		this.inputBox.setEnabled(false);
		this.inputBox.setText("");
		this.inputValue="";
		this.print.setText("");
		return result;
	}

	
}