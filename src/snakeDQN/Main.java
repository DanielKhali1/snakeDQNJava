package snakeDQN;

import java.util.Arrays;

import DQN.NeuralNetwork;

public class Main
{
	public static void main(String[] args)
	{
		final int[] topology = {4, 30, 4};
		final SimpleSnakeDQN dqn = new SimpleSnakeDQN(topology, 0.001, 0.995, 10, 10);
		dqn.setEpsilonMin(0.001);
		
		NeuralNetwork loadedNet = NeuralNetwork.loadNetwork("snake.nn");
		if(loadedNet != null)
		{
			System.out.println("Loaded network!");
			dqn.setNetwork(loadedNet);
			
			for(int round = 0; round < 100; round++)
			{
				double averageScore = 0;
				int maxScore = 0;
				for(int gameIndex = 0; gameIndex < 5000; gameIndex++)
				{
					dqn.reset();
					
					while(!dqn.isDone())
					{
						dqn.step();
					}
					
					averageScore += dqn.getScore();
					
					if(dqn.getScore() > maxScore)
					{
						maxScore = dqn.getScore();
					}
				}
				
				averageScore /= 1000.0;
				System.out.println(round + "," + averageScore + "," + maxScore);
			}
			
			NeuralNetwork.saveNetwork(dqn.getNetwork(), "snake.nn");
			
			while(!dqn.isDead())
			{
				dqn.step();
				System.out.println(dqn.toString()+"\n");
			}
		}
		else
		{
		
		System.out.println("round,avgScore,maxScore");
		for(int round = 0; round < 100; round++)
		{
			double averageScore = 0;
			int maxScore = 0;
			for(int gameIndex = 0; gameIndex < 5000; gameIndex++)
			{
				dqn.reset();
				
				while(!dqn.isDone())
				{
					dqn.step();
				}
				
				averageScore += dqn.getScore();
				
				if(dqn.getScore() > maxScore)
				{
					maxScore = dqn.getScore();
				}
			}
			
			averageScore /= 1000.0;
			System.out.println(round + "," + averageScore + "," + maxScore);
		}
		}
		
		NeuralNetwork.saveNetwork(dqn.getNetwork(), "snake.nn");
	}
}

// The game is simply: u are a pixel and u must go to another pixel (fruit)
// if u go out of bounds, u die
// if u reach the fruit, u are awarded
// the fruit will respawn in another spot (thats how u get score)
// the state is simply 4 values, the snake & fruits position
class SimpleSnakeDQN extends DQN
{
	
	private boolean dead;
	private int steps;
	private int score;
	private int fruitX, fruitY;
	private int snakeX, snakeY;
	private int width, height;
	
	public SimpleSnakeDQN(int[] topology, double learningRate, double discountFactor, int width, int height)
	{
		super(topology, learningRate, discountFactor);
		this.width = width;
		this.height = height;
		reset();
	}
	
	public String toString()
	{
		String grid = "";
		
		char[][] x = new char[width][height];
		
		for(int i = 0; i < x.length; i++)
		{
			for(int j = 0; j < x[0].length; j++)
			{
				x[i][j] = ' ';
			}
		}
		
		try {
		x[fruitY][fruitX] = 'f';
		x[snakeY][snakeX] = 'O';
		}
		catch(Exception e)
		{
			
		}
		for(int i = 0; i < x.length; i++)
		{
			grid += Arrays.toString(x[i]) + "\n";
		}
		
		return grid;
	}

	public void reset()
	{
		snakeX = (int) (Math.random() * width);
		snakeY = (int) (Math.random() * height);
		
		do
		{
			fruitX = (int) (Math.random() * width);
			fruitY = (int) (Math.random() * height);
		}
		while(fruitX != snakeX && fruitY != snakeY);
		
		score = 0;
		steps = 0;
		dead = false;
	}
	
	@Override
	protected double[] getState()
	{
		return new double[] {snakeX, snakeY, fruitX, fruitY};
	}

	@Override
	protected boolean isDone()
	{
		return steps >= 500 || dead;
	}

	@Override
	protected double executeActionAndGetReward(int actionIndex)
	{
		if(actionIndex == 0) // go up
		{
			snakeY++;
		}
		else if(actionIndex == 1) // go down
		{
			snakeY--;
		}
		else if(actionIndex == 2) // go left
		{
			snakeX--;
		}
		else if(actionIndex == 3) // go right
		{
			snakeX++;
		}
		
		steps++;
		
		// dead if out of bounds
		if(snakeX < 0 || snakeY < 0 || snakeX >= width || snakeY >= height)
		{
			dead = true;
			return -1;
		}
		
		// award if on fruit
		if(snakeX == fruitX && snakeY == fruitY)
		{
			score++;
			
			do
			{
				fruitX = (int) (Math.random() * width);
				fruitY = (int) (Math.random() * height);
			}
			while(fruitX != snakeX && fruitY != snakeY);
			
			return 1;
		}
		
		// punish otherwise
		return -0.1;
	}
	
	public boolean isDead()
	{
		return dead;
	}

	public int getSteps()
	{
		return steps;
	}

	public int getScore()
	{
		return score;
	}

	public int getFruitX()
	{
		return fruitX;
	}

	public int getFruitY()
	{
		return fruitY;
	}

	public int getSnakeX()
	{
		return snakeX;
	}

	public int getSnakeY()
	{
		return snakeY;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}


}