package unit;

import models.Link;
import org.junit.Test;
import play.mvc.Before;
import play.test.Fixtures;
import play.test.UnitTest;

public class CoreUnitsTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }
    
    @Test
    public void storeToDbAndGetRecordBackTest() {
        String ident = "abcdef";
        String targetUrl = "http://virtalab.net"; //o, yeah! This one is really long :)

        Link.init().setIdent(ident).setLongUrl(targetUrl).validateAndSave();

        Link justStoreLink = Link.find("byIdent", ident).first();
        assertNotNull(justStoreLink);
        assertEquals("abcdef", justStoreLink.getIdent());

    }

}
