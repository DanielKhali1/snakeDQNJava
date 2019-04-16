package snakeDQN;

public class GameTest
{
	public static void main(String[] args)
	{
		final int iterations = 100_000;
		final int[] topology = {1, 10, 2};
		final MyDQN dqn = new MyDQN(topology, 0.001, 0.99);
		
		for(int gameIndex = 0; gameIndex < iterations; gameIndex++)
		{
			dqn.reset();
			
			while(!dqn.isDone())
			{
				dqn.step();
			}
			
			System.out.println("Game " + gameIndex + " has angle " + dqn.getAngle() + " and epsilon " + dqn.getEpsilon());
		}
	}
}

class MyDQN extends DQN
{
	private double angle = 0;
	private int steps = 0;
	
	public MyDQN(int[] topology, double learningRate, double discountFactor)
	{
		super(topology, learningRate, discountFactor);
	}
	
	public void reset()
	{
		angle = 0;
		steps = 0;
	}

	public double getAngle()
	{
		return angle;
	}
	
	@Override
	protected double[] getState()
	{
		return new double[]{angle};
	}

	@Override
	protected boolean isDone()
	{
		return steps >= 100;
	}

	@Override
	protected double executeActionAndGetReward(int actionIndex)
	{
		double oldDelta = Math.abs(angle);
		if(actionIndex == 0) // reduce angle
		{
			angle -= 1;
		}
		else if(actionIndex == 1)// increase angle
		{
			angle += 1;
		}
		else // do nothing
		{
			
		}
		
		steps++;
		
		double delta = Math.abs(angle);
		
		if(oldDelta == delta) // if didnt change angle, only award if its at 0, otherwise punish
		{
			if(angle == 0)
				return 1;
			else
				return -1;
		}
		else // award if going towards 0, and punish if moving away
		{
			return Math.signum(oldDelta - delta);
		}
	}
	
}