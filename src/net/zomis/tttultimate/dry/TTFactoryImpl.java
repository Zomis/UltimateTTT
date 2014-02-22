package net.zomis.tttultimate.dry;

public class TTFactoryImpl {
	// TODO: Return a TTController instead of a TTBase?
	private static final TTMNKParameters mnk = new TTMNKParameters(3, 3, 3);
	private static final TTMNKParameters mnkEmpty = new TTMNKParameters(0, 0, 0);
	
	private static final TTFactory lastFactory = new TTFactory() {
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, mnkEmpty, null);
		}
	};
	private static final TTFactory secondLastFactory = new TTFactory() {
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, parent.getMNKParameters(), lastFactory);
		}
	};
	
	public static class Factory implements TTFactory {
		private final TTMNKParameters	mnk;
		private final TTFactory	next;
		public Factory(TTMNKParameters mnk, TTFactory nextFactory) {
			this.mnk = mnk;
			this.next = nextFactory;
		}
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, mnk, next);
		}
	}
	
	public TTBase classicMNK(int width, int height, int consecutive) {
		return new TTBase(null, new TTMNKParameters(width, height, consecutive), lastFactory);
	}
	public TTBase classicMNK(int mnk) {
//		return new TTBase(null, new TTMNKParameters(1, 1, 1), new Factory(new TTMNKParameters(mnk, mnk, mnk), lastFactory));
		return classicMNK(mnk, mnk, mnk);
	}
	public TTBase ultimateWithSize(int i) {
		return new TTBase(null, mnk, secondLastFactory);
	}
	public TTBase ultimate() {
		return ultimateWithSize(3);
	}
	public TTBase othello(int size) {
		return new TTBase(null, new TTMNKParameters(size, size, size + 1), lastFactory);
	}
	

}
