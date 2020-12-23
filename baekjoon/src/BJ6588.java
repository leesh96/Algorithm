import java.util.Arrays;
import java.util.Scanner;

public class BJ6588 {
    static final int MAX = 1000000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean[] prime = new boolean[MAX];
        Arrays.fill(prime, true);

        for (int i = 2; i < (int)Math.sqrt(MAX); i++){
            if (prime[i]){
                for (int j = i+i; j < MAX; j+=i) {
                    prime[j] = false;
                }
            }
        }

        while(true) {
            int num = sc.nextInt();

            if (num == 0)   return;

            for (int k = 3; k <= num; k++) {
                if (prime[k]) {
                    if (prime[num - k]) {
                        System.out.println(num + " = " + k + " + " + (num-k));
                        break;
                    }
                }
            }
        }
    }
}