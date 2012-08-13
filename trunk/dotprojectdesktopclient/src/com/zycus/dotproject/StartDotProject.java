package com.zycus.dotproject;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Date;

import javax.swing.FocusManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.zycus.dotproject.ui.DotProjectContainer;
import com.zycus.dotproject.ui.DotProjectMenubar;
import com.zycus.dotproject.ui.DotProjectToolbar;
import com.zycus.dotproject.ui.IconHelper;
import com.zycus.dotproject.ui.ShortcutKeyHelper;
import com.zycus.dotproject.ui.StatusBar;
import com.zycus.dotproject.ui.games.MS;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.ApplicationUtility;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.ErrorStream;
import com.zycus.dotproject.util.LoginHandler;

public class StartDotProject {
	private static final Logger logger = Logger.getLogger(StartDotProject.class);

	public static void main(String[] args) {
		try {
			if(ApplicationUtility.isRunningFromJar()) {
				System.setErr(new ErrorStream());
				System.err.println("========================starting application : " + new Date() + " ===========================================");
			}
		} catch (FileNotFoundException excp) {
			excp.printStackTrace();
		}
		
//		if(System.getProperty("connection.url") == null || System.getProperty("connection.username") == null || System.getProperty("connection.password") == null) {
//			System.err.println("connection.url or connection.username or connection.password is not provided");
//			System.exit(-3);
//		}
		
		System.err.println(args);
		
		if(args != null) {
			System.err.println(args.length);
		}
		
		System.err.println("connection.url : " + System.getProperty("connection.url"));
		System.err.println("connection.username : " + System.getProperty("connection.username"));
		System.err.println("connection.password : " + System.getProperty("connection.password"));
		
		
		if (LoginHandler.getDefault().performLogin() == false)
			System.exit(0);
		
		final JFrame frm = new JFrame("Dot Project Desktop Client");

		PopupMenu popup = new PopupMenu();
		MenuItem openItem = new MenuItem("Open");
		MenuItem exitItem = new MenuItem("Exit");
		popup.add(openItem);
		popup.add(exitItem);

		final TrayIcon trayIcon = new TrayIcon(IconHelper.getProductLogoImage(), "double click here to open Dot Project Client", popup);
		final SystemTray tray = SystemTray.getSystemTray();
		trayIcon.setImageAutoSize(true);

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frm.setVisible(true);
				frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
				tray.remove(trayIcon);
			}
		});
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting...");
				tray.remove(trayIcon);
				System.exit(0);
			}
		};
		exitItem.addActionListener(exitListener);
		ActionListener openListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frm.setVisible(true);
				frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
				tray.remove(trayIcon);
			}
		};
		openItem.addActionListener(openListener);
		
		frm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {

				try {
					tray.add(trayIcon);
					frm.setVisible(false);
					trayIcon.displayMessage("Dot Project", "You can access Dot Project by double clicking this icon", TrayIcon.MessageType.INFO);
				} catch (AWTException ex) {
					System.err.println("TrayIcon could not be added.");
				}
			}
		});
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			public void eventDispatched(AWTEvent event) {
				if(event instanceof KeyEvent && event.getID() == KeyEvent.KEY_RELEASED) {
					if(DialogUtility.isWaitDialogOn()) {
						return;
					}
					if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_F1) {
						if(FocusManager.getCurrentManager().getActiveWindow() instanceof JDialog) {
							if(((JDialog)FocusManager.getCurrentManager().getActiveWindow()).getTitle().equalsIgnoreCase("Shortcut Key")) {
								return;
							}
						}
						DialogUtility.showDialog(new ShortcutKeyHelper(), "Shortcut Key", new Dimension(550, 200));
					}else if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_ESCAPE) {
						if(FocusManager.getCurrentManager().getActiveWindow() instanceof JDialog) {
							((JDialog)FocusManager.getCurrentManager().getActiveWindow()).dispose();
						}
					}else if(((KeyEvent)event).getKeyCode() == KeyEvent.VK_F8) {
						if(FocusManager.getCurrentManager().getActiveWindow() instanceof JDialog) {
							if(((JDialog)FocusManager.getCurrentManager().getActiveWindow()).getTitle().equalsIgnoreCase("Shortcut Key") || 
									((JDialog)FocusManager.getCurrentManager().getActiveWindow()).getTitle().equalsIgnoreCase("Mine Sweeper")) {
								return;
							}
						}
						DialogUtility.showDialog(new MS(), "Mine Sweeper", new Dimension(250, 350));
					}
				}else if(event instanceof FocusEvent) {
					if(event.getID() == FocusEvent.FOCUS_GAINED) {
						FocusEvent fEevnt = (FocusEvent)event;
						if(fEevnt.getComponent() instanceof JTextComponent) {
							if(((JTextComponent)fEevnt.getComponent()).isEditable() == false || 
									((JTextComponent)fEevnt.getComponent()).isEnabled() == false) {
								return;
							}
							//Not to be deployed ((JTextComponent)fEevnt.getComponent()).selectAll();
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		try {
			DotProjectContainer mainContainer = new DotProjectContainer();
			DotProjectToolbar toolBar = new DotProjectToolbar();
			toolBar.addViewChangelistener(mainContainer);
			
			frm.setSize(new Dimension(800, 600));
			frm.setLocationRelativeTo(null);
			frm.setIconImage(IconHelper.getProductLogoImage());
			
			frm.add(mainContainer, BorderLayout.CENTER);
			frm.add(StatusBar.getstStatusBar(), BorderLayout.SOUTH);
			frm.setJMenuBar(new DotProjectMenubar());
			StatusBar.showWelcomeMessage();
			frm.add(toolBar, BorderLayout.NORTH);
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
			ApplicationContext.setCurrentFrame(frm);
			try{
				//"net.sourceforge.napkinlaf.NapkinLookAndFeel"
			    UIManager.setLookAndFeel(ApplicationContext.getCurrentLookAndFeel().getClassName());
				SwingUtilities.updateComponentTreeUI(frm);
			} catch(Exception excp){
			    System.out.println(excp);
			}

			frm.setVisible(true);
		}catch(Throwable a_th) {
			a_th.printStackTrace(System.err);
			JOptionPane.showMessageDialog(null, "Could not start dotProject client, please contact your vendor", "Error:", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

}