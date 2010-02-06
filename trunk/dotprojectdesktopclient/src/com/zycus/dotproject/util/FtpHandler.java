package com.zycus.dotproject.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;

public class FtpHandler {
	private static String		serverIP	= null;
	private static String		userName	= null;
	private static String		password	= null;
	private FTPClient			ftp			= null;
	private static final String	Root		= "/";
	private Properties			properties	= new Properties();

	private boolean initConnection() {
		try {
			if (ftp == null) {
				ftp = new FTPClient();
				serverIP = getServer();
				userName = getUserName();
				password = getPassword();
			}
			ftp.connect(serverIP);
			ftp.login(userName, password);
			return true;
		} catch (SocketException a_excp) {
			throw new RuntimeException(a_excp);
		} catch (IOException a_excp) {
			throw new RuntimeException(a_excp);
		} catch (Throwable a_th) {
			throw new RuntimeException(a_th);
		}
	}

	private void closeConnection() {
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (IOException a_excp) {
			throw new RuntimeException(a_excp);
		} catch (Throwable a_th) {
			throw new RuntimeException(a_th);
		}
	}

	private boolean isProjectFolderExists(long projectID) {
		initConnection();
		try {
			String pwd = ftp.printWorkingDirectory();
			ftp.cwd(projectID + "");
			String changedPWD = ftp.printWorkingDirectory();
			if (pwd.equals(changedPWD)) {
				return false;
			} else {
				return true;
			}
		} catch (Throwable a_th) {
			throw new RuntimeException(a_th);
		} finally {
			closeConnection();
		}
	}

	private void createProjectFolder(long projectID) {
		initConnection();
		try {
			ftp.makeDirectory("" + projectID);
		} catch (Throwable a_th) {
			throw new RuntimeException(a_th);
		} finally {
			closeConnection();
		}
	}

	public void upsertFile(long projectID, String fileName, File source) {
		if (isProjectFolderExists(projectID) == false) {
			createProjectFolder(projectID);
		}

		initConnection();
		try {
			ftp.cwd("" + projectID);
			FileInputStream fis = new FileInputStream(source);
			ftp.storeFile(fileName, fis);
			fis.close();
		} catch (IOException a_excp) {
			throw new RuntimeException(a_excp);
		} finally {
			closeConnection();
		}
	}

	public void saveFileContent(long projectID, String fileName, File targetFile) {
		if (isProjectFolderExists(projectID) == false) {
			createProjectFolder(projectID);
		}

		initConnection();
		try {
			ftp.cwd("" + projectID);
			FileOutputStream fos = new FileOutputStream(targetFile);
			ftp.retrieveFile(fileName, fos);
			fos.flush();
			fos.close();
		} catch (IOException a_excp) {
			throw new RuntimeException(a_excp);
		} finally {
			closeConnection();
		}
	}

	public void deleteFile(long projectID, String fileName) {
		if (isProjectFolderExists(projectID) == false) {
			return;
		}
		initConnection();
		try {
			ftp.cwd("" + projectID);
			ftp.deleteFile(fileName);
		} catch (IOException a_excp) {
			throw new RuntimeException(a_excp);
		} finally {
			closeConnection();
		}
	}

	private String getServer() {
		if (properties.size() <= 0) {
			try {
				properties.load(getClass().getClassLoader().getResourceAsStream("dotProjectClient.properties"));
			} catch (IOException a_th) {}
		}
		return properties.getProperty("FTP_SERVER");
	}

	private String getUserName() {
		if (properties.size() <= 0) {
			try {
				properties.load(getClass().getClassLoader().getResourceAsStream("dotProjectClient.properties"));
			} catch (IOException a_th) {}
		}
		return properties.getProperty("FTP_USER_NAME");
	}

	private String getPassword() {
		if (properties.size() <= 0) {
			try {
				properties.load(getClass().getClassLoader().getResourceAsStream("dotProjectClient.properties"));
			} catch (IOException a_th) {}
		}
		return properties.getProperty("FTP_PASSWORD");
	}

}