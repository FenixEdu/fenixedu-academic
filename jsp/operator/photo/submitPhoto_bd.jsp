<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<br />
<html:form action="/submitPhoto.do?method=photoUpload" enctype="multipart/form-data">
	<html:hidden property="page" value="1" />

	<h3><bean:message key="link.operator.submitPhoto" /></h3>

		<strong><bean:message key="property.login.username" /></strong>
	<br/>	
 	<html:text property="username" size="55"/>
 	<br/> 	
 	<br/>
 	<strong><bean:message key="title.loadMarks" /></strong>
 	<br/>
 	<html:file property="theFile" size="50"/>
	<br />
	<br />
	<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit>
</html:form> 
