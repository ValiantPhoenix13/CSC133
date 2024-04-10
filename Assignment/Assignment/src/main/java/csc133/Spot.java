package csc133;

import org.joml.Vector4f;

public class Spot {
    public static final int MAX_ROWS = 20;
    public static final int MAX_COLS = 18;
    public static final Vector4f liveColor = new Vector4f(0f, 1f, 0f, 1f);
    public static final Vector4f deadColor = new Vector4f(1f,0f,0f,1f);
    public static final int VPS = 4;
    public static final int FPS = 2;
    public static final int IPS = 6;
    public static final float length = 30.0f;
    public static final float offsetVertical = 10.0f;
    public static final float offsetHorizontal = 10.0f;
    public static final float padding = 5.0f;
    public static int WIN_WIDTH = 900;
    public static int WIN_HEIGHT = 900;


}
