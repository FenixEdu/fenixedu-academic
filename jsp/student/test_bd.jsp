<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<html:form action="showStudentGroupInfo.do" method="get">
Numero:	    <html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username"/><br>
Password:	<html:text bundle="HTMLALT_RESOURCES" altKey="text.password" property="password"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="OK"/>
</html:form>