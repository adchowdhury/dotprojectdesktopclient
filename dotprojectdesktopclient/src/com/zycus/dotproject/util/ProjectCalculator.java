package com.zycus.dotproject.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zycus.dotproject.bo.BOHistory;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.DateRange;
import com.zycus.dotproject.bo.HistoryAction;
import com.zycus.dotproject.bo.HistoryTable;
import com.zycus.dotproject.bo.TaskDynamic;

public class ProjectCalculator {
	/**
	 * few things to keep in mind 1) always have update the time of the dates of
	 * tasks, as that is not being set from 2) have to keep track or recursion
	 * in parent child relations, as that is not being checked from UI and take
	 * some decision
	 */
	public static void calculateProject() {
		BOProject project = ApplicationContext.getCurrentProjet();
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.calculateProject()");
	}

	public static void taskDeleted(BOTask taskTobeDeleted, BOUser currentUser) 
	{
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.taskDeleted()");
		
		if(taskTobeDeleted.getParentTask()!=null && taskTobeDeleted.getParentTask().getEnhancedTaskID()!=taskTobeDeleted.getEnhancedTaskID())
		{
			modifyTask(taskTobeDeleted.getParentTask());
		}
		
		if(taskTobeDeleted.getDependentTasks()!=null && taskTobeDeleted.getDependentTasks().size()!=0)
		{
			modifySuccessors(taskTobeDeleted.getDependentTasks(), null);
		}
		
		addToProjectHistory(currentUser, taskTobeDeleted.getProject().getProjectID(), taskTobeDeleted.getTaskID(), HistoryAction.Delete, HistoryTable.Tasks, taskTobeDeleted.getTaskName(), taskTobeDeleted.getProject());
	}

	public static void taskAdded(BOTask taskToBeAdded, BOUser currentUser) 
	{
		Date startDate=DateRange.getNormalizedStartDate(taskToBeAdded.getStartDate(), true);
		taskToBeAdded.setStartDate(startDate);
		Date endDate = DateRange.calculateEndDate(taskToBeAdded.getStartDate(), taskToBeAdded.getDuration());
		taskToBeAdded.setEndDate(endDate);
		
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.taskAdded(" + taskToBeAdded + ")");
		
		if(taskToBeAdded.getParentTask()!=null && taskToBeAdded.getParentTask().getEnhancedTaskID()!=taskToBeAdded.getEnhancedTaskID())
		{
			//System.out.println("ProjectCalculator.taskAdded() parentTask is " + task.getParentTask());
			modifyTask(taskToBeAdded.getParentTask());
		}
		
		addToProjectHistory(currentUser, taskToBeAdded.getProject().getProjectID(), taskToBeAdded.getTaskID(), HistoryAction.Add, HistoryTable.Tasks, taskToBeAdded.getTaskName(), taskToBeAdded.getProject());
	}

	public static void parentChanged(BOTask previousParent, BOTask newParent, BOTask task, BOUser currentUser) 
	{
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.parentChanged()");
		
		if(newParent.getEnhancedTaskID()!=previousParent.getEnhancedTaskID())
		{
    		if(newParent.getEnhancedTaskID()!=task.getEnhancedTaskID())
    		{
    			modifyTask(newParent);
    		}
    		
    		if(previousParent.getEnhancedTaskID()!=task.getEnhancedTaskID())
    		{
    			modifyTask(previousParent);
    		}
		}
		
		addToProjectHistory(currentUser, task.getProject().getProjectID(), task.getTaskID(), HistoryAction.Update, HistoryTable.Tasks, task.getTaskName(), task.getProject());
	}

	public static void predecessorsChanged(Set<BOTask> predecessors, BOTask task, BOUser currentUser) 
	{
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.predecessorsChanged() starts");
		
		Date maxStartDateForSuccesor = null;
		BOTask[] predecessorArr = task.getPreviousTasks().toArray(new BOTask[]{});
		for(BOTask prevTask : predecessorArr)
		{
			System.out.println("ProjectCalculator.predecessorsChanged() : " + prevTask.getTaskName());
			
			//TODO Invalid operation handled
			if(taskExistsInHierarchy(task, prevTask) || circularDependencyExists(prevTask, prevTask, 0))
			{
				//System.out.println("MANINI DEBUGS : "+(task.getPreviousTasks().contains(prevTask)));
				task.getPreviousTasks().remove(prevTask);
			}
			else
			{
    			if(maxStartDateForSuccesor==null || prevTask.getEndDate().after(maxStartDateForSuccesor))
    			{
    				maxStartDateForSuccesor = prevTask.getEndDate();
    			}
			}
		}
		if(maxStartDateForSuccesor!=null)
		{
			Date startDateForSuccesor = DateRange.getStartDateForTheSuccessor(maxStartDateForSuccesor, task.getDuration());
			task.setStartDate(startDateForSuccesor);
			
			//System.out.println(Thread.currentThread().getName() + "startDateForSuccesor : " + startDateForSuccesor);
			modifyTask(task);
		}
		
		addToProjectHistory(currentUser, task.getProject().getProjectID(), task.getTaskID(), HistoryAction.Update, HistoryTable.Tasks, task.getTaskName(), task.getProject());
		
		//System.out.println(Thread.currentThread().getName() + "ProjectCalculator.predecessorsChanged() finishes");
	}
	
	private static boolean circularDependencyExists(BOTask task, BOTask predecessorTask, int level)
	{
		boolean circularDependencyExists = false;
		
		/*if(level==0)
		{
			System.out.println("ProjectCalculator.circularDependencyExists() : task : " + task.getTaskName());
		}*/
		
		BOTask[] predecessorArr = predecessorTask.getPreviousTasks().toArray(new BOTask[]{});
		System.out.println("ProjectCalculator.circularDependencyExists() : predecessorArr.length : " + predecessorArr.length);
		for(BOTask currentTask : predecessorArr)
		{
			System.out.println("ProjectCalculator.circularDependencyExists() : currentTask.getTaskName() : " + currentTask.getTaskName());
			if(currentTask.getEnhancedTaskID()==task.getEnhancedTaskID())
			{
				System.out.println("ProjectCalculator.circularDependencyExists() currentTask.getEnhancedTaskID()==task.getEnhancedTaskID()");
				circularDependencyExists = true;
				break;
			}
			else if(currentTask.getPreviousTasks().size()==0)
			{
				System.out.println("ProjectCalculator.circularDependencyExists() currentTask.getPreviousTasks().size()==0");
				circularDependencyExists = false;
				break;
			}
			else
			{
				System.out.println("ProjectCalculator.circularDependencyExists() : incrementing level : ");
				circularDependencyExists = circularDependencyExists(task, currentTask, ++level);
				
				if(circularDependencyExists)
				{
					break;
				}
			}
		}
		
		System.out.println("ProjectCalculator.circularDependencyExists() : circularDependencyExists : " + circularDependencyExists);
		return circularDependencyExists;
	}
	
	private static boolean taskExistsInHierarchy(BOTask task, BOTask predecessorTask)
	{
		boolean taskExistsInHierarchy = false;
		if(predecessorTask.getEnhancedTaskID()==task.getEnhancedTaskID())
    	{
			taskExistsInHierarchy = true;
    	}
		
		BOTask currentTask = task;
		BOTask parentTask = task.getParentTask();
		while(parentTask.getEnhancedTaskID()!=currentTask.getEnhancedTaskID())
		{
			if(parentTask.getEnhancedTaskID()==predecessorTask.getEnhancedTaskID())
			{
				taskExistsInHierarchy = true;
				break;
			}
			currentTask = parentTask;
			parentTask = parentTask.getParentTask();
		}
		
		System.out.println("ProjectCalculator.taskExistsInHierarchy() taskExistsInHierarchy : " + taskExistsInHierarchy);
		return taskExistsInHierarchy;
	}

	public static void startDateOrDurationChanged(BOTask... tasks) 
	{
		System.out.println(Thread.currentThread().getName() + "ProjectCalculator.startDateOrDurationChanged()");
		for (BOTask task : tasks) {
			modifyTask(task);
		}
	}
	
	public static void main(String[] args)
	{
		testComplex();
	}
	
	public static void calcParent()
	{
		BOTask task1 = new BOTask();
		task1.setTaskID(1);
		task1.setTaskName("task1");
		Date task1StartDate = new Date();
		task1StartDate.setHours(19);
		task1StartDate.setMinutes(0);
		task1.setStartDate(task1StartDate);
		task1.setDurationInDays(4);
		Date task1EndDate =  DateRange.calculateEndDate(task1StartDate, 1);
		task1.setEndDate(task1EndDate);
		
		BOTask task2 = new BOTask();
		task2.setTaskID(2);
		task2.setTaskName("task2");
		Date task2StartDate = new Date();
		task2StartDate.setHours(19);
		task2StartDate.setMinutes(0);
		task2.setStartDate(task2StartDate);
		task2.setDurationInDays(4);
		Date task2EndDate =  DateRange.calculateEndDate(task2StartDate, 1);
		task2.setEndDate(task2EndDate);
		
		BOTask task3 = new BOTask();
		task3.setTaskID(3);
		task3.setTaskName("task3");
		Date task3StartDate = new Date();
		task3.setStartDate(task2StartDate);
		
		task1.setParentTask(task3);
		task2.setParentTask(task3);
		
		LinkedHashSet<BOTask> task3Kids = new LinkedHashSet<BOTask>();
		task3Kids.add(task1);
		task3Kids.add(task2);
		task3.setChildTasks(task3Kids);
		task3.setParentTask(task3);
		
		modifyTask(task1);
		
		System.out.println("ProjectCalculator.calcParent() " + task3.getDuration());
	}
	
	public static void testComplex()
	{
		Date startDate = new Date(System.currentTimeMillis());
		startDate.setDate(7);
		startDate.setHours(11);
		startDate.setMinutes(0);
		startDate.setSeconds(0);
				
		BOTask task1 = new BOTask();
		task1.setTaskID(1);
		task1.setTaskName("task1");
		Date task1StartDate = (Date) startDate.clone();
		task1StartDate.setDate(7);
		task1StartDate.setHours(11);
		task1StartDate.setMinutes(0);
		task1StartDate.setSeconds(0);
		task1.setStartDate(task1StartDate);
		task1.setDurationInDays(1);
		Date task1EndDate =  DateRange.calculateEndDate(task1StartDate, 1);
		task1.setEndDate(task1EndDate);
		
		BOTask task2 = new BOTask();
		task2.setTaskID(2);
		task2.setTaskName("task2");
		Date task2StartDate = DateRange.getStartDateForTheSuccessor(task1.getEndDate(), 1);
		task2.setStartDate(task2StartDate);
		task2.setDurationInDays(1);
		Date task2EndDate =  DateRange.calculateEndDate(task2StartDate, 1);
		task2.setEndDate(task2EndDate);
		
		
		
		BOTask task3 = new BOTask();
		task3.setTaskID(3);
		task3.setTaskName("task3");
		task3.setStartDate(task1.getStartDate());
		task3.setDurationInDays(task1.getDurationInDays()+task2.getDurationInDays());
		task3.setEndDate(task2.getEndDate());
		
		
		
		BOTask task4 = new BOTask();
		task4.setTaskID(4);
		task4.setTaskName("task4");
		Date task4StartDate = DateRange.getStartDateForTheSuccessor(task1.getEndDate(), 1);
		task4StartDate.setDate(7);
		task4StartDate.setHours(11);
		task4StartDate.setMinutes(0);
		task4StartDate.setSeconds(0);
		task4.setStartDate(task4StartDate);
		task4.setDurationInDays(1);
		Date task4EndDate =  DateRange.calculateEndDate(task4StartDate, 1);
		task4.setEndDate(task4EndDate);
		
		BOTask task5 = new BOTask();
		task5.setTaskID(5);
		task5.setTaskName("task5");
		Date task5StartDate = DateRange.getStartDateForTheSuccessor(task5.getEndDate(), 1);
		task5.setStartDate(task5StartDate);
		task5.setDurationInDays(1);
		Date task5EndDate =  DateRange.calculateEndDate(task5StartDate, 1);
		task5.setEndDate(task5EndDate);
		
		
		BOTask task6 = new BOTask();
		task6.setTaskID(6);
		task6.setTaskName("task6");
		task6.setStartDate(task6.getStartDate());
		task6.setDurationInDays(task4.getDurationInDays()+task5.getDurationInDays());
		task6.setEndDate(task6.getEndDate());
		
		
		
		// parent child
		task1.setParentTask(task3);
		task2.setParentTask(task3);
		
		LinkedHashSet<BOTask> task3Kids = new LinkedHashSet<BOTask>();
		task3Kids.add(task1);
		task3Kids.add(task2);
		task3.setChildTasks(task3Kids);
		
		
		task4.setParentTask(task6);
		task5.setParentTask(task6);
		
		LinkedHashSet<BOTask> task6Kids = new LinkedHashSet<BOTask>();
		task6Kids.add(task4);
		task6Kids.add(task5);
		task6.setChildTasks(task6Kids);
		
		//Successors and predecessors
		LinkedHashSet<BOTask> task2Pre = new LinkedHashSet<BOTask>();
		task2Pre.add(task1);
		task2.setPreviousTasks(task2Pre);
		
		LinkedHashSet<BOTask> task1Suc = new LinkedHashSet<BOTask>();
		task1Suc.add(task2);
		task1Suc.add(task4);
		task1Suc.add(task5);
		task1.setDependentTasks(task1Suc);
		
		
		LinkedHashSet<BOTask> task4Pre = new LinkedHashSet<BOTask>();
		task4Pre.add(task1);
		task4.setPreviousTasks(task4Pre);
		
		LinkedHashSet<BOTask> task5Pre = new LinkedHashSet<BOTask>();
		task5Pre.add(task4);
		task5Pre.add(task1);
		task5.setPreviousTasks(task5Pre);
		
		LinkedHashSet<BOTask> task4Suc = new LinkedHashSet<BOTask>();
		task4Suc.add(task5);
		task4.setDependentTasks(task4Suc);
		
		
		
		startDate.setDate(8);
		
		System.out.println(Thread.currentThread().getName() + "B4 Modify");
		
		System.out.println(Thread.currentThread().getName() + task1.toString());
		System.out.println(Thread.currentThread().getName() + task2.toString());
		System.out.println(Thread.currentThread().getName() + task3.toString());
		
		System.out.println(Thread.currentThread().getName() + task4.toString());
		System.out.println(Thread.currentThread().getName() + task5.toString());
		System.out.println(Thread.currentThread().getName() + task6.toString());
		
		startDate.setDate(9);
		task1.setStartDate(startDate);
		
		modifyTask(task1);
		
		System.out.println(Thread.currentThread().getName() + "After Modify");
		
		
		System.out.println(Thread.currentThread().getName() + task1.toString());
		System.out.println(Thread.currentThread().getName() + task2.toString());
		System.out.println(Thread.currentThread().getName() + task3.toString());
		
		System.out.println(Thread.currentThread().getName() + task4.toString());
		System.out.println(Thread.currentThread().getName() + task5.toString());
		System.out.println(Thread.currentThread().getName() + task6.toString());
		
	}
	
	private static void modifyLeafTask(BOTask leafTask)
	{
		//System.out.println(Thread.currentThread().getName() + "modifyLeafTask starts " + leafTask.getTaskName());
		
		leafTask.setStartDate(DateRange.getNormalizedStartDate(leafTask.getStartDate(), false));
		leafTask.setEndDate(DateRange.calculateEndDate(leafTask.getStartDate(), leafTask.getDurationInDays()));
		
		//System.out.println(Thread.currentThread().getName() + "modifyLeafTask starts " + leafTask.getTaskName());
	}

	private static void modifyTask(BOTask task) 
	{
		//System.out.println(Thread.currentThread().getName() + "modifyTask starts " + task.getTaskName());
		
		if(task.getChildTasks()!=null && task.getChildTasks().size()>0)
		{
			task.setTaskDynamic(TaskDynamic.dynamicTask);
			modifyParent(task);
		}
		
		else
		{
			task.setTaskDynamic(TaskDynamic.dependencyCheckingOn);
			modifyLeafTask(task);
		}
		
		if(task.getDependentTasks()!=null && task.getDependentTasks().size()!=0)
		{
			modifySuccessors(task.getDependentTasks(), task);
		}
		
		if(task.getParentTask()!=null && task.getParentTask().getEnhancedTaskID()!=task.getEnhancedTaskID())
		{
			modifyTask(task.getParentTask());
		}
		
		//System.out.println(Thread.currentThread().getName() + "modifyTask ends " + task.getTaskName());
	}

	private static void modifyParent(BOTask parentTask) 
	{
		System.out.println(Thread.currentThread().getName() + "modifyParent starts " + parentTask.getTaskName());
		
		Set set = parentTask.getChildTasks();
		Date minStartdate = null;
		Date maxEndDate = null;
		
		for (Iterator iterator = set.iterator(); iterator.hasNext();)
		{
			BOTask childTask = (BOTask) iterator.next();
			if(childTask.getEnhancedTaskID()!=parentTask.getEnhancedTaskID())
			{
				//System.out.println(Thread.currentThread().getName() + "modifyParent child name : " + childTask.getTaskName());
				if(minStartdate==null || childTask.getStartDate().before(minStartdate))
				{
					minStartdate = childTask.getStartDate();
				}
				
				if(maxEndDate==null || childTask.getEndDate().after(maxEndDate))
				{
					//System.out.println("ProjectCalculator.modifyParent() childTask.getTaskID() " + childTask.getEnhancedTaskID());
					maxEndDate = childTask.getEndDate();
					//System.out.println("ProjectCalculator.modifyParent() childTask maxEndDate " + maxEndDate);
				}
			}
		}
		
		System.out.println("ProjectCalculator.modifyParent() minStartdate " + minStartdate);
		System.out.println("ProjectCalculator.modifyParent() maxEndDate " + maxEndDate);
		
		parentTask.setStartDate(minStartdate);
		parentTask.setEndDate(maxEndDate);
		System.out.println("ProjectCalculator.modifyParent() : " +DateRange.calculateDuration(minStartdate, maxEndDate));
		parentTask.setDurationInDays(DateRange.calculateDuration(minStartdate, maxEndDate));
		
		System.out.println(Thread.currentThread().getName() + "modifyParent ends " + parentTask.getTaskName());
	}
	

	private static void modifySuccessors(Set<BOTask> successors, BOTask task) 
	{
		//System.out.println(Thread.currentThread().getName() + "modifySuccessors starts");
		for (Iterator successorIterator = successors.iterator(); successorIterator.hasNext();) 
		{
			BOTask successor = (BOTask) successorIterator.next();
			
			//TODO check required while adding a dependency
			if(task==null || successor.getEnhancedTaskID()!=task.getEnhancedTaskID())
			{
    			System.out.println("modifySuccessors starts " + successor.getTaskName());
    			Date maxStartDateForSuccesor = null;
    			Set<BOTask> set = successor.getPreviousTasks();
    			for (Iterator iterator = set.iterator(); iterator.hasNext();)
    			{
    				BOTask prevTask = (BOTask) iterator.next();
    				if(maxStartDateForSuccesor==null || prevTask.getEndDate().after(maxStartDateForSuccesor))
    				{
    					maxStartDateForSuccesor = prevTask.getEndDate();
    				}
    			}
    			Date startDateForSuccesor = DateRange.getStartDateForTheSuccessor(maxStartDateForSuccesor, successor.getDuration());
    			successor.setStartDate(startDateForSuccesor);
    			modifyTask(successor);
			}
		}
		
		//System.out.println(Thread.currentThread().getName() + "modifySuccessors ends");
	}

	private static Set<BOTask> getSuccessors() {
		return null;
	}

	private static void updateStartAndEndDate(BOTask task, Date startDate) {

	}

	public static void assineeChanged(Set<BOUser> sssinees, BOTask... tasks) {
		//System.out.println(Thread.currentThread().getName() + "ProjectCalculator.assineeChanged()");
	}
	
	public static void addToProjectHistory(BOUser historyUser, long historyProject, long historyItem, 
			HistoryAction historyAction, HistoryTable historyTable, 
			String historyDescription, BOProject project)
	{
		
		ArrayList<BOHistory> currentHistoryForProject = project.getCurrentHistoryForProject();
		BOHistory history = new BOHistory(historyUser, project.getProjectID(), historyItem, historyAction, historyTable, null, null, historyDescription);
	}
}