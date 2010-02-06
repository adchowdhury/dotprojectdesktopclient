package com.zycus.dotproject.util;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumericDocument extends PlainDocument {
	public static enum DocumentType {
		Byte, Short, Integer, Float, Long, Double
	}

	private DocumentType	docType			= DocumentType.Integer;

	public NumericDocument(DocumentType documentType) {
		this.docType = documentType;
	}

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
				if (docType == DocumentType.Byte) {
					Byte.parseByte(newValue);
				} else if (docType == DocumentType.Short) {
					Short.parseShort(newValue);
				} else if (docType == DocumentType.Integer) {
					Integer.parseInt(newValue);
				} else if (docType == DocumentType.Float) {
					Float.parseFloat(newValue);
				} else if (docType == DocumentType.Long) {
					Long.parseLong(newValue);
				} else if (docType == DocumentType.Double) {
					Double.parseDouble(newValue);
				}
				super.insertString(offset, string, attributes);
			} catch (NumberFormatException exception) {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

}