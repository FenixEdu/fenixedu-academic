<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	

<logic:present name="<%= SessionConstants.INFO_SITES_LIST %>" scope="session">
	<h3><bean:message key="label.professorships" />	</h3>
		<table width="100%" cellpadding="0" border="0">
	 	
	<% int index = 0; %>	 

	<logic:iterate name="<%= SessionConstants.INFO_SITES_LIST %>" id="site" >
		<tr>
		<bean:define id="executionCourse" name="site" property="infoExecutionCourse"/>
				<td><html:link page="/viewSite.do" indexId="index" indexed="true"><bean:write name="executionCourse" property="sigla"/></html:link></td>			
		<td><html:link page="/viewSite.do" indexId="index" indexed="true"><bean:write name="executionCourse" property="nome"/></html:link></td>
	
		
		</tr>
	 <% index++; %>	
	</logic:iterate>
	 </table>
</logic:present>
