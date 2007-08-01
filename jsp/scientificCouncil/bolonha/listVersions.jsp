<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.teaching" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="label.list.versions" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/competenceCourses/manageVersions.do?method=prepare"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>
<logic:notEmpty name="changeRequests">
	<table class="tstyle2 thlight">
	<tr>
		<th><bean:message key="label.year" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.semester" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.competenceCourse" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationRequestedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationsAnalisedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.status" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th></th>
	</tr>
	
	<logic:iterate id="changeRequest" name="changeRequests" type="net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest">
		<bean:define id="changeRequestID" name="changeRequest" property="idInternal"/>
		<bean:define id="competenceCourseID" name="changeRequest" property="competenceCourse.idInternal"/>
		<tr>
			<td><fr:view name="changeRequest" property="executionPeriod.executionYear.year"/></td>			
			<td><fr:view name="changeRequest" property="executionPeriod.name"/></td>
			<td><fr:view name="changeRequest" property="competenceCourse.name"/></td>
			<td><fr:view name="changeRequest" property="requester.name"/></td>
			<td class="acenter">
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

			<td>			
			<html:link page="<%= "/competenceCourses/manageVersions.do?method=viewVersion&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
				<bean:message key="label.generic.check" bundle="APPLICATION_RESOURCES"/>
			</html:link>
			
			<logic:notPresent name="changeRequest" property="approved">
			,
			<html:link page="<%= "/competenceCourses/manageVersions.do?method=approveRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
				<bean:message key="label.approve" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</html:link>
			,
			<html:link page="<%= "/competenceCourses/manageVersions.do?method=rejectRequest&changeRequestID=" + changeRequestID + "&departmentID=" + request.getParameter("departmentID") %>">
				<bean:message key="label.reject" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</html:link>
			</logic:notPresent>		
			</td>			
		</tr>


	</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="changeRequests">
	<p>
		<em><bean:message key="label.no.request.for.department" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>