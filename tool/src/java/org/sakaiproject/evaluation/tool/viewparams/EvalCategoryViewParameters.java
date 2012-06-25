/**
 * Copyright 2005 Sakai Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.sakaiproject.evaluation.tool.viewparams;

/**
 * used for passing a category to the show eval categories view
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class EvalCategoryViewParameters extends BaseViewParameters {

	/**
	 * An evaluation category - should match with an evalCategory from an {@link EvalEvaluation} object
	 */
	public String evalCategory;

	public EvalCategoryViewParameters() {}

	public EvalCategoryViewParameters(String viewID, String evalCategory) {
		this.viewID = viewID;
		this.evalCategory = evalCategory;
	}

}
