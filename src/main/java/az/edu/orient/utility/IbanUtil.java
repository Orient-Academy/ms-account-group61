package az.edu.orient.utility;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class IbanUtil {

    public String generateIBAN(String countryCode, String bankCode, String accountNumber) {
        String bban = bankCode + accountNumber;
        String countryCodeNumeric = convertCountryCodeToNumeric(countryCode);
        String checkDigits = "00";  // Placeholder for check digits
        String preIBAN = bban + countryCodeNumeric + checkDigits;

        // Convert preIBAN to a numeric string
        StringBuilder numericIBAN = new StringBuilder();
        for (char ch : preIBAN.toCharArray()) {
            if (Character.isDigit(ch)) {
                numericIBAN.append(ch);
            } else {
                numericIBAN.append(ch - 'A' + 10);
            }
        }

        // Calculate the check digits
        BigInteger preIBANNumber = new BigInteger(numericIBAN.toString());
        BigInteger mod97 = new BigInteger("97");
        BigInteger remainder = preIBANNumber.mod(mod97);
        int checkDigitValue = 98 - remainder.intValue();
        String checkDigitString = String.format("%02d", checkDigitValue);

        // Construct the final IBAN
        return countryCode + checkDigitString + bban;
    }

    private static String convertCountryCodeToNumeric(String countryCode) {
        StringBuilder numericCountryCode = new StringBuilder();
        for (char ch : countryCode.toCharArray()) {
            int numericValue = ch - 'A' + 10;
            numericCountryCode.append(numericValue);
        }
        return numericCountryCode.toString();
    }
}
