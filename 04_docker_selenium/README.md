# 04: Docker and Selenium
Docker with selenium has many benefits for automation testing. Headless execution consumes less resources - with docker able to emulate virtual screen sizes if needed - and can run tests in parallel using Selenium Grid.

## Selenium container
Run a selenium container with the following command
```bash
sudo docker run -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome
```

## Maven with Selenium
This container can be interacted with during the Maven build phase:

```java
ChromeOptions chromeOptions = new ChromeOptions();
WebDriver driver = new RemoteWebDriver(new URL("htpp://172.17.0.3:4444/wd/hub"), chromeOptions);

driver.manage().window().setSize(new Dimension(1280, 1020));
```

Selenium tests can then be run headlessly.
