<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<h2><bean:message key="message.parentSection" /></h2>
<h3><bean:write name="<%=SessionConstants.INFO_SECTION%>" property="name" /></h3>
<span class="error"><html:errors property="error.default"/></span>
<html:form action="/editSection">
<table>
	<logic:present name="<%=SessionConstants.INFO_SECTION%>" property="superiorInfoSection">
		<tr>
			<td>
				<html:link page="/editSection.do?method=changeParent">
					<bean:message key="label.rootSection"/>
				</html:link>
			</td>
		</tr>	
	</logic:present>
	<tr>
		<td>
			<app:generateSectionMenu name="<%=SessionConstants.POSSIBLE_PARENT_SECTIONS%>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" renderer="sectionChooser" />
		</td>
	</tr>	
</table>
</html:form>