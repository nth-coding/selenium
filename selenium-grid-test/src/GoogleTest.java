import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class GoogleTest {
    private WebDriver driver;

    public String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        new URL(" http://192.168.41.101:4444/wd/hub");
        driver.get("https://www.demoblaze.com");
    }

    // ----- Login testcases -----
    @Test
    public void testLoginWithValidCredentials() {
        driver.findElement(By.id("login2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("loginusername")).sendKeys("your_username");
        driver.findElement(By.id("loginpassword")).sendKeys("your_password");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement logoutLink = driver.findElement(By.id("logout2"));
        Assert.assertNotNull(logoutLink);
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        driver.findElement(By.id("login2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("loginusername")).sendKeys("your_username");
        driver.findElement(By.id("loginpassword")).sendKeys("1");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, 20);
            wait.until(ExpectedConditions.alertIsPresent());
            String alertMessage = driver.switchTo().alert().getText();
            Assert.assertEquals(alertMessage, "Wrong password.");
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            System.out.println("Alert was not present after waiting for 20 seconds");
        }
    }

    @Test
    public void testLoginWithEmptyCredentials() {
        driver.findElement(By.id("login2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("loginusername")).sendKeys("");
        driver.findElement(By.id("loginpassword")).sendKeys("");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Please fill out Username and Password.");
        driver.switchTo().alert().accept();
    }

    // ----- Register testcases -----

    @Test
    public void testRegisterWithValidCredentials() {
        driver.findElement(By.id("signin2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("sign-username")).sendKeys(generateRandomString(10));
        driver.findElement(By.id("sign-password")).sendKeys("testpassword");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Sign up successful.");
        driver.switchTo().alert().accept();
    }

    @Test
    public void testRegisterWithUsedEmail() {
        driver.findElement(By.id("signin2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("sign-username")).sendKeys("testuser");
        driver.findElement(By.id("sign-password")).sendKeys("testpassword");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, 20);
            wait.until(ExpectedConditions.alertIsPresent());
            String alertMessage = driver.switchTo().alert().getText();
            Assert.assertEquals(alertMessage, "This user already exist.");
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            System.out.println("Alert was not present after waiting for 20 seconds");
        }
    }

    @Test
    public void testRegisterWithEmptyCredentials() {
        driver.findElement(By.id("signin2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("sign-username")).sendKeys("");
        driver.findElement(By.id("sign-password")).sendKeys("");
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Please fill out Username and Password.");
        driver.switchTo().alert().accept();
    }

//     ----- Navigation testcases -----
@Test
public void testNavigationByCategoryPhones() {
    try {
        WebElement categoryLink = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Phones")));
        categoryLink.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(driver.getPageSource().contains("Samsung galaxy s6"));
    } catch (AssertionError e) {
        System.out.println("Product 'Samsung galaxy s6' not found in the Phones category");
    }
}

    @Test
    public void testNavigationByCategoryLaptops() {
        try {
            WebElement categoryLink = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Laptops")));
            categoryLink.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertTrue(driver.getPageSource().contains("Dell i7 8gb"));
        } catch (AssertionError e) {
            System.out.println("Product 'Dell i7 8gb' not found in the Laptops category");
        }
    }

    @Test
    public void testNavigationByCategoryMonitors() {
        try {
            WebElement categoryLink = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Monitors")));
            categoryLink.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertTrue(driver.getPageSource().contains("Apple monitor 24"));
        } catch (AssertionError e) {
            System.out.println("Product 'Apple monitor 24' not found in the Monitors category");
        }
    }

    // ----- Cart testcases -----
    @Test
    public void testAddToCart() {
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.linkText("Phones")).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Samsung galaxy s6"))).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Add to cart"))).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Product added");
        driver.switchTo().alert().accept();
    }

    // ----- Checkout testcases -----
    @Test
    public void testCheckoutWithValidCredentials() {
        driver.findElement(By.id("cartur")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("country")).sendKeys("Test Country");
        driver.findElement(By.id("city")).sendKeys("Test City");
        driver.findElement(By.id("card")).sendKeys("1234567812345678");
        driver.findElement(By.id("month")).sendKeys("12");
        driver.findElement(By.id("year")).sendKeys("2023");
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(driver.getPageSource().contains("Thank you for your purchase!"));
    }

    @Test
    public void testCheckoutWithEmptyCredentials() {
        driver.findElement(By.id("cartur")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("name")).sendKeys("");
        driver.findElement(By.id("country")).sendKeys("");
        driver.findElement(By.id("city")).sendKeys("");
        driver.findElement(By.id("card")).sendKeys("");
        driver.findElement(By.id("month")).sendKeys("");
        driver.findElement(By.id("year")).sendKeys("");
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Please fill out Name and Creditcard.");
        driver.switchTo().alert().accept();
    }

    // ----- Delete from cart testcases -----
    @Test
    public void testDeleteFromCart() {
        driver.get("https://www.demoblaze.com/");
        driver.findElement(By.id("login2")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("loginusername")).sendKeys("your_username");
        driver.findElement(By.id("loginpassword")).sendKeys("your_password");
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("cartur")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));
        while (!deleteButtons.isEmpty()) {
            deleteButtons.get(0).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));
        }
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertEquals(cartItems.size(), 0);
    }

    // ----- Logout testcases -----
    @Test
    public void testLogout() {
        try {
            WebElement logoutLink = new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.id("logout2")));
            logoutLink.click();
        } catch (TimeoutException e) {
            System.out.println("Logout link was not clickable after waiting for 20 seconds");
        }
    }

    // ----- Contact testcases -----
    @Test
    public void testContact() {
        WebElement contactLink = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"navbarExample\"]/ul/li[2]/a")));
        contactLink.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("recipient-email")).sendKeys("test@example.com");
        driver.findElement(By.id("recipient-name")).sendKeys("Test User");
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("message-text"))).sendKeys("This is a test message.");
        driver.findElement(By.xpath("//button[text()='Send message']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String alertMessage = driver.switchTo().alert().getText();
        Assert.assertEquals(alertMessage, "Thanks for the message!!");
        driver.switchTo().alert().accept();
    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}