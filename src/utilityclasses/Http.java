package utilityclasses;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {

	public static RequestHttp readRequest(InputStream input) throws IOException {

		BufferedInputStream buffer = new BufferedInputStream(input);
		StringBuilder requestStringBuilder = new StringBuilder();

		if (!(buffer.available() > 0)) {

			return null;

		}

		while (buffer.available() > 0) {

			requestStringBuilder.append((char) buffer.read());

		}

		String[] request = requestStringBuilder.toString().split("\r\n\r\n");
		String[] headers = request[0].split("\r\n");
		String[] requestFirstLine = headers[0].split(" ");
		Map<String, String> mapHeaders = new HashMap<>();

		String method = requestFirstLine[0];
		String path = requestFirstLine[1];
		String httpVersion = requestFirstLine[2];

		for (int headerLine = 1; headerLine < headers.length; headerLine++) {

			String[] headerKeyValue = headers[headerLine].split(":");
			mapHeaders.put(headerKeyValue[0], headerKeyValue[1].trim());

		}

		if (request.length > 1) {

			String jsonBody = request[1];
			return new RequestHttp(method, path, httpVersion, mapHeaders, jsonBody);
		}

		return new RequestHttp(method, path, httpVersion, mapHeaders);

	}

	public static ResponseHttp sendHTTPRequestAndGetHttpResponse(RequestHttp requestHttp, String ip) throws IOException {

		OkHttpClient client = new OkHttpClient();
		String url = "http://" + ip + ":8000" + requestHttp.getPath();
		Request request = new Request.Builder().url(url).method(requestHttp.getMethod(),
				(requestHttp.getBody() == null ? null : RequestBody.create(requestHttp.getBody().getBytes("UTF-8"))))
				.headers(Headers.of(requestHttp.getHeaders())).build();
		Response response;
		response = client.newCall(request).execute();
		ResponseHttp responseHttp = formatHTTPResponse(response);

		return responseHttp;

	}

	public static ResponseHttp formatHTTPResponse(Response responseHttp) {

		Map<String, String> headersMap = new HashMap<String, String>();

		for (int i = 0, size = responseHttp.headers().size(); i < size; i++) {

			String name = responseHttp.headers().name(i);
			String value = responseHttp.headers().value(i);
			headersMap.put(name, value);

		}

		ResponseHttp response = new ResponseHttp(HttpCodes.valueOf("HTTP_" + responseHttp.code()).getCodeHttp(),
				headersMap, responseHttp.body().toString());

		return response;

	}

}
