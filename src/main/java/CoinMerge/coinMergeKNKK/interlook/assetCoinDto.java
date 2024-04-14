package CoinMerge.coinMergeKNKK.interlook;

public class assetCoinDto {
    String name;
    double totalNum;
    long inUse;
    long available;
    long xCoinLast;

    public assetCoinDto(String n, double t, long i, long a, long x) {
        name = n; totalNum = t; inUse = i; available = a; xCoinLast = x;
    }
    public assetCoinDto(String n, double t) {
        name = n; totalNum = t;
    }

    @Override
    public String toString() {
        return "name = " + name + " totalNum = " + totalNum;
    }
}
