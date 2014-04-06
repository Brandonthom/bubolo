package bubolo.controllers.ai;

import java.util.Random;

import bubolo.controllers.Controller;
import bubolo.world.Tile;
import bubolo.world.World;
import bubolo.world.entity.Terrain;
import bubolo.world.entity.concrete.Crater;
import bubolo.world.entity.concrete.Grass;
import bubolo.world.entity.concrete.Rubble;
import bubolo.world.entity.concrete.Tree;

/**
 * controls the growth rate of trees
 * 
 * @author BU CS673 - Clone Productions
 */
public class AITreeController implements Controller
{
	/**
	 * base score for tiles based on terrain.  surrounding tiles use these divided by 8
	 */
	private int grassScore = 40;
	private int rubbleCraterScore = 10;
	private int treeScore = 40;

	/**
	 * tile locations
	 */
	private int createAtX = 0;
	private int createAtY = 0 ; 
	private int tempX = 0;
	private int tempY = 0;
	
	/**
	 * calculated tile scores
	 */
	private int tileScore = 0;
	private int tempScore = 0;
	
	/**
	 * timing variables
	 */
	private int ticksSinceReset = 0;
	private int ticksPerGrowth = 300;
	/**
	 * random number generator used to decide a random tile to grow on
	 */
	Random randomGenerator = new Random();
	
	/**
	 * default constructor
	 */
	public AITreeController()
	{
		
	}

	@Override
	public void update(World world) 
	{
		pickATile(world);
		getTempTileScore(world);
		if ((tempScore > tileScore))
		{
			createAtX = tempX;
			createAtY = tempY;
			tileScore = tempScore;
			ticksSinceReset = 0;
		}
		else
		{
			if ((ticksSinceReset >= ticksPerGrowth) && tileScore >0)
			{
				Tree tree = world.addEntity(Tree.class);
				tree.setParams(createAtX*32-16, createAtY*32-16, 0);
				world.getMapTiles()[createAtX-1][createAtY-1].setElement(tree);
				ticksSinceReset = 0;
				tileScore = 0;				
			}
			else
			{
				ticksSinceReset++;
			}
		}	
	}
	private void pickATile(World world)
	{
		//get map size in tiles
		int mapHeight = world.getMapHeight()/32;
		int mapWidth = world.getMapWidth()/32;
		
		//get a random tile that is not on the border
		tempX = randomGenerator.nextInt(mapWidth-2)+2;
		tempY = randomGenerator.nextInt(mapHeight-2)+2;
	}
	/**
	 * sums up the score of a tile based on all its neighbors
	 * @param world
	 */
	private void getTempTileScore(World world)
	{
		 boolean unbuildable = false;
		tempScore = 0;
		Tile[][] tiles = world.getMapTiles();
		Tile tile = tiles[tempX-1][tempY-1];
		//System.out.print(tile.getTerrain().toString()+'\n');
		//if(tile.hasElement())
			//System.out.print(tile.getElement().toString()+'\n');
		if (tile == null)
		{
			unbuildable = false;
		}
		else
		{
			Terrain terrain = tile.getTerrain();
			//trees will grow on grass or craters or rubble nothing else
			if (!tile.hasElement())
			{
				if (terrain.getClass() == Grass.class)
				{
					tempScore = grassScore;
				}
				else
				{
					unbuildable = true;			
				}
			}
			else
			{
				if (tile.getElement().getClass() == Rubble.class ||
						tile.getElement().getClass() == Crater.class )
				{
					tempScore = rubbleCraterScore;
				}
				else
				{
					unbuildable = true;
				}
			}
			if (!unbuildable)
			{
				//get score of each surrounding tile
				tempScore += getNeighborTileScore(tiles[tempX][tempY]); //top right
				tempScore += getNeighborTileScore(tiles[tempX][tempY-1]);//mid right
				tempScore += getNeighborTileScore(tiles[tempX][tempY-2]);//bottom right
				tempScore += getNeighborTileScore(tiles[tempX-1][tempY]);//top mid
				tempScore += getNeighborTileScore(tiles[tempX-1][tempY-2]);//bottom mid
				tempScore += getNeighborTileScore(tiles[tempX-2][tempY]);//top left
				tempScore += getNeighborTileScore(tiles[tempX-2][tempY-1]);//mid left
				tempScore += getNeighborTileScore(tiles[tempX-2][tempY-2]);//bottom left
			}
			else
			{
				tempScore = 0;
			}
		}
	}
	/**
	 * returns the score of a specific tile
	 * 
	 * @param tile
	 * 		the tile to be inspected
	 * @return
	 * 		the score of the tile
	 */
	
	private int getNeighborTileScore(Tile tile)
	{
		int score = 0;
		Terrain terrain;
		if (!tile.hasElement())
		{
			terrain = tile.getTerrain();
			if (terrain.getClass() == Grass.class)
			{
				score = grassScore/8;
			}
		}
		else
		{
			//tree is most valuable however any other stationary element is a 0
			if (tile.getElement().getClass() == Tree.class)
			{
				score = treeScore;
			}
		}
		return score;
	}
}
