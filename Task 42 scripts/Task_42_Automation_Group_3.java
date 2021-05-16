package com.example.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class Main {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private List<Double> rightValues = new ArrayList<Double>();
    private List<Double> leftValues = new ArrayList<Double>();

    public boolean elementHasClass(WebElement element, String active) {
        return Arrays.asList(element.getAttribute("class").split(" ")).contains(active);
    }

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","driver/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testMain() throws Exception {

        try {
            File myObj = new File("files/right_values.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                this.rightValues.add(Double.parseDouble(myReader.nextLine()));
            }
            myObj = new File("files/left_values.txt");
            myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                this.leftValues.add(Double.parseDouble(myReader.nextLine()));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // since it's not guaranteed to have the same number of left and right visits, testing the /2 number guarantees
        // we have enough values
        int howManyTests = Math.round((rightValues.size() + leftValues.size())/2);
        if (rightValues.size() != leftValues.size()) {
            howManyTests = 250; // arbitrary
        }
        double value = 11500; // just to make sure

        for (int i = 0; i < howManyTests; i++) {

            driver.get("https://p-vhernandez.github.io/index.html");

            if (elementHasClass(driver.findElement(By.xpath("//button[@type='button']")), "pull-right")) {
                value = rightValues.get(0) * 1000;
                rightValues.remove(0);
            } else {
                value = leftValues.get(0) * 1000;
                leftValues.remove(0);
            }

            Thread.sleep((int) value);
            driver.findElement(By.id("input_name")).click();
            driver.findElement(By.id("input_name")).clear();
            driver.findElement(By.id("input_name")).sendKeys("ChromeTest");
            driver.findElement(By.id("input_surname")).click();
            driver.findElement(By.id("input_surname")).clear();
            driver.findElement(By.id("input_surname")).sendKeys("Number " + (i+1));
            // driver.findElement(By.xpath("//div[3]")).click();
            driver.findElement(By.xpath("//button[@type='button']")).click();

        }
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
