package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        // the size; how many items
        AList<Integer> Ns = new AList<>();
        // the time required to complete all operations
        AList<Double> times = new AList<>();
        // the number of calls to addLast
        AList<Integer> opCounts = new AList<>();

        int[] testSizes = {1000, 2000, 4000, 8000, 16000, 32000, 64000,128000,256000, 1000000};
        for(int N:testSizes){
            Ns.addLast(N);

            AList<Integer> listToTest = new AList<>();
            int opCount = 0;
            Stopwatch sw = new Stopwatch();
            for(int i=0; i<N;i++){
                listToTest.addLast(i);
                opCount++;
            }
            double time = sw.elapsedTime();
            times.addLast(time);
            opCounts.addLast(opCount);
        }


        printTimingTable(Ns, times, opCounts);

    }
}
