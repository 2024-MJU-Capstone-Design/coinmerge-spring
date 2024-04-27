package CoinMerge.coinMergeSpring.interlock;

public class assetCoinDto {
    String name;
    double totalTokenNum;
    long inUseToken;//주문 중 묶여 잇는 해당 가상자산 수량
    long availableOrderToken;//주문가능한 해당 가상자산 수량
    long coinLastTransaction;//해당 자산의 마지막 체결금액

    public assetCoinDto(String n, double t, long i, long a, long x) {
        name = n; totalTokenNum = t; inUseToken = i; availableOrderToken = a; coinLastTransaction = x;
    }
    public assetCoinDto(String n, double t) {
        name = n; totalTokenNum = t;
    }

    @Override
    public String toString() {
        return "name = " + name + " totalTokenNum = " + totalTokenNum;
    }
}
