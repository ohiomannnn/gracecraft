package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityEngine {

    public static final EntityEngine INSTANCE = new EntityEngine();

    private final List<ScreenEntity> entities = new ArrayList<>();
    private final List<ScreenEntity> pendingEntities = new ArrayList<>();

    public EntityEngine() { }

    public void clear() {
        this.entities.clear();
    }

    public void add(ScreenEntity entity) {
        this.entities.add(entity);
    }

    public void tick() {
        // we dont need to process empty lists
        if (this.entities.isEmpty() && pendingEntities.isEmpty()) return;

        if (!pendingEntities.isEmpty()) {
            entities.addAll(pendingEntities);
            pendingEntities.clear();
        }

        Iterator<ScreenEntity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            ScreenEntity particle = iterator.next();

            particle.tick();
            if (particle.dead) {
                iterator.remove();
            }
        }

        if (!pendingEntities.isEmpty()) {
            entities.addAll(pendingEntities);
            pendingEntities.clear();
        }
    }
    public void render(GuiGraphics graphics) {
        this.entities.forEach(entity -> entity.render(graphics));
    }
}
