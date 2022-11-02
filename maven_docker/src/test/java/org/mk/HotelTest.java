package org.mk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HotelTest {

    @Test
    public void testHotel() throws InterruptedException {
    /* TASK: Book 1 room for 4 nights (1 Deluxe Apartment)
        The 'deluxe' apartment is not on the rooms list: the 'Double Superior Room' was used instead.

          0. Instantiate selenium, navigate to page */
        System.setProperty("webdriver.chrome.driver", "src/resources/chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        String url = "https://www.clock-software.com/demo-clockpms/index.html";
        driver.get(url);

        driver.manage().window().maximize();

//        1. Select a valid date, and number of rooms and start the booking process

        WebElement date = driver.findElement(By.xpath("//input[@id='date-start']"));

        date.sendKeys("12-10-2022");

        WebElement numberOfNights = driver.findElement(By.xpath("//input[@id='to-place']"));

        numberOfNights.clear();
        numberOfNights.sendKeys("4");

        WebElement bookNowButton = driver.findElement(By.xpath("//div[@id='flights']/form[@role='form']//input[@name='commit']"));
        bookNowButton.click();
        driver.switchTo().frame("clock_pms_iframe_1"); // switch selection context to iFrame


//        2. Under Deluxe Apartment, select the most expensive package

        List<WebElement> bookingOptions = driver.findElements(By.className("btn-success"));
        bookingOptions.get(bookingOptions.size() - 1).click();


//      3. Select any 2 add-ons

        List<WebElement> inputs = driver.findElements(By.className("number-field"));
        List<WebElement> extrasPrices = driver.findElements(By.xpath("//*[contains(text(),'EUR')]"));

        inputs.get(0).sendKeys("1");
        inputs.get(2).sendKeys("1");
        String expectedExtrasPrice1 = extrasPrices.get(0).getText();
        String expectedExtrasPrice2 = extrasPrices.get(2).getText();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addButton = driver.findElement(By.xpath("//input[@value='Add the selected services >>']"));
        wait.until(ExpectedConditions.elementToBeClickable(addButton));

        addButton.submit(); // .click() is sometimes interrupted


//        4. Validate all details – Date, no of nights, room type, rate, add on (extra services charges), total

        List<WebElement> prices = driver.findElements(By.xpath("//*[contains(text(),'EUR')]"));

//      Validate date:
        String expectedArrivalDate = "12 Oct 2022";
        String actualArrivalDate = driver.findElement(By.xpath("//b[contains(text(), 'Arrival')]/following::div")).getText();

        Assertions.assertEquals(expectedArrivalDate, actualArrivalDate, "Arrival date mismatch");

        String expectedDepartureDate = "16 Oct 2022";
        String actualDepartureDate = driver.findElement(By.xpath("//b[contains(text(), 'Departure')]/following::div")).getText();

        Assertions.assertEquals(expectedDepartureDate, actualDepartureDate, "Departure date mismatch");

//      Validate number of nights:
        String expectedNumberOfNights = "4";
        String actualNumberOfNights = driver.findElement(By.xpath("//b[contains(text(), 'Stay')]/following::div")).getText();

        Assertions.assertEquals(expectedNumberOfNights, actualNumberOfNights, "Errors in 'Number of nights' calculation");

//      Validate room type:
        String expectedRoomType = "Double Superior Room";
        String actualRoomType = driver.findElement(By.xpath("//b[contains(text(), 'Room Type')]/following::div")).getText();

        Assertions.assertEquals(expectedRoomType, actualRoomType, "Room type mismatch");

//      Validate rate:
        String expectedRate =  "Package Rate Spa for Family";
        WebElement rateElement = driver.findElement(By.xpath("//b[contains(text(), 'Rate')]/following::div"));
        String actualRate = rateElement.getText();

        Assertions.assertTrue(actualRate.contains(expectedRate), "Rate mismatch");

//      Validate add-on:
        float expectedServicesPrice =
                Float.parseFloat(expectedExtrasPrice1.split(" ")[0].replace(",",""))
                + Float.parseFloat(expectedExtrasPrice2.split(" ")[0].replace(",",""));

        String extraServices = prices.get(2).getText();

        float actualServicesPrice =
                Float.parseFloat(extraServices.split(" ")[0].replace(",",""));

        Assertions.assertEquals(expectedServicesPrice, actualServicesPrice, "Addon price mismatch");

//      Validate total:

        String roomsPrice = prices.get(0).getText();
        String cityTax = prices.get(1).getText();

        String vat = prices.get(3).getText();

        float expectedTotal =
                expectedServicesPrice
                + Float.parseFloat(roomsPrice.split(" ")[0].replace(",",""))
                + Float.parseFloat(cityTax.split(" ")[0].replace(",",""));
//                + Float.parseFloat(vat.split(" ")[0].replace(",",""));

        float actualTotal = Float.parseFloat(
                driver.findElement(By.xpath("//h3[contains(text(), 'Total')]/following::h3"))
                    .getText().split(" ")[0].replace(",","")
        );

        // validation failed if VAT is included: total does not include VAT which may be unintended
        Assertions.assertEquals(expectedTotal, actualTotal, "Total prices mismatch");


//      5. Add traveler details and payment method to CC
        WebElement formEmail = driver.findElement(By.xpath("//input[@title='E-mail']"));
        formEmail.sendKeys("matej.kondrot@group.com");

        WebElement formLastName = driver.findElement(By.xpath("//input[@title='Last name']"));
        formLastName.sendKeys("Kondrot");
        WebElement formFirstName = driver.findElement(By.xpath("//input[@title='First name']"));
        formFirstName.sendKeys("Matej");
        WebElement formPhone = driver.findElement(By.xpath("//input[@title='Phone']"));
        formPhone.sendKeys("0123456789");

        WebElement creditCard = driver.findElement(By.xpath("//input[@title='Credit Card']"));
        creditCard.click();

        WebElement agree = driver.findElement(By.xpath("//input[@title='I agree with the hotel and guarantee policy']"));
        agree.click();

        WebElement createBooking = driver.findElement(By.xpath("//input[@value='Create Booking']"));
        createBooking.click();

//        6. Use a dummy Visa CC and complete payment
        WebElement cardNumber = driver.findElement(By.id("cardNumber"));
        cardNumber.sendKeys("4111111111111111");

        WebElement cardBrand = driver.findElement(By.id("credit_card_collect_purchase_brand"));
        cardBrand.sendKeys("v");

        WebElement expireMonth = driver.findElement(By.name("credit_card_collect_purchase[expire_month]"));
        expireMonth.sendKeys("1");

        WebElement expireYear = driver.findElement(By.name("credit_card_collect_purchase[expire_year]"));
        expireYear.sendKeys("2037");

        WebElement billingAddress = driver.findElement(By.id("credit_card_collect_purchase_address"));
        billingAddress.sendKeys("1 Example Street");

        WebElement billingZip = driver.findElement(By.id("credit_card_collect_purchase_zip"));
        billingZip.sendKeys("01234");

        WebElement billingCity = driver.findElement(By.id("credit_card_collect_purchase_city"));
        billingCity.sendKeys("Foo");

        WebElement billingState = driver.findElement(By.id("credit_card_collect_purchase_state"));
        billingState.sendKeys("Bar");

        WebElement billingCountry = driver.findElement(By.id("credit_card_collect_purchase_country"));
        billingCountry.sendKeys("United Kingdom");

        WebElement payButton = driver.findElement(By.xpath("//button[@type='submit']"));
        payButton.submit();

//        7. Validate Booking complete msg
        WebElement confirm = driver.findElement(By.className("text-success"));
        Assertions.assertNotNull(confirm);

        driver.quit();
    }
}
