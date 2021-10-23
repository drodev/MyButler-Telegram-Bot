package my.butler.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import my.butler.config.Configuration;
import my.butler.parser.MyHTMLParser;

public class Bot extends TelegramLongPollingBot {
	
    /**
     * Method for receiving messages.
     * @param update Contains a message from the user.
     */
	public void onUpdateReceived(Update update) {
		String chatID = update.getMessage().getChatId().toString();
		String inputCommand = update.getMessage().getText();
		
		// Debug Command selection
		switch (inputCommand.toLowerCase()) {
		case "/debugon":
			Configuration.debug = true;
			sendMsg(chatID, "Debug ON");
			break;
		case "/debugoff":
			Configuration.debug = false;
			sendMsg(chatID, "Debug OFF");
			break;
		case "/printconfig":
			sendMsg(chatID, "Debug: " + Configuration.debug + "\n" + "Interval: " + Configuration.interval/1000 + "s\n");
			break;
		case "/help":
			String help;
			help = "Commands:\n"+
					"/debugon\n"+
					"/debugoff\n"+
					"/printconfig\n"+
					"/setinterval (sec)\n";
			sendMsg(chatID, help);
			break;
		}
		
		if (inputCommand.toLowerCase().startsWith("/setinterval")) {
			try {
				String interval = inputCommand.substring(12).trim();
				long intervalLong = Long.valueOf(interval)*1000;
				Configuration.interval = intervalLong;
				sendMsg(chatID, "Interval changed to " + intervalLong/1000);
				MyHTMLParser.timer.cancel();
				MyHTMLParser.triggerTimerSchedule();
			}catch(NumberFormatException e){
				sendMsg(chatID, "Error: Enter only seconds.");
			}
		}
	}

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     * @param s The String that you want to send as a message.
     */
    public synchronized void sendMsg(String chatId, String s) {
    	if (s == null || s.isEmpty() ) {
    		return;
    	}
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
        	String exception = "Exception: " + e.toString();
        	sendMessage = new SendMessage();
            sendMessage.setText(exception);
            try {
				super.execute(sendMessage);
			} catch (TelegramApiException e1) {
				System.out.println("Exception " + e.toString());
			}
        }
    }

    /**
     * This method returns the bot's name, which was specified during registration.
     * @return bot name
     */
    
    public String getBotUsername() {
        return Configuration.botUsername;
    }

    /**
     * This method returns the bot's token for communicating with the Telegram server
     * @return the bot's token
     */
    @Override
    public String getBotToken() {
        return Configuration.botToken;
    }
}