package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class Entity {

    public boolean dead;

    protected boolean kill = false;

    protected Minecraft mc;

    public int age;
    public int lifetime = 200;

    public Entity() {
        this.mc = Minecraft.getInstance();
    }

    public void tick() {
        if (age++ >= lifetime) {
            this.remove();
        }
    }

    public abstract void render(GuiGraphics graphics);

    public void remove() {
        this.dead = true;
    }
}
