import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FormAutomationTest {
     WebDriver driver;

     @BeforeAll
     public void setup() {
          driver = new ChromeDriver();
          driver.manage().window().maximize();
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
     }

     @Test
     @DisplayName("Guest Registration Form Automation")
     public void guestRegistrationFormTest() throws InterruptedException {
          driver.get("https://demo.wpeverest.com/user-registration/guest-registration-form/");
          driver.findElement(By.id("first_name")).sendKeys("Sumaiyea Akter");
          driver.findElement(By.id("last_name")).sendKeys("Prema");
          driver.findElement(By.id("user_email")).sendKeys("sumaiyeaprema@gmail.com");
          driver.findElement(By.id("user_pass")).sendKeys("SecuredPass1214!");
          driver.findElement(By.id("radio_1665627729_Female")).click();
          WebElement femaleRadio = driver.findElement(By.id("radio_1665627729_Female"));
          assertTrue(femaleRadio.isSelected(), "Female radio button should be selected");

          WebElement dobInput = driver.findElement(By.id("date_box_1665628538"));
          JavascriptExecutor JS = (JavascriptExecutor) driver;
          JS.executeScript("arguments[0].value = '1999-06-03';", dobInput);
          String selectedDate = dobInput.getAttribute("value");
          System.out.println("Selected Date: " + selectedDate);




          driver.findElement(By.id("input_box_1665629217")).sendKeys("Bangladeshi");
          WebElement nationalityInput = driver.findElement(By.id("input_box_1665629217"));
          String nationality = nationalityInput.getAttribute("value");
          assertEquals("Bangladeshi", nationality, "Nationality should be Bangladeshi");

          driver.findElement(By.name("phone_1665627880")).sendKeys("8801403292");

          WebElement dropdown = driver.findElement(By.id("country_1665629257"));
          Select countrySelect = new Select(dropdown);
          countrySelect.selectByVisibleText("Bangladesh");
          WebElement selectedOption = countrySelect.getFirstSelectedOption();
          String selectedCountry = selectedOption.getText();
          assertEquals("Bangladesh", selectedCountry, "Country selection should be Bangladesh");


          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript("window.scrollBy(0,2000)");
          WebElement checkbox = driver.findElement(By.name("privacy_policy_1665633140"));
          if (checkbox.isDisplayed() && checkbox.isEnabled() && !checkbox.isSelected()) {
               checkbox.click();
          }
          assertTrue(checkbox.isSelected(), "Terms & Conditions checkbox should be checked.");


          WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));
          submitBtn.click();
          Thread.sleep(3000);


          WebElement confirmation = driver.findElement(By.id("ur-submit-message-node"));
          assertTrue(confirmation.isDisplayed(), "Success message should be visible");
          String confirmationText = confirmation.getText().trim();
          assertEquals("User successfully registered.", confirmationText,
                  "Expected success message not found");

          System.out.println("User successfully registered. Message shown: " + confirmationText);

     }


     @Test
     @DisplayName("Scrape Table Data from DSE")
     public void scrapeDSETableData() {
          try {
               driver.get("https://dsebd.org/latest_share_price_scroll_by_value.php");
               WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
               wait.until(ExpectedConditions.presenceOfElementLocated(
                       By.cssSelector("div.inner-scroll")
               ));

               JavascriptExecutor js = (JavascriptExecutor) driver;
               js.executeScript("document.querySelector('.inner-scroll').scrollTop = 10000;");
               Thread.sleep(3000);

               WebElement innerTable = driver.findElement(
                       By.cssSelector("div.inner-scroll table.shares-table")
               );


               WebElement thead = innerTable.findElement(By.tagName("thead"));
               List<WebElement> headerCells = thead.findElements(By.tagName("th"));
               for (WebElement headerCell : headerCells) {
                    System.out.print(headerCell.getText().trim() + " | ");
               }
               System.out.println("\n" + "-".repeat(150));


               List<WebElement> rows = innerTable.findElements(By.xpath(".//tbody/tr"));
               BufferedWriter writer = new BufferedWriter( new FileWriter("DSE_Share_Data.txt"));
               for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    for (WebElement cell : cells) {
                        // System.out.print(cell.getText().trim() + " | ");
                         String cellText = cell.getText().trim();
                         System.out.print(cellText + " | ");
                         writer.write(cellText + " | ");
                    }
                    System.out.println();
                    writer.write("\n");
               }
               writer.close();
               System.out.println("Data saved to DSE_Share_Data.txt");

          } catch (Exception e) {
               System.out.println("Error: " + e.getMessage());
          }
     }

     @AfterAll
     public void teardown() {
          // driver.quit();
     }
}


