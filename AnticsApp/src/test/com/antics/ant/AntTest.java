package com.antics.ant;

import com.antics.data.Board;
import junit.framework.TestCase;

/**
 * Created by danmalone on 09/10/2013.
 */
public class AntTest extends TestCase {
    Board testBoard = new Board(10, 10);
    Ant testAnt = new Builder(testBoard, 0, 0);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCheckForFood() throws Exception {

        boolean testResult = testAnt.checkForFood();

        assertEquals(true, testResult);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
