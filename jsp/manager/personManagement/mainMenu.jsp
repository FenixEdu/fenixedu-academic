<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=mainPage">
		<bean:message key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message key="label.manager.personManagement"/></h2>

<p><strong>&raquo; 
	<html:link page="/findPerson.do?method=prepareFindPerson">
		<bean:message key="label.manager.findPerson" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/manageRoles.do?method=prepare">
		Gest&atilde;o de Privilégios
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/generateNewPassword.do?method=prepare&page=0">
		<bean:message key="link.operator.newPassword" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/generateNewStudentsPasswords.do?method=prepareGeneratePasswords&page=0">
		<bean:message key="link.newPasswordForStudentRegistration" />
	</html:link>
</strong></p>