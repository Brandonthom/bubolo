package bubolo.graphics;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

import bubolo.world.entity.concrete.Tank;

public class TankSpriteTest
{
	private SpriteBatch batch;
	private Camera camera;
	
	private boolean isComplete;
	private boolean passed;
	
	@Before
	public void setUp()
	{	
		LibGdxAppTester.createApp();
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(100, 100);
		Graphics g = new Graphics(50, 500);
	}
	
	@Test
	public void constructTankSprite() throws InterruptedException
	{
		isComplete = false;
		passed = false;
		
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run()
			{
				// Fails if the constructor throws an exception.
				Sprite<Tank> sprite = new TankSprite(new Tank());
				passed = true;
				isComplete = true;
			}
		});

		while (!isComplete)
		{
			Thread.yield();
		}
		
		assertTrue(passed);
	}

	
	@Test
	public void drawTankSprite()
	{
		isComplete = false;
		passed = false;
		
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run()
			{
				Sprite<?> sprite = Sprites.getInstance().create(new Tank());
				batch.begin();
				sprite.draw(batch, camera, DrawLayer.TANKS);
				passed = true;
				isComplete = true;
			}
		});

		while (!isComplete)
		{
			Thread.yield();
		}
		
		assertTrue(passed);
	}
}
