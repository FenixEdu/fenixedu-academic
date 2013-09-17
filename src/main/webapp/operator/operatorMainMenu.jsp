<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

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
	
	<li class="navheader"><bean:message key="label.contacts" bundle="APPLICATION_RESOURCES"/></li>
		<li>
			<html:link page="/validate.do?method=prepare">
				<bean:message key="label.contacts.validate.address" bundle="ACADEMIC_OFFICE_RESOURCES" />
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
</ul>