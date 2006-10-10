<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.welcome"/>		
		</td>
	</tr>
</table>