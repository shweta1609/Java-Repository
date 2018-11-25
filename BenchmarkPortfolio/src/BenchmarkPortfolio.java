package src;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class BenchmarkPortfolio {

    private static List<List<String>> portfolio;
    private static List<List<String>> benchmark;


    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        String line;
        portfolio_list = new ArrayList<>();
        benchmark_list = new ArrayList<>();
        Map<String, Double> asset_val;
        Map<String, Double> bm_val;
        List<Asset> portfolio;
        List<Asset> benchmark;
        while ((line = in.readLine()) != null) {
            String[] input = line.split(Pattern.quote(":"));
            portfolio_list = getList(input[0]);
//            System.out.println(portfolio);
            benchmark_list = getList(input[1]);
//            System.out.println(benchmark);
//            for(int i =0; i< portfolio.size(); i++){
//                asset = new Asset(portfolio.get(i));
//
//            }



        }
    }


    private static List<List<String>> getList(String input) {
        String[] val_all = input.split(Pattern.quote("|"));
        List<List<String>> result = new ArrayList<>();
        for (String s: val_all) {
            ArrayList<String> list_val =new ArrayList<>();
            String[] str_val;
            str_val = s.split(Pattern.quote(","));
            for(String v: str_val){
//            System.out.println(v);
                list_val.add(v);
            }
            result.add(list_val);
        }
        return result;
    }

}

class Asset{

    String asset_name;
    String asset_type;
    float num_shares;
    float price;
    float accrued_interest;
    double asset_val;
    double asset_percent;

    public Asset(){}

    public Asset(List<String> asset_list) {
        this.asset_name = asset_list.get(0);
        this.asset_type = asset_list.get(1);
        this.num_shares = Float.parseFloat(asset_list.get(2));
        this.price = Float.parseFloat(asset_list.get(3));
        this.accrued_interest = Float.parseFloat(asset_list.get(4));
    }

    public double getAssetValue(){
        if (this.asset_type == "STOCK"){
            this.asset_val = this.num_shares * this.price;
        }
        else if (this.asset_type == "BOND"){
            this.asset_val = this.num_shares * (this.price + this.accrued_interest) * 0.01;
        }
        return this.asset_val;
    }

    public double getAssetPercentage(double total_val){
        this.asset_percent = this.asset_val/total_val;
        return this.asset_percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Double.compare(asset.asset_percent, asset_percent) == 0 &&
                asset_name.equals(asset.asset_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset_name, asset_percent);
    }


    //    private static Map<String, List<Double>> getMktVal(List<List<String>> portfolio){
//        Map<String, Double> mkt_val = new HashMap<>();
//        double total_val = 0;
//        for(int i =0; i< portfolio.size(); i++){
//            List<String> asset;
//            List<Double> values;
//            asset = portfolio.get(i);
//            String name = asset.get(0);
//            String type = asset.get(1);
//            float shares = Float.parseFloat(asset.get(2));
//            float price = Float.parseFloat(asset.get(3));
//            float acc_int = Float.parseFloat(asset.get(4));
//            double val = 0;
//            System.out.println(name + ","  + type + "," + shares + "," + price + "," + acc_int);
//            if (type == "STOCK"){
//                val = shares*price;
//            }
//            else if (type == "BOND"){
//                val = shares * (price + acc_int) * 0.01;
//            }
//
////            mkt_val.put(name,val);
//            total_val = total_val + val;
//
//        }
//        return mkt_val;
//    }


}


