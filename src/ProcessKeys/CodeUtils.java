package ProcessKeys;

public class CodeUtils {
    private static final int[] IP_BOX = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] FP_BOX = {4, 1, 3, 5, 7, 2, 8, 6};//IP-1
    private static final int[] EP_BOX = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int[] SP_BOX = {2, 4, 3, 1};
    private static final String[][] SBox1 = new String[][]{
            {"01", "00", "11", "10"},
            {"11", "10", "01", "00"},
            {"00", "10", "01", "11"},
            {"11", "01", "00", "10"}
    };
    private static final String[][] SBox2 = new String[][]{
            {"00", "01", "10", "11"},
            {"10", "11", "01", "00"},
            {"11", "00", "01", "10"},
            {"10", "01", "00", "11"}
    };

    //将plaintext按P置换
    public static String ChangeBy(String plaintext, int[] P) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < P.length; i++) {
            temp.append(plaintext.charAt(P[i] - 1));
        }
        return temp.toString();
    }

    private static String Initial_Permutation(String plaintext) {
        return ChangeBy(plaintext, IP_BOX);
    }

    //异或
    private static String XOR(String value1, String value2) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < value1.length(); i++) {
            temp.append((value1.charAt(i) == value2.charAt(i) ? '0' : '1'));
        }
        return temp.toString();
    }

    //4-bit扩展为8-bit
    private static String EP(String value) {
        return ChangeBy(value, EP_BOX);
    }

    //压缩8->4
    private static String SBox(String value) {
        int row = 0, col = 0;
        StringBuilder builder = new StringBuilder();

        row = 2 * Integer.parseInt(String.valueOf(value.charAt(0))) + Integer.parseInt(String.valueOf(value.charAt(3)));
        col = 2 * Integer.parseInt(String.valueOf(value.charAt(1))) + Integer.parseInt(String.valueOf(value.charAt(2)));
        builder.append(SBox1[row][col]);

        row = 2 * Integer.parseInt(String.valueOf(value.charAt(4))) + Integer.parseInt(String.valueOf(value.charAt(7)));
        col = 2 * Integer.parseInt(String.valueOf(value.charAt(5))) + Integer.parseInt(String.valueOf(value.charAt(6)));
        builder.append(SBox2[row][col]);
        return builder.toString();
    }

    private static String SP(String value) {
        return ChangeBy(value, SP_BOX);
    }

    private static String F(String s4, String s8) {
        String s = SBox(XOR(EP(s4), s8));
        return SP(s);
    }

    //加密
    public static String Encode(String PlainText, String Keys) {
        KeysUtils.setKeys(Keys);
        StringBuilder result = new StringBuilder();
        //初始置换
        String P10 = Initial_Permutation(PlainText);
        //分割为左右
        String l0 = P10.substring(0, 4);
        String r0 = P10.substring(4, 8);

        String r1 = XOR(l0, F(r0, KeysUtils.K1()));
        String l1 = r0;

        String l2 = XOR(l1, F(r1, KeysUtils.K2()));
        String r2 = r1;

        result.append(l2);
        result.append(r2);

        return ChangeBy(result.toString(), FP_BOX);

    }

    //解密
    public static String Uncode(String ciphertext, String Keys) {
        KeysUtils.setKeys(Keys);
        StringBuilder result = new StringBuilder();
        //初始置换
        String P10 = Initial_Permutation(ciphertext);
        //分割为左右
        String l0 = P10.substring(0, 4);
        String r0 = P10.substring(4, 8);

        String r1 = XOR(l0, F(r0, KeysUtils.K2()));
        String l1 = r0;

        String l2 = XOR(l1, F(r1, KeysUtils.K1()));
        String r2 = r1;

        result.append(l2);
        result.append(r2);

        return ChangeBy(result.toString(), FP_BOX);
    }
}
