<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="<%= SessionConstants.INFO_DEGREES_LIST %>" scope="session">
<logic:notEmpty name="<%= SessionConstants.INFO_DEGREES_LIST %>" >
	<h2><bean:message key="label.manager.degrees" /></h2>
		
	<html:link page="/insertDegree.do"><b><bean:message key="label.manager.insert.degree" /></b></html:link>
	<br>
	<br>
 	     
 	    <html:form action="/deleteDegrees" method="get">
		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
				</td>
			</tr>
			<tr>
				<% int index = 0; %>	 
				<logic:iterate id="degree" name="<%= SessionConstants.INFO_DEGREES_LIST %>">
				
				<td class="listClasses">
				<html:multibox property="internalIds">
					<bean:write name="degree" property="idInternal"/>
				</html:multibox>
				</td>	
				<td class="listClasses"><html:link page="/readDegree.do" paramId="idInternal" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="sigla"/></html:link>
				</td>			
				<td class="listClasses"><html:link page="/readDegree.do" paramId="idInternal" paramName="degree" paramProperty="idInternal"><bean:write name="degree" property="nome"/></html:link>
				</td>
				<% index++; %>
	 		</tr>
	 				
				</logic:iterate>
			<span class="error"><html:errors /></span>
				
				
	 	</table>
	<br>
	 <br>	
	<html:submit styleClass="inputbutton"><bean:message key="label.manager.delete.selected.degrees"/></html:submit>

</html:form> 
</logic:notEmpty>	 	
</logic:present>