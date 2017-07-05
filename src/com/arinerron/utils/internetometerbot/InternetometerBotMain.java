package com.arinerron.utils.internetometerbot;

import java.io.BufferedReader;
import java.util.Base64; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

public class InternetometerBotMain {
	public static boolean empty = false;
	public static int threadNumber = 100;
	static int iii = 0;
	public static int attempted = 0;
	public static List<String> list = new ArrayList<>();
	public static List<String> proxylist = new ArrayList<>();

	public static int fails = 0;
	public static int works = 0;

	public static int USER_ID = 44772;
	
	public static final boolean checkIPv4(final String ip) {
		boolean isIPv4;
		try {
			final InetAddress inet = InetAddress.getByName(ip);
			isIPv4 = inet.getHostAddress().equals(ip) && (inet instanceof Inet4Address);
		} catch (final Exception e) {
			isIPv4 = false;
		}
		return isIPv4;
	}

	public static void done() {
		System.out.println("Finished! Gave " + InternetometerBotMain.works + " internets.\nThe failed proxies count is " + InternetometerBotMain.fails + ".");
		System.out.println("\n[= InternetometerBot by Arinerron finished =]");
	}

	public static String finish(final String outer) {
		final StringBuilder builder = new StringBuilder();

		final String[] data = outer.split(Pattern.quote("</span>"));
		// System.out.println(data.length);
		for (String endspan : data) {
			endspan = endspan.replaceAll("         ", "").trim();
			if (!endspan.equals(""))
				try {
					final int index = endspan.indexOf('<');
					if ((index != 0) && (index != -1))
						builder.append(endspan.substring(0, index));

					if (!endspan.contains("block") && !endspan.contains("none")) {
						final int fin = endspan.indexOf('>');
						builder.append(endspan.substring(fin));
					} else {
					}
				} catch (final Exception e) {
					try {
						Integer.parseInt(endspan.replaceAll(Pattern.quote("."), ""));
						builder.append(endspan);
					} catch (final Exception ex) {
						System.out.println("Failed for: " + endspan);
						e.printStackTrace();
					}
				}

			// if(builder.toString().contains(("..")))
			// System.out.println(builder.toString().replaceAll(Pattern.quote(">"),
			// "") + " / " + endspan);
		}

		return builder.toString().replaceAll(Pattern.quote(">"), "");
	}

	private static String getKey(String html) throws Exception {
		final int startIndex = html.indexOf("<input type='hidden' name='key' value='") + 38;
		final int endIndex = startIndex + 32;
		return (html.substring(startIndex + 1, endIndex));
	}

	public static String getText(final String url) throws Exception {
		final URL website = new URL(url);
		final URLConnection connection = website.openConnection();
		final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		final StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}

	public static String getTextFrom(final String url) throws Exception {
		final URL website = new URL(url);
		final URLConnection connection = website.openConnection();
        connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        connection.setRequestProperty("Pragma", "no-cache"); // HTTP 1.0.
        connection.setRequestProperty("Expires", "0"); // Proxies.
        connection.setUseCaches(false);
		final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		final StringBuilder response = new StringBuilder();
		
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine + "\n");

		in.close();

		return response.toString();
	}

	public static String getTextFromURL(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}

	public static void internet(final String ip, final String port) {
		try {
		    //System.setProperty("java.net.useSystemProxies", "true");
		    //System.setProperty("socksProxyHost", ip);
	        //System.setProperty("socksProxyPort", port);
			InternetometerBotMain.attempted++;
			
			final UserAgent userAgent = new UserAgent();
			userAgent.setProxyHost(ip);
			userAgent.setProxyPort(Integer.parseInt(port));
			//System.out.println("IP:" + InternetometerBotMain.getTextFrom("https://wtfismyip.com/text"));
			userAgent.visit("http://internetometer.com/give/" + InternetometerBotMain.USER_ID + "/");
			userAgent.doc.submit("Give an Internet");
			if(userAgent.doc.innerHTML().contains("Success")) {
				InternetometerBotMain.works++;
				System.out.println("    Proxy " + ip + ":" + port + " successful.");
			} else
				InternetometerBotMain.fails++;
			
			userAgent.close();
			
            if (!InternetometerBotMain.list.contains(ip + ":" + port))
                InternetometerBotMain.list.add(ip + ":" + port);
		} catch (Exception e) {
			InternetometerBotMain.fails++;
		}
		
	}
	
	public static void main(String[] args) {
		InternetometerBotMain.normalWay(args);
	    // otherWay();
	}
	
	public static void torWay() { // this never worked.
	    while(true) {
	        try {
	            System.out.println("Reloading...");
    	        Runtime.getRuntime().exec("killall -HUP tor");
    	        
    	        System.out.println("Giving...");
    	        internet("localhost", 9050 + "");
    	        
    	        System.out.println("Done... " + InternetometerBotMain.works);
	        } catch(Exception e) {
	            System.out.println("Error: " + e.toString());
	        }
	    }
	}

	public static void normalWay(String[] args) {
		final File file = new File(new File(System.getProperty("user.home")), "proxies_working.txt"); // store working proxies.
		System.out.println("[= InternetometerBot by Arinerron =]\n");

		if (args.length == 0) {
			System.out.println("Please enter the userid of the person you want to give internets to...");
			System.out.println("    `internetometerbot <user_id> <optional:number_of_threads>`");
		} else {
			final String id = args[0];
			try {
				InternetometerBotMain.USER_ID = Integer.parseInt(id);
			} catch (Exception e) {
				System.out.println("Error: Invalid user id.\nExample id: 44772");
				System.exit(0);
			}
			System.out.println(
					"Note: Most of the proxy lists update around 30 minutes, so try re-running this program then to get more internets.");
			final List<String> xx2 = new ArrayList<>();
			System.out.println("Fetching first alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch first alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching second alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update2();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch second alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching third alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update3();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch third alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching fourth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update4();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch fourth alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching fifth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update7();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch fifth alternate proxy list. Reason: " + e.getMessage());
			}
			
			System.out.println("Fetching sixth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update6();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch sixth alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching seventh alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.grabMeProxies();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch seventh alternate proxy list. Reason: " + e.getMessage());
			}
			
			System.out.println("Fetching eighth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update8();
				final StringBuilder b = new StringBuilder();
				for (final String s : moreips)
					b.append(s + "\n");
				final String[] ss = b.toString().split("\n");
				for (final String s : ss)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch eighth alternate proxy list. Reason: " + e.getMessage());
			}
			
			System.out.println("Fetching ninth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.update5();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch ninth alternate proxy list. Reason: " + e.getMessage());
			}

			System.out.println("Fetching tenth alternate proxy list...");
			try {
				final List<String> moreips = InternetometerBotMain.scrapeAlot();
				for (final String s : moreips)
					xx2.add(s);
			} catch (final Exception e) {
				System.out.println("Warning: Failed to fetch tenth alternate proxy list. Reason: " + e.getMessage());
			}
			
			
			 
			/*
			 * 
			 * ======================== FROM FILE ============================
			 * 
			 * /
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		 
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					xx2.add(line);
					System.out.print(".");
				}
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		 
			try {
				br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}*/

/*			for (final String s : xx)
				xx2.add(s);*/

			System.out.println("Found " + xx2.size() + " proxies.");

			final Set<String> hs = new LinkedHashSet<>();
			hs.addAll(xx2);
			xx2.clear();
			xx2.addAll(hs);

			System.out.println("Filtered duplicates, now " + xx2.size() + " proxies.");
			
			final Timer tttt = new Timer();
			tttt.schedule(new TimerTask() {
				@Override
				public void run() {
					if(InternetometerBotMain.empty)
						tttt.cancel();
					else {
						System.out.println("    [ STATUS: Attempting " + (InternetometerBotMain.attempted / 15) + " proxies/sec ]");
						InternetometerBotMain.attempted = 0;
					}
				}
			}, 15000, 15000);
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					Writer output = null;
					try {
						output = new BufferedWriter(new FileWriter(file, true));
					} catch (IOException e) {
						System.out.println("Error: Failed to save proxies. Reason: " + e.toString());
					}
					for (final String s : InternetometerBotMain.list)
						try {
							output.append(s + "\n");
						} catch (final Exception e1) {
							if(!e1.toString().contains("Concurrent"))
								System.out.println("Error: Failed to save proxies. Reason: " + e1.toString());
							else
								System.out.println("Warning: Skipped some proxies saving due to concurrent modification.");
						}
					try {
						output.close();
					} catch (final IOException e) {
						System.out.println("Warning: Failed to save proxies. Reason: " + e.toString());
					}
					
					done();
				}
			});

			try {
				if (args.length >= 2)
					InternetometerBotMain.threadNumber = Integer.parseInt(args[1]);
			} catch (Exception e) {
				System.out.println("Error: Failed to parse number of threads, defaulting to "
						+ InternetometerBotMain.threadNumber + ".");
			}
			System.out.println("Note: " + (InternetometerBotMain.threadNumber < 0 ? "The specified thread count is less than 0, and is invalid. Defaulting to " + InternetometerBotMain.threadNumber + " threads...": (InternetometerBotMain.threadNumber > 750) ? InternetometerBotMain.threadNumber + " is a lot of threads. This may crash your computer!" : "Starting " + InternetometerBotMain.threadNumber + " threads..." ));
			System.out.println("Trying proxies, this may take a while...");
			final List<Thread> threads = new ArrayList<>();
			for (int i = 0; i < InternetometerBotMain.threadNumber; i++) {
				final Thread t = new Thread(new Runnable() {
					public boolean doneyet = false;

					@Override
					public void run() {
						while (!InternetometerBotMain.empty)
							if (xx2.size() != 0) {
								final String temp = xx2.get(0);
								xx2.remove(0);
								try {
									if(temp != null) {
										final String[] ss = temp.split(":"); // space :
										final String ip = ss[0];
										final String port = ss[1]; // 3 1
										if (!ip.equals("proxy-list.org") && !ip.contains("{"))
											InternetometerBotMain.internet(ip, port);
									}
								} catch (Exception e) {
									if(!e.toString().contains("IndexOutOfBounds") && temp != null)
										System.out.println(
											"Error: Unknown error, please report this. code=\"" + e.toString() + "\" per \"" + temp + "\"");
								}
							} else {
								InternetometerBotMain.empty = true;
								this.doneyet = true;
								if (InternetometerBotMain.threadNumber == 1)
									//InternetometerBotMain.done();
									;
								else
									InternetometerBotMain.threadNumber--;
							}
						if (!this.doneyet) {
							this.doneyet = true;
							if (InternetometerBotMain.threadNumber == 1)
								//InternetometerBotMain.done();
								;
							else
								InternetometerBotMain.threadNumber--;
						}
					}
				});
				threads.add(t);
				t.start();
			}
		}
	}
	
	public static List<String> scrapeAlot() {
		List<String> list = new ArrayList<>();
		
		String[] urls = {"http://www.gatherproxy.com/proxylist/country/?c=India", "http://www.gatherproxy.com/proxylist/country/?c=Venezuela", "http://www.gatherproxy.com/proxylist/country/?c=Thailand", "http://www.gatherproxy.com/proxylist/anonymity/?t=Anonymous", "http://www.gatherproxy.com/proxylist/port/9999", "http://www.gatherproxy.com/proxylist/port/8118", "http://www.gatherproxy.com/proxylist/country/?c=France", "http://www.gatherproxy.com/proxylist/country/?c=Hong%20Kong", "http://www.gatherproxy.com/proxylist/country/?c=Indonesia", "http://www.gatherproxy.com/proxylist/country/?c=Russia", "http://www.gatherproxy.com/proxylist/port/8998", "http://www.gatherproxy.com/proxylist/anonymity/?t=Transparent", "http://www.gatherproxy.com", "http://www.gatherproxy.com/proxylist/anonymity/?t=Elite", "http://www.gatherproxy.com/proxylist/country/?c=United%20States", "http://www.gatherproxy.com/proxylist/port/8080", "http://www.gatherproxy.com/proxylist/country/?c=China", "http://www.gatherproxy.com/proxylist/country/?c=Brazil", "http://www.gatherproxy.com/proxylist/port/80", "http://www.gatherproxy.com/proxylist/port/3128", "http://www.gatherproxy.com/proxylist/port/8888"};
		for(final String url : urls) {
			try {
				List<String> listt = scrapeSome(url);
				for(String s : listt)
					list.add(s);
				listt = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public static List<String> scrapeSome(String base) throws Exception {
		List<String> list = new ArrayList<>();
		List<String> ip = new ArrayList<>();
		List<Integer> port = new ArrayList<>();
		
		UserAgent agent = new UserAgent();
		agent.visit(base);
		final String html = agent.doc.outerHTML();
		
		String[] ip_split = html.split(Pattern.quote("\"PROXY_IP\":\""));
		ip_split[0] = "";
		for(String s : ip_split)
			if(!(s.length() == 0))
				ip.add(s.split(Pattern.quote("\""))[0]);
		ip_split = null;
		
		String[] port_split = html.split(Pattern.quote("\"PROXY_PORT\":\""));
		for(String s : port_split)
			try {port.add(Integer.parseInt(s.split(Pattern.quote("\""))[0], 16));} catch(Exception e){}
		ip_split = null;
		
		int index = 0;
		for(String s : ip) {
			list.add(s + ":" + port.get(index));
			index++;
		}
		
		agent.close();
		return list;
	}
	
	public static List<String> grabMeProxies() throws Exception {
		List<String> list = new ArrayList<>();
		List<String> urls = new ArrayList<>();

		final String base = "http://proxyserverlist-24.blogspot.com/";
		UserAgent agent = new UserAgent();
		agent.visit(base);

		Elements tables = agent.doc.findEach("<a>");
		for (Element e : tables) {
			if(e.outerHTML().contains("Proxy Server List") && !e.outerHTML().contains("/search/label/") && !e.outerHTML().contains("Read more")) {
				String old = (e.outerHTML()).split(Pattern.quote("'"))[1].split(Pattern.quote("'"))[0];
				urls.add(old);
			}
		}
		
		for(final String url : urls) {
			agent.visit(url);
			StringBuilder s = new StringBuilder();
			
			Elements tablez = agent.doc.findEach("<pre style>");
			for (Element e : tablez) {
				s.append((e.innerHTML()) + "");
			}
			
			String[] array = s.toString().split("\n");
			for(String line : array) {
				if(line.contains(":") && !line.contains("<") && !line.contains("split") )
					list.add(line);
			}
			
			array = null;
			s = null;
			tablez = null;
		}

		agent.close();
		return list;
	}
	
	public static List<String> update7() {
		final List<String> listx = new ArrayList<>();
		final String baseurl = "http://proxy50-50.blogspot.com/";

		final UserAgent browser = new UserAgent();
		try {
			browser.visit(baseurl);

			Elements tables = browser.doc.findEach("<tr>");

			for (final Element table : tables) {
				Elements col = table.findEach("<td>");
				final String contents = col.toList().get(1).innerHTML();
				final String contents2 = col.toList().get(2).innerHTML();
				
				if (!contents.contains("IP"))
					listx.add(StringEscapeUtils.unescapeHtml4(contents + ":" + contents2));
			}
		} catch (Exception e) {
			
		}
		
		try {
			browser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listx;
	}
	
	public static List<String> update8() {
		final List<String> listx = new ArrayList<>();
		
		final String pre = "http://www.samair.ru/proxy/proxy-";
		final String pro = ".htm";
		
		for(int i = 1; i < 31; i++) {
			final UserAgent agent = new UserAgent();
			try {
				final Elements urlt = agent.visit(pre + (i < 10 ? "0" + i : i) + pro).findEach("<a>");
				for(Element e : urlt) {
					if(e.getText().contains("do it")) {
						agent.visit(e.getAt("href"));
						final String dat = agent.doc.findFirst("<pre>").getText();
						listx.add(dat.substring(0, dat.length() - 1));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				agent.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return listx;
	}

	public static List<String> update6() {
		final List<String> listx = new ArrayList<>();
		final List<String> ip = new ArrayList<>();
		final List<String> port = new ArrayList<>();
		final String baseurl = "https://www.cool-proxy.net/proxies/http_proxy_list/page:";
		final String endurl = "/sort:score/direction:desc";

		for (int i = 1; i < 17; i++) {
			String url = baseurl + i + endurl;

			final UserAgent browser = new UserAgent();
			try {
				browser.visit(url);

				Elements tables = browser.doc.findEach("<td>");

				for (final Element table : tables) {
					final String contents = table.innerHTML();
					if (contents.contains("Base64.decode")) {
						final String[] split = contents.split(Pattern.quote("rot13(\""));
						final String[] split2 = split[1].split(Pattern.quote("\""));
						ip.add(new String(Base64.getDecoder().decode(rot13(split2[0]))));
					} else {
						try {
							Integer.parseInt(contents);
							port.add(contents);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				i = 17;
				// e.printStackTrace();
			}
			
			try {
				browser.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < ip.size(); i++) {
			listx.add(ip.get(i) + ":" + port.get(i));
		}

		return listx;
	}

	public static String rot13(String value) {

		char[] values = value.toCharArray();
		for (int i = 0; i < values.length; i++) {
			char letter = values[i];

			if (letter >= 'a' && letter <= 'z') {
				// Rotate lowercase letters.

				if (letter > 'm') {
					letter -= 13;
				} else {
					letter += 13;
				}
			} else if (letter >= 'A' && letter <= 'Z') {
				// Rotate uppercase letters.

				if (letter > 'M') {
					letter -= 13;
				} else {
					letter += 13;
				}
			}
			values[i] = letter;
		}
		// Convert array to a new String.
		return new String(values);
	}

	public static void otherWay() throws Exception {
		// final File file = new File(new File(System.getProperty("user.home")),
		// "parsed-proxies.txt");
		final File file = new File(new File(System.getProperty("user.home")), "apr9-proxies.txt");
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(file);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				final String line = sc.nextLine();

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						final String[] split = line.split(":");
						System.out.println(line + " / ");
						InternetometerBotMain.internet(split[0], split[1]);
					}
				});

				t.start();
				Thread.sleep(50);
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null)
				throw sc.ioException();
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (sc != null)
				sc.close();
		}

	}

	public static String patch(final String code, String code2) {
		final String[] split = code.split(";");
		for (final String s : split) {
			final String[] again = s.split("=");
			code2 = code2.replaceAll(again[0], again[1]);
		}
		return code2.replaceAll(Pattern.quote("+"), "");
	}

	public static String patch2(final String code, String outer) {
		String[] outerx = outer.split(Pattern.quote("</style>"));
		outer = outerx[1];
		final String[] split = code.split(Pattern.quote("."));
		outer = outer.replaceAll(Pattern.quote("<span></span>"), "");
		for (final String s : split)
			try {
				if ((s.length() != 0) && !s.equals("\n")) {
					final String[] x = s.split(Pattern.quote("{"));
					if (!x[1].contains("inline"))
						outer = outer.replaceAll(x[0], ("display:    none"));
					// System.out.println("\'" + x[1].replaceAll("\n", "") + "\'
					// does not contain \'inline\'. Replacing " + x[0] + " with
					// the displaynone...");
					else
						outer = outer.replaceAll(x[0], "inline");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		// outer.replaceAll(Pattern.quote("style=\"display: inline\""),
		// "style=\"yes\"");
		// System.out.println(outer);
		return outer;
	}

	public static List<String> update() throws Exception {
		final List<String> listx = new ArrayList<>();

		String base = "http://nntime.com/proxy-list-";
		String end = ".htm";

		for (int i = 1; i < 30; i++) {
			String url = base + (i < 10 ? "0" + i : i) + end;
			UserAgent browser = new UserAgent();
			browser.visit(url);
			String code = "";
			Elements codes = browser.doc.findEach("<script type=\"text/javascript\">");
			boolean done = false;
			for (final Element codex : codes)
				if (!codex.innerHTML().equals("") && !done) {
					code = (codex.innerHTML()).replaceAll("\n", "");
					done = true;
				}
			Elements tables = browser.doc.findEach("<table id=\"proxylist\" class=\"data\">").findEach("<td>");
			for (final Element table : tables) {
				final String outer = table.innerHTML().replaceAll("\n", "");
				try {
					if (outer.contains("script"))
						listx.add(outer.substring(0, outer.indexOf("<")) + ":" + InternetometerBotMain.patch(code,
								outer.substring(outer.indexOf("+"), outer.indexOf(")"))));
				} catch (Exception e) {
				}
			}
			
			browser.close();
		}

		return listx;
	}

	public static List<String> update2() throws Exception {
		final List<String> listx = new ArrayList<>();
		final List<Integer> ipx = new ArrayList<>();
		final List<String> ip2x = new ArrayList<>();

		final String base = "http://proxylist.hidemyass.com/search-1292985/";
		final String end = "#listable";

		for (int i = 1; i < 7; i++) {
			String url = base + i + end;
			UserAgent browser = new UserAgent();
			browser.visit(url);
			Elements tables = browser.doc.findEach("<span>");
			Elements tablex = browser.doc.findEach("<td>");

			for (final Element table : tablex)
				try {
					final int ic = Integer.parseInt(table.innerText().replaceAll(" ", "").replaceAll("\n", ""));
					ipx.add(ic);
				} catch (final Exception e) {
				}

			for (final Element table : tables) {
				String outer = table.innerHTML().replaceAll("\n", "").replaceAll("          ", "");
				try {
					if (!outer.contains("img src") && !outer.contains("script type") && !outer.contains("roasted")
							&& (outer.length() > 20)) {
						// outer =
						// outer.substring(outer.indexOf(Pattern.quote("</style>"))
						// + 8);
						// System.out.println("new: " + outer);
						final Elements codex = table.getEach("<style>");
						String code = "";
						for (final Element ele : codex)
							code = (ele.innerHTML());

						final String finalized = InternetometerBotMain
								.finish(InternetometerBotMain.patch2(code, outer.replaceAll("div", "span")));
						ip2x.add(finalized);
						// System.out.println(finalized);
					}
				} catch (Exception e) {
					System.out.println("err for " + outer);
					e.printStackTrace();
				}
			}
			
			browser.close();
		}

		for (int xi = 0; xi < ip2x.size(); xi++)
			// System.out.println(ip2x.get(xi) + ":" + ipx.get(xi));
			listx.add(ip2x.get(xi) + ":" + ipx.get(xi));

		return listx;
	}

	public static List<String> update3() throws Exception {
		final List<String> listx = new ArrayList<>();
		final List<String> ipx = new ArrayList<>();
		final List<String> portx = new ArrayList<>();

		final String baseurl = "https://www.us-proxy.org/";

		final UserAgent browser = new UserAgent();
		browser.visit(baseurl);
		Elements tables = browser.doc.findEach("<td>");

		for (final Element table : tables) {
			final String s = table.getText();
			final char[] array = s.toCharArray();
			boolean clean = true;

			for (final char c : array)
				if (Character.isLetter(c)) {
					clean = false;
					break;
				}

			if (clean)
				if (s.contains("."))
					ipx.add(s);
				else
					portx.add(s);

		}
		
		browser.close();
		
		for (int xi = 0; xi < ipx.size(); xi++)
			listx.add(ipx.get(xi) + ":" + portx.get(xi));

		return listx;
	}

	public static List<String> update4() throws Exception {
		final List<String> listx = new ArrayList<>();
		final String baseurl = "https://hidester.com/proxydata/php/data.php?mykey=csv&country=&port=&type=undefined&anonymity=undefined&ping=undefined&gproxy=2";

		final UserAgent browser = new UserAgent();
		browser.visit(baseurl);

		final JSONArray arr = new JSONArray(browser.doc.innerHTML());
		for (int i = 0; i < arr.length(); i++) {
			final JSONObject jsonobject = arr.getJSONObject(i);
			final String ip = jsonobject.getString("IP");
			final int port = jsonobject.getInt("PORT");
			listx.add(ip + ":" + port);
		}
		
		browser.close();
		
		return listx;
	}

	public static List<String> update5() throws Exception {
		final List<String> listx = new ArrayList<>();
		final String baseurl = "http://txt.proxyspy.net/proxy.txt";

		final String[] lines = InternetometerBotMain.getTextFrom(baseurl).split("\n");
		for (final String s : lines)
			if (s.contains(":"))
				listx.add(s.split(" ")[0]);

		return listx;
	}
}
