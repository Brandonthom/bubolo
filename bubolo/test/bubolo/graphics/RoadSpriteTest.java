package bubolo.graphics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bubolo.world.entity.concrete.Road;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RoadSpriteTest
{
	private SpriteBatch batch;
	private Camera camera;
	
	private boolean isComplete;
	private boolean passed;
	
	@Before
	public void setUp()
	{	
			LibGdxAppTester.createApp();
			
			Gdx.app.postRunnable(new Runnable() {
				@Override public void run() {
					batch = new SpriteBatch();
					camera = new OrthographicCamera(100, 100);
					Graphics g = new Graphics(50, 500);
				}
			});
	}
	

	@Test
	public void drawSprite()
	{
		synchronized(LibGdxAppTester.getLock())
		{
			isComplete = false;
			passed = false;
			
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run()
				{
					Sprite sprite = Sprites.getInstance().createSprite(new Road());
					batch.begin();
					sprite.draw(batch, camera, sprite.getDrawLayer());
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

}
