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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.scientificCommission.members"/></h2>

<logic:iterate id="executionDegree" name="executionDegrees">

	<em><bean:write name="executionDegree" property="degreeCurricularPlan.degree.presentationName"/> (<bean:write name="executionDegree" property="executionYear.year"/>)</em>

	<fr:view name="executionDegree" property="scientificCommissionMembers" schema="coordinator.commissionTeam.manage.contacts">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",acenter,"/>
			<fr:property name="linkFormat(remove)" value="<%= "/scientificCouncilManageThesis.do?method=removeScientificCommission&commissionID=${externalId}&degreeId=" + request.getParameter("degreeId") + "&executionYearId=" + request.getParameter("executionYearId") %>"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="bundle(remove)" value="APPLICATION_RESOURCES"/>
			<fr:property name="contextRelative(remove)" value="true"/>
			<fr:property name="confirmationKey(remove)" value="label.scientificCommission.delete.confirmation.message"/>
			<fr:property name="confirmationBundle(remove)" value="APPLICATION_RESOURCES"/>
		</fr:layout>
	</fr:view>

	<bean:define id="executionDegreeID" name="executionDegree" property="externalId" />
	<p class="mvert05">
		<span class="color888"><bean:message key="message.add.scientific.member.to.council.by.username"/></span>
	</p>
	<logic:messagesPresent message="true" property="addError">
		<html:messages id="message" message="true" property="addError">
			<p><span class="error0"><bean:write name="message"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	<fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=addScientificCommission&amp;executionDegreeID=%s&amp;degreeId=%s&amp;executionYearId=%s", executionDegreeID, request.getParameter("degreeId"), request.getParameter("executionYearId")) %>">
		<fr:edit id="usernameChoice" name="usernameBean" schema="coordinator.commissionTeam.addMember">
		    <fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle5 thlight thright thmiddle mvert05"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		    </fr:layout>
		    
		    <fr:destination name="invalid" path="<%= "/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=" + request.getParameter("degreeId") + "&executionYearId=" + request.getParameter("executionYearId") %>"/>
		</fr:edit>
		<html:submit styleClass="mtop05">
		    <bean:message key="button.add"/> 
		</html:submit>
	</fr:form>

</logic:iterate>

