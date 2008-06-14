<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="INTERNATIONAL_RELATION_OFFICE">
	<ul>
		<li class="navheader"><bean:message key="link.student" bundle="INTERNATIONAL_RELATION_OFFICE"/></li>	
		<li><html:link page="/students.do?method=prepareSearch"><bean:message key="link.studentOperations.viewStudents" bundle="INTERNATIONAL_RELATION_OFFICE"/></html:link></li> 
	</ul>

</logic:present>