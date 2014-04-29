package bubolo.util;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bubolo.world.GameWorld;
import bubolo.world.Tile;
import bubolo.world.entity.concrete.Grass;
import bubolo.world.entity.concrete.Tank;
import bubolo.world.entity.concrete.Wall;
import bubolo.util.AStar;

/**
 * @author BU CS673 - Clone Productions
 */
public class AStarTest
{
	static GameWorld world;
	static Tile[][] tiles;
	static Tank tank;

	static final int width = 8;
	static final int height = 8;
	
	@BeforeClass
	public static void setup()
	{
		world = new GameWorld(Coordinates.TILE_TO_WORLD_SCALE * width, Coordinates.TILE_TO_WORLD_SCALE * height);
		tiles = new Tile[width][height];
		
		// Populate the world with grass. To make this more interesting we
		// could add some other terrain.
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				tiles[i][j] = new Tile(i, j, new Grass());
			}
		}

		world.setMapTiles(tiles);
		tank = world.addEntity(Tank.class);
		tank.setParams(16, 16, 0);

		// Add a wall on some tiles.
		tiles[width/2][height/2].setElement(new Wall());
	}
	
	@Test
	public void calculateShortestPath()
	{
		Tile start = tiles[0][0];
		Tile goal = tiles[width-1][height-1];
		
		List<Tile> path = AStar.calculateShortestPath(world, start, goal);

		// We have created a world in which there is at least one path from
		// the start to the goal. Therefore, the algorithm should not return
		// null.
		assert(path != null);
		
		// Make sure the path is contiguous.
		Tile prev = start;
		for (Tile t: path)
		{
			// Make sure this tile is a neighbor of the previous tile.
			int prevX = prev.getGridX();
			int prevY = prev.getGridY();
			int curX = t.getGridX();
			int curY = t.getGridY();
			assert(Math.abs(curX - prevX) <= 1);
			assert(Math.abs(curY - prevY) <= 1);
			prev = t;
		}
		
		// We should now be at the goal
		assert(prev == goal);
	}
}
