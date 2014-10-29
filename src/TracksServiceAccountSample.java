import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Make a request to Tracks API.
 * Command Line Usage Examples (with Maven)
 * <pre>{@code
 * mvn -q exec:java -Dexec.args="METHOD JSON_REQUEST_BODY"
 * mvn -q exec:java -Dexec.args="crumbs/getlocationinfo {'entityId':'280415822391405995','timestamp':'1334643465000000'}"
 * mvn -q exec:java -Dexec.args="entities/list ''"
 * mvn -q exec:java -Dexec.args="entities/create {'entities':[{'name':'auto001','type':'AUTOMOBILE'}]}"}</pre>
 *
 */
public class TracksServiceAccountSample {

  /** E-mail address of the service account. */
  private static final String SERVICE_ACCOUNT_EMAIL =
      "700133348888-np57pj2a1uevi8vvpdmb7nrbnio60sci@developer.gserviceaccount.com";

  /** Global configuration of OAuth 2.0 scope. */
  private static final Collection<String> TRACKS_SCOPE = new ArrayList<String>();
  
  /** Global configuration for location of private key file. */
  private static final String PRIVATE_KEY = "176514ba2e55f55e811bf7d4b9fd3d87cd8e73f0-privatekey.p12";

  /** Global instance of the HTTP transport. */
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  public static void main(String[] args) throws IOException, GeneralSecurityException {
	  TRACKS_SCOPE.add("https://www.googleapis.com/auth/tracks");

	  // Check for valid setup.
    Preconditions.checkArgument(!SERVICE_ACCOUNT_EMAIL.startsWith("[["),
        "Please enter your service account e-mail from the Google APIs " +
        "Console to the SERVICE_ACCOUNT_EMAIL constant in %s",
        TracksServiceAccountSample.class.getName());
    String p12Content = Files.readFirstLine(new File(PRIVATE_KEY),
        Charset.defaultCharset());
    Preconditions.checkArgument(!p12Content.startsWith("Please"),
        p12Content);

    // Build service account credential.
    GoogleCredential credential =
        new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
            .setJsonFactory(JSON_FACTORY)
            .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
            .setServiceAccountScopes(TRACKS_SCOPE)
            .setServiceAccountPrivateKeyFromP12File(new File(PRIVATE_KEY))
            .build();

    // Set up and execute Tracks API Request.
    String method = args[0];
    String URI = "https://www.googleapis.com/tracks/v1/" + method;
    String requestBody = args[1];
    HttpRequestFactory requestFactory =
        HTTP_TRANSPORT.createRequestFactory(credential);
    GenericUrl url = new GenericUrl(URI);
    HttpRequest request =
        requestFactory.buildPostRequest(url,ByteArrayContent.fromString(null, requestBody));
    request.getHeaders().setContentType("application/json");
    // Google servers will fail to process a POST/PUT/PATCH unless the Content-Length
    // header >= 1
    //request.setAllowEmptyContent(false);
    HttpResponse shortUrl = request.execute();

    // Print response.
    BufferedReader output = new BufferedReader(new InputStreamReader(shortUrl.getContent()));
    for (String line = output.readLine(); line != null; line = output.readLine()) {
      System.out.println(line);
    }
  }
}
