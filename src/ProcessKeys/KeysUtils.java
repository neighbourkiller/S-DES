package ProcessKeys;

public class KeysUtils {
    //密钥
    private static String Keys = "1010000010";
    private static String AF1;
    private static final int[] IP_Box = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] Left_Shift = {2, 3, 4, 5, 1};
    private static final int[] Left_Shift2 = {2, 3, 4, 5, 1};
    private static final int[] Tte = {6, 3, 7, 4, 8, 5, 10, 9};

    public static void setKeys(String keys) {
        Keys = keys;
    }
    //初始置换
    private static String Initial_Permutation() {
        return CodeUtils.ChangeBy(Keys, IP_Box);
    }
    //10-bit转8-bit
    private static String TenToEight(String afterShift) {
        return CodeUtils.ChangeBy(afterShift, Tte);
    }

    public static String K1() {
        String P10 = KeysUtils.Initial_Permutation();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < P10.length(); i++) {
            int index = Left_Shift[i % 5] - 1;
            if (i >= 5) {
                index += 5;
            }
            temp.append(P10.charAt(index));
        }
        AF1 = temp.toString();
        return TenToEight(AF1);
    }

    public static String K2() {
        K1();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < AF1.length(); i++) {
            int index = Left_Shift2[i % 5] - 1;
            if (i >= 5) {
                index += 5;
            }
            temp.append(AF1.charAt(index));
        }
        return TenToEight(temp.toString());
    }



    public static void main(String[] args) {
        System.out.println(K1());
        System.out.println(K2());
    }
}
