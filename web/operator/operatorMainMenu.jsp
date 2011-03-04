<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link href="https://ciist.ist.utl.pt/ciistadmin/admin" target="_blank"><bean:message key="link.operator.newPassword" /></html:link>
	</li>
	<li>
		<html:link page="/generateUserUID.do?method=prepareSearchPerson">
			<bean:message key="generate.userUID.title" bundle="MANAGER_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/submitPhoto.do?method=preparePhotoUpload&page=0"><bean:message key="link.operator.submitPhoto" /></html:link>
	</li>
    <li>
        <html:link page="/pendingPhotos.do?method=prepare">
            <bean:message key="link.operator.photo.pending" bundle="MANAGER_RESOURCES"/>
        </html:link>
    </li>
	<li>
		<html:link page="/generatePasswordsForCandidacies.do?method=prepareChooseExecutionDegree">
			<bean:message key="link.operator.candidacy.passwords" />
		</html:link>
	</li>
	
	<li class="navheader"><bean:message key="ALUMNI" bundle="MANAGER_RESOURCES"/></li>
		<li>
			<html:link page="/alumni.do?method=prepareIdentityRequestsList">
				<bean:message key="alumni.identity.requests" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
	
	<li class="navheader"><bean:message key="label.system.management" bundle="MANAGER_RESOURCES"/></li>
		<li>
			<html:link page="/monitorSystem.do?method=monitor">
				<bean:message key="title.system.information" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/loginsManagement.do?method=prepareSearchPerson">
				<bean:message key="logins.management.title" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
		
	<li class="navheader"><bean:message key="link.manager.equivalencies" bundle="MANAGER_RESOURCES"/></li>
		<!--<li>
			<html:link page="/showNotNeedToEnroll.do?method=prepare">
				<bean:message key="link.manager.notNeedToEnrol" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>-->
		<li>
			<html:link page="/curricularCourseEquivalencies.do?method=prepare">
				<bean:message key="title.equivalencies" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
	
	<li class="navheader"><bean:message key="title.people" bundle="MANAGER_RESOURCES"/></li>
		<li>
			<html:link page="/studentsManagement.do?method=show">
				<bean:message key="link.manager.studentsManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/personManagement.do?method=firstPage">
				<bean:message key="label.manager.personManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/teachersManagement.do?method=firstPage">
				<bean:message key="label.manager.teachersManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/manageHolidays.do?method=prepare&page=0">
				<bean:message key="label.manage.holidays" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/viewPersonsWithRole.do?method=prepare">
				<bean:message key="title.manage.roles" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		
	<li class="navheader"><bean:message key="title.executions" bundle="MANAGER_RESOURCES"/></li>
		<li> 
			<html:link page="/manageExecutionPeriods.do?method=prepare">
				<bean:message key="title.execution.periods" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/executionCourseManagement.do?method=firstPage">
				<bean:message key="label.manager.executionCourseManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<!--<li>
			<html:link page="/executionDegreesManagementMainPage.do">
				<bean:message key="label.manager.executionDegreeManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>-->
		<li>
			<html:link page="/manageEnrolementPeriods.do?method=prepare">
				<bean:message key="title.manage.enrolement.period" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
</ul>