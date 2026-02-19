package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

// engine for saving, rendering, entities, neat
public class EntityEngine {

    public static final EntityEngine INSTANCE = new EntityEngine();

    private final List<Entity> entities;

    public EntityEngine() {
        this.entities = new ArrayList<>();
    }

    public void add(Entity entity) {
        this.entities.add(entity);
    }

    public void tick() {
        this.tickList(this.entities);
    }

    private void tickList(List<Entity> entities) {
        if (entities.isEmpty()) return;

        for (Entity entity : new ArrayList<>(entities)) {
            if (entity == null) continue;
            entity.tick();
            if (entity.dead) {
                entities.remove(entity);
            }
        }
    }

    public void render(GuiGraphics graphics) {
        this.entities.forEach(entity -> entity.render(graphics));
    }

    public void clear() {
        this.entities.clear();
    }
}
