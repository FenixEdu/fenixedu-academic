<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
	<html:link page="/viewSite.do">
	<li><bean:message key="link.home"/></li>
</html:link>
<html:link page="/alternativeSite.do?method=management">
	<li>	<bean:message key="link.personalizationOptions"/></li>
	</html:link>
<html:link  page="/accessAnnouncementManagementAction.do">
	<li><bean:message key="link.announcements"/></li>
</html:link><html:link page="/objectivesManagerDA.do?method=acessObjectives">
	<li><bean:message key="link.objectives"/></li>
</html:link>
<html:link page="/programManagerDA.do?method=acessProgram">
	<li><bean:message key="link.program"/></li>
</html:link>
<html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference&page=0">
	<li><bean:message key="link.bibliography"/></li>
</html:link>
	<html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship">
	<li><bean:message key="link.teachers"/></li>
</html:link>
	<html:link page="/logoff.do">
	<li><bean:message key="link.logout"/></li>
</html:link>
<logic:present name="<%= SessionConstants.SECTIONS %>" >
	<logic:present name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" />
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
	</logic:notPresent>		
</logic:present>	
