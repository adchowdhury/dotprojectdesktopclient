package com.zycus.dotproject.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.zycus.dotproject.ui.event.ViewChangeListener;
import com.zycus.dotproject.ui.event.ViewChangeListener.ViewType;
import com.zycus.dotproject.util.ApplicationContext;

public class ViewBar extends JPanel{
	private JComboBox					viewsCombo	= null;
	private List<ViewChangeListener>	listeners	= null;

	public ViewBar() {
		init();
		initLayout();
	}
	
	public void addViewChangelistener(ViewChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeViewChangelistener(ViewChangeListener listener) {
		listeners.remove(listener);
	}

	private void init() {
		listeners = new ArrayList<ViewChangeListener>();
		viewsCombo = new JComboBox();
		viewsCombo.setEditable(false);
		viewsCombo.addItem("Tree View");
		viewsCombo.addItem("Resource View");
		viewsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				ApplicationContext.setViewTYpe((viewsCombo.getSelectedIndex() == 0) ? ViewType.TreeView : ViewType.ResourceView);
				for(ViewChangeListener listener : listeners) {
					listener.viewChanged(ApplicationContext.getViewTYpe());
				}
			}
		});

	}

	private void initLayout() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(viewsCombo);
	}
}