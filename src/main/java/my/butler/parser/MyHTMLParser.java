package my.butler.parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import my.butler.config.Configuration;
import my.butler.telegram.Bot;


public class MyHTMLParser {
	
	public static Timer timer;
	public static TimerTask timerTask;
	private static Bot bot;
	
	public static List<String> testHtml() throws Exception {
		List<String> results = new ArrayList<String>();

			for (Entry<String, String[]> site : Configuration.urlMap.entrySet()) {

				Document doc = Jsoup.connect(site.getKey())
						.maxBodySize(0).timeout(0).get();
				
				if (stringContainsItemFromList(doc.body().text(), site.getValue())) {
						if(Configuration.debug) {
							results.add(getDomain(site.getKey()) + " -- " + stringContained(doc.body().text(), site.getValue()));
						}
				} else {
					results.add(site.getKey() + " -- PS5 Available for Ordering!!!");
				}
			
			}
		
		return results;
	}
	
	public static boolean stringContainsItemFromList(String inputStr, String[] items) {
	    return Arrays.stream(items).anyMatch(inputStr::contains);
	}
	public static String stringContained(String inputStr, String[] items) {
	    for (String item : items) {
			if(inputStr.contains(item)) {
				return item;
			}
		}
		throw new InternalError("couldnt find String "+inputStr);
	}
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		Configuration.init();
		TelegramBotsApi telegramBotApi = null;
		bot = new Bot();
		try {
			telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotApi.registerBot(bot);
        } catch (TelegramApiException e) {
        	bot.sendMsg(Configuration.chatID, e.getMessage());
		}
		triggerTimerSchedule();
	}

	public static void triggerTimerSchedule() {
		// creating timer task, timer  
		timer = new Timer();  
		timerTask = new TimerTask() {  
		    @Override  
		    public void run() {  	
		    	List<String> results = new ArrayList<>();
			
				try {
					results = testHtml();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				String outgoingMSG = "";
				for (String singleResult : results) {
					outgoingMSG += singleResult+"\n\n";
				}
				// Send the Message
				bot.sendMsg(Configuration.chatID, outgoingMSG); 		          
		    };  
		};  
		
		timer.schedule(timerTask, new Date(), Configuration.interval);
	}
	
	

	public static String getDomain(String url) throws URISyntaxException {
		URI uri = new URI(url);
		return uri.getHost();
	}
}
