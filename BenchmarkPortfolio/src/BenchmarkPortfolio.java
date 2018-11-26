package src;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class BenchmarkPortfolio {

    private static List<List<String>> portfolio_list;
    private static List<List<String>> benchmark_list;
    private static List<Transaction> transaction_list;


    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        String line;
        portfolio_list = new ArrayList<>();
        benchmark_list = new ArrayList<>();
        List<Asset> portfolio = new ArrayList<>();
        List<Asset> benchmark = new ArrayList<>();
        double portfolio_total = 0;
        double benchmark_total = 0;
        while ((line = in.readLine()) != null) {
            String[] input = line.split(Pattern.quote(":"));
            portfolio_list = getList(input[0]);
//            System.out.println(portfolio);
            benchmark_list = getList(input[1]);
//            System.out.println(benchmark);

            for (List<String> list: portfolio_list){
                Asset asset = new Asset(list);
                portfolio.add(asset);
                portfolio_total = portfolio_total + asset.getAssetValue();
            }
//            System.out.println(portfolio_list);

            for (List<String> list: benchmark_list){
                Asset asset = new Asset(list);
                benchmark.add(asset);
                benchmark_total = benchmark_total + asset.getAssetValue();
            }
//            System.out.println(benchmark_list);
            portfolio.sort(Comparator.comparing(Asset::getAssetName));
            benchmark.sort(Comparator.comparing(Asset::getAssetName));
            Iterator<Asset> port = portfolio.iterator();
            Iterator<Asset> bench = benchmark.iterator();
            Transaction trans;
            transaction_list = new ArrayList<>();

            while (port.hasNext() && bench.hasNext()) {
                double diff = 0;
                Asset p = port.next();
                Asset b = bench.next();
                if (p.getAssetName().equals(b.getAssetName())) {
                    p.calculateAssetPercentage(portfolio_total);
                    b.calculateAssetPercentage(benchmark_total);
                    if (!p.equals(b)) {
                        diff = p.getAssetValue() - b.getAssetValue();
//                        System.out.println(diff);
                        if(p.getAsset_type().equals("STOCK")) {
                            trans = new Transaction();
                            getTransaction(trans, diff, p);
                            transaction_list.add(trans);
                        }
                        else if (p.getAsset_type().equals("BOND")){
                            trans = new Transaction();
                            diff = (p.getNumShares()*p.getPrice())-(b.getNumShares()*p.getPrice()) ;
                            getTransaction(trans, diff, p);
                            transaction_list.add(trans);
                        }
                    }

                }

                else if (!p.getAssetName().equals(b.getAssetName())){
//                    Asset is not in benchmark, so selling the number of shares
                    trans = new Transaction();
                    diff  = p.getNumShares();
                    getTransaction(trans, diff, p);
                    transaction_list.add(trans);
                }
            }
            transaction_list.sort(Comparator.comparing(Transaction::getAsset_name));
            for (Transaction result: transaction_list) {
                result.printTransaction();
            }
            transaction_list = null;
        }
    }

    private static void getTransaction(Transaction trans, double diff, Asset p) {
        if (diff < 0) {
            trans.setSide("BUY");
            trans.setAsset_name(p.getAssetName());
            trans.setNum_shares(trans.calculateNumShares(diff, p.getPrice()));
        } else if (diff > 0) {
            trans.setSide("SELL");
            trans.setAsset_name(p.getAssetName());
            trans.setNum_shares(trans.calculateNumShares(diff, p.getPrice()));
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

class Transaction{
    private String side;
    private String asset_name;
    private int num_shares;

    public Transaction(){
        side = "";
        asset_name = "";
        num_shares = 0;

    }

    public double getNum_shares() {
        return num_shares;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public void setNum_shares(double num_shares) {
        this.num_shares = (int) num_shares;
    }

    public double calculateNumShares(double difference, double price){
        double num;
        num = Math.abs(difference)/price;
        return num;
    }

    public void printTransaction(){
        System.out.println(side+","+asset_name+","+num_shares);
    }

}

class Asset{

    private String asset_name;
    private String asset_type;
    private float num_shares;
    private float price;
    private float accrued_interest;
    private double asset_val;
    private double asset_percent;

    public Asset(){}

    public Asset(List<String> asset_list) {
        this.asset_name = asset_list.get(0);
        this.asset_type = asset_list.get(1);
        this.num_shares = Float.parseFloat(asset_list.get(2));
        this.price = Float.parseFloat(asset_list.get(3));
        this.accrued_interest = Float.parseFloat(asset_list.get(4));
    }

    public double getAssetValue(){
        if (asset_type.equals("STOCK")){
            this.asset_val = this.num_shares * this.price;
        }
        else if (asset_type.equals("BOND")){
            this.asset_val = this.num_shares * (this.price + this.accrued_interest) * 0.01;
        }
        return this.asset_val;
    }

    public void calculateAssetPercentage(double total_val){
        this.asset_percent = this.asset_val/total_val;
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

    public String getAssetName() {
        return asset_name;
    }

    public float getNumShares() {
        return num_shares;
    }

    public double getAsset_percent() {
//        System.out.println(asset_percent);
        return asset_percent;
    }

    public float getPrice() {
        return price;
    }

    public String getAsset_type(){
        return asset_type;
    }

    public float getAccrued_interest() {
        return accrued_interest;
    }


}


