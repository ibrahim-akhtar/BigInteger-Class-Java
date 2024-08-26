import java.util.*;

public class BigInteger
{
    private String number;

    // Non-parameterized constructor
    public BigInteger()
    {
        this.number = "0";
    }

    // Parameterized constructor
    public BigInteger(String number)
    {
        if (isValid(number))
        {
            this.number = number;
        }
        else
        {
            System.out.println("Invalid input: The number must only contain digits 0-9.");
            System.exit(1);
        }
    }

    // Method to check validity of the entered number
    private boolean isValid(String number)
    {
        if (number.isEmpty())
        {
            return false;
        }
        
        int startIndex = 0;
        if (number.charAt(0) == '-')
        {
            if (number.length() == 1)   // A single '-' is not valid
            {
                return false;
            }
            
            startIndex = 1;
        }
    
        for (int i = startIndex; i < number.length(); i++)
        {
            if (!Character.isDigit(number.charAt(i)))
            {
                return false;
            }
        }
        
        return true;
    }


    // Getter for the number
    public String getNumber()
    {
        return number;
    }

    // Method to get the length of the number
    public int length()
    {
        return number.length();
    }

    // Method to add two BigInteger objects
    public BigInteger add(BigInteger other)
    {
        String num1 = this.number;
        String num2 = other.getNumber();

        StringBuilder result = new StringBuilder();
        int len1 = num1.length();
        int len2 = num2.length();
        int maxLength = Math.max(len1, len2);

        // Pad the shorter number with leading zeros
        while (len1 < maxLength)
        {
            num1 = "0" + num1;
            len1++;
        }

        while (len2 < maxLength)
        {
            num2 = "0" + num2;
            len2++;
        }

        // Initially set the carry as 0
        int carry = 0;

        // Perform the addition from right to left
        for (int i = maxLength - 1; i >= 0; i--)
        {
            int digit1 = num1.charAt(i) - '0';
            int digit2 = num2.charAt(i) - '0';
            int sum = digit1 + digit2 + carry;
            carry = sum / 10;
            result.insert(0, sum % 10);
        }

        // If there's a carry left, prepend it to the result
        if (carry != 0)
        {
            result.insert(0, carry);
        }

        return new BigInteger(result.toString());
    }

    
    // Method to subtract another BigInteger from this BigInteger
    public BigInteger subtract(BigInteger other)
    {
        String num1 = this.number;
        String num2 = other.getNumber();
    
        // If both numbers are negative
        if (num1.charAt(0) == '-' && num2.charAt(0) == '-')
        {
            // Negate both numbers and swap them
            return new BigInteger(num2.substring(1)).subtract(new BigInteger(num1.substring(1)));
        }
    
        // If the first number is negative and the second is positive
        if (num1.charAt(0) == '-' && num2.charAt(0) != '-')
        {
            // Subtracting a positive from a negative is the same as adding their absolute values and making the result negative
            BigInteger result = new BigInteger(num1.substring(1)).add(new BigInteger(num2));
            return new BigInteger("-" + result.getNumber());
        }
    
        // If the first number is positive and the second is negative
        if (num1.charAt(0) != '-' && num2.charAt(0) == '-')
        {
            // Subtracting a negative from a positive is the same as adding their absolute values
            return this.add(new BigInteger(num2.substring(1)));
        }
    
        // Handle the case where both numbers are positive or both are negative (but negation has already been handled)
        StringBuilder result = new StringBuilder();
        boolean negative = false;
    
        // Determine if the result will be negative
        if (num1.length() < num2.length() || (num1.length() == num2.length() && num1.compareTo(num2) < 0))
        {
            String temp = num1;
            num1 = num2;
            num2 = temp;
            negative = true;
        }
    
        int len1 = num1.length();
        int len2 = num2.length();
        int borrow = 0;
    
        // Pad the shorter number with leading zeros
        while (len2 < len1)
        {
            num2 = "0" + num2;
            len2++;
        }
    
        // Perform the subtraction from right to left
        for (int i = len1 - 1; i >= 0; i--)
        {
            int digit1 = num1.charAt(i) - '0' - borrow;
            int digit2 = num2.charAt(i) - '0';
    
            if (digit1 < digit2) {
                digit1 += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
    
            result.insert(0, digit1 - digit2);
        }
    
        // Remove leading zeros
        while (result.length() > 1 && result.charAt(0) == '0')
        {
            result.deleteCharAt(0);
        }
    
        // If the result is negative, prepend a - sign
        return new BigInteger((negative ? "-" : "") + result.toString());
    }


    // Method to multiply two BigInteger objects
    public BigInteger multiply(BigInteger other)
    {
        String num1 = this.number;
        String num2 = other.getNumber();
        int len1 = num1.length();
        int len2 = num2.length();

        int[] result = new int[len1 + len2];

        for (int i = len1 - 1; i >= 0; i--)
        {
            for (int j = len2 - 1; j >= 0; j--)
            {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int sum = mul + result[i + j + 1];
                result[i + j] += sum / 10;
                result[i + j + 1] = sum % 10;
            }
        }

        StringBuilder resultStr = new StringBuilder();
        for (int num : result)
        {
            if (!(resultStr.length() == 0 && num == 0))
            {
                resultStr.append(num);
            }
        }

        return new BigInteger(resultStr.length() == 0 ? "0" : resultStr.toString());
    }

    // Method to get the quotient of two BigInteger objects
    public BigInteger quotient(BigInteger other)
    {
        if (other.getNumber().equals("0"))
        {
            throw new ArithmeticException("Division by zero");
        }

        BigInteger zero = new BigInteger("0");
        BigInteger quotient = new BigInteger("0");
        BigInteger remainder = new BigInteger(this.number);

        // Will run as long as remainder is greater than or equal to the divisor (other)
        while (remainder.compareTo(other) >= 0)
        {
            // temporary quotient (tempQuotient) is initialized to 1
            BigInteger tempQuotient = new BigInteger("1");
            // temporary divisor (temp) is initialized to other
            BigInteger temp = other;

            // shifts the divisor and quotient left (i.e. increasing their magnitude)
            // until the reaminder is no longer >= shifted divisor
            while (remainder.compareTo(temp.multiply(new BigInteger("10"))) >= 0)
            {
                // multiplies temp by 10 and tempQuotient by 10 in order
                //to shift the divisor and quotient to the left (increasing their magnitudes)
                temp = temp.multiply(new BigInteger("10"));
                tempQuotient = tempQuotient.multiply(new BigInteger("10"));
            }

            quotient = quotient.add(tempQuotient);
            remainder = remainder.subtract(temp);
        }

        return quotient;
    }

    // Method to get the remainder of two BigInteger objects
    // Similar to quotient method except for storing quotient part...
    public BigInteger remainder(BigInteger other)
    {
        if (other.getNumber().equals("0"))
        {
            throw new ArithmeticException("Division by zero");
        }

        BigInteger remainder = new BigInteger(this.number);

        while (remainder.compareTo(other) >= 0)
        {
            BigInteger temp = other;

            while (remainder.compareTo(temp.multiply(new BigInteger("10"))) >= 0)
            {
                temp = temp.multiply(new BigInteger("10"));
            }

            remainder = remainder.subtract(temp);
        }

        return remainder;
    }

    // Method to compare two BigInteger objects
    public int compareTo(BigInteger other)
    {
        boolean thisNegative = this.number.charAt(0) == '-';
        boolean otherNegative = other.getNumber().charAt(0) == '-';

        // Case 1: this is negative, and other is positive
        if (thisNegative && !otherNegative)
        {
            return -1;
        }

        // Case 2: this is positive, and other is negative
        if (!thisNegative && otherNegative)
        {
            return 1;
        }

        // Both numbers are either positive or negative
        String thisNumber = thisNegative ? this.number.substring(1) : this.number;
        String otherNumber = otherNegative ? other.getNumber().substring(1) : other.getNumber();

        // Compare lengths first if both are positive or negative
        int lengthComparison = Integer.compare(thisNumber.length(), otherNumber.length());

        if (lengthComparison != 0)
        {
            return thisNegative ? -lengthComparison : lengthComparison;
        }

        // Compare lexicographically if lengths are the same
        int lexicographicComparison = thisNumber.compareTo(otherNumber);

        // If both numbers are negative, invert the result
        return thisNegative ? -lexicographicComparison : lexicographicComparison;
    }

    
    // Method to calculate power of BigIntegers: base ^ exponent
    public BigInteger power(BigInteger exponent)
    {
        // Convert exponent to integer for simpler manipulation
        int exp = Integer.parseInt(exponent.getNumber());

        // Base case: if exponent is 0, return 1
        if (exp == 0)
        {
            return new BigInteger("1");
        }

        // Base case: if exponent is 1, return the base itself
        if (exp == 1)
        {
            return new BigInteger(this.number);
        }

        // Recursive case: compute base ^ (exp / 2)
        // helps break down the problem into smaller sub-problems
        BigInteger halfPower = power(new BigInteger(String.valueOf(exp / 2)));

        // If exponent is even: result = halfPower * halfPower
        if (exp % 2 == 0)
        {
            return halfPower.multiply(halfPower);
        }
        else
        {
            // If exponent is odd: result = halfPower * halfPower * base
            return halfPower.multiply(halfPower).multiply(new BigInteger(this.number));
        }
    }

    // Main method
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit)
        {
            System.out.println("\nMenu:");
            System.out.println("1. Add");
            System.out.println("2. Subtract");
            System.out.println("3. Multiply");
            System.out.println("4. Divide (Quotient)");
            System.out.println("5. Remainder");
            System.out.println("6. Compare");
            System.out.println("7. Power");
            System.out.println("8. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // To consume newline left by nextInt()

            switch (choice)
            {
                case 1:
                    performAddition(sc);
                    break;
                case 2:
                    performSubtraction(sc);
                    break;
                case 3:
                    performMultiplication(sc);
                    break;
                case 4:
                    performDivision(sc);
                    break;
                case 5:
                    performRemainder(sc);
                    break;
                case 6:
                    performComparison(sc);
                    break;
                case 7:
                    performPower(sc);
                    break;
                case 8:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        }

        sc.close();
    }

    // Helper method to get a valid BigInteger input from user
    private static BigInteger getInputBigInteger(Scanner sc, String message)
    {
        System.out.print(message);
        String input = sc.nextLine();
        return new BigInteger(input);
    }

    // Method to perform addition
    private static void performAddition(Scanner sc)
    {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger: ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger: ");
        
        BigInteger sum = bigInt1.add(bigInt2);
        System.out.println("Sum: " + sum.getNumber() + ", Length: " + sum.length());
    }

    // Method to perform subtraction
    private static void performSubtraction(Scanner sc)
    {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger: ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger: ");
        
        BigInteger difference = bigInt1.subtract(bigInt2);
        System.out.println("Difference: " + difference.getNumber() + ", Length: " + difference.length());
    }

    // Method to perform multiplication
    private static void performMultiplication(Scanner sc)
    {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger: ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger: ");
        
        BigInteger product = bigInt1.multiply(bigInt2);
        System.out.println("Product: " + product.getNumber() + ", Length: " + product.length());
    }

    // Method to perform division (quotient)
    private static void performDivision(Scanner sc)
    {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger (dividend): ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger (divisor): ");
        
        try
        {
            BigInteger quotient = bigInt1.quotient(bigInt2);
            System.out.println("Quotient: " + quotient.getNumber() + ", Length: " + quotient.length());
        }
        catch (ArithmeticException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to perform remainder calculation
    private static void performRemainder(Scanner sc) {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger (dividend): ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger (divisor): ");
        
        try
        {
            BigInteger remainder = bigInt1.remainder(bigInt2);
            System.out.println("Remainder: " + remainder.getNumber() + ", Length: " + remainder.length());
        }
        catch (ArithmeticException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to perform comparison
    private static void performComparison(Scanner sc)
    {
        BigInteger bigInt1 = getInputBigInteger(sc, "Enter the 1st BigInteger: ");
        BigInteger bigInt2 = getInputBigInteger(sc, "Enter the 2nd BigInteger: ");
        
        int comparison = bigInt1.compareTo(bigInt2);
        
        if (comparison > 0)
        {
            System.out.println("First Number is greater than Second Number.");
        }
        else if (comparison < 0)
        {
            System.out.println("First Number is less than Second Number.");
        }
        else
        {
            System.out.println("First Number is equal to Second Number.");
        }
    }

    // Method to perform power calculation
    private static void performPower(Scanner sc)
    {
        BigInteger base = getInputBigInteger(sc, "Enter the base BigInteger: ");
        BigInteger exponent = getInputBigInteger(sc, "Enter the exponent BigInteger (within integer limits): ");
        
        BigInteger pow = base.power(exponent);
        System.out.println(base.getNumber() + " ^ " + exponent.getNumber() + " = " + pow.getNumber() + ", Length: " + pow.length());
    }
    
}