import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class Main {

    private static float principle;
    private static float years;
    private static float rate;
    private static float downpayment;

    public Main(){
        principle = 0;
        years = 0;
        rate = 0;
    }


    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String[] input = line.split(Pattern.quote("~"));
            principle = Float.parseFloat(input[0]);
            years = Float.parseFloat(input[1]);
            rate = Float.parseFloat(input[2]);
            downpayment = Float.parseFloat(input[3]);
            double payment = calculatePayments();
            int interest = calculateInterest(payment);
            System.out.println("$" + String.format( "%.2f", payment ) + "~$" + interest );

        }
    }

    private static double calculatePayments(){
        double payment;
        double loan_amount = principle - downpayment;
        double monthly_rate;
        monthly_rate = rate/12;
        double period = years*12;
        double numerator = (monthly_rate/100) * loan_amount;
        double denominator = (1 - (1/(Math.pow((1+(monthly_rate/100)), (period)))));
        payment = numerator/denominator;
//        System.out.println(payment);
        return payment;
    }

    private static int calculateInterest(double payment){
        double total_payment;
        double interest;
        total_payment = payment * years * 12;
        interest = total_payment - (principle - downpayment);
//        System.out.println((int) Math.round(interest));
        return (int) Math.round(interest);
    }
}
