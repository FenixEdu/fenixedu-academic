<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ul>
	<li>
		<html:link page="/back.do">
			<bean:message key="label.return" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
		
	<li class="navheader">Gestão de Pessoas</li>
		<li>
			<html:link page="/findPerson.do?method=prepareFindPerson">
				<bean:message key="label.manager.findPerson" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/recoverInactivePerson.do?method=prepare&page=0">
				<bean:message key="link.recover.inactive.person" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/editPerson.do?method=prepareSearchPersonToEdit">
				<bean:message key="edit.person.title" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/manageRoles.do?method=prepare">
				<bean:message key="label.manager.privilegesManagement" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/generateNewPassword.do?method=prepare&page=0">
				<bean:message key="link.operator.newPassword" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link module="/manager" page="/functionsManagement/personSearchForFunctionsManagement.faces">
				<bean:message key="link.functions.management" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
    
	<li class="navheader">Pessoas Externas-Convidadas</li>
    	<li>
			<html:link page="/createInvitedPerson.do?method=prepareSearchExistentPersonBeforeCreateNewInvitedPerson">
				<bean:message key="link.create.external.person.invited" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link page="/invitationsManagement.do?method=prepareSearchPersonForManageInvitations">
				<bean:message key="invitations.management.title" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>

</ul>