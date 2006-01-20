<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.personManagement"/></h2>

<p><strong>&raquo; 
	<html:link module="/manager" page="/findPerson.do?method=prepareFindPerson">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.findPerson" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link module="/manager" page="/manageRoles.do?method=prepare">
		Gest&atilde;o de Privilégios
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link module="/manager" page="/generateNewPassword.do?method=prepare&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="link.operator.newPassword" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link module="/manager" page="/generateNewStudentsPasswords.do?method=prepareGeneratePasswords&page=0">
		<bean:message bundle="MANAGER_RESOURCES" key="link.newPasswordForStudentRegistration" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/functionsManagement/personSearchForFunctionsManagement.faces">
		<bean:message bundle="MANAGER_RESOURCES" key="link.functions.management"/>
	</html:link>		
</strong></p>

<!-- 
<p><strong>&raquo;
	<html:link module="/manager" page="/mergePersons.do?method=prepare">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" />
	</html:link>
</strong></p>
 -->