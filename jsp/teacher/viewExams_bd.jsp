<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<table align="center">
<tr>
	<th><bean:message key="label.season"/></th>
	<th><bean:message key="label.day"/></th>
	<th><bean:message key="label.beginning"/></th>
	<th><bean:message key="label.end"/></th>		
</tr>	
<logic:present name="exams">

	<logic:iterate id="exam" name="exams" >
		
	<tr>
	<td><bean:write name="exam" property="season"/></td>	
	<td><bean:write name="exam" property="date"/></td>
	<td><bean:write name="exam" property="beginningHour"/></td>
	<td><bean:write name="exam" property="endHour"/></td>
	
	</tr>
</logic:iterate>
</logic:present>
</table>