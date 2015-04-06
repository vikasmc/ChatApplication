import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpServerDemo {
	public static void main(String[] args) throws IOException {
		InetSocketAddress addr = new InetSocketAddress(4444);
		HttpServer server = HttpServer.create(addr, 0);
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port 4444");
	}
}

class MyHandler implements HttpHandler {
	//to store user names
	public static ArrayList<String> users = new ArrayList<String>();
	//to store messages from the clients.
	public static HashMap<String, String> message = new HashMap<String, String>();
	public static Map<String, HashMap<String, String>> messages = new HashMap<String, HashMap<String, String>>();
	//to know the client is free or not.
	public static HashMap<String, Boolean> isFree = new HashMap<String, Boolean>();

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			OutputStream responseBody = exchange.getResponseBody();
			Headers requestHeaders = exchange.getRequestHeaders();
			Set<String> keySet = requestHeaders.keySet();
			Iterator<String> iter = keySet.iterator();
			exchange.sendResponseHeaders(200, 0);
			String name = null;
			while (iter.hasNext()) {
				String key = iter.next();
				List values = requestHeaders.get(key);
				if (key.equalsIgnoreCase("name")) {
					name = values.toString();
				}
			}
			name = name.substring(1, name.length() - 1);
			String output = GetMessage(name);
			responseBody.write(output.getBytes());
			responseBody.close();
		}
		if (requestMethod.equalsIgnoreCase("PUT")) {
			OutputStream responseBody = exchange.getResponseBody();
			InputStream requestBody = exchange.getRequestBody();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					requestBody));
			String inputLine = in.readLine();
			String namepass[] = inputLine.split(":");
			String output = null;
			if (namepass[0].equals("chatTo")) {
				output = IsFree(namepass[1]);
			} else {
				output = Add(namepass[1]);
				if (output.equals("the user name is addedd:")) {
					output += ShowUser();
				}
			}
				exchange.sendResponseHeaders(200, 0);
				responseBody.write(output.getBytes());
				responseBody.close();
		}
		if (requestMethod.equalsIgnoreCase("POST")) {
			InputStream stream = exchange.getRequestBody();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(stream));
			String output = in.readLine();
			String out = SendMessage(output);
			exchange.sendResponseHeaders(200, 0);
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(out.getBytes());
			responseBody.close();
		}
		if (requestMethod.equalsIgnoreCase("DELETE")) {
			OutputStream responseBody = exchange.getResponseBody();
			Headers requestHeaders = exchange.getRequestHeaders();
			Set<String> keySet = requestHeaders.keySet();
			Iterator<String> iter = keySet.iterator();
			exchange.sendResponseHeaders(200, 0);
			String name = null;
			String option = null;
			while (iter.hasNext()) {
				String key = iter.next();
				List values = requestHeaders.get(key);
				if (key.equalsIgnoreCase("name")) {
					name = values.toString();
				}
				if (key.equalsIgnoreCase("option")) {
					option = values.toString();
				}
			}
			name = name.substring(1, name.length() - 1);
			option = option.substring(1, option.length() - 1);
			System.out.println(DeleteUser(name, option));
			responseBody.close();
		}
	}

	public String Add(String n) {
		if (!users.contains(n)) {
			users.add(n);
			isFree.put(n, true);
			return "the user name is addedd:";
		} else {
			return "Username is already exist";
		}
	}

	public String IsFree(String name) {
		if (isFree.containsKey(name)) {
			if (isFree.get(name)) {
				isFree.put(name, false);
				return "success";
			} else {
				return "user is busy";
			}
		} else {
			return "the user name does not exist";
		}
	}

	public String ShowUser() {
		String Names = "";
		for (String s2 : users) {
			Names += s2 + " ";
		}
		return Names;
	}

	public String GetMessage(String name) {
		if (messages.containsKey(name)) {
			HashMap<String, String> m = messages.get(name);
			String from = "";
			String message = "";
			for (Entry<String, String> entry : m.entrySet()) {
				from = entry.getKey();
				message = entry.getValue();
				m.remove(from);
			}
			messages.remove(name);
			return from + ":" + message;
		} else {
			return "failure";
		}
	}

	public String DeleteUser(String names, String options) {
		if (options.equals("chatOff")) {
			if (!isFree.get(names)) {
				isFree.put(names, true);
			}
		}
		if (options.equals("del")) {
			if (users.contains(names)) {
				users.remove(names);
				isFree.remove(names);
			}
		}
		return "sucess";
	}

	public String SendMessage(String toa) {
		String namepass[] = toa.split(":");
		if (isFree.get(namepass[1]) != null) {
			if (isFree.get(namepass[1])) {
				return "the user " + namepass[1] + " has left chat";
			} else {
				isFree.put(namepass[0], false);
				isFree.put(namepass[1], false);
				message.put(namepass[0], namepass[2]);
				messages.put(namepass[1], message);
				return "Message sent from " + namepass[0] + " to "
						+ namepass[1];
			}
		} else {
			return "the user " + namepass[1] + " is not online";
		}

	}
}
