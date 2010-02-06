package com.zycus.dotproject.impl;

import java.io.File;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.zycus.dotproject.api.IFileManager;
import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.persistence.db.hibernate.BaseHibernateDataHandler;
import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;
import com.zycus.dotproject.util.FtpHandler;

public class FileManagerImpl extends BaseHibernateDataHandler implements IFileManager {
	private FtpHandler ftpHandler = new FtpHandler();
	
	public void addFile(BOFile file, File realFile) {
		ftpHandler.upsertFile(file.getProject().getProjectID(), file.getFileName(), realFile);
		
		file.setRootFileID(1);
		performDBAction(file, DBActionCriteria.Update);
		file.setRootFileID(file.getFileID());
		performDBAction(file, DBActionCriteria.Update);
	}

	public void deleteFile(BOFile file, BOUser user) {
		ftpHandler.deleteFile(file.getProject().getProjectID(), file.getFileName());
		performDBAction(file, DBActionCriteria.Delete);
		
		
		/*SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			
			Criteria criteria = session.createCriteria(BOFile.class);
			criteria.add(Restrictions.eq("rootFileID", file.getRootFileID()));
			
			List<BOFile> files = criteria.list();
			List<String> fileNames = new ArrayList<String>();
			for(BOFile fileD : files) {
				fileNames.add(fileD.getFileName());
			}
			files.clear();
			
			String deleteQuery = "delete from files where file_version_id = " + file.getRootFileID();
			SQLQuery query = session.createSQLQuery(deleteQuery);
			int row = query.executeUpdate();
			if(row > 0) {
				for(String fileName : fileNames) {
					ftpHandler.deleteFile(file.getProject().getProjectID(), fileName);
				}
			}else {
				throw new RuntimeException("Could not delete file");
			}
			
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}*/
	}

	public void saveFile(BOFile file, File targetFile) {
		ftpHandler.saveFileContent(file.getProject().getProjectID(), file.getFileName(), targetFile);
	}

	public boolean isServerAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateFile(BOFile file, BOUser user) {
		// TODO Auto-generated method stub

	}

	public void updateFile(BOFile file, File realFile, BOUser user) {
		// TODO Auto-generated method stub

	}

	public List<BOFile> getFiles(long taskID, BOUser user) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(BOFile.class);
			criteria.add(Restrictions.eq("taskID", taskID));
			criteria.add(Restrictions.sqlRestriction(" file_id in(select max(fl.file_id) from files as fl group by fl.file_version_id) "));
			return criteria.list();
			
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public void checkOutFile(BOFile file, BOUser user) {
		performDBAction(file, DBActionCriteria.Update);
	}

	public void checkInFile(BOFile originalFile, BOFile newFile, File realFile, BOUser user) {
		ftpHandler.upsertFile(newFile.getProject().getProjectID(), newFile.getFileName(), realFile);
		
		newFile.setRootFileID(originalFile.getRootVersionFile().getFileID());
		performDBAction(originalFile, DBActionCriteria.Update);
		performDBAction(newFile, DBActionCriteria.Update);
	}

	public List<BOFile> getAllVersions(long versionID, BOUser user) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			String query = "from BOFile as f where f.rootFileID = :rootFileID";
			Query q = session.createQuery(query);
			q.setParameter("rootFileID", versionID);
			return q.list();
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public List<BOFile> getAllFile(BOProject project, BOUser user) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BOFile.class);
			criteria.add(Restrictions.eq("project", project));
			criteria.add(Restrictions.sqlRestriction(" file_id in(select max(fl.file_id) from files as fl group by fl.file_version_id) "));
			return criteria.list();
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
}