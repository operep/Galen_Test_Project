package main.java;

import freemarker.template.TemplateException;
import net.mindengine.galen.api.Galen;
import net.mindengine.galen.reports.GalenTestInfo;
import net.mindengine.galen.reports.HtmlReportBuilder;
import net.mindengine.galen.reports.model.LayoutReport;
import net.mindengine.galen.validation.ValidationListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GalenTests {

    private WebDriver driver;
    public static final String BASE_URL = "http://www.allegro.pl";
    public static final String SPECS_PATH = "specs/allegro-home-page.spec";
    public static final String SPECS_SECTION = "Header";
    public static final String REPORTS_PATH = "target/galen-html-reports";
    public static final String ERROR_MESSAGE = "Home page appearance has errors";
    
    
    @BeforeTest
    public void setUp() {
        this.driver = new FirefoxDriver();
        driver.get(BASE_URL);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testHomePageLayout() throws IOException, TemplateException {

        LayoutReport layoutReport = Galen.checkLayout(driver, SPECS_PATH, Arrays.asList(SPECS_SECTION), null, null, null);

        // Creating a list of tests
        List<GalenTestInfo> tests = new LinkedList<GalenTestInfo>();

        // Creating an object that will contain the information about the test
        GalenTestInfo test = GalenTestInfo.fromString("Home page layout test");

        // Adding layout report to the test report
        test.getReport().layout(layoutReport, "Check layout on home page");
        tests.add(test);

        // Exporting all test reports to html
        new HtmlReportBuilder().build(tests, REPORTS_PATH);

        if (layoutReport.errors() > 0) {
            throw new RuntimeException(ERROR_MESSAGE);
        }
    }
}
