package net.zomis.tttultimate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Connect4Test {

    @Test
    public void winnables() {
        TTBase board = new TTFactories().classicMNK(7, 6, 4);
        assertEquals(7 + 6 + 6*2, board.getWinConds().size());
    }

}
