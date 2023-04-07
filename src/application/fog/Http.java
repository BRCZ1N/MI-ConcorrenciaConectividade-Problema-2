package application.fog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Http {

	public static RequestHttp readRequest(InputStream input) throws IOException {

		BufferedInputStream buffer = new BufferedInputStream(input);
		StringBuilder requestStringBuilder = new StringBuilder();
		int c;

		while ((c = buffer.read()) != -1) {

			requestStringBuilder.append((char) c);

		}

		String[] request = requestStringBuilder.toString().split("\r\n\r\n");
		String[] headers = request[0].split("\r\n");
		String[] requestFirstLine = headers[0].split(" ");
		Map<String, String> mapHeaders = new HashMap<String, String>();

		String method = requestFirstLine[0];
		String path = requestFirstLine[1];
		String httpVersion = requestFirstLine[2];

		for (int headerLine = 1; headerLine < headers.length; headerLine++) {

			String[] headerKeyValue = headers[headerLine].split(":");
			mapHeaders.put(headerKeyValue[0], headerKeyValue[1]);

		}

		String jsonBody = request[1];

		return new RequestHttp(method, path, httpVersion, mapHeaders, jsonBody);

	}

	public static void sendResponse(OutputStream out,String response) throws UnsupportedEncodingException, IOException {

		BufferedOutputStream buffer = new BufferedOutputStream(out);
		buffer.write(response.getBytes("UTF-8"));
		buffer.flush();

	}

}
