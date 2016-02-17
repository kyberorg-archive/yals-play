package func.api;

import func.HzFunctionalTest;
import models.Link;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.mvc.Http;
import play.test.CleanTest;
import play.test.Fixtures;
import struct.ErrorJson;
import struct.Store;
import struct.StoreReply;
import util.Paths;

import java.net.MalformedURLException;
import java.util.List;


/**
 * Store API Tests
 *
 * @since 1.0
 */
@CleanTest(removeCurrent = true, createDefault = false)
public class StoreTest extends HzFunctionalTest {

    @Before
    public void cleanDb() {
        Logger.info("Let's keep DB clean. CleanUp started");
        Fixtures.deleteDatabase();
    }

    @Test
    public void storeOnceAndCheck() {
        String longUrl = "http://ya.ru";
        Store incomingStruct = Store.get().setLongUrl(longUrl);
        String jsonString = gson.toJson(incomingStruct, Store.class);

        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, jsonString);
        List<Link> links = Link.find("LongUrl", longUrl).fetch();

        assertStatus(Http.StatusCode.CREATED, response);
        assertNotNull("Fetch returned NULL instead of records list", links);
        assertFalse("No records in DB", links.isEmpty());
        assertEquals("DB has more then one record", 1, links.size());
        assertNotNull("First record is NULL", links.get(0));
        assertEquals("Long URLs don't match", longUrl, links.get(0).getLongUrl());
    }

    @Test
    public void storeTwoRecords() {
        String longUrl1 = "http://google.com";
        String longUrl2 = "http://google.ee";

        String jsonString1 = gson.toJson(Store.get().setLongUrl(longUrl1), Store.class);
        Http.Response response1 = POST(Paths.API_STORE, APPLICATION_JSON, jsonString1);

        String jsonString2 = gson.toJson(Store.get().setLongUrl(longUrl2), Store.class);
        Http.Response response2 = POST(Paths.API_STORE, APPLICATION_JSON, jsonString2);

        assertStatus(Http.StatusCode.CREATED, response1);
        assertStatus(Http.StatusCode.CREATED, response2);

        List<Link> links = Link.findAll();
        assertNotNull("Got NULL instead of records", links);
        assertFalse("No records in DB", links.isEmpty());
        assertEquals("Expectation that DB has 2 records is failed", 2, links.size());
        assertNotNull("First record is NULL", links.get(0));
        assertNotNull("Second record is NULL", links.get(1));

        assertEquals("First long URLs don't match", longUrl1, links.get(0).getLongUrl());
        assertEquals("Second long URLs don't match", longUrl2, links.get(1).getLongUrl());

        assertNotEquals("First and second URLs are equal", links.get(0).getLongUrl(), links.get(1).getLongUrl());
    }

    @Test
    public void passingEmptyBody() {
        String emptyBody = "";

        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, emptyBody);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body has no body", responseBody);
        assertFalse("Response body has empty body", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
    }

    @Test
    public void savingNotAJson() {
        String notAJsonString = "I am Not A JSON";

        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, notAJsonString);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body has no body", responseBody);
        assertFalse("Response body has empty body", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
    }

    @Test
    public void testingMalformedJson() {
        Object obj = new Object();
        String validJson = gson.toJson(obj, Object.class);
        String damagedJson = validJson.substring(1);

        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, damagedJson);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body has no body", responseBody);
        assertFalse("Response body has empty body", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
    }

    @Test
    public void savingNotURL() {
        String garbage = "Trash";

        String garbageJson = gson.toJson(Store.get().setLongUrl(garbage), Store.class);
        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, garbageJson);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body has no body", responseBody);
        assertFalse("Response body has empty body", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }


    @Test
    public void savingVeryLongURL() {
        int maxUrlAllowed = 15613; //From StackOverFlow.com
        int lettersInOurUrlWithoutO = 10; //I counted it manually
        StringBuilder looooooongUrl = new StringBuilder("http://l");
        for (int i = 0; i <= maxUrlAllowed - lettersInOurUrlWithoutO; i++) {
            looooooongUrl.append("o");
        }
        looooooongUrl.append("ng.com");
        looooooongUrl.append("/"); //trailing slash to exceed length

        String looooooongJson = gson.toJson(Store.get().setLongUrl(looooooongUrl.toString()), Store.class);
        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, looooooongJson);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body has no body", responseBody);
        assertFalse("Response body has empty body", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);

    }

    @Test
    public void isValidURLReturned() {
        String longUrl = "http://vr.fi";
        String jsonString = gson.toJson(Store.get().setLongUrl(longUrl), Store.class);
        Http.Response response = POST(Paths.API_STORE, APPLICATION_JSON, jsonString);

        assertStatus(Http.StatusCode.CREATED, response);
        String responseBody = response.out.toString();

        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        StoreReply reply = gson.fromJson(responseBody, StoreReply.class);
        assertNotNull("Cannot parse response JSON", reply);

        String shortUrl = reply.getShortUrl();
        assertNotNull("Response JSON is parsed, but has no shortUrl", shortUrl);
        assertNotNull("Response JSON is parsed, but has empty shortUrl", shortUrl.isEmpty());

        try {
            new java.net.URL(shortUrl);
        } catch (MalformedURLException e) {
            assertTrue("Got " + e.getClass().getSimpleName() + " .String was: '" + shortUrl+"'", false);
        }
    }

    @AfterClass
    public static void cleanUp() {
        Logger.info("Cleaning DB after all tests");
        Fixtures.deleteDatabase();
    }
}
