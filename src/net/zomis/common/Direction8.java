package net.zomis.common;

public enum Direction8 {
	W(-1, 0), NW(-1, -1), N(0, -1), NE(1, -1), E(1, 0), SE(1, 1), S(0, 1), SW(-1, 1);
	
	private final int dx;
	private final int dy;

	private Direction8(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getDeltaX() {
		return dx;
	}
	public int getDeltaY() {
		return dy;
	}
}
