package my.butler.config;

import java.util.LinkedHashMap;

public class Configuration {
	public static LinkedHashMap<String, String[]> urlMap = new LinkedHashMap<String, String[]>();
	public static boolean debug;
	public static String chatID;
	public static long interval;
	public static String botUsername;
	public static String botToken;
	
	public static void init() {
		urlMap.put("https://www.test1.com/playstation-5/p-1077687/",new String[] { "PS5 Available"});
		urlMap.put("https://www.test2.com/playstation-5.html",new String[] { "Buy PS5 now"});
		
		botUsername="MyButlerApiSmartBot";
		debug=false;
		chatID = "{add your chatID}";
		botToken = "{add your botToken";
		interval = 60000;
	}
	
	
}
