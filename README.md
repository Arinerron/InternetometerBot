# About
This is the partial source code for the bot that got me to first place on Internetometer.com.

# YouTube
You can watch a demonstration of the bot here: https://www.youtube.com/watch?v=LhoOI5XP16k

# How to use
On linux, execute:
```
sh run.sh <id> <optional:#ofthreads>
```

On windows, make sure you have both the jre and jdk in your PATH. Then run the batch script at `run.bat`.

# Disclaimers
- Don't expect this code to be readable. I was never planning on publishing the source code.
- Some parts of the source code have been modified.
- There are a lot of pointless jar files in the lib folder. When developing the functions to scrape proxies, I was using different web libraries and messing around and stuff. You can safely ignore the majority of them.
- You'll have to update the JauntAPI library every month, because the author of the library placed a 1 month limit on the library, and I can't legally publish my hacked version. Here are the steps:
  - Download the library here... http://jaunt-api.com/download.htm
  - Delete all files matching `lib/jaunt*`.
  - Put the new jar in the `lib/` folder.
  - Recompile && tada!
