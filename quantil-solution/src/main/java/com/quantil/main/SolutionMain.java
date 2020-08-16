package com.quantil.main;


/**
* Log Simulator and Retriever Tool Main
*
* @author Kalyani Yaganti
*/
public class SolutionMain {


	private static final String logSimulator = "SIMULATOR";
	private static final String logRetriever = "RETRIEVER";

	
	public static void main(String[] args) throws Exception {

		//Program arguments validation
		
		if (args.length != 2) {
			throw new IllegalStateException("Only one argument accepted in the monitoring system program!: DATA_PATH (folder path)");
		}
		
		// Logs simulation
		if(args[0].equals(logSimulator)) {
			if(!CommonUtil.isPathAFolder(args[1])) {
				throw new IllegalArgumentException("The DATA_PATH argument: "+args[1]+" is not a folder!");
			}
			LogFileSimulator logFileSimulator = new LogFileSimulator(args[1]);
			logFileSimulator.generateLogs();
		}
	
		//Logs retrieval
		if(args[0].equals(logRetriever)) {
			if(!CommonUtil.isLogFileExistsInPath(args[1]+"/"+Constants.logFileName)) {
				throw new IllegalArgumentException("Couldn't find the log file in the DATA_PATH argument: "+args[1]);
			}
			LogFileAnalyzer logFileAnalyzer = new LogFileAnalyzer(args[1]);
			logFileAnalyzer.retrieveLogs();
		}
		
	}

}
