<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>

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
	<li>
		<html:link page="<%="/competenceCourses/manageVersions.do?method=prepareCreateVersion&competenceCourseID=" + competenceCourseID  %>">
			<bean:message key="label.create.version" bundle="BOLONHA_MANAGER_RESOURCES"/>
		</html:link>			
	</li>
</ul>


<h3 class="mvert15"><span><fr:view name="competenceCourse" property="name"/></span></h3>


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
		<th><bean:message key="label.semester" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationRequestedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationsAnalisedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.status" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.department" bundle="APPLICATION_RESOURCES"/></th>
		<th></th>
	</tr>
	
	<logic:iterate id="changeRequest" name="competenceCourse" property="competenceCourseInformationChangeRequests" type="net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest">
		<bean:define id="changeRequestID" name="changeRequest" property="externalId"/>
		<tr>
			<td><fr:view name="changeRequest" property="executionPeriod.executionYear.year"/></td>			
			<td><fr:view name="changeRequest" property="executionPeriod.name"/></td>
			<td><fr:view name="changeRequest" property="requester" type="net.sourceforge.fenixedu.domain.Person">
					<fr:layout name="null-as-label">
						<fr:property name="label" value="-"/>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="showNickName"/>
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<fr:view name="changeRequest" property="analizedBy" type="net.sourceforge.fenixedu.domain.Person">
					<fr:layout name="null-as-label">
						<fr:property name="label" value="-"/>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="showNickName"/>
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