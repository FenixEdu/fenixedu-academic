<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<h2><bean:message key="message.parentSection" /></h2>
<span class="error"><html:errors property="error.default"/></span>
<html:form action="/editSection">
<table>
	<tr>
		<td>
			<app:generateSectionMenu name="ALL_SECTIONS" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" renderer="sectionChooser" />
		</td>
	</tr>	
</table>
</html:form>