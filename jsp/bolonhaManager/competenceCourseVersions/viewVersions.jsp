<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="competenceCourseID" value="<%= request.getParameter("competenceCourseID") %>"/>
<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

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


<h3 class="mvert15"><fr:view name="competenceCourse" property="name"/></h3>


<p class="mbottom05">
	<strong><bean:message key="label.competeceCourseInformations" bundle="BOLONHA_MANAGER_RESOURCES"/></strong>
</p>

<fr:view name="competenceCourse" property="competenceCourseInformations" schema="view.competenceCourseInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop05"/>
		<fr:property name="link(view)" value="<%= "/competenceCourses/manageVersions.do?method=showCompetenceCourseInformation&competenceCourseID=" + competenceCourseID %>"/>
		<fr:property name="param(view)" value="idInternal/oid"/>		
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
		<fr:property name="key(view)" value="label.generic.check"/>
		<fr:property name="link(propose)" value="<%= "/competenceCourses/manageVersions.do?method=prepareCreateVersion&proposal=y&competenceCourseID=" + competenceCourseID %>"/>
		<fr:property name="param(propose)" value="executionPeriod.idInternal/executionPeriodID"/>
		<fr:property name="bundle(propose)" value="BOLONHA_MANAGER_RESOURCES"/>
		<fr:property name="key(propose)" value="label.new.version.proposal"/>
		<fr:property name="visibleIfNot(propose)" value="competenceCourseInformationChangeRequestDraftAvailable"/>
		<fr:property name="sortBy" value="executionPeriod"/>
	</fr:layout>

</fr:view>


<p class="mtop15 mbottom05">
	<strong><bean:message key="label.proposals" bundle="BOLONHA_MANAGER_RESOURCES"/></strong>
</p>

<logic:notEmpty name="competenceCourse" property="competenceCourseInformationChangeRequests">
	<table class="tstyle2 thlight tdcenter mtop05">
	<tr>
		<th><bean:message key="label.year" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.semester" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationRequestedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.modificationsAnalisedBy" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th><bean:message key="label.status" bundle="BOLONHA_MANAGER_RESOURCES"/></th>
		<th></th>
	</tr>
	
	<logic:iterate id="changeRequest" name="competenceCourse" property="competenceCourseInformationChangeRequests" type="net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest">
		<bean:define id="changeRequestID" name="changeRequest" property="idInternal"/>
		<tr>
			<td><fr:view name="changeRequest" property="executionPeriod.executionYear.year"/></td>			
			<td><fr:view name="changeRequest" property="executionPeriod.name"/></td>
			<td><fr:view name="changeRequest" property="requester.name"/></td>
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
			<td class="aleft" style="color: #000;">
				<html:link page="<%= "/competenceCourses/manageVersions.do?method=viewVersion&changeRequestID=" + changeRequestID %>">
					<bean:message key="label.generic.check" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				
				<logic:notPresent name="changeRequest" property="approved">
					,
					<html:link page="<%="/competenceCourses/manageVersions.do?method=revokeVersion&competenceCourseID=" + competenceCourseID + "&changeRequestID=" + changeRequestID %>">
							<bean:message key="label.revoke.proposal" bundle="BOLONHA_MANAGER_RESOURCES"/>
						</html:link>
					,
					<html:link page="<%="/competenceCourses/manageVersions.do?method=editVersion&competenceCourseID=" + competenceCourseID + "&changeRequestID=" + changeRequestID + "&proposal=y"%>">
							<bean:message key="label.edit.proposal" bundle="BOLONHA_MANAGER_RESOURCES"/>
					</html:link>
				</logic:notPresent>

			</td>			
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="competenceCourse" property="competenceCourseInformationChangeRequests">
	<p class="mtop05"><em><bean:message key="label.no.versions.proposed"/></em></p>
</logic:empty>