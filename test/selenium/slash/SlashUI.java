package selenium.slash;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import play.test.UITest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Testing /(Slash) URL
 *
 * @since 1.0
 */
public class SlashUI extends UITest {
    @Rule // automatically takes screenshot of every failed test
    public ScreenShooter makeScreenShotsOnFail = ScreenShooter.failedTests();

    @AfterClass
    public static void closeBrowser() {
        Selenide.close();
    }

    @Before
    public void setUp() {
        Configuration.baseUrl = "http://localhost:9000";
        Configuration.timeout = 5000;
    }

    @Test
    public void urlWithJustSlashWillOpenFrontPage() {
        open("/");
        $("#longUrl").shouldBe(present);
        $("#shortenIt").shouldBe(present);
    }

    @Test
    public void saveLinkAndClickOnResult() {
        open("/");
        $("input#longUrl").setValue("https://vr.fi");
        $("button#shortenIt").click();

        $("a#resultLink").shouldBe(visible);
        $("#resultLink").click();

        verifyThatVROpened();
    }

    @Test
    public void saveLinkAndCopyValueAndOpenIt() {
        open("/");
        $("input#longUrl").setValue("https://vr.fi");
        $("button#shortenIt").click();

        $("a#resultLink").shouldBe(visible);
        String linkText = $("a#resultLink").getText();

        open(linkText);
        verifyThatVROpened();
    }

    @Test
    public void openSomethingNonExisting() {
        open("/perkele");
        $("h1").shouldBe(present);
        $("h1").shouldHave(text("404"));
    }

    private void verifyThatVROpened() {
        SelenideElement vrLogo = $("a.mainLogo").find("img");
        vrLogo.shouldBe(present);
        vrLogo.shouldHave(attribute("alt", "VR"));
    }
}
