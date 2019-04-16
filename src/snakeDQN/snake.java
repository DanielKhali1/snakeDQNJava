package snakeDQN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Snake 
{
	
	public int[][] gameGrid;
	public ArrayList<int[]> Positions = new ArrayList<int[]>();
	public int score;
	public CurrentDirection m_CurrentDirection;
	public int width, height;
	private int[] objectiveItem;
	
	public int[][] relativePosition;
	public int relativePositionIndex;
	
	private boolean dead = false;
	
	public Snake(int width, int height)
	{
		this.width = width;
		this.height = height;
		createGrid();
		createSnake();
		spawnObjectiveItem();
		
		relativePositionIndex = 1;
		
		//				  up	  right   down	   left
		int temp[][] = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };
		relativePosition = temp;
		
		updateGrid();
	}
	
	public void createGrid()
	{
		gameGrid = new int[width][height];
		for(int i = 0; i < gameGrid.length; i++)
		{
			for(int j = 0; j < gameGrid[i].length; j++)
			{
				gameGrid[i][j] = 0;
			}
		}
	}
	
	public void createSnake()
	{
		int[] head = {0, 0};
		Positions.add(head);
	}
	
	public void spawnObjectiveItem()
	{
		Random random = new Random();
		int row = random.nextInt(width-1);
		int col = random.nextInt(height-1);
		
		int[] temp = {row, col}; 
		setObjectiveItem(temp);
	}
	
	public void updateGrid()
	{
		createGrid();
		
		for(int i = 0; i < Positions.size(); i++)
		{
			try 
			{
				gameGrid[Positions.get(i)[0]][Positions.get(i)[1]] = 1;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				dead = true;
			}
		}
		
		gameGrid[getObjectiveItem()[0]][getObjectiveItem()[1]] = 2;
		
	}
	
	//1 = left, 0 = keep moving in same direction, -1 = right to relative position
	public double step(int changeDir)
	{
		double reward = 0;
		
		double initialDistance = distanceFromFruit();
		
		
		boolean didEat = didEatObjectiveItem();
		
		ateObjectiveItem();
		CheckIfSnakeHitSelf();
		

		
		changeDirection(changeDir);
		move();
		
		double finalDistance = distanceFromFruit();

		if(didEat)
		{
			reward += 1;
		}
		else if(dead || Positions.get(0)[0] > width-1 || Positions.get(0)[0] < 0 || Positions.get(0)[1] > height-1 || Positions.get(0)[1] < 0)
		{
			reward -= 1;
		}
		else if(finalDistance > initialDistance)
		{
			reward += .1;
		}
		else
		{
			reward -= .1;
		}
		
		
		
		

		updateGrid();
		
		
		
		
		//System.out.println("moved " + Arrays.toString(Positions.get(0)) + " " + relativePositionIndex);
		
		return reward;
	}
	
	public void CheckIfSnakeHitSelf()
	{
		for(int i = 1; i < Positions.size(); i++)
			if((Positions.get(0)[0] == Positions.get(i)[0] && Positions.get(0)[1] == Positions.get(i)[1]) && 0 != i)
					dead = true;
	}
	
	public void ateObjectiveItem(){
		if(Positions.get(0)[0] == getObjectiveItem()[0] && Positions.get(0)[1] == getObjectiveItem()[1])
		{

			spawnObjectiveItem();

			int[] tempPosition = new int[2];

				tempPosition[0] = Positions.get(0)[0];
				tempPosition[1] = Positions.get(0)[1];

			Positions.add(tempPosition);

			score++;
		}
	}
	
	public void move(){

		for(int i = Positions.size()-1; i > 0; i--)
		{
			int[] temp = new int[2];
			temp[0] = Positions.get(i-1)[0];
			temp[1] = Positions.get(i-1)[1];

			Positions.set(i, temp);


		}
		
		switch(relativePositionIndex)
		{
			case 0: updatePosition(relativePosition[0][0], relativePosition[0][1]);
				break;
			case 1: updatePosition(relativePosition[1][0], relativePosition[1][1]);
				break;
			case 2: updatePosition(relativePosition[2][0], relativePosition[2][1]);
				break;
			case 3: updatePosition(relativePosition[3][0], relativePosition[3][1]);
				break;
		
		}
	}
	
	public void changeDirection(int changeDir)
	{		
		if(relativePositionIndex + changeDir < 0)
		{
			relativePositionIndex = 3;
		}
		else if(relativePositionIndex + changeDir > 3)
		{
			relativePositionIndex = 0;
		}	
		else
			relativePositionIndex += changeDir;

		
	}
	
	public double[] flattenedSnakeGrid()
	{
		ArrayList<Integer> flattenedList = new ArrayList<Integer>();
		for(int i = 0; i < gameGrid.length ; i++)
		{
			for(int j = 0; j < gameGrid[0].length; j++)
			{
				flattenedList.add(gameGrid[i][j]);
			}
		}
		
		double[] flattenedArray = new double[flattenedList.size()];
		for(int i = 0; i < flattenedArray.length; i++)
		{
			flattenedArray[i] = flattenedList.get(i);
		}
		
		return flattenedArray;
	}
	
	public double distanceFromFruit()
	{
		double dx = Positions.get(0)[0] - objectiveItem[0];
		double dy = Positions.get(0)[1] - objectiveItem[1];
		return Math.sqrt( dx*dx - dy*dy);
	}
	
	
	public void updatePosition(int x, int y)
	{
		Positions.get(0)[0] += x;
		Positions.get(0)[1] += y;
	}
	
	public boolean didEatObjectiveItem(){
		if(Positions.get(0)[0] == getObjectiveItem()[0] && Positions.get(0)[1] == getObjectiveItem()[1])
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		String grid = "";
		for(int i = 0; i < gameGrid.length; i++)
		{
			grid += Arrays.toString(gameGrid[i]) + "\n";
		}
		return grid;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int[] getObjectiveItem() {
		return objectiveItem;
	}

	public void setObjectiveItem(int[] objectiveItem) {
		this.objectiveItem = objectiveItem;
	}
	

	
	
}
