package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class AboutUSPanel extends CustomJPanel{
	private JEditorPane editor = null;
	
	public AboutUSPanel() {
		init();
		initLayout();
	}

	private void init() {
		try {
			editor = new JEditorPane(getClass().getClassLoader().getResource("htmls/aboutUS.htm"));
			editor.setEditable(false);
		} catch (IOException e) {
			
		}
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(editor));
	}
	
	@Override
	public void onExit() {
		editor = null;		
	}
}