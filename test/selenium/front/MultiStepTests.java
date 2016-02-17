package selenium.front;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.*;
import play.test.UITest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Contains multi step tests for Front page
 *
 * @since 1.0
 */
public class MultiStepTests extends UITest {
    @Rule // automatically takes screenshot of every failed test
    public ScreenShooter makeScreenShotsOnFail = ScreenShooter.failedTests();

    @AfterClass
    public static void closeBrowser() {
        Selenide.close();
    }

    @After
    public void refresh() {
        Selenide.refresh();
    }

    @Before
    public void setUp() {
        Configuration.baseUrl = "http://localhost:9000";
        Configuration.timeout = 5000;
        open("/");
    }

    @Test
    public void closeButtonReallyClosesErrorDiv() {
        pasteValueInFormAndSubmitIt(" ");

        $("#errorClose").click();
        $("div#error").shouldNotBe(visible);

    }

    @Test
    public void closeButtonClosesErrorDivButNotRemoves() {
        pasteValueInFormAndSubmitIt(" ");

        $("#errorClose").click();
        $("div#error").shouldNotBe(visible);
        $("div#error").shouldBe(present);
    }

    @Test
    public void shortenItButtonClearsResultAndValueIfVisible() {
        pasteValueInFormAndSubmitIt("http://virtadev.net");
        $("div#result").shouldBe(visible);
        $("input#longUrl").shouldBe(empty);

        pasteValueInFormAndSubmitIt("ggg");
        $("div#result").shouldNotBe(visible);
        $("#resultLink").shouldBe(empty);

    }

    private void pasteValueInFormAndSubmitIt(String link) {
        $("#longUrl").setValue(link);
        $("form").find("button").click();
    }
}
