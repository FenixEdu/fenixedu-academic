<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="ALUMNI">
	<ul>
	  	<li>
	  		<html:link page="/viewCurriculum.do?method=getStudentCP" >
	  			<bean:message key="link.student.curriculum"/>
	  		</html:link>
	  	</li>
	</ul>	
</logic:present>
