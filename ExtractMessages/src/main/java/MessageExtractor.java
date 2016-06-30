import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Stack;

// TODO: Auto-generated Javadoc
/**
 * This class extract messages from input log file as (txt file) , the extracted
 * messages will be written on an a txt output file has the same name as input
 * the file but appended with a 'Messages' word
 * 
 * @author Wala' Sleem
 * @version 1.0
 */
public class MessageExtractor {

	/**
	 * The main method for the message extractor program.
	 *
	 * @param args
	 *            are not used
	 * @throws Throwable
	 *             f there is a problem with opening the file for reading or
	 *             writing , exception will be thrown
	 */
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub

		Scanner scan = new Scanner(System.in);
		/* taking the input file name or directory */
		String fileName = scan.nextLine();

		File inputFile = new File(fileName + ".txt");
		File messagesFile = new File(fileName + "Messages.txt");

		StringBuilder str = new StringBuilder("");
		BufferedWriter bwr = new BufferedWriter(new FileWriter(messagesFile));
		String msgType = "";

		/* counters for the messages types */
		int networkMessagesCounter = 0;
		int provisionMessagesCounter = 0;

		Scanner in = new Scanner(inputFile); // reading from file

		/**
		 * the input file will be read line by line until one of the messages
		 * types are found , then it will increment its counter and extract it
		 * to the output file
		 * 
		 */
		while (in.hasNext()) {
			String line = in.nextLine();
			if (line.contains("| NetworkIntentMessage:")) {
				msgType = "NetworkIntentMessage";
				networkMessagesCounter++;
			}

			else if (line.contains("| ProvisionFeatureMessage:")) {
				msgType = "ProvisionFeatureMessage";
				provisionMessagesCounter++;
			}

			if (msgType != "") {
				str.append(msgType + ":\r\n");
				str.append(in.nextLine()); // the line which contain the first
											// left brace {
				while (isBalanced(str) == false) {
					str.append(in.nextLine().replace('|', ' ') + "\r\n");
				}

				str.append("\r\n\r\n");

				// write contents of StringBuffer to a file
				bwr.write(str.toString());

				// flush the stream
				bwr.flush();

				// reinitialize the stringbuilder in order to use it ti search
				// for another msg
				str.setLength(0);
				msgType = "";

			}

		}

		// close the stream
		bwr.close();
		in.close();
		scan.close();

		System.out.println("Messages have been extracted successfully with\n " + networkMessagesCounter
				+ " NetworkIntent Messages and \n " + provisionMessagesCounter + " ProvisionFeature Messages ");
	}

	/**
	 * Checks if is balanced.
	 *
	 * @param str
	 *            represent the message's body that is appended as long as the
	 *            braces is not balanced
	 * 
	 * @return true, if the message braces is balanced
	 */
	public static boolean isBalanced(StringBuilder str) {
		// Meaningful names for characters
		final char LEFT_CURLY = '{';
		final char RIGHT_CURLY = '}';

		Stack<Character> store = new Stack<Character>(); // Stores braces
		int i; // An index into the string
		boolean failed = false; // Change to true for a mismatch

		for (i = 0; !failed && (i < str.length()); i++) {
			switch (str.charAt(i)) {

			case LEFT_CURLY:
				store.push(str.charAt(i));
				break;
			case RIGHT_CURLY:
				if (store.isEmpty() || (store.pop() != LEFT_CURLY))
					failed = true;// not balanced
				break;

			}
		}

		return store.isEmpty() && !failed;
	}

}
