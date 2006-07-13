<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" width="100" height="100"/>
</center>

<ul>
	<li>
	<html:link module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
	</li>
	<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.personManagement"/></li>
	<li>
	<html:link module="/manager" page="/personManagement/createPerson.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.createPerson" />
	</html:link>
	</li>
	<li>
	<html:link module="/manager" page="/findPerson.do?method=prepareFindPerson">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" />
	</html:link>
	</li>
	<li>
	<html:link module="/manager" page="/manageRoles.do?method=prepare">
		Gest&atilde;o de Privilégios
	</html:link>
	</li>
	<li>
	<html:link module="/manager" page="/generateNewPassword.do?method=prepare&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="link.operator.newPassword" />
	</html:link>
	</li>
	<li>
	<html:link module="/manager" page="/generateNewStudentsPasswords.do?method=prepareGeneratePasswords&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="link.newPasswordForStudentRegistration" />
	</html:link>
	</li>
	<li>
	<html:link page="/functionsManagement/personSearchForFunctionsManagement.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="link.functions.management"/>
	</html:link>
	</li>
	<li>
	<html:link page="/recoverInactivePerson.do?method=prepare&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="link.recover.inactive.person"/>
	</html:link>	
	</li>
<%-- 
	<li>
	<html:link module="/manager" page="/mergePersons.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" />
	</html:link>
	</li>
--%>
</ul>






 
