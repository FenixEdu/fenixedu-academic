<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exams" name="component" property="infoExams" />
<table align="center">
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></th>
	<td class="listClasses-header" ><bean:message key="label.day"/></th>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></th>
	<td class="listClasses-header" ><bean:message key="label.end"/></th>
	
</tr>	
	<logic:iterate id="exam" name="exams">
		
	<tr>
	<td class="listClasses"><bean:write name="exam" property="season"/></td>		
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
	<td class="listClasses"><bean:write name="exam" property="endHour"/></td>
	</tr>
</logic:iterate>
</table>