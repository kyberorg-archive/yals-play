package selenium.front;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.*;
import play.test.UITest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Checks state of front page (elements and so on...)
 *
 * @since 1.0
 */
public class VisibleState extends UITest {

    @Rule // automatically takes screenshot of every failed test
    public ScreenShooter makeScreenShotsOnFail = ScreenShooter.failedTests();

    @AfterClass
    public static void closeBrowser() {
        Selenide.close();
    }

    @Before
    public void setUp() {
        Configuration.baseUrl = "http://localhost:9000";
        open("/");
    }

    @Test
    public void errorBlockIsHidden() {
        $("#error").shouldNotBe(visible);
    }

    @Test
    public void mainBlockIsVisible() {
        $("#main").shouldBe(visible);
    }

    @Test
    public void resultBlockIsHidden() {
        $("#result").shouldNotBe(visible);
    }

    @Test
    public void formHasFieldAndButton() {
        SelenideElement formField = $("form").find("input#longUrl");

        formField.shouldBe(present);
        formField.shouldHave(type("text"));

        SelenideElement button = $("form").find("button");
        button.shouldBe(present);
    }

    @Test
    public void formHasOnlyOneButton() {
        $("form").findAll("button").shouldHaveSize(1);
    }

    @Test
    public void inputAndButtonAreNotDisabled() {
        $("form").find("input#longUrl").shouldNotBe(disabled);
        $("form").find("button").shouldNotBe(disabled);
    }

    @Test
    public void inputShouldHavePlaceholder() {
        $("form").find("input#longUrl").shouldHave(attribute("placeholder"));
    }

    @Test
    public void shouldHaveCorrectTitle() {
        String title = title();
        Assert.assertEquals("Link shortener for friends", title);
    }

    @Test
    public void mainDivShouldHaveH2() {
        $("#main h2").shouldBe(present);
    }

    @Test
    public void inputFieldHasLabel() {
        SelenideElement label = $("#longUrl").parent().find("label");
        label.shouldBe(present);
        label.shouldNotBe(empty);
        label.shouldHave(attribute("for", "longUrl"));
    }

    @Test
    public void buttonIsPrimaryAndHasText() {
        $("form").find("button").has(cssClass("btn-primary")); //This class make button blue
        $("form").find("button").shouldHave(text("Shorten it!"));
    }
}
