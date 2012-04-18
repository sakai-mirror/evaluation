/******************************************************************************
 * EvaluationVPInferrer.java - created by aaronz on 28 May 2007
 * 
 * Copyright (c) 2008 Centre for Applied Research in Educational Technologies, University of Cambridge
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 * Contributors:
 * Aaron Zeckoski (aaronz@vt.edu) - primary
 * 
 *****************************************************************************/

package org.sakaiproject.evaluation.tool.inferrers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.evaluation.constant.EvalConstants;
import org.sakaiproject.evaluation.logic.EvalCommonLogic;
import org.sakaiproject.evaluation.logic.EvalEvaluationService;
import org.sakaiproject.evaluation.logic.entity.EvalReportsEntityProvider;
import org.sakaiproject.evaluation.model.EvalEvaluation;
import org.sakaiproject.evaluation.tool.producers.ReportsViewingProducer;
import org.sakaiproject.evaluation.tool.viewparams.ReportParameters;


import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * This will handle the case of reports viewing redirection
 * 
 * @author Aaron Zeckoski (azeckoski @ unicon.net)
 */
public class ReportsVPInferrer implements EntityViewParamsInferrer {

    private static Log log = LogFactory.getLog(ReportsVPInferrer.class);

    private EvalCommonLogic commonLogic;
    public void setCommonLogic(EvalCommonLogic commonLogic) {
        this.commonLogic = commonLogic;
    }

    private EvalEvaluationService evaluationService;
    public void setEvaluationService(EvalEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    public void init() {
        log.info("VP init");
    }

    /* (non-Javadoc)
     * @see uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer#getHandledPrefixes()
     */
    public String[] getHandledPrefixes() {
        return new String[] {
                EvalReportsEntityProvider.ENTITY_PREFIX
        };
    }

    /* (non-Javadoc)
     * @see uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer#inferDefaultViewParameters(java.lang.String)
     */
    public ViewParameters inferDefaultViewParameters(String reference) {
        String refId = EntityReference.getIdFromRef(reference);
        Long evaluationId = Long.valueOf(refId);
        EvalEvaluation evaluation = evaluationService.getEvaluationById(evaluationId);
        if (evaluation == null) {
            throw new IllegalArgumentException("Received an invalid evaluation id ("+evaluationId+") which cannot be resolved to an eval for the reports inferrer");
        }

        String currentUserId = commonLogic.getCurrentUserId();
        if (EvalConstants.SHARING_PUBLIC.equals(evaluation.getResultsSharing())) {
            // for public results, no login check is needed
        } else {
            // for all others we will require a login
            if (commonLogic.isUserAnonymous(currentUserId) ) {
                throw new SecurityException("User must be authenticated to access this page");
            }
        }

        String[] groupIds = new String[] {};
        /** Can only do this when we can get more info in the inferrer (like an EV)
        String evalGroupId = null;
        Long AssignGroupId = new Long(ep.id);
        EvalAssignGroup assignGroup = evaluationService.getAssignGroupById(AssignGroupId);
        evalGroupId = assignGroup.getEvalGroupId();
        evaluationId = assignGroup.getEvaluation().getId();
        evaluation = evaluationService.getEvaluationById(evaluationId);
        if (evalGroupId != null) {
            groupIds = new String[] {evalGroupId};
        }
        **/

        // just send the user to the reporting page, permissions are handled there
        ReportParameters vp = new ReportParameters(ReportsViewingProducer.VIEW_ID, evaluationId, groupIds);
        vp.external = true;
        return vp;
    }

}
