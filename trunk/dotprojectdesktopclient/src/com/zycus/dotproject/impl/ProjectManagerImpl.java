package com.zycus.dotproject.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.bo.BOCompany;
import com.zycus.dotproject.bo.BOCustomField;
import com.zycus.dotproject.bo.BODepartment;
import com.zycus.dotproject.bo.BOHistory;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.DateRange;
import com.zycus.dotproject.bo.HistoryAction;
import com.zycus.dotproject.bo.HistoryTable;
import com.zycus.dotproject.bo.ProjectStatus;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.exception.InsufficientPrivilagesException;
import com.zycus.dotproject.persistence.db.hibernate.BaseHibernateDataHandler;
import com.zycus.dotproject.persistence.db.hibernate.HibernateSessionFactory;

public class ProjectManagerImpl extends BaseHibernateDataHandler implements IProjectManager {

	public List<BOProject> getAllProjects(BOCompany company, BOUser user) {
		return getAllProjects(company, user, null);
	}

	public List<BOProject> getAllProjects(BOCompany company, BOUser user, ProjectStatus projectStatus) {
		//initCustomFields();
		SessionFactory sessionFactory = null;
		Session session = null;
		try {

			System.out.println("ProjectManagerImpl.getAllProjects() started " + user.getLoginName());
			System.out.println("ProjectManagerImpl.getAllProjects()  " + user.getUserType());

			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(BOProject.class);
			// List<BOProject> toProjectList = new ArrayList<BOProject>();

			if (projectStatus != null) {
				criteria.add(Restrictions.eq("projectStatus", projectStatus));
			}

			// List<BOProject> fromProjectList = criteria.list();
			/*
			 * if(true) { return fromProjectList; }
			 */
			if (user.getUserType() == UserType.Administrator || user.getUserType() == UserType.CEO) {
				System.out.println("ProjectManagerImpl.getAllProjects() Administrator ");
				// toProjectList = fromProjectList;
			} else {
				System.out.println("ProjectManagerImpl.getAllProjects() Others ");

				String departmentsOwned = getAllDepartmentsAndChildDepartmentsForUserAsString(new StringBuilder(), user.getDepartmentsOwned(), 0);

				// criteria.add(Restrictions.sqlRestriction(
				// "select * from projects p where p.project_owner = ? or p.project_id in( select t.task_project	from tasks t, user_tasks ut	where ut.task_id = t.task_id and ( t.task_owner = ? or ut.user_id = ? ))"
				// , 2, Hibernate.LONG));

				String restriction = "(" + "	project_owner = " + user.getUserID() + " " + "	or project_company in " + "	( " + "		select company.company_id " + "		from companies company "
						+ "		where company.company_owner = " + user.getUserID() + " " + "	) ";

				if (!departmentsOwned.trim().equals("")) {
					restriction += "	or project_id in " + "	( " + "		select pdept.project_id " + "		from project_departments pdept, departments dept " + "		where pdept.department_id = dept.dept_id "
							+ "		and pdept.department_id in ( " + departmentsOwned + ") " + "	) ";
				}

				restriction += "	or project_id in " + "	( " + "		select t.task_project" + "		from tasks t, user_tasks ut	" + "		where ut.task_id = t.task_id " + "		and " + "		("
						+ "			t.task_owner = " + user.getUserID() + " " + "			or ut.user_id = " + user.getUserID() + " " + ")" + "	)" + ")";

				criteria.add(Restrictions.sqlRestriction(restriction));
				// fillRelevantProjects(fromProjectList, toProjectList, user,
				// projectStatus);
			}

			System.out.println("ProjectManagerImpl.getAllProjects() ended ");
			return criteria.list();
			// return toProjectList;

		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	private String getAllDepartmentsAndChildDepartmentsForUserAsString(StringBuilder stringBuilder, Set<BODepartment> departmentsOwned, int counter) {

		for (Iterator departmentsOwnedIterator = departmentsOwned.iterator(); departmentsOwnedIterator.hasNext();) {
			BODepartment currentDepartment = (BODepartment) departmentsOwnedIterator.next();
			if (counter == 0) {
				stringBuilder.append(currentDepartment.getDepartmentID());
			} else {
				stringBuilder.append(", " + currentDepartment.getDepartmentID());
			}

			counter++;

			if (currentDepartment.getChildDepartments() != null) {
				getAllDepartmentsAndChildDepartmentsForUserAsString(stringBuilder, currentDepartment.getChildDepartments(), counter);
			}
		}

		return stringBuilder.toString();
	}

	public boolean isCompanyOwner(BOUser user, BOProject project) {
		Set<BOCompany> companiesOwned = user.getComapaniesOwned();

		if (companiesOwned.contains(project.getProjectCompany())) {
			return true;
		}

		else {
			return false;
		}
	}

	public boolean isDepartmentOrparentDepartmentOwner(BOUser user, BOProject project) {
		BODepartment projectDepartment = null;
		if (project.getProjectDepartments() != null && project.getProjectDepartments().size() > 0) {
			projectDepartment = project.getProjectDepartments().iterator().next();

			Set<BODepartment> departmentsOwned = user.getDepartmentsOwned();

			return containsDepartment(projectDepartment, departmentsOwned);
		} else {
			return false;
		}
	}

	private boolean containsDepartment(BODepartment projectDepartment, Set<BODepartment> departmentsOwned) {
		if (departmentsOwned.contains(projectDepartment)) {
			return true;
		} else {
			for (Iterator depOwnedIterator = departmentsOwned.iterator(); depOwnedIterator.hasNext();) {
				BODepartment currOwnedDep = (BODepartment) depOwnedIterator.next();

				Set<BODepartment> childDep = currOwnedDep.getChildDepartments();
				containsDepartment(projectDepartment, childDep);
			}
		}

		return false;
	}

	public BOProject getCompleteProject(long projectId) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			BOProject project = (BOProject) session.get(BOProject.class, projectId);
			System.out.println(project.getTasks());

			return project;

		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public List<BOProject> getAllProjects(BOCompany company, ProjectStatus projectStatus, BOUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BOProject> getAllProjects(BOCompany company, ProjectStatus projectStatus, DateRange dateRange, BOUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteProject(BOUser user, BOProject project) {
		if (project.canSaveProjectTasks(user)) {
			performDBAction(project, DBActionCriteria.Delete);
		}
	}

	public void saveProjectInfo(BOUser user, BOUser oldOwner, BOProject project) {
		if (project.canSaveProjectInfo(user, oldOwner)) {
			performDBAction(project, DBActionCriteria.Update);
		}
	}

	public void saveProjectTasks(BOUser user, BOProject project) {
		if (project.canSaveProjectTasks(user)) {
			performDBAction(project, DBActionCriteria.Update);
			deleteTasks(user, project);
		} else {
			throw new InsufficientPrivilagesException();
		}
	}

	private void deleteTasks(BOUser user, BOProject project) {
		SessionFactory sessionFactory = null;
		Session session = null;
		String strQuery = "delete from BOTask where taskID=";
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();
			for (BOHistory history : project.getCurrentHistoryForProject()) {
				if (history.getHistoryAction() == HistoryAction.Delete && history.getHistoryTable() == HistoryTable.Tasks) {
					Query query = session.createQuery(strQuery + history.getHistoryItem());
					int rows = query.executeUpdate();
				}
			}
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}

	public static void initCustomFields() {
		if (BOCustomField.CustomTaskModule != null) {
			return;
		}
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = HibernateSessionFactory.getInstance();
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(BOCustomField.class);
			criteria.add(Restrictions.ilike("customFieldModule", "tasks"));
			criteria.add(Restrictions.ilike("customFieldName", "Module"));
			BOCustomField.CustomTaskModule = (BOCustomField) criteria.list().get(0);
		} catch (Exception a_th) {
			throw new RuntimeException(a_th);
		} finally {
			close(sessionFactory, session);
		}
	}
}