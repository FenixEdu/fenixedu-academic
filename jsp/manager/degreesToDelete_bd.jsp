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
	<br>
	<h3><bean:message key="label.manager.select.degrees" /></h3>
<form name="input" action="deleteDegree.do" method="get">

		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
				</td>
				
			</tr>
				<% int index = 0; %>	 
				<logic:iterate name="<%= SessionConstants.INFO_DEGREES_LIST %>" id="degree" >
			<tr>
				<%--<bean:define id="curso"  name="curso" property="codInt"/>--%>
				
				<td class="listClasses"><input type="checkbox" property="sigla">
				</td>
				<td class="listClasses"><bean:write name="degree" property="sigla"/>
				</td>			
				<td class="listClasses"><bean:write name="degree" property="nome"/>
				</td>
				
				
			</tr>
	 			<% index++; %>	
				</logic:iterate>
	 	</table>
	 	<br>
	 <br>	
<input type="submit" value="Submit">

</form> 



</form>
	
</logic:notEmpty>	 	
</logic:present>