package func.api;

import com.google.gson.Gson;
import func.HzFunctionalTest;
import org.junit.AfterClass;
import org.junit.Test;
import play.Logger;
import play.mvc.Before;
import play.mvc.Http;
import play.test.CleanTest;
import play.test.Fixtures;
import struct.ErrorJson;
import struct.IdentReply;
import struct.Store;
import struct.StoreReply;
import util.Paths;

/**
 * Testing func.api/link methods
 *
 * @since 1.0
 */
@CleanTest(removeCurrent = true, createDefault = false)
public class GetURLTest extends HzFunctionalTest {

    private static final Gson gson = new Gson();

    @Before
    public void prepare() {
        Logger.info("Let's keep DB clean. CleanUp started");
        Fixtures.deleteDatabase();
    }

    @Test
    public void storeAndGet() {
        String longUrlToStore = "http://google.ee";

        //We store using API to avoid duplicate store method code (create and check ident)
        String jsonStringForStore = gson.toJson(Store.get().setLongUrl(longUrlToStore), Store.class);
        Http.Response storeResponse = POST(Paths.API_STORE, APPLICATION_JSON, jsonStringForStore);

        assertStatus(Http.StatusCode.CREATED, storeResponse);

        String storeResponseBody = storeResponse.out.toString();

        assertNotNull("Store response body is NULL", storeResponseBody);
        assertFalse("Store response body is Empty", storeResponseBody.isEmpty());

        StoreReply reply = gson.fromJson(storeResponseBody, StoreReply.class);
        assertNotNull("Cannot parse response JSON", reply);
        assertNotNull("Response JSON is parsed, but has no ident", reply.getShortUrl());

        String shortUrl = reply.getShortUrl();
        String[] split = shortUrl.split("/");
        assertTrue("No valid ident is short URL found", split.length > 0);

        String ident = split[split.length - 1];
        assertFalse("Got empty ident from short URL", ident.isEmpty());

        Http.Response getLinkResponse = GET(Paths.API_GET_LINK + ident);

        assertIsOk(getLinkResponse);

        String getLinkResponseBody = getLinkResponse.out.toString();

        assertNotNull("GetLink Response Body is NULL", getLinkResponseBody);

        IdentReply identReply = gson.fromJson(getLinkResponseBody, IdentReply.class);
        assertNotNull("Cannot parse GetLink response JSON", identReply);
        assertNotNull("GetLink Response JSON is parsed, but has no longUrl", identReply.getLongUrl());

        String actualLongUrl = identReply.getLongUrl();
        assertEquals("Stored URL is not the same as return from API", longUrlToStore, actualLongUrl);
    }

    @Test
    public void queryForNonExistentIdent(){
        //we didn't save anything - so every world will be good
        String ident = "custom";

        Http.Response response = GET(Paths.API_GET_LINK + ident);
        assertIsNotFound(response);
    }

    @Test
    public void noIdentShouldNotPass() {
        Http.Response response = GET(Paths.API_GET_LINK);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }

    @Test
    public void identWithNumbersShouldNotValidate() {
        String identWithNumbers = "43sd2d";
        Http.Response response = GET(Paths.API_GET_LINK + identWithNumbers);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }

    @Test
    public void identWithSpecialCharsShouldNotValidate() {
        String identWithSpecialChars = "qwe{]r";
        Http.Response response = GET(Paths.API_GET_LINK + identWithSpecialChars);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }

    @Test
    public void cyrillicIdentShouldNotValidate() {
        String cyrIdent = "Йцукен";
        Http.Response response = GET(Paths.API_GET_LINK + cyrIdent);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }

    @Test
    public void umlautIdentShouldNotValidate() {
        String umlautIdent = "kõrval";
        Http.Response response = GET(Paths.API_GET_LINK + umlautIdent);

        assertStatus(Http.StatusCode.BAD_REQUEST, response);

        String responseBody = response.out.toString();
        assertNotNull("Response body is NULL", responseBody);
        assertFalse("Response body is Empty", responseBody.isEmpty());

        ErrorJson errorJson = gson.fromJson(responseBody, ErrorJson.class);
        assertNotNull("Expected valid ErrorJson struct", errorJson);
        assertNotNull("Error JSON hasn't got message", errorJson.getMessage());
        assertFalse("Error JSON has empty message", errorJson.getMessage().isEmpty());
        assertTrue("Error JSON has no errors", errorJson.getErrors().size() > 0);
    }


    @AfterClass
    public static void cleanUp() {
        Logger.info("Cleaning DB after all tests");
        Fixtures.deleteDatabase();
    }
}
