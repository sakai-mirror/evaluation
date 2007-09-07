package org.sakaiproject.evaluation.tool.producers;

import org.sakaiproject.evaluation.logic.EvalExternalLogic;
import org.sakaiproject.evaluation.logic.externals.ExternalHierarchyLogic;
import org.sakaiproject.evaluation.logic.model.EvalHierarchyNode;
import org.sakaiproject.evaluation.logic.providers.EvalHierarchyProvider;
import org.sakaiproject.evaluation.tool.viewparams.ModifyHierarchyNodeParameters;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ModifyHierarchyNodeProducer implements ViewComponentProducer, ViewParamsReporter {
    public static final String VIEW_ID = "modify_hierarchy_node";

    public String getViewID() {
        return VIEW_ID;
    }
    
    private EvalExternalLogic external;
    public void setExternal(EvalExternalLogic external) {
       this.external = external;
    }

    private ExternalHierarchyLogic hierarchyLogic;
    public void setHierarchyLogic(ExternalHierarchyLogic hierarchyLogic) {
       this.hierarchyLogic = hierarchyLogic;
    }

    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
        String currentUserId = external.getCurrentUserId();
        boolean userAdmin = external.isUserAdmin(currentUserId);

        if (!userAdmin) {
            // Security check and denial
            throw new SecurityException("Non-admin users may not access this page");
        }
        
        /*
         * top menu links and bread crumbs here
         */
        UIInternalLink.make(tofill, "summary-toplink", UIMessage.make("summary.page.title"), new SimpleViewParameters(SummaryProducer.VIEW_ID));
        UIInternalLink.make(tofill, "administrate-toplink", UIMessage.make("administrate.page.title"), new SimpleViewParameters(AdministrateProducer.VIEW_ID));
        UIInternalLink.make(tofill, "hierarchy-toplink", UIMessage.make("controlhierarchy.breadcrumb.title"), new SimpleViewParameters(ControlHierarchyProducer.VIEW_ID));
        UIMessage.make(tofill, "page-title", "modifyhierarchynode.breadcrumb.title");
        
        //EvalHierarchyNode toEdit 
        ModifyHierarchyNodeParameters params = (ModifyHierarchyNodeParameters) viewparams;
        boolean addingChild = params.addingChild;
        EvalHierarchyNode node = hierarchyLogic.getNodeById(params.nodeId);
        if (addingChild) {
            UIMessage.make(tofill, "modify-location-message", "modifyhierarchynode.add.location", new String[] {node.title});
        }
        else {
            UIMessage.make(tofill, "modify-location-message", "modifyhierarchynode.modify.location", new String[] {node.title});
        }
        
        /*
         * The Submission Form
         */
        UIForm form = UIForm.make(tofill, "modify-node-form");
        UIInput.make(form, "node-title", "");
        UIInput.make(form, "node-abbr", "");
        UICommand.make(form, "save-node-button", "");
        UIInternalLink.make(form, "cancel-link", UIMessage.make("modifyhierarchynode.cancel"), new SimpleViewParameters(ControlHierarchyProducer.VIEW_ID));
    }

    public ViewParameters getViewParameters() {
        return new ModifyHierarchyNodeParameters();
    }

}