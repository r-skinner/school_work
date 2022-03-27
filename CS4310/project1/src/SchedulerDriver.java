import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Random;

public class SchedulerDriver {
    static String fileName;
    public static void main(String[] args)  throws Exception {

        LinkedList<Process> processes = parseInput(args);


        Scheduler fcfs = new Scheduler(processes, "FCFS",new Comparator<>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.getPid() - p2.getPid();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        fcfs.run();

        Scheduler sjf = new Scheduler(processes, "SJF",  new Comparator<>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.getBurstTime() - p2.getBurstTime();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        sjf.run();

        Scheduler rr20 = new Scheduler(processes, "RR20", 20,  new Comparator<>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.getPid() - p2.getPid();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        rr20.run();

        Scheduler rr40 = new Scheduler(processes, "RR40", 40,  new Comparator<>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.getPid() - p2.getPid();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        rr40.run();

        Scheduler lot40 = new Scheduler(processes, "LOT40", 40, true,  new Comparator<>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p2.getPriority() - p1.getPriority();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        lot40.run();

    }
    static LinkedList<Process> copyList(LinkedList<Process> processes){
        LinkedList<Process> out = new LinkedList<>();
        for(Process p : processes ){
            out.add(p.clone());
        }
        return out;
    }

    private static LinkedList<Process> parseInput(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("Invalid input.\nTry 'java Scheduler {inputdata.txt}'");
        }

        LinkedList<Process> p = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            p = new LinkedList<>();
            while (scanner.hasNextLine()) {
                p.addLast(new Process(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
            }
        } catch (FileNotFoundException e) {
            System.out.printf("[%s] not found.", args[0]);
            System.exit(-1);
        } catch (NoSuchElementException ignored){
        }
        fileName = args[0].split(".txt")[0];
        return p;
    }
}

class Scheduler{
    private final int CONTEXT_SWITCHING_COST = 3;
    private int cpuTime = 0;
    private LinkedList<Process> processes;
    private String schedulerName;
    private int burstTime;
    private boolean isLottery;


    Scheduler(LinkedList<Process> processes, String schedulerName, int burstTime, boolean isLottery, Comparator<Process> processComparator) {
        this.processes = SchedulerDriver.copyList(processes);
        this.processes.sort(processComparator);
        this.schedulerName = schedulerName;
        this.burstTime = burstTime;
        this.isLottery = isLottery;
    }

    Scheduler(LinkedList<Process> processes, String schedulerName, Comparator<Process> processComparator) {
        this(processes,schedulerName, -1, false, processComparator);
    }
    
    Scheduler(LinkedList<Process> processes, String schedulerName, int burstTime, Comparator<Process> processComparator) {
        this(processes,schedulerName, burstTime, false, processComparator);
    }

    void run() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(schedulerName + "-" + SchedulerDriver.fileName + ".csv", "UTF-8");
        writer.write("CpuTime, PID, StartingBurstTime, EndingBurstTime, CompletionTime\n");

        while (processes != null && !processes.isEmpty()) {
            int lotteryMax = 0;
            int lotteryRand = 0;
            if (isLottery) {
                for (Object p : processes.toArray()) {
                    lotteryMax += ((Process) p).getPriority();
                }

                Random random = new Random(123456789);
                lotteryRand = random.nextInt(lotteryMax);
            }
            for (Object p : processes.toArray()) {
                Process process = (Process) p;

                if(isLottery)
                    lotteryRand -= process.getPriority();

                if(!isLottery || lotteryRand < 0) {
                    int startingTime = cpuTime, startingBurst = process.getBurstTime();

                    if(burstTime > 0 && burstTime < process.getBurstTime()){
                        cpuTime += (burstTime);
                        process.runFor(burstTime);

                    }else{
                        cpuTime += process.getBurstTime();
                        process.runFor(process.getBurstTime());
                    }

                    int completionTime;
                    if (process.getBurstTime() <= 0) {
                        processes.remove(process);
                        completionTime = cpuTime;
                    } else {
                        completionTime = 0;
                    }
                    cpuTime += CONTEXT_SWITCHING_COST;

                    writer.printf("%d,%d,%d,%d,%d\n", startingTime, process.getPid(), startingBurst, process.getBurstTime(), completionTime);

                }
            }
        }
        writer.close();
    }
}

class Process{
    private int pid, burstTime, priority;
    Process(int pid, int burst_time, int priority){
        this.pid = pid;
        this.burstTime = burst_time;
        this.priority = priority;
    }

    int getBurstTime() {
        return burstTime;
    }
    void runFor(int burstTime) {
        this.burstTime -= burstTime;
    }

    int getPid() {
        return pid;
    }

    int getPriority() {
        return priority;
    }

    protected Process clone(){
        return new Process(pid,burstTime,priority);
    }
    @Override
    public String toString() {
        return pid + ", " + burstTime + ", " + priority;
    }
}
