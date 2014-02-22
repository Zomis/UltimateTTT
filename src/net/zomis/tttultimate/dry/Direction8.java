package net.zomis.tttultimate.dry;



public enum Direction8 {
	W(-1, 0), NW(-1, -1), N(0, -1), NE(1, -1), E(1, 0), SE(1, 1), S(0, 1), SW(-1, 1);
	// TODO: Complete Direction8 enum
	private int	dx;
	private int	dy;

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
/*	
	/**
	 * Up is down, down is up. Logic went on ski vacation with it's buddy reason.
	 * @return
	 *
	public Direction8 getOpposite() {
		return direction(0, 0, -dx, -dy);
	}
	/**
	 * Left --> Up --> Right --> Down --> Left
	 * @return A direction that has been rotated 90 degrees clockwise.
	 *
	public Direction8 getRotation90() {
		return direction(0, 0, -dy, dx);
	}
/*	
	public int getDegreeRotation() {
		switch (this) {
			
		}
	}
	
	public static Direction8 direction(int oldX, int oldY, int newX, int newY) {
		if (oldX > newX) return LEFT;
		else if (oldX < newX) return RIGHT;
		else if (oldY > newY) return UP;
		else if (oldY < newY) return DOWN;
		else return null;
	}
	
	public void addToIntPoint(IntPoint ip) {
		ip.set(ip.getX() + this.dx, ip.getY() + this.dy);
	}

	public char toChar() {
		return this.toString().charAt(0);
	}*/
}
