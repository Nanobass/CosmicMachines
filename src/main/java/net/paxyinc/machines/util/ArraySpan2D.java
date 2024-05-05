package net.paxyinc.machines.util;

public record ArraySpan2D(int x, int y, int width, int height, int stride) {

    public int total() {
        return stride * height;
    }

}
