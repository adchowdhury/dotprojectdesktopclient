package com.zycus.dotproject.ui;

import java.util.Date;

import javax.swing.JLabel;

public class TimeHolder extends JLabel {
	public TimeHolder() {
		setText(new Date().toString());
		Thread th = new Thread(new Runnable() {
			public void run() {
				while(true) {
					setText(new Date().toString());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException a_th) {
						a_th.printStackTrace();
					}
				}
			}
		});
		th.start();
	}
}