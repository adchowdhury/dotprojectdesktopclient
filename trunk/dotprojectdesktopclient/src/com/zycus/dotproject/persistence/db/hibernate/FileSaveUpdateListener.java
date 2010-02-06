package com.zycus.dotproject.persistence.db.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.LoadEventListener;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;

public class FileSaveUpdateListener implements SaveOrUpdateEventListener, LoadEventListener {
	
	public void onLoad(LoadEvent arg0, LoadType arg1) throws HibernateException {
		System.out.println("FileSaveUpdateListener.onLoad("+arg0.getEntityClassName() + ", " + arg0.getResult() +")");
	}
	
	public void onSaveOrUpdate(SaveOrUpdateEvent saveOrUpdateEvent) throws HibernateException {
		System.out.println("================START onSaveOrUpdate====================");
		System.out.println("Entity name : " + saveOrUpdateEvent.getEntityName());
		if(saveOrUpdateEvent.getEntity() != null) {
			System.out.println("Entity class : " + saveOrUpdateEvent.getEntity().getClass());
		}
		System.out.println("==================END onSaveOrUpdate===================");
	}
}