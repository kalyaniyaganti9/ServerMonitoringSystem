package com.quantil.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
* LogFileSimulator to simulate logs for 1000 servers.
* Each server has two CPUS with id 0 and 1
* The simulation of logs done per minute for each server 
* throughout 24 hours of the day "2014-10-31"
*
* @author Kalyani Yaganti
*/
public class LogFileSimulator {

	/**
	 * folder path where the 'simulator.log' is stored
	 */
	private String logFolderPath;
	private List<String> serverIPs;
	
	/**
	 * default constructor
	 */
	public LogFileSimulator() { }
	
	/**
	 * @param logFolderPath
	 */
	public LogFileSimulator(String logFolderPath) {
		this.logFolderPath = logFolderPath;
		
		// Generate 1000 IPs for 1000 servers
		this.serverIPs = CommonUtil.generateIPAddresses(Constants.default_log_ip, 1000);
	}

	/**
	 * Getter for logFolderPath
	 * @return
	 */
	public String getLogFolderPath() {
		return logFolderPath;
	}

	/**
	 * Simulate logs to 'simulator.log' and store in the 'logFolderPath'
	 */
	public void generateLogs() {
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			System.out.println("Simulating log file...");
			
			fw = new FileWriter(logFolderPath+"/"+Constants.logFileName);
			bw = new BufferedWriter(fw);
						
			//Unix start and end time for the date 2014-10-31
			int unixTimeStart = CommonUtil.convertTimestampToUnixTime(Constants.simple_date_format, Constants.default_log_start_time);
			int unixTimeEnd = CommonUtil.convertTimestampToUnixTime(Constants.simple_date_format, Constants.default_log_end_time);
			bw.write("timestamp IP cpu_id usage");
			bw.newLine();
			
			//Update log file for each minute throughout the day
			for(;unixTimeStart<unixTimeEnd;unixTimeStart+=60)
			{
				logServerUsage(bw, unixTimeStart);
			}
			
			System.out.println("Simulated log file: "+logFolderPath+"/"+Constants.logFileName);
		} catch (Exception e) {
			System.err.format("Exception: %s%n", e);
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (Exception ex) {
				System.err.format("Exception: %s%n", ex);
			}
		}
		
	}

	/**
	 * Log servers usage for CPU 0 and CPU 1
	 * @param bw
	 * @param unixTimeStamp
	 * @throws IOException
	 */
	private void logServerUsage(BufferedWriter bw, int unixTimeStamp) throws IOException {
		for(String ipAddress: serverIPs) {
			bw.write(unixTimeStamp+" "+ipAddress+" 0 "+(int)(Math.random()*100));
			bw.newLine();
			bw.write(unixTimeStamp+" "+ipAddress+" 1 "+(int)(Math.random()*100));
			bw.newLine();
		}
	}	

}
