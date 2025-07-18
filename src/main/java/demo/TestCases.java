package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import java.util.logging.Level;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    ChromeDriver driver;

    public TestCases() {
        System.out.println("Constructor: TestCases");

        WebDriverManager.chromedriver().timeout(30).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        // Set log level and type
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        // Set path for log file
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "chromedriver.log");

        driver = new ChromeDriver(options);

        // Set browser to maximize and wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }

    // TODO: Write Test Cases Here

      public void testCase01() {
        System.out.println("Start Test case: testCase01");

        driver.get("https://stackoverflow.com/");
        String currentURL = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentURL);

        if (currentURL.contains("stackoverflow")) {
            System.out.println("TestCase01 Passed: URL contains 'stackoverflow.'");
        } else {
            System.out.println("TestCase01 Failed: URL does not contain 'stackoverflow.'");
        }

        System.out.println("End Test case: testCase01");
    }

    // TestCase02: Search for a Specific Topic and Verify Results
// TestCase02: Search for a Specific Topic and Verify Results
// TestCase02: Login & Search functionality based on pseudo-code
public void testCase02() {
    System.out.println("Start Test case: testCase02");

    try {
        // Step 1: Open Stack Overflow
        driver.get("https://stackoverflow.com/");
        System.out.println("Opened Stack Overflow homepage.");

        // Step 2: Click login button
        WebElement loginBtn = driver.findElement(By.xpath("(//a[contains(text(),'Log in')])[2]"));
        loginBtn.click();
        System.out.println("Clicked Login button.");

        // Step 3: Click Sign-in with Google
        WebElement googleSignIn = driver.findElement(By.xpath("//div[@id='openid-buttons']/button[1]"));
        googleSignIn.click();
        System.out.println("Clicked Sign-in with Google.");

        // (Manual step required for Google CAPTCHA – cannot be automated reliably)

        // Step 4: Search for "Python list comprehension"
        driver.get("https://stackoverflow.com/"); // Go back to homepage after login
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Python list comprehension");
        searchBox.submit();
        System.out.println("Searched for 'Python list comprehension'.");

        Thread.sleep(3000);

        // Step 5: Verify presence of Python tag
        List<WebElement> pythonTags = driver.findElements(By.xpath("//a[@href='/questions/tagged/python']"));
        if (pythonTags.size() > 0) {
            System.out.println("TestCase02 Passed: Python tag is present in the search results.");
        } else {
            System.out.println("TestCase02 Failed: Python tag not found in the search results.");
        }

    } catch (Exception e) {
        System.out.println("TestCase02 Failed due to exception: " + e.getMessage());
    }

    System.out.println("End Test case: testCase02");
}

// TestCase03: Verify Tag Filtering Functionality
public void testCase03() {
    System.out.println("Start Test case: testCase03");

    try {
        // Step 1: Navigate to Stack Overflow Tags page
        driver.get("https://stackoverflow.com/tags");
        System.out.println("Opened Tags page.");

        // Step 2: (Removed unnecessary Tags click because we are already on tags page)
        Thread.sleep(2000);

        // Step 3: Click on the "javascript" tag link
        WebElement jsTagLink = driver.findElement(
            By.xpath("//a[@href='/questions/tagged/javascript' and text()='javascript']")
        );
        String tagText = jsTagLink.getText();
        System.out.println("GetElementText: " + tagText); // Required for assessment
        jsTagLink.click();
        System.out.println("Clicked on 'javascript' tag.");

        // Step 4: Verify that displayed questions are tagged with "javascript"
        Thread.sleep(2000);
        List<WebElement> tagElements = driver.findElements(
            By.xpath("//a[@href='/questions/tagged/javascript' and contains(@class, 'js-tagname-javascript')]")
        );
        boolean allTagged = true;

        for (WebElement tag : tagElements) {
            if (!tag.getText().equals("javascript")) {  // **case-sensitive check**
                allTagged = false;
                System.out.println("Found non-javascript tag: " + tag.getText());
                break;
            }
        }

        if (allTagged) {
            System.out.println("TestCase03 Passed: All displayed questions are tagged with 'javascript'.");
        } else {
            System.out.println("TestCase03 Failed: Some questions are not tagged with 'javascript'.");
        }

    } catch (Exception e) {
        System.out.println("TestCase03 Failed due to exception: " + e.getMessage());
    }

    System.out.println("End Test case: testCase03");
}


// TestCase04: Verify Sorting by "Score" on Query Page
// TestCase04: Verify Sorting by "Score" on Query Page
public void testCase04() {
    System.out.println("Start Test case: testCase04");
    try {
        driver.get("https://stackoverflow.com/questions");
        System.out.println("Opened Questions page.");

        // Click on sorting menu
        WebElement sortMenu = driver.findElement(By.xpath("//span[@class='s-btn--text' and @data-text='More']"));
        sortMenu.click();
        System.out.println("Clicked on sorting menu.");

        // Select "Score" sorting option
        WebElement scoreSort = driver.findElement(By.xpath("//a[contains(text(),'Score')]"));
        scoreSort.click();
        System.out.println("Selected 'Score' sorting option.");

        // Collect all vote elements
        List<WebElement> voteElements = driver.findElements(
            By.xpath("//div[contains(@class,'s-post-summary--stats')]//span[@class='s-post-summary--stats-item-number']")
        );

        List<Integer> votes = new ArrayList<>();
        for (WebElement e : voteElements) {
            try {
                String text = e.getText().replace(",", "").trim();
                if (!text.isEmpty() && text.matches("\\d+")) {
                    votes.add(Integer.parseInt(text));
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Skipping invalid vote value: " + e.getText());
            }
        }

        // Check descending order
        boolean isDescending = true;
        for (int i = 0; i < votes.size() - 1; i++) {
            if (votes.get(i) < votes.get(i + 1)) {
                isDescending = false;
                break;
            }
        }

        if (isDescending) {
            System.out.println("TestCase04 Passed: Votes are sorted in descending order.");
        } else {
            System.out.println("TestCase04 Failed: Votes are NOT sorted in descending order.");
        }

    } catch (Exception e) {
        System.out.println("TestCase04 Failed due to exception: " + e.getMessage());
    }
    System.out.println("End Test case: testCase04");
}




//span[@class='s-btn--text' and @data-text='More']

}


