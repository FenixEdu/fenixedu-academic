<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<ul>
	<li>
		<html:link page="/studentCourseReport.do?method=edit">
			<bean:message key="link.delegate.studentCourseReport"/>
		</html:link>
		<br/>
		<br/>
	</li>
</ul>
