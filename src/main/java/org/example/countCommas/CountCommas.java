package org.example.countCommas;

public class CountCommas {

    public int count(int n) {
        if (n < 1000) {
            return 0;
        }

        if (n < 1000000) {
            return n - 999;
        }

        int twoCount = (n - 999999) * 2;
        return twoCount + 999000;
    }
}
