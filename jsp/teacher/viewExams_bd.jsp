<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<br />
<table width="80%">
<tr>
	<td class="listClasses-header"><bean:message key="label.season" /></td>
	<td class="listClasses-header"><bean:message key="label.day" /></td>
	<td class="listClasses-header"><bean:message key="label.beginning" /></td>
	<td class="listClasses-header"><bean:message key="label.end" /></td>		
</tr>	
<logic:present name="exams">
	<logic:iterate id="exam" name="exams" >
<tr>
	<td class="listClasses"><bean:write name="exam" property="season" /></td>	
	<td class="listClasses"><bean:write name="exam" property="date" /></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour" /></td>
	<td class="listClasses"><bean:write name="exam" property="endHour" /></td>
</tr>
	</logic:iterate>
</logic:present>
</table>