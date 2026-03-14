package ohiomannnn.gracecraft.client.sprites;

import net.minecraft.client.gui.GuiGraphics;

public class AnimatedSpriteSheet {

    private final SpriteSheet spriteSheet;
    private final int totalFrames;
    private final long frameDurationMs;

    private long startTime;
    private long pauseTime;
    private boolean playing;
    private boolean looping;

    private long[] frameDurations;
    private long totalCycleDurationMs;

    public AnimatedSpriteSheet(SpriteSheet spriteSheet, int totalFrames, long frameDurationMs) {
        this.spriteSheet = spriteSheet;
        this.totalFrames = totalFrames;
        this.frameDurationMs = frameDurationMs;
        this.totalCycleDurationMs = frameDurationMs * totalFrames;
        this.startTime = System.currentTimeMillis();
        this.playing = true;
        this.looping = true;
        this.frameDurations = null;
    }

    public AnimatedSpriteSheet(SpriteSheet spriteSheet, long frameDurationMs) {
        this(spriteSheet, spriteSheet.getTotalSprites(), frameDurationMs);
    }

    public AnimatedSpriteSheet(SpriteSheet spriteSheet, long[] frameDurations) {
        this.spriteSheet = spriteSheet;
        this.totalFrames = frameDurations.length;
        this.frameDurations = frameDurations;
        this.frameDurationMs = 0;

        long total = 0;
        for (long d : frameDurations) total += d;
        this.totalCycleDurationMs = total;

        this.startTime = System.currentTimeMillis();
        this.playing = true;
        this.looping = true;
    }

    public int getCurrentFrame() {
        if (!playing) {
            return computeFrame(pauseTime - startTime);
        }

        long elapsed = System.currentTimeMillis() - startTime;
        return computeFrame(elapsed);
    }

    private int computeFrame(long elapsed) {
        if (elapsed < 0) return 0;

        if (looping) {
            elapsed = elapsed % totalCycleDurationMs;
        } else {
            if (elapsed >= totalCycleDurationMs) {
                return totalFrames - 1;
            }
        }

        if (frameDurations == null) {
            return (int) (elapsed / frameDurationMs) % totalFrames;
        }

        long accumulator = 0;
        for (int i = 0; i < totalFrames; i++) {
            accumulator += frameDurations[i];
            if (elapsed < accumulator) {
                return i;
            }
        }
        return totalFrames - 1;
    }

    public float getFrameProgress() {
        long elapsed = playing ? System.currentTimeMillis() - startTime : pauseTime - startTime;

        if (elapsed < 0) return 0f;

        if (looping) {
            elapsed = elapsed % totalCycleDurationMs;
        } else if (elapsed >= totalCycleDurationMs) {
            return 1.0f;
        }

        if (frameDurations == null) {
            long inFrame = elapsed % frameDurationMs;
            return (float) inFrame / frameDurationMs;
        }

        long accumulator = 0;
        for (int i = 0; i < totalFrames; i++) {
            if (elapsed < accumulator + frameDurations[i]) {
                return (float) (elapsed - accumulator) / frameDurations[i];
            }
            accumulator += frameDurations[i];
        }
        return 1.0f;
    }

    public float getTotalProgress() {
        long elapsed = playing
                ? System.currentTimeMillis() - startTime
                : pauseTime - startTime;

        if (looping) {
            return (float) (elapsed % totalCycleDurationMs) / totalCycleDurationMs;
        }
        return Math.min(1.0f, (float) elapsed / totalCycleDurationMs);
    }


    public void render(GuiGraphics guiGraphics, int x, int y, int renderWidth, int renderHeight) {
        spriteSheet.renderSprite(guiGraphics, x, y, getCurrentFrame(), renderWidth, renderHeight);
    }

    public void render(GuiGraphics guiGraphics, int x, int y) {
        spriteSheet.renderSprite(guiGraphics, x, y, getCurrentFrame());
    }

    public void play() {
        if (!playing) {
            long pausedDuration = System.currentTimeMillis() - pauseTime;
            startTime += pausedDuration;
            playing = true;
        }
    }

    public void pause() {
        if (playing) {
            pauseTime = System.currentTimeMillis();
            playing = false;
        }
    }

    public void stop() {
        playing = false;
        startTime = System.currentTimeMillis();
        pauseTime = startTime;
    }

    public void restart() {
        startTime = System.currentTimeMillis();
        pauseTime = startTime;
        playing = true;
    }

    public void setFrame(int frame) {
        frame = Math.max(0, Math.min(frame, totalFrames - 1));

        long offset;
        if (frameDurations == null) {
            offset = (long) frame * frameDurationMs;
        } else {
            offset = 0;
            for (int i = 0; i < frame; i++) offset += frameDurations[i];
        }

        startTime = System.currentTimeMillis() - offset;
        if (!playing) {
            pauseTime = System.currentTimeMillis();
        }
    }

    public void setSpeed(float speed) {
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;
        long newElapsed = (long) (elapsed / speed);
        startTime = now - newElapsed;
    }

    public void setLooping(boolean looping) { this.looping = looping; }
    public boolean isPlaying() { return playing; }
    public boolean isFinished() { return !looping && !playing; }
    public SpriteSheet getSpriteSheet() { return spriteSheet; }
    public int getTotalFrames() { return totalFrames; }
}