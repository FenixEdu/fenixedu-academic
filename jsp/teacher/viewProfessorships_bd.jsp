<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<logic:present name="<%= SessionConstants.INFO_SITES_LIST %>" scope="session">
	<h2><bean:message key="label.professorships" /></h2>
		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</td>
				<td class="listClasses-header"><bean:message key="label.professorships.name" />
				</td>
			</tr>
				<% int index = 0; %>	 
				<logic:iterate name="<%= SessionConstants.INFO_SITES_LIST %>" id="site" >
			<tr>
					<bean:define id="executionCourse" name="site" property="infoExecutionCourse"/>
				<td class="listClasses"><html:link page="/viewSite.do" indexId="index" indexed="true"><bean:write name="executionCourse" property="sigla"/></html:link>
				</td>			
				<td class="listClasses"><html:link page="/viewSite.do" indexId="index" indexed="true"><bean:write name="executionCourse" property="nome"/></html:link>
				</td>
			</tr>
	 			<% index++; %>	
				</logic:iterate>
	 	</table>
</logic:present>
