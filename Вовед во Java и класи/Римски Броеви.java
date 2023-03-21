import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here
        StringBuilder number = new StringBuilder();

        while (n >= 1000) {
            number.append("M");
            n -= 1000;
        }
        while (n >= 100) {
            if (n >= 900) {
                number.append("CM");
                n = n - 900;
            } else if (n >= 500) {
                number.append("D");
                n = n - 500;
            } else if (n >= 400) {
                number.append("CD");
                n = n - 400;
            } else {
                number.append("C");
                n = n - 100;
            }
        }

        while (n >= 10) {
            if (n >= 90) {
                number.append("XC");
                n -= 90;
            } else if (n >= 50) {
                number.append("L");
                n -= 50;
            } else if (n >= 40) {
                number.append("XL");
                n -= 40;
            } else {
                number.append("X");
                n -= 10;
            }
        }

        while (n > 0) {
            if (n == 9) {
                number.append("IX");
                n -= 9;
            } else if (n >= 5) {
                number.append("V");
                n -= 5;
            } else if (n == 4) {
                number.append("IV");
                n -= 4;
            } else {
                number.append("I");
                n--;
            }
        }
        return number.toString();
    }

}
