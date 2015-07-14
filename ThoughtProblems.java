import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThoughtProblems {

	public static void main(String[] args) {
		ThoughtProblems problems = new ThoughtProblems();

		try {
			String[] response = problems.thoughtSentence(args[0]);
			System.out.print("About to print response:\n");

			for (int i  = 0; i < response.length; i++) {
				System.out.print(response[i] + " ");
			}

			System.out.println();
		} catch (Exception ex) {
			// In a more elavorated approach, this must be shown to an user
			System.out.print(ex);
		}
	}

	public String[] thoughtSentence(String sentence) throws Exception {
		if (sentence == null || sentence.isEmpty()) {
			return new String[] {"Bad Input"};
		}

		// Check in the dictionary, then return
		if (containedInDictionary(sentence)) {
			return new String[] {sentence};
		}

		for (int i = 0; i < sentence.length(); i++) {
			String aux = sentence.substring(0, (i + 1));
			String leftOver = sentence.substring(i + 1);

			// If is a word from the dictionary, check for the next one
			if (containedInDictionary(aux)) {
				String response[] = thoughtSentence(leftOver);
				if (response.length == 0) {
					continue;
				}
				String[] toReturn = new String[response.length + 1];
				toReturn[0] = aux;

				for (int j = 0; j < response.length; j++) {
					toReturn[j + 1] = response[j];
				}

				return toReturn;
			}
		}

		return new String[] {"Unparseable Input"};
	}

	private boolean containedInDictionary (String word) throws Exception {
		String response = sendGetToDictionary(word);

		if (response == null || response.isEmpty()) {
			return false;
		}

		return (response.indexOf("[{") != -1);
	}

	// HTTP GET request adapted from mkyong
	private String sendGetToDictionary(String word) throws Exception {

		final String url = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=NEED_A_KEY&lang=en-en&text=" + word;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		    new InputStreamReader(con.getInputStream())
		);

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return response.toString();
	}
}