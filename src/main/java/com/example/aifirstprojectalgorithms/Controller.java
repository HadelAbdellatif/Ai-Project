package com.example.aifirstprojectalgorithms;


// Hana Kafri 1190084 Sec.3
// Hadeel Abdellatif 1190451 Sec.2

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class Controller {

	@FXML
	TextField source = new TextField();
	@FXML
	TextField target = new TextField();
	@FXML
	TextField timeC = new TextField();
	@FXML
	TextField spaceC = new TextField();

	private Label sourceCountry;
	private Label targetCountry;

	@FXML
	private Button loadMapButton;

	@FXML
	private Button go;
	@FXML
	private TextField totalDistanceView;

	@FXML
	private ListView<String> pathListView;

	@FXML
	private AnchorPane pane;
	Line line;
	@FXML
	private ComboBox<String> selectstart;
	@FXML
	private ComboBox<String> selecttarget;
	@FXML
	private ComboBox<String> choseAlgo;

	private final ArrayList<Country> Countris = new ArrayList<>();
	private ArrayList<String> Algorithms = new ArrayList<>();

	private final ArrayList<Edge> edges = new ArrayList<>();
	private final ArrayList<Table> table = new ArrayList<>();

	private final ArrayList<Line> lines = new ArrayList<>();
	ArrayList<Circle> circles = new ArrayList<>();
	ArrayList<Label> labels = new ArrayList<>();

	private final ObservableList<String> pathCountris = FXCollections.observableArrayList();
	ArrayList<Country> loc = new ArrayList<Country>();

	// Get Data
	@FXML
	void getData(ActionEvent event) throws IOException {
		loadMapButton.setDisable(true);
		try {
			getData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getData() throws FileNotFoundException, IOException {
		getCities();
		getEdges();

		sourceCountry = new Label("A");
		targetCountry = new Label("B");

		sourceCountry.setLayoutX(-10);
		targetCountry.setLayoutX(-10);

		final double MAX_FONT_SIZE = 14.0;
		go.setShape(new Circle(1.5));
		sourceCountry.setFont(new Font(MAX_FONT_SIZE));
		sourceCountry.setTextFill(Color.BLUE);
		targetCountry.setFont(new Font(MAX_FONT_SIZE));
		targetCountry.setTextFill(Color.BLUE);
		pane.getChildren().addAll(sourceCountry, targetCountry);

		initalizeMap();
	}


	// Read Cities file and store its content to the Countris Array List


	private void getCities() throws NumberFormatException, IOException {

		BufferedReader br = new BufferedReader(new FileReader("city.csv"));

		String line;

		while ((line = br.readLine()) != null) {

			String[] spStr = line.trim().split(",");

			if (spStr.length == 3) {
				// 31.999 -- > top most longitude of the map
				// 1132 --> height of map
				// 0.28 --> top most long-bottom most lng difference btween Top and bottomin lag
				// 34.97837 --> left most point

				// that's when we go to right
				// 688--> width of map
				// 0.34 -->left and right diff lat btw left side and right side

				// 1523 1042
				double y = (33.40 - Double.parseDouble(spStr[1])) * 1523 / 3.68;
				double x = (Double.parseDouble(spStr[2]) - 33.34) * 1042 / 4.1;

				// make an object of the cities and their location on the map X and Y.
				Countris.add(new Country(x, y, Double.parseDouble(spStr[1]), Double.parseDouble(spStr[2]), spStr[0],
						new Circle()));
			}
		}
	}

	/*
	 * Read Edges file and store its content to the Edges Array List
	 */
	private void getEdges() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("roadsH.csv"));
		String line;
		int i=0;
		// read until distance data
		while ((line = br.readLine()) != null) {
			//System.out.println(i++ +" bb");
			// String[] spStr = line.replaceAll("\\s+", " ").trim().split("[ ]");
			String[] spStr = line.trim().split(",");

			Country Country1 = getCountryByName(spStr[0]);
			Country Country2 = getCountryByName(spStr[1]);
			double hCar = Double.parseDouble(spStr[2]);
			double dis = Double.parseDouble(spStr[3]);
			double hWalking = Double.parseDouble(spStr[4]);

			// make an object of each pair and the distance between them.
			edges.add(new Edge(Country1, Country2, hCar, dis, hWalking));
			Country1.getAdjacent().add(Country2);

		}
		br.close();
	}

	/*
	 * Search for a Country by name
	 */
	private Country getCountryByName(String name) {

		Country tempCountry = null;

		for (int i = 0; i < Countris.size(); i++) {
			if (Countris.get(i).getName().equalsIgnoreCase(name)) {
				tempCountry = Countris.get(i);
			}
		}
		return tempCountry;
	}

	/*
	 * fill the map with Countris by putting each Country on the right coordinates.
	 * each Country with its name and a circle that represents it
	 */
	@FXML
	public void initalizeMap() {

		for (int i = 0; i < Countris.size(); i++) {

			// the circle that represents the Country on the map
			Circle point = new Circle(5);
			circles.add(point);
			Label name = new Label(Countris.get(i).getName());
			labels.add(name);
			name.setLayoutX(Countris.get(i).getCoordinateX());
			name.setLayoutY(Countris.get(i).getCoordinateY());
			// set circle coordinates
			point.setCenterX(Countris.get(i).getCoordinateX());/////////////////////////////////////////////////
			point.setCenterY(Countris.get(i).getCoordinateY());

			point.setFill(Color.TRANSPARENT);
			point.setStroke(Color.BLACK);

			Tooltip tooltip = new Tooltip(Countris.get(i).toString());
			tooltip.setAutoFix(true);
			Tooltip.install(point, tooltip);

			// setting Country circle to the circle above
			Countris.get(i).setCircle(point);

			// add the circle and the label to the scene
			pane.getChildren().addAll(Countris.get(i).getCircle(), name);

			String temp = Countris.get(i).toString();

			/*
			 * Get Country name and coordinates, and choose it in the choice box(if it is
			 * null) when clicking on the circle
			 */

			try {
				point.setOnMouseClicked(e -> {
					String[] sp = temp.split("[-]");
					point.setFill(Color.BLACK);
					if (source.getText() == null || source.getText().equals("")) {
						source.setText(sp[0].trim());
						selectstart.setValue(sp[0].trim());

					} else if (target.getText() == null || target.getText().equals("")) {
						target.setText(sp[0].trim());
						selecttarget.setValue(sp[0].trim());
					}
				});
			} catch (Exception e) {
				System.out.println("enter from combo box");
			}
		}
		Algorithms.add("A * BY CAR");
		Algorithms.add("A * BY WAlKING");
		Algorithms.add("BFS");
		Algorithms.add("DFS");
		for (String s : Algorithms) {
			choseAlgo.getItems().add(s);
		}

		for (int j = 0; j < Countris.size(); j++) {
			selectstart.getItems().add(Countris.get(j).getName());
			selecttarget.getItems().add(Countris.get(j).getName());
		}
		selectstart.setEditable(true);
		selecttarget.setEditable(true);
	}

	/*
	 * Reset the table
	 */
	private void initializeTable() {
		table.clear();
		for (Country i : Countris) {
			table.add(new Table(i, false, Double.POSITIVE_INFINITY, null));
		}
	}

	String Source1;
	String Target1;

	/*
	 * Run the program by clicking on the (Run) button and get the shortest path
	 */
	@FXML
	void run(ActionEvent event) {
		try {
			if (choseAlgo.getValue() == null) {
				JOptionPane.showMessageDialog(null, "Please Chose Algoithm First");
			}
			resetMap();
			Source1 = selectstart.getValue();
			Target1 = selecttarget.getValue();

			// if all choice boxes are not null
			if (!Source1.equals(null) && !Target1.equals(null)) {

				String[] sourceXY = getCityCoordinates(Source1).trim().split("[,]");///////////////////////////
				String[] targetXY = getCityCoordinates(Target1).trim().split("[,]");

				sourceCountry.setLayoutX(Double.parseDouble(sourceXY[0]) - 10);
				sourceCountry.setLayoutY(Double.parseDouble(sourceXY[1]) - 28);
				targetCountry.setLayoutX(Double.parseDouble(targetXY[0]));
				targetCountry.setLayoutY(Double.parseDouble(targetXY[1]) - 28);

				/*
				 * ----> run "shortest path"
				 */
				getShortestPath(getCountryByName(Source1), getCountryByName(Target1));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void resetMap() {
		loc.clear();
		for (Line line : lines) {
			pane.getChildren().remove(line);
		}
		lines.clear();

		for (int i = 0; i < circles.size(); i++) {
			circles.get(i).setFill(Color.TRANSPARENT);
			circles.get(i).setStroke(Color.BLACK);
		}
	}

	/*
	 * Get City coordinates by its name from the cities Array List
	 */
	private String getCityCoordinates(String CountryName) {
		for (int i = 0; i < Countris.size(); i++) {
			if (Countris.get(i).getName().equalsIgnoreCase(CountryName)) {
				return Countris.get(i).getCoordinateX() + "," + Countris.get(i).getCoordinateY();
			}
		}
		return null;
	}

	private void getShortestPath(Country sourceCountry, Country targetCountry) throws IOException {

		cityNum();
		// reset the table to start new one
		initializeTable();

		// build the table
		if (choseAlgo.getValue().equals("A * BY CAR")) {
			buildTableCar(sourceCountry, targetCountry);
			JOptionPane.showMessageDialog(null, "A* By CAR Choosed");
		} else if (choseAlgo.getValue().equals("A * BY WAlKING")) {
			buildTableWalking(sourceCountry, targetCountry);
			JOptionPane.showMessageDialog(null, "A* By WAlKING Choosed");
		} else if (choseAlgo.getValue().equals("BFS")) {

			int src = -1, dst = -1;
			String source = Source1.toString().toLowerCase(Locale.ROOT);
			String destination = Target1.toString().toLowerCase(Locale.ROOT);

			// loop to get the id number of the entered source and destination, to use them in the algorithm
			for (String[] strings : citiesNum) {

				// compare the lowered case entered words, with lowered letters of the original cities
				if (strings[1].toLowerCase(Locale.ROOT).equals(source)) {

					// get the id number of the source
					src = Integer.parseInt(strings[0]);
				}

				if (strings[1].toLowerCase(Locale.ROOT).equals(destination)) {
					// get the id number of the destination
					dst = Integer.parseInt(strings[0]);
				}
			}

			BFSPath(g, src, dst);

			JOptionPane.showMessageDialog(null, "Breath-First Search Chosen");
			spaceC.setText("");
			timeC.setText("");
		} else if (choseAlgo.getValue().equals("DFS")) {

			DFS(sourceCountry, targetCountry);
			JOptionPane.showMessageDialog(null, "Depth-First Search Chosen");
			spaceC.setText("");
			timeC.setText("");
		}

		//System.out.println(table);
		// clear the Observable List that holds all cities between the target and source
		// Countris
		pathCountris.clear();

		// clear the list view that shows all cities between the target and source
		// cities
		pathListView.getItems().clear();

		// reset the total distance Text Field
		totalDistanceView.setText("0.0");

		// get shortest path between source and target chosen Countries******* and drow
		// lines**********
		loc.add(targetCountry);
		shortestPath(sourceCountry, targetCountry);/////////////////////////////////////////////////////
		loc.add(sourceCountry);

		for (int i = 0; i < loc.size() - 1; i++) {
			line = new Line();
			line.setStroke(Color.BLACK);
			line.setStartX(loc.get(i).getCoordinateX());
			line.setStartY(loc.get(i).getCoordinateY());
			line.setEndX(loc.get(i + 1).getCoordinateX());
			line.setEndY(loc.get(i + 1).getCoordinateY());
			pane.getChildren().add(line);
			lines.add(line);
			//////////////////////////////////////////////////////////////////////////////////
		}

		totalDistanceView
				.setText(String.valueOf(table.get(searchInTable(targetCountry)).getDistance()) + " " + "KiloMeter");
		/*
		 * Add all the Countries that were found between the source and target Countries
		 */
		pathListView.getItems().add(sourceCountry.getName() + " (start) --->");
		for (int i = pathCountris.size() - 1; i >= 0; i--) {
			if (i == 0) {
				pathListView.getItems().add(pathCountris.get(i) + " (end)");
			} else {
				pathListView.getItems().add(pathCountris.get(i) + " --->");
			}
		}
	}

	/*
	 * A recursive method to trace the path between two cities
	 */
	private void shortestPath(Country sourceCountry, Country targetCountry) {
		pathCountris.add(targetCountry.getName());

		targetCountry.getCircle().setFill(Color.GREEN);

		if ((table.get(searchInTable(targetCountry)).getSourceCountry() == sourceCountry)
				|| (sourceCountry == targetCountry)) {
			sourceCountry.getCircle().setFill(Color.GREEN);
			return;
		}
		Country c;
		c = table.get(searchInTable(targetCountry)).getSourceCountry();
		loc.add(c);

		Country ccc = table.get(searchInTable(targetCountry)).getSourceCountry();
		shortestPath(sourceCountry, ccc);
	}

	/*
	 * Get an index of a wanted city in the table
	 */
	private int searchInTable(Country country) {
		for (int i = 0; i < table.size(); i++) {
			if (country == table.get(i).getCurrentCountry()) {
				return i;
			}
		}
		return 0;
	}

// Hadeel :
	private void buildTableCar(Country source, Country destination) {

		int time = 0, space = 0;
		double distanceWithHeuristic = 0;
		Queue<Table> q = new PriorityQueue<Table>();

		// search for source Country and start building the table
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getCurrentCountry() == source && !table.get(i).isKnown()) {  // table : arrayList
				table.get(i).setKnown(true);
				table.get(i).setDistance(0.0);
				q.add(table.get(i));
			}
		}

		// for each adjacent for the current Country
		while (!q.isEmpty()) {
			time++;
			// calculate the fringe size
			if (q.size() > space)
				space = q.size();
			Table temp = q.poll(); // obj from class table >> root
			temp.setKnown(true);
			Country co = temp.getCurrentCountry();  // object from class country
			if (co.equals(destination))
				break;
			for (Country adjacent : co.getAdjacent()) { // for(i=0; i<co.getAdjacent().size(); i++) // Country adjacent[i] = co[i];
				time++;
				Country co2 = adjacent;
				double weight = getDistanceBetween(co, co2);
				double distanceWithOutHeuristic = weight + temp.getDistance();
				distanceWithHeuristic = distanceWithOutHeuristic + getHeursticWalkingBetween(co2, co);
				int ind = Countris.lastIndexOf(co2);
				if (table.get(ind).getDistance() > distanceWithHeuristic) {
					table.get(ind).setDistance(distanceWithOutHeuristic);
					table.get(ind).setDistanceWithHeuristic(distanceWithHeuristic);
					table.get(ind).setSourceCountry(co);
					q.add(table.get(ind));
				}
			}
		}
		System.out.println("Car distance between "+source+" and "+destination+" is "+distanceWithHeuristic);
		timeC.setText("# Of iterations : " + time);
		spaceC.setText("Maximum Size of heap : " + space);
	}

	private void buildTableWalking(Country source, Country destination) {

		int time = 0, space = 0;
		double distanceWithHeuristic = 0;
		Queue<Table> q = new PriorityQueue<Table>();

		// search for source Country and start building the table
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getCurrentCountry() == source && !table.get(i).isKnown()) {
				table.get(i).setKnown(true);
				table.get(i).setDistance(0.0);
				q.add(table.get(i));
			}
		}

		// for each adjacent for the current Country
		while (!q.isEmpty()) {
			time++;
			// calculate the fringe size
			if (q.size() > space)
				space = q.size();
			Table temp = q.poll();
			temp.setKnown(true);
			Country co = temp.getCurrentCountry();
			if (co.equals(destination))
				break;
			for (Country adjacent : co.getAdjacent()) {
				time++;
				Country co2 = adjacent;
				double weight = getDistanceBetween(co, co2);
				double distanceWithOutHeuristic = weight + temp.getDistance();
				distanceWithHeuristic = distanceWithOutHeuristic + getHeursticCarBetween(co2, co);
				int ind = Countris.lastIndexOf(co2);
				if (table.get(ind).getDistance() > distanceWithHeuristic) {
					table.get(ind).setDistance(distanceWithOutHeuristic);
					table.get(ind).setDistanceWithHeuristic(distanceWithHeuristic);
					table.get(ind).setSourceCountry(co);
					q.add(table.get(ind));
				}
			}
		}
		System.out.println("Walking distance between "+source+" and "+destination+" is "+distanceWithHeuristic);
		timeC.setText("# Of iterations : " + time);
		spaceC.setText("Maximum Size of heap : " + space);
	}


	//////////////////////////////////////////////////////////////////////////////////


	String[][] citiesNum = new String[20][20]; // Array to add all cities with their ID
	String[][] cities = new String[78][7]; //Array to add each pair (names) and the distances between them.

	// To read and get each city with its ID.
	public void cityNum() throws IOException {

		BufferedReader br1 = new BufferedReader(new FileReader("cityNum.txt"));
		String line1;
		//////// save each city with its number

		int w = 0;
		while ((line1 = br1.readLine()) != null) {

			String[] spStr = line1.trim().split(",");
			System.arraycopy(spStr, 0, citiesNum[w], 0, spStr.length);
			w++;
		}
		br1.close();

		// to get the data of all pairs.
		city();
	}


	List<List<Integer>> g = new ArrayList<>();

	public void city() throws IOException {

		// save the cities and their IDs.

		BufferedReader br = new BufferedReader(new FileReader("Data2.txt"));
		String line;

		//////////// save each pair with the distance
		int j = 0;
		while ((line = br.readLine()) != null) {

			String[] spStr = line.trim().split(",");

			for (int y = 0; y < spStr.length; y++) {
				cities[j][y] = spStr[y];
			}
			j++;
		}
		br.close();

		String nu = null;

		// to get the numbers of the destination.
		for (int h = 0; h < cities.length; h++) {
			String c2 = cities[h][2];
			for (String[] strings : citiesNum) {
				if (strings[1].equals(c2)) {
					nu = strings[0];
				}
			}
			cities[h][6] = nu;
		}

		int v = 78;
		for (int i = 0; i < v; i++) {
			g.add(new ArrayList<>());
		}

		for (String[] strings : cities) {
			g.get(Integer.parseInt(strings[0])).add(Integer.valueOf(strings[6]));
		}
	}


	private boolean isNotVisited(int x, List<Integer> path) {
		for (Integer integer : path)
			if (integer == x)
				return false;

		return true;
	}

	private void BFSPrint(List<Integer> path) {
		System.out.print("Path... ");

		pathListView.getItems().add("Start:");

		for (Integer i : path){

			System.out.print(" -> " + citiesNum[i][1]);
			pathListView.getItems().add(citiesNum[i][1]);
		}
		pathListView.getItems().add("END");
		System.out.println();
	}


	// Utility function for finding paths in graph
	// from source to destination
	private void BFSPath(List<List<Integer> > g, int src, int dst) {

		// Create a queue which stores the paths
		Queue<List<Integer>> queue = new LinkedList<>();
		// Path vector to store the current path
		List<Integer> path = new ArrayList<>();
		path.add(src);
		queue.offer(path);

		while (!queue.isEmpty()) {
			path = queue.poll();
			int last = path.get(path.size() - 1);

			// If last vertex is the desired destination
			// then print the path
			if (last == dst) {
				BFSPrint(path);
				break;
			}

			// Traverse to all the nodes connected to
			// current vertex and push new path to queue
			List<Integer> lastNode = g.get(last);
			for (Integer integer : lastNode) {
				if (isNotVisited(integer, path)) {

					List<Integer> newPath = new ArrayList<>(path);
					newPath.add(integer);
					queue.offer(newPath);
				}
			}
		}
	}

	private void DFS(Country source, Country destination) {

		Stack<Table> stack = new Stack<Table>();

		// search for source Country and start building the table
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getCurrentCountry() == source && !table.get(i).isKnown()) {
				table.get(i).setKnown(true);
				table.get(i).setDistance(0.0);
				stack.add(table.get(i));
			}
		}

		// for each adjacent for the current Country
		while (!stack.isEmpty()) {
			Table temp = stack.peek();
			temp.setKnown(true);
			Country co = temp.getCurrentCountry();
			if (co.equals(destination))
				break;
			for (Country adjacent : co.getAdjacent()) {

				Country co2 = adjacent;
				double weight = getDistanceBetween(co, co2);
				double distance = weight + temp.getDistance();

				int ind = Countris.lastIndexOf(co2);

				table.get(ind).setDistance(distance);

				table.get(ind).setSourceCountry(co);
				stack.add(table.get(ind));
			}
		}
	}

	/*
	 * Get the distance between two Countris from the edges Array List
	 */
	private double getDistanceBetween(Country Country1, Country Country2) {

		for (Edge i : edges) {
			if (i.getSourceCountry().getName().equals(Country1.getName())
					&& i.getTargetCountry().getName().equals(Country2.getName())) {
				return i.getDistance();
			} else if (i.getSourceCountry().getName().equals(Country2.getName())
					&& i.getTargetCountry().getName().equals(Country1.getName())) {
				return i.getDistance();
			}
		}

		return 0;
	}

	private double getHeursticCarBetween(Country Country1, Country Country2) {

		for (Edge i : edges) {
			if (i.getSourceCountry().getName().equals(Country1.getName())
					&& i.getTargetCountry().getName().equals(Country2.getName())) {
				return i.getHeuristicCar();
			} else if (i.getSourceCountry().getName().equals(Country2.getName())
					&& i.getTargetCountry().getName().equals(Country1.getName())) {
				return i.getHeuristicCar();
			}
		}
		return 0;
	}

	private double getHeursticWalkingBetween(Country Country1, Country Country2) {

		for (Edge i : edges) {
			if (i.getSourceCountry().getName().equals(Country1.getName())
					&& i.getTargetCountry().getName().equals(Country2.getName())) {
				return i.getHeuristicWalking();
			} else if (i.getSourceCountry().getName().equals(Country2.getName())
					&& i.getTargetCountry().getName().equals(Country1.getName())) {
				return i.getHeuristicWalking();
			}
		}

		return 0;
	}

/////Reset button /////////////////////////////////////

	@FXML
	void resetAction(ActionEvent event) {
		loadMapButton.setDisable(false);
		Algorithms.clear();
		Algorithms = new ArrayList<String>();
		Countris.clear();
		edges.clear();
		pathListView.getItems().clear();
		totalDistanceView.clear();
		spaceC.setText("");
		timeC.setText("");

		for (int i = 0; i < lines.size(); i++) {
			pane.getChildren().remove(lines.get(i));
		}

		for (int i = 0; i < circles.size(); i++) {
			pane.getChildren().remove(circles.get(i));
			pane.getChildren().remove(labels.get(i));
		}
		pane.getChildren().remove(sourceCountry);
		pane.getChildren().remove(targetCountry);
		ObservableList<Node> xx = pane.getChildren();
		lines.clear();
		loc.clear();
		choseAlgo.getItems().clear();
		selectstart.getItems().clear();
		selecttarget.getItems().clear();

		source.setText(null);
		target.setText(null);

	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

