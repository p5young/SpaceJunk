package com.spacejunk.game.utilities;

/**
 * Created by vidxyz on 3/24/18.
 */

public class MultipleVirtualViewportBuilder {

    private final float minWidth;
    private final float minHeight;
    private final float maxWidth;
    private final float maxHeight;

    public MultipleVirtualViewportBuilder(float minWidth, float minHeight, float maxWidth, float maxHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public VirtualViewPort getVirtualViewport(float width, float height) {
        if (width >= minWidth && width <= maxWidth && height >= minHeight && height <= maxHeight)
            return new VirtualViewPort(width, height, true);

        float aspect = width / height;

        float scaleForMinSize = minWidth / width;
        float scaleForMaxSize = maxWidth / width;

        float virtualViewportWidth = width * scaleForMaxSize;
        float virtualViewportHeight = virtualViewportWidth / aspect;

        if (insideBounds(virtualViewportWidth, virtualViewportHeight))
            return new VirtualViewPort(virtualViewportWidth, virtualViewportHeight, false);

        virtualViewportWidth = width * scaleForMinSize;
        virtualViewportHeight = virtualViewportWidth / aspect;

        if (insideBounds(virtualViewportWidth, virtualViewportHeight))
            return new VirtualViewPort(virtualViewportWidth, virtualViewportHeight, false);

        return new VirtualViewPort(minWidth, minHeight, true);
    }

    private boolean insideBounds(float width, float height) {
        if (width < minWidth || width > maxWidth)
            return false;
        if (height < minHeight || height > maxHeight)
            return false;
        return true;
    }

}
