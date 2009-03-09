/**
 * $Id$
 * $URL$
 * EvalEmailsLogic.java - evaluation - Dec 27, 2006 12:07:31 AM - azeckoski
 **************************************************************************
 * Copyright (c) 2008 Centre for Applied Research in Educational Technologies, University of Cambridge
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 *
 * Aaron Zeckoski (azeckoski@gmail.com) (aaronz@vt.edu) (aaron@caret.cam.ac.uk)
 */

package org.sakaiproject.evaluation.logic;

import org.sakaiproject.evaluation.constant.EvalConstants;

/**
 * Handles all logic associated with processing email notifications,
 * the methods to send emails are in {@link EvalCommonLogic}
 *
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public interface EvalEmailsLogic {

   // NOTIFICATION methods

   /**
    * Send notifications to evaluatees (and owner if desired) that a new evaluation
    * has been created, includes links directly to add their own questions and details
    * about the dates for the evaluation. If questions may be added by instructor
    * include that in the notification. If instructor may opt in or opt out include
    * that in the notification.
    * 
    * @param evaluationId the id of an EvalEvaluation object
    * @param includeOwner if true then send an email to the owner (creator of this evaluation) also, else do not include the owner
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalCreatedNotifications(Long evaluationId, boolean includeOwner);

   /**
    * Send notifications to evaluators that there is an evaluation ready for them to take
    * and includes information about the evaluation (dates), also includes links to
    * take the evaluation in "one-click" (i.e. link directly to the take_eval page)
    * 
    * @param evaluationId the id of an EvalEvaluation object
    * @param includeEvaluatees if true, if evaluatees (probably instructors)
    * have not opted into an evaluation which is opt-in include notification, otherwise this does nothing
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalAvailableNotifications(Long evaluationId, boolean includeEvaluatees);

   /**
    * Send late notification to evaluators that there is an evaluation ready for them to take
    * and includes information about the evaluation (dates), also includes links to
    * take the evaluation in "one-click" (i.e. link directly to the take_eval page)
    * Called if an instructor opts-in after receiving an email sent on the 
    * Start Date saying that an evaluation may be taken if the instructor opts-in
    * 
    * @param evaluationId the identifier for the EvalEvaluation that may be taken
    * @param evalGroupId the identifier for the EvalGroup to be notified
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalAvailableGroupNotification(Long evaluationId, String evalGroupId);
   
   /**
    * Send a single email notification to any user having one or more evaluations ready for them to take,
    * and include the earliest due date of those evaluations, also include a link to the summary page of 
    * the evaluation tool on the user's My Workspace site
    * 
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalAvailableSingleEmail();


   /**
    * Send reminder notifications to all users who are taking an evaluation,
    * these include a direct link to the evaluation and information about the
    * evalGroupId being evaluated and the dates of the evaluation, 
    * uses the reminder template and the users to include can be controlled
    * 
    * @param evaluationId the id of an EvalEvaluation object
    * @param includeConstant a constant to indicate what users should receive the notification, EVAL_INCLUDE_* from {@link EvalConstants}
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalReminderNotifications(Long evaluationId, String includeConstant);
   
   /**
    * Send a single email reminder to any user having one or more un-submitted responses to active evaluations,
    * and include the earliest due date of those evaluations, also include a link to the summary page of 
    * the evaluation tool on the user's My Workspace site
    * 
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalReminderSingleEmail();

   /**
    * Send notifications that the evaluation is now complete and the results are viewable,
    * includes stats for the evaluation (response rate, etc.) and links directly to
    * the results page for this evaluation, owner of the evaluation is always included
    * in the notification
    * 
    * @param evaluationId the id of an EvalEvaluation object
    * @param includeEvaluatees if true, include notifications to all evaluated users
    * @param includeAdmins if true, include notifications to all admins above the contexts and
    * eval groups evaluated in this evaluation, otherwise include evaluatees only
    * @param jobType JOB_TYPE_VIEWABLE, JOB_TYPE_VIEWABLE_INSTRUCTORS or JOB_TYPE_VIEWABLE_STUDENTS
    * @return an array of the email addresses that were sent to
    */
   public String[] sendEvalResultsNotifications(Long evaluationId, boolean includeEvaluatees, boolean includeAdmins, String jobType);
   
   /**
    * Send confirmation to a user that an evaluation has been submitted (might be required by Instructor)
    * @param evalId the id of an EvalEvaluation object
    
    * @return the email address of the user
    */
   public String sendEvalSubmissionConfirmationEmail(Long evalId);

}
