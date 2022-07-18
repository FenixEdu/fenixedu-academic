<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.fenixedu.academic.predicate.AccessControl"%>

<bean:define id="competenceCourseID" value="<%= request.getParameter("competenceCourseID") %>"/>
<em><bean:message key="bolonhaManager" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="BOLONHA_MANAGER_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<ul>
	<li>
		<html:link page="/competenceCourses/manageVersions.do?method=prepare"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
	<c:if test="${competenceCourse.isApproved()}">
		<li>
			<html:link page="<%="/competenceCourses/manageVersions.do?method=prepareCreateVersion&competenceCourseID=" + competenceCourseID  %>">
				<bean:message key="label.create.version" bundle="BOLONHA_MANAGER_RESOURCES"/>
			</html:link>
		</li>
	</c:if>	
</ul>


<h3 class="mvert15"><span><logic:notEmpty name="competenceCourse" property="code"><fr:view name="competenceCourse" property="code"/> - </logic:notEmpty><fr:view name="competenceCourse" property="name"/></span></h3>


<p class="mbottom05">
	<strong><bean:message key="label.competenceCourseInformations" bundle="BOLONHA_MANAGER_RESOURCES"/></strong>
</p>

<fr:view name="competenceCourse" property="competenceCourseInformations" schema="view.competenceCourseInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		<fr:property name="link(view)" value="<%= "/competenceCourses/manageVersions.do?method=showCompetenceCourseInformation&competenceCourseID=" + competenceCourseID %>"/>
		<fr:property name="param(view)" value="externalId/oid"/>		
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
		<fr:property name="key(view)" value="label.generic.check"/>
		<fr:property name="link(propose)" value="<%= "/competenceCourses/manageVersions.do?method=prepareCreateVersion&proposal=y&competenceCourseID=" + competenceCourseID %>"/>
		<fr:property name="param(propose)" value="executionPeriod.externalId/executionPeriodID"/>
		<fr:property name="bundle(propose)" value="BOLONHA_MANAGER_RESOURCES"/>
		<fr:property name="key(propose)" value="label.new.version.proposal"/>
		<fr:property name="visibleIf(propose)" value="loggedPersonAllowedToEdit"/>
		<fr:property name="sortBy" value="executionPeriod=desc"/>
	</fr:layout>
</fr:view>


<p class="mbottom05">
	<strong><bean:message key="label.proposals" bundle="BOLONHA_MANAGER_RESOURCES"/></strong>
</p>

<logic:notEmpty name="competenceCourse" property="competenceCourseInformationChangeRequests">
	<table class="tstyle1 thlight tdcenter mtop05">
	<tr>
		<th><bean:message key="label.year" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
<%-- 		<th><bean:message key="label.semester" bundle="BOLONHA_MANAGER_RESOURCES"/></th> --%>
		<th><bean:message key="label.modificationRequestedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationsAnalisedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.status" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.department" bundle="APPLICATION_RESOURCES"/></th>
		<th></th>
	</tr>
	
	<logic:iterate id="changeRequest" name="competenceCourse" property="competenceCourseInformationChangeRequests" type="org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest">
		<bean:define id="changeRequestID" name="changeRequest" property="externalId"/>
		<tr>
			<td><fr:view name="changeRequest" property="executionPeriod.executionYear.year"/></td>			
<%-- 			<td><fr:view name="changeRequest" property="executionPeriod.name"/></td> --%>
			<td><fr:view name="changeRequest" property="requester" type="org.fenixedu.academic.domain.Person">
					<fr:layout name="null-as-label">
						<fr:property name="label" value="-"/>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="responsiblePerson.name"/>
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<fr:view name="changeRequest" property="analizedBy" type="org.fenixedu.academic.domain.Person">
					<fr:layout name="null-as-label">
						<fr:property name="label" value="-"/>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="responsiblePerson.name"/>
					</fr:layout>
				</fr:view>
			</td>
			<td class="<%= (changeRequest.getApproved() == null ? "draft" : (changeRequest.getApproved() ? "approved" : "rejected")) %>">
				<fr:view name="changeRequest" property="status"/>
			</td>
			<td><%= changeRequest.getCompetenceCourse().getDepartmentUnit(changeRequest.getExecutionPeriod()).getAcronym() %></td>
			<td style="text-align: left;">
				<html:link page="<%= "/competenceCourses/manageVersions.do?method=viewVersion&competenceCourseID=" + competenceCourseID + "&changeRequestID=" + changeRequestID %>">
					<bean:message key="label.generic.check" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				
				<bean:define id="isAllowedToEdit" value="<%= changeRequest.isLoggedPersonAllowedToEdit() ? "true" : "false" %>" />
				<logic:notPresent name="changeRequest" property="approved">
				<logic:equal name="isAllowedToEdit" value="true">
					,
					<html:link page="<%="/competenceCourses/manageVersions.do?method=revokeVersion&competenceCourseID=" + competenceCourseID + "&changeRequestID=" + changeRequestID %>">
						<bean:message key="label.revoke.proposal" bundle="BOLONHA_MANAGER_RESOURCES"/>
					</html:link>
					,
					<html:link page="<%="/competenceCourses/manageVersions.do?method=editVersion&competenceCourseID=" + competenceCourseID + "&changeRequestID=" + changeRequestID + "&proposal=y"%>">
						<bean:message key="label.edit.proposal" bundle="BOLONHA_MANAGER_RESOURCES"/>
					</html:link>
				</logic:equal>
				</logic:notPresent>

			</td>			
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="competenceCourse" property="competenceCourseInformationChangeRequests">
	<p class="mtop05"><em><bean:message key="label.no.versions.proposed"/></em></p>
</logic:empty>