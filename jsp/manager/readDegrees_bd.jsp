<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="<%= SessionConstants.INFO_DEGREES_LIST %>" scope="session">
<logic:notEmpty name="<%= SessionConstants.INFO_DEGREES_LIST %>" >
	<h2><bean:message key="label.manager.degrees" /></h2>
	
	<html:link page="/degreesToDelete.do"><b><bean:message key="label.manager.delete.selected.degrees" /></b></html:link>
	<br>	
	<html:link page="/insert_degree.do"><b><bean:message key="label.manager.insert.degree" /></b></html:link>
	<br>
	<br>
 	
		<table width="70%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			<tr>
				<% int index = 0; %>	 
				<logic:iterate id="degree" name="<%= SessionConstants.INFO_DEGREES_LIST %>">
				<td class="listClasses"><html:link page="/readDegree.do" ><bean:write name="degree" property="sigla"/></html:link>
				</td>			
				<td class="listClasses"><html:link page="/readDegree.do" ><bean:write name="degree" property="nome"/></html:link>
				</td>
				<td class="listClasses"><html:link page="/deleteDegrees.do" paramId="idInternal" paramName="degree" paramProperty="idInternal" ><bean:message key="label.manager.delete.degree"/></html:link>     
				</td>
	 			<% index++; %>
	 		</tr>
	 				
				</logic:iterate>
				<td>
	 	        <span class="error"><html:errors /></span>
                </td>
	 	</table>
</table>

	 
	
	 	      
		
	
</logic:notEmpty>	 	
</logic:present>