package com.moe.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;


public class JavaUtils {

	private static Logger log = Logger.getLogger(JavaUtils.class);
	private static final Long MILLISECONDS_IN_SECOND = Long.valueOf(1000);


	/**
	 * @param fileToDelete
	 * @throws FileNotFoundException
	 */
	public static void deleteFile(String fileToDelete) throws FileNotFoundException {
		log.info("Deleting a file --> " + fileToDelete);
		try {
			File file = new File(fileToDelete);
			if (file.delete()) {
				log.info(file.getName() + " is deleted!");
			} else {
				log.error("Nothing to delete.");
			}
		} catch (Exception e) {
			log.error("Something went Wrong --> " + e.getMessage());
		}
	}

	public static void deleteFileWithExtension(String folderPathOfTheFile, String extensionOfTheFile) {
		log.info("Deleting a file with an extension --> {" + extensionOfTheFile +"} ");
		try {
	        File dir = new File(folderPathOfTheFile);
	        String [] str = {extensionOfTheFile};
	        Collection<File> files = FileUtils.listFiles(dir,str,true);   
	        for(File f:files){
	            f.delete();
	        }
		} catch (Exception e) {
			log.error("Something went Wrong --> " + e.getMessage());
		}
	}


	public static String generateRandomDate(String Format, String startDate, String endDate)
			throws java.text.ParseException {
		log.info("Generating Random Date");
		DateFormat formatter = new SimpleDateFormat(Format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatter.parse(startDate));
		Long value1 = cal.getTimeInMillis();

		cal.setTime(formatter.parse(endDate));
		Long value2 = cal.getTimeInMillis();

		long value3 = (long) (value1 + Math.random() * (value2 - value1));
		cal.setTimeInMillis(value3);
		return formatter.format(cal.getTime());
	}


	public static String arrayToString(String[] str, String separator) {
		log.info("Converting a String Array to a String");
		StringBuilder result = new StringBuilder();

		if (str.length > 0) {
			result.append(str[0]);
			for (int i = 1; i < str.length; i++) {
				result.append(separator);
				result.append(str[i]);
			}
		}
		return result.toString();
	}

	public static boolean compareArrays(int[] array1, int[] array2) {
		log.info("Comparing two integer arrays");
		boolean isMatch = true;
		if (array1 != null && array2 != null) {
			if (array1.length != array2.length)
				return !isMatch;
			else
				for (int i = 0; i < array2.length; i++) {
					if (array2[i] != array1[i]) {
						return !isMatch;
					}
				}
		} else {
			return !isMatch;
		}
		return isMatch;
	}

	public static int getRandomNumberBetween(int min, int max) {
		log.info("Generating a random number");
		Random foo = new Random();
		int randomNumber = foo.nextInt(max - min) + min;
		if (randomNumber == min) {
			return min + 1;
		} else {
			return randomNumber;
		}

	}

	public static String generateRandomString(int length) {
		log.info("Generating a random String");
		StringBuilder str = new StringBuilder(RandomStringUtils.randomAlphabetic(length));
		int idx = str.length() - 8;

		while (idx > 0) {
			str.insert(idx, " ");
			idx = idx - 8;
		}
		return str.toString();
	}


	public static String generateRandomPhoneNumber() {
		log.info("Generating a random phone number");
		return RandomStringUtils.randomNumeric(10);
	}

	public static String generateRandomNumber(int length) {
		log.info("Generating a random number");
		return RandomStringUtils.randomNumeric(length);
	}

	public static String generateRandomAlphaNumeric(int length) {
		log.info("Generating an Alphanumeric String");
		StringBuilder str = new StringBuilder(RandomStringUtils.randomAlphanumeric(length));
		int idx = str.length() - 8;

		while (idx > 0) {
			str.insert(idx, " ");
			idx = idx - 8;
		}
		return str.toString();

	}


	public static String generateRandomStringWithAllowedSplChars(int length, String allowdSplChrs) {
		log.info("Generating a Random String");
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890" + allowdSplChrs
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder str1 = new StringBuilder(RandomStringUtils.random(length, allowedChars));
		int idx = str1.length() - 8;

		while (idx > 0) {
			str1.insert(idx, " ");
			idx = idx - 8;
		}
		return str1.toString();
	}


	public static String generateRandomEmail(int length) {
		log.info("Generating a Random email String");
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890" + "_-.";
		String email = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		email = temp.substring(0, temp.length() - 9) + "@testdata.com";
		return email;
	}


	public static String generateRandomUrl(int length) {
		log.info("Generating a Random URL String");
		String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890" + "-.";
		String url = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		url = temp.substring(0, 3) + "." + temp.substring(4, temp.length() - 4) + "." + "com";
		return url;
	}


	public static String getOnlyDigits(String str) {
		log.info("Getting the degits only from " + str);
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				result += str.charAt(i) + "";
			}
		}

		return result;
	}


	public static String removeQuotation(final String value) {
		log.info("Removing quotations from " + value);
		if (value == null) {
			return null;
		}
		String trimmedValue = value.trim();
		if (trimmedValue.length() > 1) {
			String firstChar = trimmedValue.substring(0, 1);
			String lastChar = trimmedValue.substring(trimmedValue.length() - 1);
			if (firstChar.equals(lastChar) && firstChar.matches("[\\\"']")) {
				// this value has leading and flanking quotation signs
				return trimmedValue.substring(1, trimmedValue.length() - 1);
			}
		}
		return trimmedValue;
	}

	public static String getCharRegexp(final char ch) {
		log.info("Getting character regular expression ");
		String regex = "";
		if ("\\.^$|?*+[]{}()\\t\\n\\f\\r".indexOf(ch) != -1) {
			regex += "\\" + ch;
		} else if (Character.isSpaceChar(ch)) {
			regex += "[" + ch + "]";
		} else {
			regex += ch;
		}
		return regex;
	}


	public static Long getTimeInSeconds(final Long milliseconds) {
		log.info("Converting milliseconds to seconds");
		return milliseconds / MILLISECONDS_IN_SECOND;
	}

	public static File saveAsFormat(final String outputfilePath, final byte[] srcFile, final String format)
			throws IOException {
		log.info("Saving a file in a specific format");
		BufferedImage imagePNG = ImageIO.read(new ByteArrayInputStream(srcFile));

		BufferedImage imageRGB = new BufferedImage(imagePNG.getWidth(), imagePNG.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		imageRGB.createGraphics().drawImage(imagePNG, 0, 0, Color.WHITE, null);
		File resultFile = new File(outputfilePath + "." + format);
		ImageIO.write(imageRGB, format, resultFile);
		return resultFile;
	}


	public static void javaWait(int timeToWaitInSeconds) {
		log.info("waiting " + timeToWaitInSeconds + " seconds");
		try {
			Thread.sleep(timeToWaitInSeconds * 1000);
		} catch (InterruptedException e) {
			log.error("Something went Wrong --> " + e.getMessage());
		}
	}

	public static String getTimeStamp() {
		log.info("Getting current time");
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime()).toString();
	}


	public static void killProcess(String processName) throws IOException {
		log.info("Killing process : " + processName);
		Runtime.getRuntime().exec("taskkill /f /im " + processName + ".exe");
	}

	public static void clearTempFolder() throws IOException {
		log.info("Clearing Temp folder");
		try {
			File file = new File(System.getProperty("java.io.tmpdir"));
			FileUtils.cleanDirectory(file);
		}

		catch (IOException e) {
			log.info("Issue while clearing the temp folder: " + e.getMessage());
			// Do nothing since we do not worry about the files that cannot be deleted

		}
	}


	public static void replaceElementInArrayOfString(String[] items, String oldValue, String newValue) {
		log.info("Replacing element in an array");
		System.out.println("********Printing old array*********");
		System.out.println(Arrays.toString(items));
		for (int index = 0; index < items.length; index++)
			if (items[index] == oldValue)
				items[index] = newValue;
		System.out.println("********Printing New array*********");
		System.out.println(Arrays.toString(items));
	}


	public static String[] arrayListToArray(List<String> list) {
		list = new ArrayList<String>();
		String[] array = list.toArray(new String[list.size()]);
		return array;

	}


	public static List<String> arrayToArrayList(String[] array) {
		List<String> list = new ArrayList<String>(Arrays.asList(array));
		return list;

	}
	
	
	
	@SuppressWarnings("restriction")
	public static String encodeImageToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

}
