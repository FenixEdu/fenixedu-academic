<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.welcome"/>		
		</td>
	</tr>
</table>