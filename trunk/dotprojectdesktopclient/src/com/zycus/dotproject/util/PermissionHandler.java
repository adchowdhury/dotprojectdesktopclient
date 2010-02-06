package com.zycus.dotproject.util;

import java.util.Iterator;
import java.util.Set;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;

public class PermissionHandler
{
	public boolean canViewProject(BOUser user, BOProject project)
	{
		Set<BOTask> taskSet = project.getTasks();
		for (Iterator taskIterator = taskSet.iterator(); taskIterator
				.hasNext();)
		{
			BOTask currentTask = (BOTask) taskIterator.next();
			if(currentTask.canAddTaskLog(user))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//TODO send the parent task, if parent task is null then check for project ownership
	public boolean canAddTasks(BOUser user, BOProject project)
	{
		if(project.getProjectOwner().getUserID()==user.getUserID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean canEditProjectDataOtherThanTasks(BOUser user, BOProject project) 
	{
		if(project.getProjectOwner().getUserID()==user.getUserID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean canDeleteProject(BOUser user, BOProject project) 
	{
		//TODO should the admin be allowed to delete a project ... I think not  (user.getUserType()==UserType.Administrator)
		if(project.getProjectOwner().getUserID()==user.getUserID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean canSaveProjectTasks(BOUser user, BOProject project) 
	{
		Iterator<BOTask> taskIterator = project.getTasks().iterator();
		while(taskIterator.hasNext())
		{
			BOTask task = taskIterator.next();
			if(task.canBeEdited(user))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean canEditTask(BOUser user, BOTask task) 
	{
		if (user.getUserID() == task.getProject().getProjectOwner().getUserID()) 
		{
			return true;
		}

		else 
		{
			if (user.getUserID() == task.getOwner().getUserID()) 
			{
				return true;
			}

			BOTask currenTask = task;
			BOTask currParentTask = task.getParentTask();
			while (currParentTask != null && currParentTask.getEnhancedTaskID() != currenTask.getEnhancedTaskID()) 
			{
				if (user.getUserID() == currParentTask.getOwner().getUserID()) 
				{
					return true;
				}
				currenTask = currParentTask;
				currParentTask = currParentTask.getParentTask();
			}
		}
		return false;
	}

	public boolean canAddTaskLog(BOUser user, BOTask task) 
	{
		if (user.isContainedIn(task.getAssineeUsers()) || canEditTask(user, task)) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	public boolean canBeDeleted(BOUser user, BOTask task) 
	{
		if (user.getUserID() == task.getProject().getProjectOwner().getUserID()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	public boolean canAddTasks(BOUser user, BOTask parentTask) 
	{
		return canEditTask(user, parentTask);
	}


}
