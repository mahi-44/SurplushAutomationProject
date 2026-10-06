package surplushutility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Javautility {

    public int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(100000);
    }

    public String generateRandomEmail() {
        return "testuser" + generateRandomNumber() + "@example.com";
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date());
    }

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        return sdf.format(new Date());
    }
}