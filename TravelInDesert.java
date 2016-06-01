/* ------------------------------------------
 * Travel In Desert Algorithm
 * Aline Kolczycki Borges, May/2015
 * Data Structure II - Computer Engineering
 * UTFPR
 * https://github.com/alinekborges/TravelInDesert
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TravelInDesert {
	
	//Graph is represented as a matrix of Paths
	//Each has a temperature and distance.
	Path graph[][];
	int startPoint;
	int endPoint;
	
	int vertices = 0;
	
	public class Path {
		double temperature;
		double distance;
	}
	
	public static void main(String[] args) {
		TravelInDesert travelInDesert = new TravelInDesert();
		travelInDesert.start();
	}
	
	public void start() {
		
		println("Enter test data or press ENTER to use sample test:");
		String input = getInput();
		
		if (input == "") { //indicates an error
			start();	//start over
		} else {
			parseInput(input);
			//As the distance is secondary - the lowest temperature is the most important
			//The algorithm used is Prim's algorithm for minimum spannable tree.
			//if there are two paths with same temperature, then lowest distance is considered
			int[] tree = calculateMinimumTree();
			findTravelPath(tree);
		}
		
	}
	
	
	/*
	 * Function that creates the minimum spanable tree for the graph
	 */
	int[] calculateMinimumTree() {
		
		Path paths[] = new Path[vertices];
		
		int parent[] = new int[vertices];
		
		boolean visited[] = new boolean[vertices];
		
		//include first in the tree
		 paths[0] = new Path();
		 paths[0].temperature = 0.0;
		 paths[0].distance = 0.0;
		
		for (int i = 0 ; i < vertices - 1 ; i++) {
			
			int u = this.minimumPath(paths, visited);
			
			//set vertice as visited
			visited[u] = true;
			
			for (int v = 0 ; v < vertices ; v++) {
				if (this.graph[u][v] != null && visited[v] == false) {
					if (paths[v] == null || graph[u][v].temperature < paths[v].temperature) {
						parent[v] = u;
						paths[v] = graph[u][v];
					} else if (graph[u][v].temperature == paths[v].temperature && graph[u][v].distance < paths[v].distance) {
						parent[v] = u;
						paths[v] = graph[u][v];
					}
				}
			}
			
		}
		
		printTree(parent, vertices, graph);
	
		return parent;
	}
	
	/*
	 * Find path between start and end of the travel in desert and print path and maximum temperature
	 */
	void findTravelPath(int[] parents) {
		
		println("\n\n Path found:");
		
		ArrayList<Path> paths = new ArrayList<Path>();
		ArrayList<Integer> fullPath = new ArrayList<Integer>();
		
		int parent = parents[endPoint];
		int current = endPoint;
		fullPath.add(current);
		//go from endpoint and find parent until we find the start point and print path
		while (parent != startPoint) {
			paths.add(graph[current][parent]);
			current = parent;
			parent = parents[current];
			fullPath.add(current);			
		}
		
		//add start point
		paths.add(graph[current][parent]);
		current = parent;
		fullPath.add(current);
		
		for (int oasis : fullPath) {
			print((oasis + 1) + " ");
		}
		
		double temp = findMaximumTemperature(paths);
		double lenght = findLength(paths);
		println("\n" + lenght + " " + temp);
		
	}
	
	/*
	 * Return maximum temperature in a path
	 */
	double findMaximumTemperature(ArrayList<Path> paths) {
		double max = -1;
		for (Path path : paths) {
			if (path.temperature > max) {
				max = path.temperature;
			}
		}
		return max;
	}
	
	/*
	 * Returns total distance of a path
	 */
	double findLength(ArrayList<Path> paths) {
		double length = 0;
		for (Path path : paths) {
			length += path.distance;
		}
		return length;
	}
	
	/*
	 * Print minimum tree for testing pruposes
	 */
	void printTree(int parent[], int vertices, Path graph[][]) {
		println("Minimum Tree:"); 
		println("Path   Temperature");
		for (int i = 1; i < vertices; i++) {
		    println(parent[i]+1 + " - " + (i+1) + "    " + graph[i][parent[i]].temperature);
		}
	}
	
	/*
	 * Returns the next path with the lowest temperature not yet in the tree
	 * If the temperature is the same, lowest distance is considered
	 */
	int minimumPath(Path[] paths, boolean visited[]) {
		double minTemp = Double.MAX_VALUE;
		int minPathIndex = -1;
		
		for (int i = 0 ; i < vertices ; i++) {
			if (paths[i] != null) { 
				//only check oasis that were not visited yet
				if (visited[i] == false) {
					if (paths[i].temperature < minTemp) {
						//if temperature is lower, this is the minimum value
						minTemp = paths[i].temperature;
						minPathIndex = i;
					} else if (paths[i].temperature == minTemp && paths[i].distance < paths[minPathIndex].distance) {
						//if temperature is the same, the path with minimum distance is considered
						minTemp = paths[i].temperature;
						minPathIndex = i;
					}
				}
			}
		}
		
		return minPathIndex;
		
	}
	
	/*
	 * Parse Input and create a graph based on the string
	 */
	public void parseInput(String input) {
		
		String[] substrings = input.split("\n");
		
		String firstLine = substrings[0];
		String secondLine = substrings[1];
		
		this.graph = initializeGraph(firstLine, secondLine);
		
		for (int i = 2 ; i < substrings.length ; i++) {
			String line = substrings[i];
			parsePath(line);
		}
		
		
	}
	
	/*
	 * Initialize number of oasis in the list
	 */
	public Path[][] initializeGraph(String firstLine, String secondLine) {
		
		
		String[] substrings = firstLine.split(" ");
		String[] substrings_destin = secondLine.split(" ");
		
		int numberOfOasis = Integer.parseInt(substrings[0]);
		int startPoint = Integer.parseInt(substrings_destin[0]);
		int endPoint = Integer.parseInt(substrings_destin[1]);
		
		Path[][] graph = new Path[numberOfOasis][numberOfOasis];
		
		
		this.vertices = numberOfOasis;
		this.startPoint = startPoint-1;
		this.endPoint = endPoint-1;
		
		return graph;
	}
	
	/*
	 * Parse path string and creates the object and connections
	 */
	public void parsePath(String input) {
		String[] substrings = input.split(" ");
		
		int oasis1 = Integer.parseInt(substrings[0]) - 1;
		int oasis2 = Integer.parseInt(substrings[1]) - 1;
		double temperature = Double.parseDouble(substrings[2]);
		double distance = Double.parseDouble(substrings[3]);
		
		Path path = new Path();
		path.temperature = temperature;
		path.distance = distance;
		
		if (this.graph[oasis1][oasis2] == null) {
			this.graph[oasis1][oasis2] = path;
			this.graph[oasis2][oasis1] = path;
		} else {
			//if it already exists a path between the 2 oasis, choose the one with lowest temperature
			if (path.temperature < this.graph[oasis1][oasis2].temperature) {
				this.graph[oasis1][oasis2] = path;
				this.graph[oasis2][oasis1] = path;
			} else if (this.graph[oasis1][oasis2].temperature == path.temperature && path.distance < this.graph[oasis1][oasis2].distance ) {
				//if same temperature, discard highest distance
				this.graph[oasis1][oasis2] = path;
				this.graph[oasis2][oasis1] = path;
			}
		}
		
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
					println("First line should be 2 integers.\nTry again:");
				}
			
				return getUserInput(br, s);
				
			}
		} catch (Exception ex) {
			println("Error reading user input");
			return "";
		}
		
		
	}
	
	public String getUserInput(BufferedReader reader, String firstLine) {
		String input = firstLine + "\n";
		
		try {
			String destination = reader.readLine();
			
			
			while (!destination.matches(integerRegex)) {
				println("Second line should be 2 integers.\nTry again:");
				destination = reader.readLine(); 
			} 
			input += destination + "\n";
			
			int numberOfOases = numberOfOasisFromString(firstLine);
			
			for (int i = 0 ; i < numberOfOases ; i++) { 
				String info = reader.readLine();
				
				while(!info.matches(oasesRegex)) {
					println("This line does not match regex. \nLine should be 2 integers, followed by 2 decimals with one decimal point.\nTry again:");
					info = reader.readLine();
				}
				
				input += info + "\n";
				
			}
			
			println(input); 
		} catch (Exception ex) { 
			println("Error reading user input");
		}
		return input;
	}
	
	public int numberOfOasisFromString(String string) {
		String[] array = string.split(" ");
		return Integer.parseInt(array[array.length - 1]);
	}
	
	public void println(Object object) {
		System.out.println(object);
	}
	
	public void print(Object object) {
		System.out.print(object);
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
