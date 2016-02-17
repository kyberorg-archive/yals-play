package unit;

import controllers.Store;
import models.Link;
import org.junit.Test;
import play.mvc.Before;
import play.test.Fixtures;
import play.test.UnitTest;
import util.IdentGenerator;

/**
 * Storage sub-system tests
 *
 * @since 1.0
 */
public class StoreUnitsTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

        @Test
        public void testGenerator() {
        String str1 = IdentGenerator.generateNewIdent();
        String str2 = IdentGenerator.generateNewIdent();

        assertNotNull("Ident 1 MUST be not NULL", str1);
        assertNotNull("Ident 2 MUST be not NULL", str2);

        assertFalse("Idents MUST be not equal", str1.equals(str2));

        int minLength = 5;
        assertTrue("Ident 1 MUST be longer than 5 chars", str1.length() >= minLength);
        assertTrue("Ident 2 MUST be longer than 5 chars", str2.length() >= minLength);

        int maxLength = 255;
        assertTrue("Ident 1 MUST be longer than 5 chars", str1.length() < maxLength);
        assertTrue("Ident 2 MUST be longer than 5 chars", str2.length() < maxLength);

        //TODO char set
    }

    @Test
    public void testIfIdentAlreadyExists() {

        //Generate and store
        String ident1 = IdentGenerator.generateNewIdent();
        Link.init().setIdent(ident1).setLongUrl("http://domain.example").validateAndSave();

        //Just generate it
        String ident2 = IdentGenerator.generateNewIdent();

        assertTrue("Reports that already saved ident 1 is not exists", Store.isIdentAlreadyExists(ident1));
        assertFalse("Reports that unsaved ident 2 is exists", Store.isIdentAlreadyExists(ident2));
    }

}
