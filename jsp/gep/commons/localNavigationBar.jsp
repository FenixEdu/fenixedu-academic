<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<ul>
	<li>
		<html:link page="/searchCoursesInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.executionCoursesInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
		<br/>
		<br/>
	</li>
	<li>
		<html:link page="/searchTeachersInformation.do?method=doBeforeSearch">
			<bean:message key="link.gep.teachersInformation"
						  bundle="GEP_RESOURCES"/>
		</html:link>
		<br/>
		<br/>
	</li>
</ul>
<%--
Devido às inscrições, as funcionalidades do portal GEP estão temporariamente desactivadas para não causar sobrecarga no servidor.
As nossas desculpas pelo incómodo.
--%>