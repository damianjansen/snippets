package mydriver;

import io.restassured.RestAssured;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/*
** Checks for broken links during testing.
*/
public class BrokenLinkListener implements WebDriverEventListener {


    private Map<String, String> linkList = new HashMap();
    private String linksName = "";

    /**
     * Set the name of the listener
     *
     * @param name
     */
    public void setName(String name) {
        assert(!name.isEmpty());
        linksName = "[$name]";
    }

    /**
     * Add a link check entry to the stored list
     *
     * @param driver webdriver object
     */
    private void update(WebDriver driver) {
        if (driver == null)
            return;
        driver.findElements(By.tagName("a")).stream()
            .map(e -> e.getAttribute("href")).filter(t -> !t.trim().isEmpty()).collect(Collectors.toList())
                .forEach(l -> {
            if (!linkList.containsKey(l)) {
                int code = 0;
                try {
                    code = RestAssured.given().get(l).getStatusCode();
                } catch (Exception e) {
                    code = 404;
                }
                linkList.put(l, String.valueOf(code));
                System.out.println(linksName + ":" + code + ":" + l);
            }
        });
    }

    /**
     * Return the current list of stored links
     *
     * @return mutable map of strings
     */
    public Map<String, String> getLog() {
        return linkList;
    }

    /**
     * Write out the links csv file
     *
     * @param identifier unique id of test
     */
    public void finish(String identifier) {
        File logFile = new File("target/logging/" + linksName + "_" + identifier + "_links.csv");
        logFile.getParentFile().mkdirs();
        try {
            FileOutputStream str = new FileOutputStream(logFile);
            getLog().entrySet().iterator().forEachRemaining(ent -> {
                String s = ent.getKey() + "," + ent.getValue();
                try {
                    str.write(s.getBytes());
                } catch (IOException ioe) {
                    System.out.println("Could not write to file");
                }
            });
            str.flush();
            str.close();
        } catch (Exception ex) {
            System.out.println("Could not write to file");
        }
    }

    @Override
    public void onException(Throwable p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        update(driver);
    }

    @Override
    public void afterNavigateTo(String p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void beforeClickOn(WebElement p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void afterClickOn(WebElement p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void beforeScript(String p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        update(driver);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        update(driver);
    }

    @Override
    public void afterScript(String p0, WebDriver driver) {
        update(driver);
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        update(driver);
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        update(driver);
    }

    // -- UNUSED Overrides -- //
    @Override
    public void beforeFindBy(By p0, WebElement p1, WebDriver p2) {
        // Not required
    }

    @Override
    public void afterSwitchToWindow(String p0, WebDriver driver) {
        // Not required
    }

    @Override
    public void beforeAlertDismiss(WebDriver p0) {
        // Not required
    }

    @Override
    public void afterFindBy(By p0, WebElement p1, WebDriver p2) {
        // Not required
    }

    @Override
    public void beforeGetText(WebElement p0, WebDriver driver) {
        // Not required
    }

    @Override
    public void afterNavigateRefresh(WebDriver p0) {
        // Not required
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
        // Not required
    }

    @Override
    public void afterAlertAccept(WebDriver p0) {
        // Not required
    }

    @Override
    public void beforeNavigateRefresh(WebDriver p0) {
        // Not required
    }

    @Override
    public void beforeChangeValueOf(WebElement p0, WebDriver driver, CharSequence[] p2) {
        // Not required
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> target) {
        // Not required
    }

    @Override
    public void afterGetText(WebElement p0, WebDriver driver, String p2) {
        // Not required
    }

    @Override
    public void beforeAlertAccept(WebDriver p0) {
        // Not required
    }

    @Override
    public void afterChangeValueOf(WebElement p0, WebDriver driver, CharSequence[] p2) {
        // Not required
    }

    @Override
    public void beforeSwitchToWindow(String p0, WebDriver driver) {
        // Not required
    }

    @Override
    public void afterAlertDismiss(WebDriver p0) {
        // Not required
    }

}
