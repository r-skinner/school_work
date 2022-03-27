import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;


public class proj1{
    private static PrintWriter out;
    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        out = new PrintWriter("output.csv", "UTF-8");
        out.print(",");
        for (int i = 1; i <= 16; i++)
            out.print(", 2^" + i);

        String[] tests = {"Insertion", "Merge", "Quick", "Quick2", "Quick3"};
        for(String t : tests)
            try {
                test(t,false);
            } catch (InvocationTargetException e){
                System.out.println("\tPlease increase the stack size. Try 'java -Xss4m' instead of just 'java'");
            }

        for(String t : tests)
            try {
                test(t,true);
            } catch (InvocationTargetException e){
                System.out.println("\tPlease increase the stack size. Try 'java -Xss4m' instead of just 'java'");
            }

        out.close(); // */
    }
    private static void test(String name, boolean sorted) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method sort;

        switch(name){
            case "Insertion":
                sort = Sort.class.getDeclaredMethod("insertionSort", int[].class);
                break;
            case "Merge":
                sort = Sort.class.getDeclaredMethod("mergeSort", int[].class);
                break;
            case "Quick1":
                sort = Sort.class.getDeclaredMethod("quickSort", int[].class);
                break;
            case "Quick2":
                sort = Sort.class.getDeclaredMethod("quickSort2", int[].class);
                break;
            case "Quick3":
                sort = Sort.class.getDeclaredMethod("quickSort3", int[].class);
                break;

                default:
                    sort = Sort.class.getDeclaredMethod("insertionSort", int[].class);
        }

        out.print("\n" + name + "; " + (sorted? "": "un") + "sorted" );

        for(int size = 2; size <= 65536; size*=2) {
            int a[] = Sort.generateArray(size, sorted);

            System.out.print(name + ", size " + size + "\t");
            if( size <= 32)
                print(a);
            int[] b = a.clone();
            int iterations = 10000; // 4194304 iteration is when all times average out
            long timeSum = 0;
            for (int i = 0; i < iterations; i++) {
                b = a.clone();
                long startTime = System.nanoTime();
                sort.invoke(null, (Object) b);
                long endTime = System.nanoTime();
                timeSum += (endTime - startTime);
            }
            long avgTime = timeSum / iterations;
            out.print(", " + avgTime);
            out.flush();
            System.out.print("Avg time: " + avgTime +"\t\t");
            if( size <= 32) {
                print(b);
            } else{
                System.out.println();
            }
        }

    }
    private static void print(int[] a) {
        for (int i : a)
            System.out.print(i + ", ");
        System.out.println();
    }
}

class Sort{
    private static Random rand = new Random();

    static int[] generateArray(int size, boolean sorted){
        int[] a = new int[size];
        for(int i = 0; i < a.length; i++)
            a[i] = sorted ? i : rand.nextInt(size*2)-size;
        return a;
    }

    static void insertionSort(int[]a){insertionSort(a,0,a.length-1);}

    private static void insertionSort(int[] a, int low, int high){
        int i = low;
        while (i <= high){
            int target = a[i];
            int j = i++;
            while (j > low && a[j-1] > target)
                a[j] = a[--j];
            a[j] = target;
        }
    }

    static void mergeSort(int a[]){ mergeSort(a, 0, a.length-1);}

    static void mergeSort(int a[], int low, int high){
        if (low < high) {
            int middle = low + (high - low) / 2;
            mergeSort(a, low, middle);
            mergeSort(a,middle + 1, high);
            merge(a, low, middle, high);
        }
    }

    private static void merge(int a[], int low, int middle, int high){
        int tempA[] = new int[a.length];
        for (int i = low; i <= high; i++)
            tempA[i] = a[i];

        int i = low;
        int j = middle + 1;
        int k = low;

        while (i <= middle && j <= high)
            if (tempA[i] <= tempA[j])
                a[k++] = tempA[i++];
            else
                a[k++] = tempA[j++];

        while (i <= middle)
            a[k++] = tempA[i++];
    }

    static void quickSort(int a[]){ quickSort(a, 0, a.length-1);}

    private static void quickSort(int a[], int low, int high) {
        if (low < high)
        {
            int partitionIndex = partition(a, low, high);

            quickSort(a, low, partitionIndex-1);
            quickSort(a, partitionIndex+1, high);
        }
    }

    private static int partition(int a[], int low, int high){
        int pivot = a[low];
        int j = (low);
        for (int i= low + 1; i <= high; i++)
        {
            if (a[i] < pivot)
            {
                int temp = a[++j];
                a[j] = a[i];
                a[i] = temp;
            }
        }

        int temp = a[j];
        a[j] = a[low];
        a[low] = temp;

        return j;
    }

    static void quickSort2(int a[]){ quickSort2(a, 0, a.length-1);}

    private static void quickSort2(int a[], int low, int high) {
        if(high-low <= 16){
                insertionSort(a, low, high);
        }
        else if (low < high)
        {
            int partitionIndex = partition(a, low, high);

            quickSort2(a, low, partitionIndex-1);
            quickSort2(a, partitionIndex+1, high);
        }
    }

    static void quickSort3(int a[]){ quickSort3(a, 0, a.length-1);}

    private static void quickSort3(int a[], int low, int high) {
        if (low < high) {
            if (high - low > 16) {
                int randIndex = rand.nextInt(high - low) + low;
                int temp = a[low];
                a[low] = a[randIndex];
                a[randIndex] = temp;
            }
            int partitionIndex = partition(a, low, high);

            quickSort3(a, low, partitionIndex - 1);
            quickSort3(a, partitionIndex + 1, high);
        }
    }
}
