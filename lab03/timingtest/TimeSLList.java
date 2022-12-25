package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        // the size; how many items
        AList<Integer> Ns = new AList<>();
        // the time required to complete all operations
        AList<Double> times = new AList<>();
        // the number of calls to addLast
        AList<Integer> opCounts = new AList<>();

        int[] testSizes = {1000, 2000, 4000, 8000, 16000, 32000, 64000,128000};

        for(int N:testSizes){
            Ns.addLast(N);

            SLList<Integer> toTest = new SLList<>();
            for(int i=0;i<N;i++){
                toTest.addLast(i);
            }

            Stopwatch sw = new Stopwatch();
            int M = 10000;
            for(int i=0;i<M;i++){
                toTest.getLast();
            }
            double time = sw.elapsedTime();

            times.addLast(time);
            opCounts.addLast(M);
        }

        printTimingTable(Ns, times, opCounts);
    }

}
