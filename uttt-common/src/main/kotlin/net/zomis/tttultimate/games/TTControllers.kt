package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTFactories

class TTControllers {

    fun connectFour(): TTController {
        return TTClassicControllerWithGravity(TTFactories().classicMNK(7, 6, 4))
    }

    fun classicTTT(): TTController {
        return TTClassicController(TTFactories().classicMNK(3))
    }

    fun ultimateTTT(): TTController {
        return TTUltimateController(TTFactories().ultimate())
    }

}
