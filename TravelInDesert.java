import java.io.BufferedReader;
import java.io.InputStreamReader;

/* ------------------------------------------
 * Travel In Desert Algorithm
 * Aline Kolczycki Borges, May/2015
 * Data Structure II - Computer Engineering
 * UTFPR
 * -------------------------------------------
 * There is a group of adventurers who like to travel in the desert.
 * Everyone knows travelling in desert can be very dangerous. That's why 
 * they plan their trip carefully every time. There are a lot of factors to consider before they 
 * make their final decision. One of the most important factors is the weather. It is undesirable to 
 * travel under extremely high temperature. They always try to avoid going to the hottest place. However, 
 * it is unavoidable sometimes as it might be on the only way to the destination. To decide where to go, 
 * they will pick a route that the highest temperature is minimized. If more than one route satisfy 
 * this criterion, they will choose the shortest one. There are several oases in the desert where 
 * they can take a rest. That means they are travelling from oasis to oasis before reaching the 
 * destination. They know the lengths and the temperatures of the paths between oases.
 */
public class TravelInDesert {
	
	public static void main(String[] args) {
		TravelInDesert travelInDesert = new TravelInDesert();
		travelInDesert.start();
	}
	
	public void start() {
		
		print("Enter test data or press ENTER to use sample test:");
		String input = getInput();
		
		if (input == "") { //indicates an error
			start();	//start over
		}
		
		//print("20 10 37.1 10.1".matches("^\\d+\\s\\d+\\s\\d+(\\.\\d{1})\\s\\d+(\\.\\d{1})$"));
	}
	
	
	/*
	 *  Get user input or use default input
	 */
	public String getInput() {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String s = br.readLine();
			if (s.isEmpty()) {
				//Use default test
				System.out.println("Using default test input: \n" + TravelInDesert.sampleTest);
				return TravelInDesert.sampleTest;
			} else {
				while (!s.matches(integerRegex)) {
					s = br.readLine();
					print("First line should be 2 integers.\nTry again:");
				}
			
				return getUserInput(br, s);
				
			}
		} catch (Exception ex) {
			print("Error reading user input");
			return "";
		}
		
		
	}
	
	public String getUserInput(BufferedReader reader, String firstLine) {
		String input = firstLine + "\n";
		
		try {
			String destination = reader.readLine();
			
			
			while (!destination.matches(integerRegex)) {
				print("Second line should be 2 integers.\nTry again:");
				destination = reader.readLine(); 
			} 
			input += destination + "\n";
			
			int numberOfOases = numberOfOasisFromString(firstLine);
			
			for (int i = 0 ; i < numberOfOases ; i++) { 
				String info = reader.readLine();
				
				while(!info.matches(oasesRegex)) {
					print("line does not match regex. \nLine should be 2 integers, followed by 2 decimals with one decimal point.\nTry again:");
					info = reader.readLine();
				}
				
				input += info + "\n";
				
			}
			
			print(input); 
		} catch (Exception ex) { 
			print("Error reading user input");
		}
		return input;
	}
	
	public int numberOfOasisFromString(String string) {
		String[] array = string.split(" ");
		return Integer.parseInt(array[array.length - 1]);
	}
	
	public void print(Object object) {
		System.out.println(object);
	}
	
	/*
	 * Sample test in form of:
	 * Number of Oases + Number of paths
	 * Start + Destination
	 * Path between (A and B) + Temperature + Distance
	 */
	public static String sampleTest = 	"6 9\n" + 
										"1 6\n" + 
										"1 2 37.1 10.2\n" + 
										"2 3 40.5 20.7\n" + 
										"3 4 42.8 19.0\n" + 
										"3 1 38.3 15.8\n" + 
										"4 5 39.7 11.1\n" + 
										"6 3 36.0 22.5\n" + 
										"5 6 43.9 10.2\n" +
										"2 6 44.2 15.2\n" +
										"4 6 34.2 17.4\n";
	
	public static String oasesRegex = "^\\d+\\s\\d+\\s\\d+(\\.\\d{1})\\s\\d+(\\.\\d{1})$";
	public static String integerRegex = "^\\d+\\s\\d+$";

}
