import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {
	/**
	 * @Vikas M C
	 */
	public String SendName(String name, String option) {
		String out = null;
		try {
			URL url = new URL("http://localhost:4444");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("PUT");
			con.setRequestProperty("User-Agent", "Chat Server");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = option + ":" + name;

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			inputLine = in.readLine();
			out = inputLine;
			in.close();
			con.disconnect();
		} catch (FileNotFoundException e) {
			return "Error in the chat";
		} catch (IOException ex) {
			return "Error in the IO";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public String GetMessage(String name) {
		String out = null;
		try {
			URL url = new URL("http://localhost:4444");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setRequestProperty("name", name);
			con.setRequestProperty("User-Agent", "Chat Server");
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			inputLine = in.readLine();
			out = inputLine;
			in.close();
			con.disconnect();
		} catch (FileNotFoundException e) {
			return "Something gone wrong";
		} catch (IOException ex) {
			return "Something gone wrong in IO exception";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public String SendMessage(String tao) {
		String out = null;
		try {
			URL url = new URL("http://localhost:4444");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Chat Server");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = tao;

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			inputLine = in.readLine();
			out = inputLine;
			in.close();
			con.disconnect();
		} catch (FileNotFoundException e) {
			return "Could not find Client to chat";
		} catch (IOException ex) {
			return "Could not find Client to chat";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public String Delete(String name, String option) {
		String out = null;
		try {
			URL url = new URL("http://localhost:4444");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.setRequestProperty("name", name);
			con.setRequestProperty("option", option);
			con.setRequestProperty("User-Agent", "Chat Server");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("name", "vikas");
			con.connect();
			int responseCode = con.getResponseCode();
			con.disconnect();
		} catch (FileNotFoundException e) {
			return "Could not find Client to chat";
		} catch (IOException ex) {
			return "Could not find Client to chat";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
