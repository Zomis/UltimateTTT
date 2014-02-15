package net.zomis.tttultimate.dry;

public interface TTFactory {
	TTBase construct(TTBase parent, int x, int y);
}
