package jpp.numbergame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberGame {
	private int width, height, initialTiles;
	public Tile grid[][];
	private int point;
	private boolean vis[][];

	public NumberGame(int width, int height) {
		if(width < 1 || height < 1)
			throw new IllegalArgumentException();
		this.width = width;
		this.height = height;
		this.point = 0;
		grid = new Tile[width][height];
		vis = new boolean[width][height];
		for(int i = 0 ; i < width ; i++)
			for(int j = 0 ; j < width ; j++)
				grid[i][j] = new Tile(new Coordinate2D(i, j), 0);
	}
	

	public NumberGame(int width, int height, int initialTiles) {
		this(width,height);
		for(int i = 0 ; i < initialTiles ; i++)
			addRandomTile();
	}

	public int get(Coordinate2D coord)
	{
		return get(coord.getX(),coord.getY());
	}
	
	public int get(int x, int y)
	{
		if(x >= width || x < 0 || y >= height || y < 0)
			throw new IndexOutOfBoundsException();
		
		return grid[x][y].getValue();
	}

	public int getPoints() {
		return point;
	}
	
	public boolean isFull() {
		for(int i = 0 ; i < width ; i++)
			for(int j = 0 ; j < height ; j++)
				if(grid[i][j].getValue() == 0)
					return false;
		return true;
	}
	
	public Tile addRandomTile() {
		if(isFull())
			throw new TileExistsException();
		Random r = new Random();
		int x = r.nextInt(width);
		int y = r.nextInt(height);
		while(true)
		{
		//	System.out.println(x + " -- " + y);
			if(grid[x][y].getValue() != 0)
			{
				 x = r.nextInt(width);
				 y = r.nextInt(height);
				 continue;
			}
			
			int chance[] = {4, 2, 2, 2, 2, 2, 2, 2, 2, 2};
			int value = chance[r.nextInt(10)];
			
			grid[x][y] = new Tile(new Coordinate2D(x, y), value);
			break;
		}

		return grid[x][y];
	}
		
	public Tile  addTile(int x, int y, int value) {
		if(grid[x][y].getValue() != 0)
			throw new TileExistsException();
		
		return grid[x][y] = new Tile(new Coordinate2D(x, y), value);
	}

	public List<Move> move(Direction dir){
		List<Move> moves = new ArrayList<Move>();
		//System.out.println(canMove(dir));
		if(!canMove(dir))
			return moves;
		for(int i = 0 ; i < width ; i++)
			for(int j = 0 ; j < height ; j++)
				vis[i][j] = false;
		
		merge(dir);
		int dx[] = {0 , 1, 0, -1};
		int dy[] = {-1, 0, 1,  0};
		//			U , R, D,  L
		int d = 0;	// UP
		if(dir == Direction.RIGHT)
			d = 1;
		if(dir == Direction.DOWN)
			d = 2;
		if(dir == Direction.LEFT)
			d = 3;
		int stW = 0;
		int stH = 0;
		
		if(dir == Direction.RIGHT || dir == Direction.DOWN) {
			stW = width - 1;
			stH = height - 1;
		}
			
		for(int ii = 0 ; ii < width ; ii++) {
			for(int jj = 0 ; jj < height ; jj++) {
				boolean moved = false;
				Move cur = null;
				int i =  Math.abs(stW - ii);
				int j =  Math.abs(stH - jj);
				
				int newX = i + dx[d];
				int newY = j + dy[d];
				if(grid[i][j].getValue() != 0) {	
					while(true) {
						if(validPosition(newX, newY) && grid[newX][newY].getValue() == 0) {
							int oldValue = (vis[i][j]) ? grid[i][j].getValue() / 2 : grid[i][j].getValue();
							cur = new Move(grid[i][j].getCoord(), grid[newX][newX].getCoord(), oldValue , grid[i][j].getValue());
							moved = true;
						}
						else
							break;
						newX = newX + dx[d];
						newY = newY + dy[d];
					}
				}
				if(moved) {
					grid[newX - dx[d]][newY - dy[d]] = new Tile(grid[newX - dx[d]][newY - dy[d]].getCoord(), grid[i][j].getValue());
					grid[i][j] = new Tile(grid[i][j].getCoord(), 0);
					moves.add(cur);
					System.out.println(cur);
				}
			}

		}
		
	/*	for(int i = 0 ; i < width ; i++) {
			for(int j = 0 ; j < height ; j++) {
				System.out.print(grid[i][j].getValue() + " ");
			}
			System.out.println();
		}

	 */
		addRandomTile();

		return moves;
	}
	
	public void merge(Direction dir) {
		
		int dx[] = {0 , 1, 0, -1};
		int dy[] = {-1, 0, 1,  0};
		//			U , R, D,  L
		int d = 2;	// UP
		if(dir == Direction.RIGHT)
			d = 3;
		if(dir == Direction.DOWN)
			d = 0;
		if(dir == Direction.LEFT)
			d = 1;
		
		
		int stW = 0;
		int stH = 0;
		
		if(dir == Direction.RIGHT || dir == Direction.DOWN) {
			stW = width - 1;
			stH = height - 1;
		}
			
		for(int ii = 0 ; ii < width ; ii++) {
			for(int jj = 0 ; jj < height ; jj++) {
				int i =  Math.abs(stW - ii);
				int j =  Math.abs(stH - jj);
				if(grid[i][j].getValue() != 0 && !vis[i][j]) {
					int newX = i + dx[d];
					int newY = j + dy[d];
					while(true) {
						if(!validPosition(newX, newY))
							break;
						if(grid[newX][newY].getValue() != 0) {
							if(grid[newX][newY].getValue() != grid[i][j].getValue())
								break;
							this.point += grid[i][j].getValue() * 2;
							grid[newX][newY] = new Tile(grid[newX][newY].getCoord(), grid[i][j].getValue() * 2);
							grid[i][j] = new Tile(grid[i][j].getCoord(), 0);
							vis[newX][newY] = true;
							break;
						}
						newX = newX + dx[d];
						newY = newY + dy[d];
					}
					
					
				}
			}
		}
	/*	for(int i = 0 ; i < width ; i++) {
			for(int j = 0 ; j < height ; j++) {
				System.out.print(grid[i][j].getValue() + " ");
			}
			System.out.println();
		}*/
	}
	
	public boolean canMove(Direction dir) {
		int dx[] = {0 , 1, 0, -1};
		int dy[] = {-1, 0, 1,  0};
		//			U , R, D,  L
		int d = 0;	// UP
		if(dir == Direction.RIGHT)
			d = 1;
		if(dir == Direction.DOWN)
			d = 2;
		if(dir == Direction.LEFT)
			d = 3;
		
		for(int i = 0 ; i < width ; i++) {
			for(int j = 0 ; j < height ; j++) {
				int newX = i + dx[d];
				int newY = j + dy[d];
				if(grid[i][j].getValue() != 0) {
				//	System.out.println("old " + i+" "+j);
				//	System.out.println("new " + newX+" "+newY);
				//	System.out.println(validPosition(newX,newY));
					if(validPosition(newX, newY)) {
				//		System.out.println(grid[i][j].getValue()+" "+grid[newX][newY].getValue());
						if(grid[newX][newY].getValue() == 0 || grid[i][j].getValue() == grid[newX][newY].getValue())
							return true;
					}
				}
					
			}
		}
		
		return false;
		
	}
	
	public boolean canMove() {
		return 	canMove(Direction.UP) || 
				canMove(Direction.RIGHT) ||
				canMove(Direction.DOWN) ||
				canMove(Direction.LEFT);
	}
	
	public boolean validPosition(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
}
