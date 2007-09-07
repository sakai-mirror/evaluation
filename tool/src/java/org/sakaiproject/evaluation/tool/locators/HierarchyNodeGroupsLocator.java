package org.sakaiproject.evaluation.tool.locators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sakaiproject.evaluation.logic.EvalExternalLogic;
import org.sakaiproject.evaluation.logic.externals.ExternalHierarchyLogic;
import org.sakaiproject.evaluation.logic.model.EvalGroup;
import org.sakaiproject.evaluation.model.constant.EvalConstants;

import uk.org.ponder.beanutil.BeanLocator;

/*
 * This is used to set whether various Groups are assigned to a Node.  In
 * reality it's just for backing a page of UIBoundBooleans to set the groups.
 * 
 * Example EL Path:
 * 
 *     hierNodeGroupsLocator.12345.mygroup
 *     
 *  Will return a boolean depending on whether that group is selected.
 */
public class HierarchyNodeGroupsLocator implements BeanLocator {

    private EvalExternalLogic external;
    public void setExternal(EvalExternalLogic external) {
       this.external = external;
    }

    private ExternalHierarchyLogic hierarchyLogic;
    public void setHierarchyLogic(ExternalHierarchyLogic hierarchyLogic) {
       this.hierarchyLogic = hierarchyLogic;
    }
    
    public Map delivered = new HashMap(); 
    
    public Object locateBean(String name) {
        checkSecurity();
        
        Object togo = delivered.get(name);
        if (togo == null) {
            List<EvalGroup> evalGroups = external.getEvalGroupsForUser("admin", EvalConstants.PERM_BE_EVALUATED);
            Set<String> assignedGroupIds = hierarchyLogic.getEvalGroupsForNode(name);
            Map assignedGroups = new HashMap();
            for (EvalGroup group: evalGroups) {
                if (assignedGroupIds.contains(group.evalGroupId)) {
                    assignedGroups.put(group.evalGroupId, new Boolean(true));
                }
                else {
                    assignedGroups.put(group.evalGroupId, new Boolean(false));
                }
            }
            togo = assignedGroups;
            delivered.put(name, togo);
        }
        return togo;
    }
    
    /*
     * Currently only administrators can use this functionality.
     */
    private void checkSecurity() {
        String currentUserId = external.getCurrentUserId();
        boolean userAdmin = external.isUserAdmin(currentUserId);

        if (!userAdmin) {
            // Security check and denial
            throw new SecurityException("Non-admin users may not access this locator");
        }
    }
}
