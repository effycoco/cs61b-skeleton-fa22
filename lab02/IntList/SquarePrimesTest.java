package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesAtEnds(){
        IntList lst = IntList.of(3,6,13,4,17);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 6 -> 169 -> 4 -> 289", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesNoPrime(){
        IntList lst = IntList.of(4,18,32,20,12);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 18 -> 32 -> 20 -> 12",lst.toString());
        assertFalse(changed);
    }
}
