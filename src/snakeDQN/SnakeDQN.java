package snakeDQN;

public class SnakeDQN extends DQN
{
	public SnakeDQN(int[] topology, double learningRate, double discountFactor)
	{
		super(topology, learningRate, discountFactor);
	}
	
	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	@Override
	protected double[] getState()
	{
		return snake.flattenedSnakeGrid();
	}

	@Override
	protected double executeActionAndGetReward(int actionIndex)
	{
		return snake.step(actions[actionIndex]);
	}
	
	@Override
	protected boolean isDone()
	{
		return snake.isDead();
	}
	
	private int[] actions = {-1, 0, 1};
	private Snake snake;
}
