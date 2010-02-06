package com.zycus.trial;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextInput extends JFrame {
	JPanel			contentPane;
	JPanel			jPanel1				= new JPanel();
	FlowLayout		flowLayout1			= new FlowLayout();
	GridLayout		gridLayout1			= new GridLayout();
	JLabel			keyLabel			= new JLabel();
	JTextField		keyText				= new JTextField();
	JLabel			focusLabel			= new JLabel();
	JTextField		focusText			= new JTextField();
	JLabel			inputLabel			= new JLabel();
	JTextField		inputText			= new JTextField();
	JLabel			modelLabel			= new JLabel();
	JTextField		modelText			= new JTextField();
	IntegerDocument	integerDocument1	= new IntegerDocument();

	public TextInput() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(flowLayout1);
		this.setSize(new Dimension(400, 300));
		this.setTitle("Input Validation");
		jPanel1.setLayout(gridLayout1);
		gridLayout1.setRows(4);
		gridLayout1.setColumns(2);
		gridLayout1.setHgap(20);
		keyLabel.setText("Key Listener");
		modelLabel.setText("Model");
		focusLabel.setText("Focus Listener");
		inputLabel.setText("Input Verifier");

		keyText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					e.consume();
				}
			}
		});

		focusText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String content = textField.getText();
				if (content.length() != 0) {
					try {
						Integer.parseInt(content);
					} catch (NumberFormatException nfe) {
						getToolkit().beep();
						textField.requestFocus();
					}
				}
			}
		});

		inputText.setInputVerifier(new InputVerifier() {
			public boolean verify(JComponent comp) {
				boolean returnValue = true;
				JTextField textField = (JTextField) comp;
				String content = textField.getText();
				if (content.length() != 0) {
					try {
						Integer.parseInt(textField.getText());
					} catch (NumberFormatException e) {
						getToolkit().beep();
						returnValue = false;
					}
				}
				return returnValue;
			}
		});

		modelText.setDocument(integerDocument1);

		contentPane.add(jPanel1);
		jPanel1.add(keyLabel);
		jPanel1.add(keyText);
		jPanel1.add(focusLabel);
		jPanel1.add(focusText);
		jPanel1.add(inputLabel);
		jPanel1.add(inputText);
		jPanel1.add(modelLabel);
		jPanel1.add(modelText);
	}

	public static void main(String args[]) {
		TextInput frame = new TextInput();
		frame.pack();
		frame.show();
	}

	static class IntegerDocument extends PlainDocument {

		public void insertString(int offset, String string, AttributeSet attributes) throws BadLocationException {

			if (string == null) {
				return;
			} else {
				String newValue;
				int length = getLength();
				if (length == 0) {
					newValue = string;
				} else {
					String currentContent = getText(0, length);
					StringBuffer currentBuffer = new StringBuffer(currentContent);
					currentBuffer.insert(offset, string);
					newValue = currentBuffer.toString();
				}
				try {
					Integer.parseInt(newValue);
					super.insertString(offset, string, attributes);
				} catch (NumberFormatException exception) {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		}
	}
}