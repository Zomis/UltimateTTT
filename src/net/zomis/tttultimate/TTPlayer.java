package net.zomis.tttultimate;

public enum TTPlayer {

	NONE, X, O, XO;
	
	public TTPlayer next() {
		if (!isRealPlayer())
			throw new UnsupportedOperationException("Only possible to call .next() on a real player but it was called on " + this);
		return this == X ? O : X;
	}
	public TTPlayer and(TTPlayer other) {
		if (this == NONE || other == NONE || other == null)
			return NONE;
		if (isRealPlayer())
			return this == other ? this : NONE;
		return other;
	}
	public boolean isRealPlayer() {
		return this == X || this == O;
	}
	public static boolean isRealPlayer(TTPlayer winner) {
		return winner != null && winner.isRealPlayer();
	}
	public TTPlayer or(TTPlayer other) {
		if (this == NONE || other == null)
			return other;
		if (other == NONE)
			return this;
		if (this == XO)
			return this;
		if (this != other)
			return XO;
		return this;
	}
	
}
