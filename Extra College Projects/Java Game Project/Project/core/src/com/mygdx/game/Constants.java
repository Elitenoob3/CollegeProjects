package com.mygdx.game;

public final class Constants {
    //For switching between menus
    public static final int MENU = 1;
    public static final int GAME = 2;
    public static final int CONTINUE = 3;
    public static final int OPTIONS = 4;
    public static final int WIN_SCREEN = 5;

    //For Box2D collision masking
    public static final short BIT_WALLS = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SCANNER = 4;
    public static final short BIT_LEVEL = 8;
    public static final short BIT_END = 16;

    //For Scaling to match Box2D scaling
    public static final float SCALE = 0.1F;
}
