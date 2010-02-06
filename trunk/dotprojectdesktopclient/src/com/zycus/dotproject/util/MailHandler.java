package com.zycus.dotproject.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;

import com.zycus.dotproject.bo.BOSimpleMail;

public class MailHandler {
	private static Properties	properties	= new Properties();

	public static void sendMail(BOSimpleMail mail) {
		String server = getMailServer();
		// iterating over all individual reciepients
		for (Iterator<String> it = mail.getAllRecipients(); it.hasNext();) {
			String reciepient = it.next();
			try {
				// Construct data
				String data = URLEncoder.encode("To", "UTF-8") + "=" + URLEncoder.encode(reciepient, "UTF-8");
				data += "&" + URLEncoder.encode("From", "UTF-8") + "=" + URLEncoder.encode(mail.getSender(), "UTF-8");
				data += "&" + URLEncoder.encode("Subject", "UTF-8") + "=" + URLEncoder.encode(mail.getSubject(), "UTF-8");
				data += "&" + URLEncoder.encode("Body", "UTF-8") + "=" + URLEncoder.encode(mail.getBody(), "UTF-8");
				// Send data
				URL url = new URL(server + "?" + data);
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
			} catch (Throwable a_th) {
				a_th.printStackTrace();
			}
		}
	}

	private static String getMailServer() {
		if (properties.size() <= 0) {
			try {
				properties.load(MailHandler.class.getClassLoader().getResourceAsStream("dotProjectClient.properties"));
			} catch (IOException a_th) {}
		}
		return properties.getProperty("EMAIL_SERVER");
	}
}