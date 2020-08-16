package com.quantil.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Common Utility class 
* 
* @author Kalyani Yaganti
*/
public class CommonUtil {
	
	/**
	 * verifies if the given folderpath is a folder
	 * @param folderPath
	 * @return {@link boolean}
	 */
	public static boolean isPathAFolder(String folderPath) {
		return new File(folderPath).isDirectory();
	}

	/**
	 * verifies if file exists in the given path
	 * @param filePath
	 * @return {@link boolean}
	 */
	public static boolean isLogFileExistsInPath(String filePath) {
		return new File(filePath).exists();
	}
	
	/**
	 * verifies the given time string is of valid format
	 * @param time
	 * @return {@link boolean}
	 */
	public static boolean isTime24HrFormat(String time) {
		Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
		Matcher matcher = pattern.matcher(time);
		return matcher.matches();
	}
	
	/**
	 * Convert any timestamp based on format to unix time
	 * @param timeStampFormat
	 * @param timeStamp
	 * @return {@link Integer}
	 */
	public static Integer convertTimestampToUnixTime(String timeStampFormat, String timeStamp) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(timeStampFormat);
			Date dt = sdf.parse(timeStamp);
			long epoch = dt.getTime();
			return (int) (epoch / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Convert unix time to any timestamp based on input format
	 * @param unixSeconds
	 * @param timeStampFormat
	 * @return {@link String}
	 */
	public static String convertUnixTimeToTimestamp(int unixSeconds, String timeStampFormat) {
		try {
			Date date = new java.util.Date(unixSeconds * 1000L);
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(timeStampFormat);
			String formattedDate = sdf.format(date);
			return formattedDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Generate IP addresses given an initial IP and number of IP address to generate
	 * @param initialIP
	 * @param ipCountToGenerate
	 * @return {@link List<String>}
	 */
	public static List<String> generateIPAddresses(String initialIP, int ipCountToGenerate){

		List<String> ipResult = new ArrayList<String>();
		String nextIp = initialIP;
		for (int i = 0; i < ipCountToGenerate; i++) {
			ipResult.add(nextIp);
			
			String[] parts = nextIp.split("\\.");
	        if (parts.length == 4) {
	            Integer part1 = Integer.parseInt(parts[0]);
	            Integer part2 = Integer.parseInt(parts[1]);
	            Integer part3 = Integer.parseInt(parts[2]);
	            Integer part4 = Integer.parseInt(parts[3]);
	            if (part4 < 255) {
	                String ip = part1 + "." + part2 + "." + part3 + "." + (++part4);
	                nextIp = ip;
	            } else if (part4 == 255) {
	                if (part3 < 255) {
	                    String ip = part1 + "." + part2 + "." + (++part3) + "." + (0);
	                    nextIp = ip;
	                } else if (part3 == 255) {
	                    if (part2 < 255) {
	                        String ip = part1 + "." + (++part2) + "." + (0) + "." + (0);
	                        nextIp = ip;
	                    } else if (part2 == 255) {
	                        if (part1 < 255) {
	                            String ip = (++part1) + "." + (0) + "." + (0) + "." + (0);
	                            nextIp = ip;
	                        } else if (part1 == 255) {
	                            System.out.println("Cannot generate "+i+"th IP in the sequence with given IP: -> "+initialIP);
	                            System.out.println("Generated "+ipResult.size()+" IP addresses");
	                        }
	                    }
	                }
	            }
	        }
		}
		return ipResult;
	}

}
