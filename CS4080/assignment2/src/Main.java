public class Main {
    public static void main(String[] args) {
        int i, k;
        int j = 0;
        /*
        k = (j + 13) / 27
        loop:
            if k > 10 then goto out
            k = k + 1
            i = 3 * k - 1
        goto loop
        out: …
        */
        for(k = (j+13)/27; k <= 10;){
            k++;
            i = 3*k -1;
        }


        /*
            if((k == 1) || (k == 2)) j = 2 * k - 1
            if((k == 3) || (k == 5)) j = 3 * k + 1
            if(k == 4) j = 4 * k - 1
            if ((k == 6) || (k == 7) || (k == 8)) j = k - 2
        */
        switch (k){
            case 1:
            case 2:
                j = 2*k-1;
                break;
            case 3:
            case 5:
                j = 3*k+1;
                break;
            case 4:
                j = 4 * k - 1;
                break;
            case 6:
            case 7:
            case 8:
                j = k - 2;
                break;
        }
    }
}
