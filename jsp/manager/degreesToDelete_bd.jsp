<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<logic:present name="<%= SessionConstants.INFO_DEGREES_LIST %>" scope="session">
<logic:notEmpty name="<%= SessionConstants.INFO_DEGREES_LIST %>" >
	<h2><bean:message key="label.manager.degrees" /></h2>
	<br>
	<h3><bean:message key="label.manager.select.degrees" /></h3>

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
				
					 
				<logic:iterate name="<%= SessionConstants.INFO_DEGREES_LIST %>" id="degree" >
			<tr>
				<td class="listClasses">
					<html:multibox property="internalIds">
						<bean:write name="degree" property="idInternal"/>
					</html:multibox>
				</td>	
				<td class="listClasses"><bean:write name="degree" property="sigla"/>
				</td>			
				<td class="listClasses"><bean:write name="degree" property="nome"/>
				</td>
				
			</tr>
				
	 			
				</logic:iterate>
	 	</table>
	 
	 	<br>
	 <br>	
<input type="submit" value="Submit">

</html:form> 

</logic:notEmpty>	 	
</logic:present>