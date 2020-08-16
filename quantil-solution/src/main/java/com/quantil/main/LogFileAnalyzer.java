package com.quantil.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
* LogFileAnalyzer retrieve the server CPU usage based on the input date and time range
*
* @author Kalyani Yaganti
*/
public class LogFileAnalyzer {

	/**
	 * Store all parsed logs in the map
	 */
	private HashMap<String, TreeMap<String, String>> log_Map;
	
	/**
	 * LogFolderPath that contains 'simulator.log' file to parse logs from 
	 */
	private String logFolderPath;
	
	/**
	 * default constructor
	 */
	public LogFileAnalyzer() { }
	
	/**
	 * 
	 * @param logFolderPath
	 */
	public LogFileAnalyzer(String logFolderPath) {
		this.logFolderPath = logFolderPath;
		
		System.out.println("Initializing the tool...");
		this.log_Map = queryLogs();
		System.out.println("Initializing Done");
		consoleLogUserInputInfo();
	}

	/**
	 * Validate user console input and fetch server logs based on timestamp
	 */
	public void retrieveLogs() {

		String userCommand = "";
		
		//read user initial input
		Scanner scanner = new Scanner(System.in);
		userCommand = scanner.nextLine().trim();
		
		//user inputting 'EXIT' exit the application
		while (!userCommand.equals("EXIT")) {

			//User input string validation
			if (!validateUserInput(userCommand)) {
				System.out.println(
						"Invalid Input! Enter of the format: QUERY 192.168.1.12 0 2014-10-31 00:00 2014-10-31 00:05");
				consoleLogUserInputInfo();

			}else {
				String server = userCommand.split(" ")[1] + userCommand.split(" ")[2];
				
				// Get the server map based on IP address and CPU
				TreeMap<String, String> serverTimeLogMap = log_Map.get(server);
				
				// Log start and end time for which the user request to retrieve cpu usage
				String logStartTime = userCommand.split(" ")[3] + " " + userCommand.split(" ")[4];
				String logEndTime = userCommand.split(" ")[5] + " " + userCommand.split(" ")[6];
	
				//Printing logs of server in between user input timestamps
				boolean afterStart = false;
				System.out.println();
				for (Map.Entry<String, String> serverEntry : serverTimeLogMap.entrySet()) {
	
					if (afterStart) {
						System.out.print("(" + serverEntry.getKey() + ", " + serverEntry.getValue() + "%) ");
					} else if (logStartTime.equals(serverEntry.getKey())) {
						System.out.print("(" + serverEntry.getKey() + ", " + serverEntry.getValue() + "%) ");
						afterStart = true;
					}
	
					if (logEndTime.equals(serverEntry.getKey())) {
						break;
					}
				}
				System.out.println();

				consoleLogUserInputInfo();
			}
			userCommand = scanner.nextLine().trim();
		}
		scanner.close();

		System.out.println("Exiting the Application....");
		System.exit(0);

	}
	
	/**
	 * Detailed user input validation before proceeding to retrieve the logs
	 * @param ipAddressSet
	 * @param inputString
	 * @return {@link boolean}
	 */
	private boolean validateUserInput(String inputString) {
		String[] userInputParts = inputString.split(" ");
		if(userInputParts.length != 7 || !userInputParts[0].equals("QUERY")) {
			validationConsoleLog("Input should start with QUERY and should contain a total of 7 arguments seperated by space");
			return false;
		}
		if(!(userInputParts[2].equals("0") || userInputParts[2].equals("1"))){
			validationConsoleLog("CPU Id should be either 0 or 1");
			return false;
		}
		if(!log_Map.keySet().contains(userInputParts[1]+userInputParts[2])) {
			validationConsoleLog(" Server IP range should be from 192.168.1.10 - 192.168.4.241");
			return false;
		}
		if(!userInputParts[3].equals(Constants.default_log_date) || !userInputParts[5].equals(Constants.default_log_date)) {
			validationConsoleLog(" The date must be : "+Constants.default_log_date);
			return false;
		}
		if(!CommonUtil.isTime24HrFormat(userInputParts[4]) || !CommonUtil.isTime24HrFormat(userInputParts[6])) {
			validationConsoleLog(" 24 HR time format incorrect!");
			return false;
		}
		return true;
		
	}

	/**
	 * Parse the 'simulator.log' file and store all the logs for later retrieval
	 * @return
	 */
	private HashMap<String, TreeMap<String, String>> queryLogs() {

		//log map to store the logs
		HashMap<String, TreeMap<String, String>> log_Map = new HashMap<String, TreeMap<String, String>>();
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(logFolderPath+"/"+Constants.logFileName);
			br = new BufferedReader(fr);
			br.readLine();
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] linesplit = line.split(" ");
				String key = linesplit[1] + linesplit[2];
				TreeMap<String, String> timeMap = log_Map.getOrDefault(key, new TreeMap<String, String>());
				
				//Convert unix time to simple date format and store the parsed logs to map
				String formattedTimeStamp = CommonUtil.convertUnixTimeToTimestamp(Integer.parseInt(linesplit[0]),
						Constants.simple_date_format);
				timeMap.put(formattedTimeStamp, linesplit[3]);
				log_Map.put(key, timeMap); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (Exception ex) {
				System.err.format("Exception: %s%n", ex);
			}
		}
		return log_Map;
	}
	
	/**
	 * Console log for user input information
	 */
	private void consoleLogUserInputInfo() {
		System.out.println();
		System.out.println("Enter one of below Available Options");
		System.out.println("1.QUERY (Eg input: QUERY 192.168.1.12 0 2014-10-31 00:00 2014-10-31 00:05) ");
		System.out.println("2. EXIT");
		System.out.print(">");
	}
	
	/**
	 * Console log for user for wrong input arguments
	 * @param log
	 */
	private void validationConsoleLog(String log) {
		System.out.println("Incorrect Input: "+log);
	}
}
