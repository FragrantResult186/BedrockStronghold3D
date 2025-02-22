package fragrant.finder;

public class BE_Random {
    public static int[] genNums(int seed, int n) {
        int[] mt = new int[n];
        int[] state = new int[n + 397];
        state[0] = seed;

        for (int i = 1; i < state.length; i++) {
            state[i] = (int) ((0xFFFFFFFFL & (0x6C078965 * (state[i - 1] ^ (state[i - 1] >>> 30)))) + i);
        }

        for (int i = 0; i < n; i++) {
            int y = (state[i] & 0x80000000) | (state[i + 1] & 0x7fffffff);
            mt[i] = (y >>> 1) ^ ((y & 1) == 0 ? 0 : 0x9908B0DF) ^ state[i + 397];
        }

        for (int i = 0; i < n; i++) {
            int y = mt[i];
            y ^= y >>> 11;
            y ^= (y << 7) & 0x9D2C5680;
            y ^= (y << 15) & 0xEFC60000;
            mt[i] = y ^ (y >>> 18);
        }

        return mt;
    }

    public static double int2Float(int x) {
        return (x & 0xFFFFFFFFL) * 2.328306436538696e-10;
    }

    public static int uMod(int a, int n) {
        return (int) ((a & 0xFFFFFFFFL) % n);
    }
}
