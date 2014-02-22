package net.zomis.tttultimate.dry;


public class TTControllers {

	public static TTController connectFour() {
		return new TTClassicControllerWithGravity(new TTFactoryImpl().classicMNK(7, 6, 4));
	}

	public static TTController classicTTT() {
		return new TTClassicController(new TTFactoryImpl().classicMNK(3));
	}

	public static TTController ultimateTTT() {
		return new TTUltimateController(new TTFactoryImpl().ultimate());
	}

}
