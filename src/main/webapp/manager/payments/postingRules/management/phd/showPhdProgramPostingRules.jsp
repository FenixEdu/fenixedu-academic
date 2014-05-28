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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.payments.postingRules.phdProgram.rulesFor"
	bundle="MANAGER_RESOURCES" /> <bean:write name="phdProgram"
	property="name" /></h2>

<br />

<bean:define id="phdProgramId" name="phdProgram"
	property="externalId" />
	
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<html:link
	action="<%="/phdPostingRules.do?method=prepareAddGratuityPhdPostingRule&phdProgramId=" + phdProgramId %>">
	Criar Regra de pagamento de doutoramento
</html:link>

<fr:view name="phdProgram" property="serviceAgreementTemplate.postingRules" schema="PostingRule.view-with-eventType">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="sortBy" value="eventType=asc,startDate=desc" />
		
		<fr:link 	name="details" label="label.details,APPLICATION_RESOURCES" 
					link="<%= "/phdPostingRules.do?method=viewPostingRuleDetails&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>" 
					order="0"/>

		<fr:link	name="edit" label="label.edit,APPLICATION_RESOURCES"
					link="<%= "/phdPostingRules.do?method=prepareEditPhdProgramPostingRule&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>"
					condition="mostRecent"
					order="1"
					/>
		<fr:link	name="delete" label="label.delete,APPLICATION_RESOURCES"
					link="<%= "/phdPostingRules.do?method=deleteDegreeCurricularPlanPostingRule&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>"
					condition="mostRecent" 
					order="2" 
					confirmation="label.payments.postingRules.confirmDeletePostingRule,MANAGER_RESOURCES" />
	</fr:layout>	
</fr:view>

<br />
<html:link
	action="<%="/postingRules.do?method=managePostGraduationRules"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
