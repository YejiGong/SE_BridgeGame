import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;
public class Map{
	private List<String> mapCells;
	private int[][] mapCellsPosition;
	private int xSize; private int ySize; private int minX; private int minY;
	public Map(String path) throws IOException {
		this.mapCells = Files.readAllLines(Paths.get(path));
		this.mapCellsPosition = new int[this.mapCells.size()][2];
		int minX=0; int maxX=0; int minY=0; int maxY=0;
		for(int i=0; i<this.mapCells.size() -1; i++) {
			char direction = this.mapCells.get(i).charAt(this.mapCells.get(i).length()-1);
			if(direction=='U') {
				this.mapCellsPosition[i+1][0]=this.mapCellsPosition[i][0];
				this.mapCellsPosition[i+1][1]=this.mapCellsPosition[i][1]+1;
			}else if(direction=='D') {
				this.mapCellsPosition[i+1][0]=this.mapCellsPosition[i][0];
				this.mapCellsPosition[i+1][1]=this.mapCellsPosition[i][1]-1;
			}else if(direction=='L') {
				this.mapCellsPosition[i+1][0]=this.mapCellsPosition[i][0]-1;
				this.mapCellsPosition[i+1][1]=this.mapCellsPosition[i][1];
			}else if(direction=='R') {
				this.mapCellsPosition[i+1][0]=this.mapCellsPosition[i][0]+1;
				this.mapCellsPosition[i+1][1]=this.mapCellsPosition[i][1];
			}
			
			minX=Math.min(this.mapCellsPosition[i+1][0], minX);
			minY=Math.min(this.mapCellsPosition[i+1][1], minY);
			maxX=Math.max(this.mapCellsPosition[i+1][0], maxX);
			maxY=Math.max(this.mapCellsPosition[i+1][1], maxY);
		}
		this.minX=minX; this.minY=minY;
		this.xSize = maxX-minX; this.ySize = maxY-minY;
	}
	
	private int getPositionIndex(int x, int y) {
		int result = -1;
		for(int i=0; i<this.mapCells.size(); i++) {
			if(this.mapCellsPosition[i][0]==x && this.mapCellsPosition[i][1]==y) {
				result = i;
			}
		}
		return result;
	}
	
	public boolean isThereCell(int x, int y) {
		int result = getPositionIndex(x,y);
		if(result==-1) {
			return false;
		}else {
			return true;
		}
	}
	public String getPositionDirection(int x, int y) {
		int index = getPositionIndex(x,y);
		return this.mapCells.get(index);
	}
	
	public char getCellType(int x, int y) {
		int index = getPositionIndex(x,y);
		return this.mapCells.get(index).charAt(0);
	}
	
	public boolean isEnd(int x, int y) {
		int index = getPositionIndex(x,y);
		if(this.mapCells.get(index).charAt(0)=='E') {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isStart(int x, int y) {
		int index = getPositionIndex(x,y);
		if(index==0) {
			return true;
		}else {
			return false;
		}
	}
	public int[] getSize() {
		int[] size=new int[2];
		size[0]=this.xSize; size[1]=this.ySize;
		return size;
	}
	
	public int[] getMinValue() {
		int[] value=new int[2];
		value[0]=this.minX; value[1]=this.minY;
		return value;
	}
}

