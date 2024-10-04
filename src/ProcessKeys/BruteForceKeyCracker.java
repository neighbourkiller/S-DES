package ProcessKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForceKeyCracker {
    // 明文和密文示例
    private static String PLAINTEXT = "10101010";
    private static String CIPHERTEXT = "11011000";
    private static String Keys = null;

/*    // 异或解密函数
    public static String xorDecrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            result.append(text.charAt(i) ^ key.charAt(i));  // XOR 逐位操作
        }
        return result.toString();
    }

    // 暴力破解函数
    public static String bruteForceKey(int start, int end) {
        for (int i = start; i < end; i++) {
            String key = String.format("%10s", Integer.toBinaryString(i)).replace(' ', '0');  // 生成10位二进制字符串
            String decryptedText = xorDecrypt(CIPHERTEXT, key);
            if (decryptedText.equals(PLAINTEXT)) {
                return key;
            }
        }
        return null;
    }

    // 多线程暴力破解
    public static void bruteForceWithThreads() throws InterruptedException {
        int numThreads = 4;  // 线程数量（可以根据 CPU 核心数调整）

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int totalKeys = 1024;  // 10 位二进制数的所有可能密钥数量

        int keysPerThread = totalKeys / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * keysPerThread;
            int end = (i == numThreads - 1) ? totalKeys : (i + 1) * keysPerThread;
            executor.submit(() -> {
                String foundKey = bruteForceKey(start, end);
                if (foundKey != null) {
                    System.out.println("找到正确密钥: " + foundKey);
                    Keys = foundKey;
                    //System.exit(0);  // 找到密钥后立即退出程序
                }
            });
        }

        // 关闭线程池并等待任务完成
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static Map<String, Long> bruteForce(String plaintext, String ciphertext) throws InterruptedException {
        PLAINTEXT = plaintext;
        CIPHERTEXT = ciphertext;

        long startTime = System.currentTimeMillis();
        // 调用多线程破解方法
        bruteForceWithThreads();
        // 结束计时
        long endTime = System.currentTimeMillis();
        long use = endTime - startTime;
        Map<String, Long> map = new HashMap<>();
        map.put(Keys, use);
        return map;
    }*/

    public static Map<Long, List<String>> bruteForce(String plaintext, String ciphertext){

        long startTime = System.currentTimeMillis();
        // 调用多线程破解方法
        List<String> Keys = bruteForce2(plaintext, ciphertext);
        // 结束计时
        long endTime = System.currentTimeMillis();
        long use = endTime - startTime;
        Map<Long, List<String>> map = new HashMap<>();
        map.put(use, Keys);
        return map;
    }


    private static List<String> bruteForce2(String plaintext, String ciphertext) {
        // 密钥空间（假设密钥长度为10位二进制）
        int keySpaceSize = 1024; // 2^10 = 1024
        boolean found = false;
        List<String> possibleKeys = new ArrayList<>();
        for (int key = 0; key < keySpaceSize; key++) {
            String keys = String.format("%10s", Integer.toBinaryString(key)).replace(' ', '0');
            String candidateCiphertext = CodeUtils.Encode(plaintext, keys); // 使用不同的密钥加密明文
            if (candidateCiphertext.equals(ciphertext)) {
                found = true;
                System.out.println("找到匹配的密钥: " + keys);
                possibleKeys.add(keys);
            }
        }

        if (!found) {
            System.out.println("没有找到匹配的密钥。");
            return null;
        }
        return possibleKeys;
    }

    public static void main(String[] args) throws InterruptedException {
        String plaintext = "10101010";  // 明文
        String ciphertext = "11001100"; // 密文
        bruteForce2(plaintext, ciphertext);

    }


}

