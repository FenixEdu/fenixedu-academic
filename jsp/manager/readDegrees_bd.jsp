<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<logic:present name="<%= SessionConstants.INFO_DEGREES_LIST %>" scope="session">
<logic:notEmpty name="<%= SessionConstants.INFO_DEGREES_LIST %>" >
	<h2><bean:message key="label.manager.degrees" /></h2>
	<html:link page="/degreesToDelete.do"><bean:message key="label.manager.deleteDegree" /></html:link>
	<html:link page="/insert_degree"><bean:message key="label.manager.insertDegree" /></html:link>
	<br>
	<br>
		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
				<% int index = 0; %>	 
				<logic:iterate id="degree" name="<%= SessionConstants.INFO_DEGREES_LIST %>"  >
			<tr>
				<%--<bean:define id="curso"  name="curso" property="codInt"/>--%>
				<td class="listClasses"><html:link page="/readDegree.do" indexId="index" indexed="true"><bean:write name="degree" property="sigla"/></html:link>
				</td>			
				<td class="listClasses"><html:link page="/readDegree.do" indexId="index" indexed="true"><bean:write name="degree" property="nome"/></html:link>
				</td>
				<td class="listClasses"><html:link page="/deleteDegree.do" indexId="index" indexed="true"><bean:message key="label.manager.deleteDegree" /></html:link>
				</td>
				
			</tr>
	 			<% index++; %>	
				</logic:iterate>
	 	</table>
</logic:notEmpty>	 	
</logic:present>