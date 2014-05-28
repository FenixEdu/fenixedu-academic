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
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>

<h2><bean:message key="label.payments.postingRules.degreeCurricularPlan.rulesFor"
	bundle="MANAGER_RESOURCES" /> <bean:write name="degreeCurricularPlan"
	property="degree.name" /> - <bean:write name="degreeCurricularPlan"
	property="name" /></h2>

<br />

<bean:define id="className" name="degreeCurricularPlan"
	property="class.simpleName" />
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
	property="externalId" />
	
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:view name="degreeCurricularPlan" property="serviceAgreementTemplate.postingRules" schema="PostingRule.view-with-eventType">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="sortBy" value="eventType=asc,startDate=desc" />

		<fr:property name="linkFormat(details)"
			value="<%="/postingRules.do?method=viewPostingRuleDetails&amp;postingRuleId=${externalId}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
		<fr:property name="key(details)" value="label.details" />
		<fr:property name="bundle(details)" value="APPLICATION_RESOURCES" />
		<fr:property name="order(details)" value="0" />

		<fr:property name="linkFormat(edit)"
			value="<%="/postingRules.do?method=prepareEditDegreeCurricularPlanPostingRule&amp;postingRuleId=${externalId}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId%>" />
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
		<fr:property name="visibleIf(edit)" value="mostRecent" />
		<fr:property name="order(edit)" value="1" />
		
		
		<fr:property name="linkFormat(delete)"
			value="<%="/postingRules.do?method=deleteDEAPostingRule&amp;postingRuleId=${externalId}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId%>" />
		<fr:property name="key(delete)" value="label.delete" />
		<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
		<fr:property name="visibleIf(delete)" value="mostRecent" />
		<fr:property name="order(delete)" value="2" />
		<fr:property name="confirmationKey(delete)" value="label.payments.postingRules.confirmDeletePostingRule" />
		<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES"></fr:property>
	</fr:layout>
</fr:view>

<br />

<logic:equal name="allowCreateGratuityPR" value="true">
	<logic:equal name="degreeCurricularPlan" property="degreeType" value="<%= DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA.name() %>">
		<html:link
			action="<%="/postingRules.do?method=prepareCreateDFAGratuityPR&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
			<bean:message key="label.payments.postingRules.createDFAGratuityPR"
				bundle="MANAGER_RESOURCES" />
		</html:link>
		<br />
	</logic:equal>

	<logic:equal name="degreeCurricularPlan" property="degreeType" value="<%= DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.name() %>">
		<html:link
			action="<%="/postingRules.do?method=prepareCreateDEAGratuityPR&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
			<bean:message key="label.payments.postingRules.createDEAGratuityPR"
				bundle="MANAGER_RESOURCES" />
		</html:link>
		<br />		
	</logic:equal>
		
	<logic:equal name="degreeCurricularPlan" property="degreeType" value="<%= DegreeType.BOLONHA_SPECIALIZATION_DEGREE.name() %>">
		<html:link
			action="<%="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPR&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
			<bean:message key="label.payments.postingRules.createSpecializationDegreeGratuityPR"
				bundle="MANAGER_RESOURCES" />
		</html:link>
		<br />		
	</logic:equal>
	
	
</logic:equal>

<logic:equal name="allowCreateStandaloneGratuityPR" value="true">

	<logic:equal name="degreeCurricularPlan" property="degreeType" value="<%= DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA.name() %>">
		<html:link
			action="<%="/postingRules.do?method=prepareCreateDEAStandaloneEnrolmentGratuityPR&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
			<bean:message key="label.payments.postingRules.createGraduationStandaloneEnrolmentGratuityPostingRule"
				bundle="MANAGER_RESOURCES" />
		</html:link>
		<br />		
	</logic:equal>
	
</logic:equal>


<br />
<html:link
	action="<%="/postingRules.do?method=managePostGraduationRules"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
