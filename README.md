# MyButler-Telegram-Bot
MyButler is a simple Telegram bot which informs you when a product is available for order in a list of websites

Library used for parsing HTML: Jsoup (https://jsoup.org/)

--Configuration
Class: my.butler.config.Configuration contains ALL configuration.
chatID and botToken are absolutely necessary

--Command: "/help" provides you all available Commands which are:
"/debugon" = enables printing even though ordering is not possible
"/debugoff" = disables the nonimportant output
"/printconfig" = prints the current config
"/setinterval (sec)" = you can set in seconds how often the bot should check the websites