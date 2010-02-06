package com.zycus.dotproject.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.ui.event.ButtonBarListener.ActionType;

public class ButtonBar extends CustomJPanel implements ActionListener {

	public static final int			ADD					= Integer.parseInt("0001", 2);
	public static final int			SAVE				= Integer.parseInt("0010", 2);
	public static final int			DELETE				= Integer.parseInt("0100", 2);
	public static final int			CANCEL				= Integer.parseInt("1000", 2);

	private List<ButtonBarListener>	listeners			= new ArrayList<ButtonBarListener>();
	private JButton					btnAddNew			= null;
	private JButton					btnSave				= null;
	private JButton					btnDelete			= null;
	private JButton					btnClose			= null;

	private int						buttonsToBeAdded	= 0;
	private boolean					inProcess			= false;

	public ButtonBar(int buttonsOptions) {
		buttonsToBeAdded = buttonsOptions;
		if (!isButtonAvailable(ADD) && !isButtonAvailable(SAVE) && !isButtonAvailable(DELETE) && !isButtonAvailable(CANCEL)) {
			throw new IllegalArgumentException("Invalid buttons selected.");
		}
		init();
		initLayout();
	}

	public void enableButton(int buttonMask, boolean status) {
		if ((buttonMask & ADD) == ADD) {
			btnAddNew.setEnabled(status);
		}
		if ((buttonMask & SAVE) == SAVE) {
			btnSave.setEnabled(status);
		}
		if ((buttonMask & DELETE) == DELETE) {
			btnDelete.setEnabled(status);
		}
		if ((buttonMask & CANCEL) == CANCEL) {
			btnDelete.setEnabled(status);
		}
	}
	
	public void showButton(int buttonMask, boolean status) {
		if ((buttonMask & ADD) == ADD) {
			btnAddNew.setVisible(status);
		}
		if ((buttonMask & SAVE) == SAVE) {
			btnSave.setVisible(status);
		}
		if ((buttonMask & DELETE) == DELETE) {
			btnDelete.setVisible(status);
		}
		if ((buttonMask & CANCEL) == CANCEL) {
			btnDelete.setVisible(status);
		}
	}

	private boolean isButtonAvailable(int buttonOption) {
		return (buttonsToBeAdded & buttonOption) == buttonOption;
	}

	public ButtonBar() {
		this(ADD | SAVE | DELETE | CANCEL);
	}

	private void init() {
		btnAddNew = getButton("Add New");
		btnAddNew.setIcon(IconHelper.getNewIcon());
		btnSave = getButton("Save");
		btnSave.setIcon(IconHelper.getSaveIcon());
		btnDelete = getButton("Delete");
		btnDelete.setIcon(IconHelper.getDeleteIcon());
		btnClose = getButton("Close");
		btnClose.setIcon(IconHelper.getCloseIcon());
	}

	private JButton getButton(String strCaption) {
		JButton btnReturn = new JButton(strCaption);
		btnReturn.addActionListener(this);
		return btnReturn;
	}

	private void initLayout() {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		if (isButtonAvailable(ADD)) {
			add(btnAddNew);
		}
		if (isButtonAvailable(SAVE)) {
			add(btnSave);
		}
		if (isButtonAvailable(DELETE)) {
			add(btnDelete);
		}
		if (isButtonAvailable(CANCEL)) {
			add(btnClose);
		}
	}

	public void addButtonBarListener(ButtonBarListener listener) {
//		if(inProcess) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException excp) {
//				excp.printStackTrace();
//			}
//			addButtonBarListener(listener);
//			return;
//		}
		listeners.add(listener);
	}

	public void removeButtonBarListener(ButtonBarListener listener) {
//		if(inProcess) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException excp) {
//				excp.printStackTrace();
//			}
//			removeButtonBarListener(listener);
//			return;
//		}
		listeners.remove(listener);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent == null) {
			return;
		}
		Object source = actionEvent.getSource();
		if (source == null) {
			return;
		}
		inProcess = true;
		if (source == btnAddNew) {
			for (ButtonBarListener listener : listeners) {
				fireEvent(listener, ActionType.Add);
			}
		} else if (source == btnSave) {
			for (ButtonBarListener listener : listeners) {
				fireEvent(listener, ActionType.Save);
			}
		} else if (source == btnDelete) {
			for (ButtonBarListener listener : listeners) {
				fireEvent(listener, ActionType.Delete);
			}
		} else if (source == btnClose) {
			for (ButtonBarListener listener : listeners) {
				fireEvent(listener, ActionType.Cancel);
			}
		}
		inProcess = false;
	}
	
	private void fireEvent(final ButtonBarListener listener, final ActionType actionType) {
		Thread th = new Thread(new Runnable() {
			public void run() {
				listener.actionPerformed(actionType);
			}
		});
		th.setName(listener + ".actionPerformed(" + actionType + ")");
		th.start();
	}

	@Override
	public void onExit() {
		listeners.clear();
		
		btnAddNew.removeActionListener(this);
		btnSave.removeActionListener(this);
		btnDelete.removeActionListener(this);
		btnClose.removeActionListener(this);
		
		btnAddNew = null;
		btnSave = null;
		btnDelete = null;
		btnClose = null;
	}
}