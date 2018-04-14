package net.zomis.tttultimate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Connect4Test {

    @Test
    public void winnables() {
        TTBase board = new TTFactories().classicMNK(7, 6, 4);
        assertEquals(7 + 6 + 6*2, board.getWinConds().size());
    }

    @Test
    public void smallestTiles() {
        TTBase game = new TTFactories().classicMNK(7, 6, 4);
        assertNotNull(game.getSub(6, 5));
        assertNotNull(game.getSmallestTile(6, 5));

        assertEquals(game.getSub(6, 5), game.getSmallestTile(6, 5));
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                assertEquals(game.getSub(x, y), game.getSmallestTile(x, y));
            }
        }
    }

}
