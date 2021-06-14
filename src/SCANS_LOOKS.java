
/**
 * BSCS191A OPERATING SYSTEMS
 * SCANS_LOOKS.java
 * Purpose: Disk scheduling algorithms (SCAN and LOOK, and their variations)
 *
 * @author Maureen Kate R. Dadap
 * @version 1.0 06/13/21
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class SCANS_LOOKS {
    // **************************************************
    // Constants
    // **************************************************

    /**
     * Contains the value of the start of the disk, value is {@value #DISK_START}.
     */
    public static final int DISK_START = 0;
    /** Contains the value of the end of the disk, value is {@value #DISK_END}. */
    public static final int DISK_END = 199;

    // **************************************************
    // Fields
    // **************************************************

    /** Contains the sequence of I/O requests. */
    public static List<Integer> requestsQueue;
    /** Contains the head start position. */
    public static int head;
    /** Contains the total seek time. */
    public static int seekTime;

    /**
     * Contains the movement or stops (tracks serviced) in the algorithm, as well as
     * the string for display. For the right version of the algorithm.
     */
    public static Map<Integer, String> movementMapRight;
    /**
     * Contains the movement or stops (tracks serviced) in the algorithm, as well as
     * the string for display. For the left version of the algorithm.
     */
    public static Map<Integer, String> movementMapLeft;

    /**
     * Contains a copy of the requests queue with which will later include the disk
     * start and end, and the head start position.
     */
    public static List<Integer> tracksList;

    // **************************************************
    // Public methods
    // **************************************************

    /**
     * Initializes the variables.
     * 
     * User inputs the request sequence and the head start position.
     * 
     * @param scanner The scanner instance from the main method.
     */
    public static void initialize(Scanner scanner) {
        requestsQueue = new ArrayList<Integer>();
        seekTime = 0;

        movementMapRight = new LinkedHashMap<Integer, String>();
        movementMapLeft = new LinkedHashMap<Integer, String>();

        requestsQueue = inputQueue(); // input requests

        do {
            System.out.println("> HEAD start position: ");
            System.out.print("-> ");
            head = scanner.nextInt();
        } while (head < 0 || head > 199);

        tracksList = requestsQueue;

        tracksList.add(head);

        if (!tracksList.contains(DISK_START))
            tracksList.add(DISK_START); // add the disk start if not in list yet
        if (!tracksList.contains(DISK_END))
            tracksList.add(DISK_END); // add the disk end if not in list yet
        Collections.sort(tracksList); // sort the list
    }

    /**
     * User inputs a string of numbers (I/O requests) separated by spaces.
     *
     * The string is parsed and created into an arraylist.
     *
     * @return The list of I/O requests.
     */
    public static List<Integer> inputQueue() {
        List<String> input = new ArrayList<String>();
        List<Integer> queue = new ArrayList<Integer>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean flag;

        do {
            flag = false;
            input.clear();
            queue.clear();

            try {
                System.out.println("> Enter the requests queue, the max is " + DISK_END + ", min is " + DISK_START
                        + ". Separate with SPACES");
                System.out.print("-> ");
                input.addAll(Arrays.asList(br.readLine().split("\\s")));
            } catch (Exception e) {
                System.out.println("\t ________________________________________________");
                System.out.println("\t||                                                ||");
                System.out.println("\t|| > INVALID INPUT                                ||");
                System.out.println("\t||________________________________________________||");
            }

            for (int i = 0; i < input.size(); i++) {
                int num = Integer.parseInt(input.get(i));
                if (num > 199 || num < 0) { // checks if input has negative or over 199 and flags it if there is
                    flag = true;
                }

                if (!queue.contains(num))
                    queue.add(num);
            }

            if (flag == true)
                System.out.println("\n!!! MAX IS " + DISK_END + ", MIN IS " + DISK_START + " !!!\n");

        } while (flag == true);

        return queue;
    }

    /**
     * Generates the string of spaces and asterisk for each "row" of the displayed
     * table that serves as a way to visualize the position/movement of the disk
     * scheduling algorithm
     * 
     * @param i the current track being serviced
     * @return the string of spaces and asterisks
     */
    public static String generateRow(int i) {
        String displayString = ""; // the string for each row in the displayed table
        for (int j = 0; j < tracksList.size(); j++) {
            if (j == i)
                displayString += "*   ";
            else
                displayString += "    ";
        }

        return displayString;
    }

    /**
     * Displays the output of the disk scheduling algorithm, in a table form and in
     * a text form with arrows
     *
     * Displays the seek time of the disk scheduling algorithm
     *
     * @param tableHeaderText the header of the table to be printed
     * @param trackList       the list of tracks, including the disk start/end and
     *                        the head start position
     * @param movementMap     the map containing the tracks in the order that they
     *                        were serviced. Its key is the track number itself, and
     *                        its value is the string of spaces and asterisks that
     *                        will serve as the "row" for the displayed table
     * @param seekTime        the total seek time of the disk scheduling algorithm
     */
    public static void display(String tableHeaderText, List<Integer> trackList, Map<Integer, String> movementMap) {
        String column = "%-3d ";

        // printing table header
        System.out.println();
        for (int i = 0; i < tracksList.size() * 4; i++)
            System.out.print("-"); // prints divider line
        System.out.println("\n\t" + tableHeaderText);
        for (int i = 0; i < tracksList.size() * 4; i++)
            System.out.print("-");

        // display the queue (with the disk start and end, and head start)
        // this is the table subheader
        System.out.println();
        for (Integer integer : trackList)
            System.out.format(column, integer); // prints the numbers
        System.out.println();
        for (int i = 0; i < trackList.size() * 4; i++)
            System.out.print("-"); // prints the divider line

        System.out.println();

        // print table form
        // print each "row" of spaces and asterisks from the movementMap
        Iterator<Entry<Integer, String>> itr = movementMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, String> mapElement = (Map.Entry<Integer, String>) itr.next();
            System.out.println(mapElement.getValue());
        }

        for (int i = 0; i < trackList.size() * 4; i++)
            System.out.print("-"); // prints the divider line

        // print text form with arrows
        System.out.print("\nSTART: ");
        Iterator<Entry<Integer, String>> itr2 = movementMap.entrySet().iterator();
        while (itr2.hasNext()) {
            Map.Entry<Integer, String> mapElement = (Map.Entry<Integer, String>) itr2.next();
            System.out.print(mapElement.getKey() + " -> ");
        }
        System.out.print("STOP\n");

        System.out.println("SEEK TIME: " + seekTime);
    }

    /**
     * Prompts the user to press enter to continue.
     */
    public static void promptEnterKey() {
        System.out.print("\n\nPress \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * The main method begins the execution of the scan, c-scan, look, and c-look
     * disk scheduling algorithms.
     *
     * @param args not used
     * @return nothing
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice = '0';
        String tableHeaderText;
        do {
            // clear screen
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                // catch exception
            }

            System.out.println();
            System.out.println("CHOOSE THE DISK SCHEDULING ALGORITHM");
            System.out.println("-------------------------------");
            System.out.println("( 1 ) SCAN");
            System.out.println("( 2 ) C-SCAN");
            System.out.println("( 3 ) LOOK");
            System.out.println("( 4 ) C-LOOK");
            System.out.println("( 0 ) EXIT");
            System.out.print("CHOICE: ");

            choice = scanner.next().charAt(0);

            // ------------------------------
            // SCAN ALGORITHM
            // ------------------------------
            if (choice == '1') {
                System.out.println("  ________________________________________________");
                System.out.println("||                                                ||");
                System.out.println("||                 SCAN ALGORITHM                 ||");
                System.out.println("||________________________________________________||");

                initialize(scanner);

                // -----------------
                // SCAN RIGHT
                // -----------------

                tableHeaderText = "SCAN to the RIGHT";

                // first go to the right starting from the head
                int index = tracksList.indexOf(head);
                int prev = head;
                for (int i = index; i < tracksList.size(); i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go back to the head and move left, until before the end of disk
                for (int i = index - 1; i > 0; i--) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapRight);

                // -----------------
                // SCAN LEFT
                // -----------------
                seekTime = 0;
                tableHeaderText = "SCAN to the LEFT";

                // first go to the left starting from the head
                index = tracksList.indexOf(head);
                prev = head;
                for (int i = index; i >= 0; i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go to the head again and move right, until before the end of disk
                for (int i = index + 1; i < tracksList.size() - 1; i++) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapLeft);
                promptEnterKey();

            }

            // ------------------------------
            // C-SCAN ALGORITHM
            // ------------------------------
            else if (choice == '2') {
                System.out.println("  ________________________________________________");
                System.out.println("||                                                ||");
                System.out.println("||               C-SCAN ALGORITHM                 ||");
                System.out.println("||________________________________________________||");

                initialize(scanner);

                // -----------------
                // C-SCAN RIGHT
                // -----------------

                tableHeaderText = "C-SCAN to the RIGHT";

                // first go to the right starting from the head
                int index = tracksList.indexOf(head);
                int prev = head;
                for (int i = index; i < tracksList.size(); i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go back to the start and move right again until before the head
                index = tracksList.indexOf(DISK_START);
                prev = DISK_END;
                for (int i = index; i < tracksList.indexOf(head); i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapRight);

                // -----------------
                // C-SCAN LEFT
                // -----------------
                seekTime = 0;
                tableHeaderText = "C-SCAN to the LEFT";

                // first go to the left starting from the head
                index = tracksList.indexOf(head);
                prev = head;
                for (int i = index; i >= 0; i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go to the end and move left again until before the head
                index = tracksList.indexOf(DISK_END);
                prev = DISK_START;
                for (int i = index; i > tracksList.indexOf(head); i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapLeft);
                promptEnterKey();
            }

            // ------------------------------
            // LOOK ALGORITHM
            // ------------------------------
            else if (choice == '3') {
                System.out.println("  ________________________________________________");
                System.out.println("||                                                ||");
                System.out.println("||                LOOK ALGORITHM                  ||");
                System.out.println("||________________________________________________||");

                initialize(scanner);
                // -----------------
                // LOOK RIGHT
                // -----------------

                tableHeaderText = "LOOK to the RIGHT";

                // first go to the right starting from the head, stop at the last request
                int index = tracksList.indexOf(head);
                int prev = head;
                for (int i = index; i < tracksList.size() - 1; i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go back to the head and move left, until before the end of disk
                for (int i = index - 1; i > 0; i--) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapRight);

                // -----------------
                // LOOK LEFT
                // -----------------
                seekTime = 0;
                tableHeaderText = "LOOK to the LEFT";

                // first go to the left starting from the head, stop at the last request
                index = tracksList.indexOf(head);
                prev = head;
                for (int i = index; i > 0; i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go to the head again and move right, until before the end of disk
                for (int i = index + 1; i < tracksList.size() - 1; i++) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapLeft);
                promptEnterKey();
            }

            // ------------------------------
            // C-LOOK ALGORITHM
            // ------------------------------
            else if (choice == '4') {
                System.out.println("  ________________________________________________");
                System.out.println("||                                                ||");
                System.out.println("||               C-LOOK ALGORITHM                 ||");
                System.out.println("||________________________________________________||");

                initialize(scanner);

                // -----------------
                // C-LOOK RIGHT
                // -----------------
                tableHeaderText = "C-LOOK to the RIGHT";

                // first go to the right starting from the head, until the last request
                int index = tracksList.indexOf(head);
                int prev = head;
                for (int i = index; i < tracksList.size() - 1; i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go back to the first request and move right again until before the head
                index = tracksList.indexOf(DISK_START) + 1;
                for (int i = index; i < tracksList.indexOf(head); i++) {
                    // put the current track and the display string in the map
                    movementMapRight.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapRight);

                // -----------------
                // C-LOOK LEFT
                // -----------------
                seekTime = 0;
                tableHeaderText = "C-LOOK to the LEFT";

                // first go to the left starting from the head, until the first request only
                index = tracksList.indexOf(head);
                prev = head;
                for (int i = index; i > 0; i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                // then go to the end and move left again until before the head
                index = tracksList.indexOf(DISK_END) - 1;
                for (int i = index; i > tracksList.indexOf(head); i--) {
                    // put the current track and the display string in the map
                    movementMapLeft.put(tracksList.get(i), generateRow(i));

                    // get the distance between the current track and prev
                    seekTime += Math.abs(tracksList.get(i) - prev);

                    // current is now prev
                    prev = tracksList.get(i);
                }

                display(tableHeaderText, tracksList, movementMapLeft);
                promptEnterKey();
            }

            System.out.println();

        } while (choice != '0');

        scanner.close();
    }
}
