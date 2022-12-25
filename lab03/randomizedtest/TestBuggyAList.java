package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeMove(){
        AListNoResizing<Integer> L1 =new AListNoResizing<>();
        L1.addLast(4);
        L1.addLast(5);
        L1.addLast(6);
        BuggyAList<Integer> L2 = new BuggyAList<>();
        L2.addLast(4);
        L2.addLast(5);
        L2.addLast(6);

        assertEquals(L1.removeLast(),L2.removeLast());
        assertEquals(L1.removeLast(),L2.removeLast());
        assertEquals(L1.removeLast(),L2.removeLast());
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(L.size(),L2.size());
            }else if(operationNumber == 2){
                // removeLast
                if(L.size()==0){
                    continue;
                }
                int last = L.removeLast();
                int last2 = L2.removeLast();
                assertEquals(last, last2);
            }else if(operationNumber == 3){
                // getLast
                if(L.size()==0){
                    continue;
                }
                int last = L.getLast();
                int last2 = L2.getLast();
                assertEquals(last,last2);
            }
        }
    }
}
