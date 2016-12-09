# About
This is the partial source code for the bot that got me to first place on Internetometer.com.

# How to use
When compiled, execute:
```
java -jar InternetometerBot.jar <id> [optional:threads]
```
For example, my internetometer profile URL is http://internetometer.com/give/44772. That means my `<id>` would be `44772`. Example:
```
java -jar InternetometerBot.jar 44772 300
```

# Disclaimers
- Don't expect this code to be readable. I was never planning on publishing the source code.
- Some parts of the source code have been modified.
- There are a lot of pointless jar files in the lib folder. When developing the functions to scrape proxies, I was using different web libraries and messing around and stuff. You can safely ignore the majority of them.
- You'll have to update the JauntAPI library every month, because the author of the library placed a 1 month limit on the library, and I can't legally publish my hacked version. Here are the steps:
  - Download the library here... http://jaunt-api.com/download.htm
  - Rename the file to `jaunt1.2.3.jar`
  - Replace the file `lib/jaunt1.2.3.jar` with the newly downloaded one
  - Recompile && tada!
