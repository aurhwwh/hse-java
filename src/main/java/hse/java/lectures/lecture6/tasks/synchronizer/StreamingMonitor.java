package hse.java.lectures.lecture6.tasks.synchronizer;

public class StreamingMonitor {
    int N, ticksPerWriter;
    int current = 0;
    int total;
    StreamingMonitor(int N,int ticksPerWriter){
        this.N=N;
        this.ticksPerWriter=ticksPerWriter;
        total=N*ticksPerWriter;
    }

}
