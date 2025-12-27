package melonslise.locks.client.gui.sprite.action;

import melonslise.locks.client.gui.sprite.Sprite;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;

//控制精灵的行为
@OnlyIn(Dist.CLIENT)
public interface IAction<S extends Sprite>
{
	boolean isFinished(S sprite);

	void update(S sprite);

	void finish(S sprite);

	IAction<S> then(BiConsumer<IAction<S>, S> cb);
}