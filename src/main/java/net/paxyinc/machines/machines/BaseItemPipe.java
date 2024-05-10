package net.paxyinc.machines.machines;

import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.world.Zone;
import net.paxyinc.machines.entities.*;
import net.paxyinc.machines.item.Item;
import net.paxyinc.machines.item.ItemInventory;

import java.util.Map;

public class BaseItemPipe extends TileEntity implements IItemProducer, IItemConsumer {

    protected ItemStorage storage;

    public BaseItemPipe(int slots, int maxConsume, int maxProduce) {
        storage = new ItemStorage(new ItemInventory(slots), maxConsume, maxProduce);
    }

    @Override
    public void onTick(Zone zone) {
        for(Map.Entry<Direction, TileEntity> entry : neighbors.entrySet()) {
            Direction face = entry.getKey();
            TileEntity neighbor = entry.getValue();
            if(neighbor instanceof IItemConsumer consumer) {
//                Item item = storage.inventory.getUniqueItems().get(0);
//                int canProduce, canConsume;
//                canConsume = consumer.consume(face, item, storage.available(item), false);
//                canProduce = produce(face, item, Math.min(canConsume, storage.maxProduce), false);
//                consumer.consume(face, item, canProduce, true);
//                produce(face, item, canProduce, true);
            }
        }
    }

    @Override
    public int consume(Direction from, Item item, int amount, boolean simulate) {
        return storage.consume(item, amount, simulate);
    }

    @Override
    public int produce(Direction from, Item item, int amount, boolean simulate) {
        return storage.produce(item, amount, simulate);
    }

    @Override
    public boolean canAcceptItems(Direction face) {
        return true;
    }

    @Override
    public boolean canOutputItems(Direction face) {
        return true;
    }
}
