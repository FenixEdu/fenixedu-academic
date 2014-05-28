<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.createSpecializationDegreeGratuityPR"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="degreeCurricularPlanId" name="createSpecializationDegreeGratuityPostingRuleBean" property="degreeCurricularPlan.externalId" />

<logic:empty name="createSpecializationDegreeGratuityPostingRuleBean" property="rule">		
	<fr:form action="<%="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRTypeChosen&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>">
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean" name="createSpecializationDegreeGratuityPostingRuleBean" visible="false" />
	
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean.chooseType"
			name="createSpecializationDegreeGratuityPostingRuleBean"
			schema="CreateSpecializationDegreeGratuityPostingRuleBean.choose-type">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thmiddle thright thlight mtop05" />
			</fr:layout>
			<fr:destination name="invalid" path="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRInvalid" />
			<fr:destination name="cancel" path="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules" />
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
	</fr:form>			
</logic:empty>

<logic:notEmpty name="createSpecializationDegreeGratuityPostingRuleBean" property="rule">
	<fr:form action="<%="/postingRules.do?method=createSpecializationDegreeGratuityPR&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>">
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean" name="createSpecializationDegreeGratuityPostingRuleBean" visible="false" />
		
		<bean:define id="postingRuleClassName" name="createSpecializationDegreeGratuityPostingRuleBean" property="rule.simpleName" />
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean.editPostingRule"
			name="createSpecializationDegreeGratuityPostingRuleBean"
			schema="<%="CreateSpecializationDegreeGratuityPostingRuleBean.edit-" + postingRuleClassName %>">
	
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thmiddle thright thlight mtop05" />
			</fr:layout>
			
			<fr:destination name="invalid" path="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRInvalid" />
			<fr:destination name="cancel" path="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules" />
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
	</fr:form>
</logic:notEmpty>

