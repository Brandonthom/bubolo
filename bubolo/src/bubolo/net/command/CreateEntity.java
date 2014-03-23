/**
 * Copyright (c) 2014 BU MET CS673 Game Engineering Team
 *
 * See the file license.txt for copying permission.
 */

package bubolo.net.command;

import java.util.UUID;

import bubolo.net.NetworkCommand;
import bubolo.world.World;
import bubolo.world.entity.Entity;

/**
 * Generic entity creator for the network.
 * 
 * @author BU CS673 - Clone Productions
 */
public class CreateEntity implements NetworkCommand
{
	private static final long serialVersionUID = 1L;

	private final Class<? extends Entity> type;
	private final UUID id;

	private final float x;
	private final float y;

	private final float rotation;

	/**
	 * Constructs a CreateEntity object.
	 * @param type the entity's class.
	 * @param id the entity's unique id.
	 * @param x the entity's x position.
	 * @param y the entity's y position.
	 * @param rotation the entity's rotation.
	 */
	public CreateEntity(Class<? extends Entity> type, UUID id, float x, float y, float rotation)
	{
		this.type = type;
		this.id = id;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	@Override
	public void execute(World world)
	{
		Entity entity = world.addEntity(type, id);
		entity.setX(x).setY(y).setRotation(rotation);
	}
}