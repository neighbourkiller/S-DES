package ProcessKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForceKeyCracker {
    // 明文和密文示例
    private static String PLAINTEXT = "10101010";
    private static String CIPHERTEXT = "11011000";
    private static String Keys = null;

    // 异或解密函数
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
        Map<String,Long> map=new HashMap<>();
        map.put(Keys,use);
        return map;
    }

    public static void main(String[] args) throws InterruptedException {
        // 开始计时
        long startTime = System.currentTimeMillis();

        // 调用多线程破解方法
        bruteForceWithThreads();

        // 结束计时
        long endTime = System.currentTimeMillis();
        System.out.println("破解耗时: " + (endTime - startTime) + " 毫秒");
    }


}

