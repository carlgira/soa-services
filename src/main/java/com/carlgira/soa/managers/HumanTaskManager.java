package com.carlgira.soa.managers;

import com.carlgira.soa.util.ServerConnection;
import oracle.bpel.services.workflow.StaleObjectException;
import oracle.bpel.services.workflow.WorkflowException;
import oracle.bpel.services.workflow.client.IWorkflowServiceClient;
import oracle.bpel.services.workflow.client.IWorkflowServiceClientConstants;
import oracle.bpel.services.workflow.client.WorkflowServiceClientFactory;
import oracle.bpel.services.workflow.query.ITaskQueryService;
import oracle.bpel.services.workflow.repos.Ordering;
import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpel.services.workflow.repos.TableConstants;
import oracle.bpel.services.workflow.task.ITaskAssignee;
import oracle.bpel.services.workflow.task.ITaskService;
import oracle.bpel.services.workflow.task.impl.TaskAssignee;
import oracle.bpel.services.workflow.task.model.IdentityTypeImpl;
import oracle.bpel.services.workflow.task.model.Task;
import oracle.bpel.services.workflow.verification.IWorkflowContext;
import java.util.*;

/**
 * Created by carlgira on 3/3/16.
 * Class to Control the human task inside of a composite
 */
public class HumanTaskManager {

    private IWorkflowServiceClient wfsvcClient;
    private IWorkflowContext ctx;
    private ITaskQueryService querySvc;

    private ServerConnection serverConnection;

    public HumanTaskManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Connect to the server
     * @throws WorkflowException
     */
    public void init() throws WorkflowException {
        Map<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String> properties = new HashMap<IWorkflowServiceClientConstants.CONNECTION_PROPERTY, String>();
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.MODE, IWorkflowServiceClientConstants.MODE_DYNAMIC);

        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_PROVIDER_URL, this.serverConnection.server);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_PRINCIPAL, this.serverConnection.adminUser);
        properties.put(IWorkflowServiceClientConstants.CONNECTION_PROPERTY.EJB_SECURITY_CREDENTIALS, this.serverConnection.adminPassword);
        wfsvcClient = WorkflowServiceClientFactory.getWorkflowServiceClient(WorkflowServiceClientFactory.REMOTE_CLIENT, properties, null);
        querySvc = wfsvcClient.getTaskQueryService();
        ctx = querySvc.authenticate(this.serverConnection.adminUser, this.serverConnection.adminPassword.toCharArray(), this.serverConnection.realm);
    }

    /**
     * Set the task outcome
     * @param task
     * @param outcome
     * @throws WorkflowException
     * @throws StaleObjectException
     */
    public void setTaskOutcome(Task task, String outcome) throws WorkflowException, StaleObjectException {

        if (task != null) {
            if (this.wfsvcClient != null) {
                ITaskService taskSvc = this.wfsvcClient.getTaskService();

                try{
                    task = taskSvc.acquireTask(ctx, task.getSystemAttributes().getTaskId());
                }
                catch (Exception e) {
                    task = querySvc.getTaskDetailsById(ctx, task.getSystemAttributes().getTaskId());
                }

                if (outcome.equals("REQUEST_INFORMATION")) {
                    String firstAssignee = "";
                    List<IdentityTypeImpl> assignees = task.getSystemAttributes().getAssignees();
                    if (assignees != null) {
                        Iterator<IdentityTypeImpl> assigneesIterator = assignees.iterator();
                        while (assigneesIterator.hasNext()) {
                            IdentityTypeImpl ident = assigneesIterator.next();
                            firstAssignee = ident.getId();
                        }
                    }
                    ITaskAssignee ta = new TaskAssignee(firstAssignee, false);
                    taskSvc.requestInfoForTask(this.ctx, task, ta);

                } else if (outcome.equals("ESCALATE")) {
                    taskSvc.escalateTask(this.ctx, task);
                } else if (outcome.equals("WITHDRAW")) {
                    taskSvc.withdrawTask(this.ctx, task);
                } else if (outcome.equals("SUSPEND")) {
                    taskSvc.suspendTask(this.ctx, task);
                } else if (outcome.equals("RESUME")) {
                    taskSvc.resumeTask(this.ctx, task);
                } else {
                    taskSvc.updateTaskOutcome(this.ctx, task.getSystemAttributes().getTaskId(), outcome);
                }
            }
        }
    }

    /**
     * Set the task outcome for a List of task
     * @param tasksId A taskId string List
     * @param outcome
     * @throws WorkflowException
     * @throws StaleObjectException
     */
    public void setTasksOutcome(List<String> tasksId, String outcome) throws Exception {

        if (this.wfsvcClient != null) {
                ITaskService taskSvc = this.wfsvcClient.getTaskService();


            if (outcome.equals("REQUEST_INFORMATION")) {
                throw new Exception("REQUEST_INFORMATION not supported for list of task");
            } else if (outcome.equals("ESCALATE")) {
                taskSvc.escalateTasks(this.ctx, tasksId);
            } else if (outcome.equals("WITHDRAW")) {
                taskSvc.withdrawTasks(this.ctx, tasksId);
            } else if (outcome.equals("SUSPEND")) {
                taskSvc.suspendTasks(this.ctx, tasksId);
            } else if (outcome.equals("RESUME")) {
                taskSvc.resumeTasks(this.ctx, tasksId);
            } else {
                taskSvc.updateOutcomeOfTasks(this.ctx, tasksId, outcome);
            }
        }
    }

    public List<Task> getTasklist(Predicate predicate) {
        List<Task> returnList = null;
        try {
            ITaskQueryService querySvc = wfsvcClient.getTaskQueryService();

            List<String> queryColumns = new ArrayList<>();
            queryColumns.add(TableConstants.WFTASK_TASKID_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_TASKNUMBER_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_TITLE_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_TASKDEFINITIONNAME_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_OUTCOME_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_ASSIGNEES_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_STATE_COLUMN.getName());
            queryColumns.add(TableConstants.WFTASK_WORKFLOWPATTERN_COLUMN.getName());

            List<ITaskQueryService.OptionalInfo> optionalInfo = new ArrayList<ITaskQueryService.OptionalInfo>();
            optionalInfo.add(ITaskQueryService.OptionalInfo.CUSTOM_ACTIONS);
            optionalInfo.add(ITaskQueryService.OptionalInfo.COMMENTS);
            optionalInfo.add(ITaskQueryService.OptionalInfo.PAYLOAD);
            optionalInfo.add(ITaskQueryService.OptionalInfo.DISPLAY_INFO);

            Ordering ordering = new Ordering(TableConstants.WFTASK_TASKNUMBER_COLUMN, true, false);

            List<Task> tasks = querySvc.queryTasks(ctx,
                    queryColumns,
                    optionalInfo,  // emptyList,
                    ITaskQueryService.AssignmentFilter.ALL,
                    null,
                    predicate,
                    ordering,
                    0,
                    0);

            returnList = tasks;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnList;
    }
}

