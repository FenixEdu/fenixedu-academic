<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<ul>
<li><html:link page="/viewSite.do"><bean:message key="link.home"/></html:link></li>
<br />
<br />
<li><html:link page="/alternativeSite.do?method=management"><bean:message key="link.personalizationOptions"/></html:link></li>
<li><html:link  page="/accessAnnouncementManagementAction.do"><bean:message key="link.announcements"/></html:link></li>
<li><html:link page="/objectivesManagerDA.do?method=acessObjectives"><bean:message key="link.objectives"/></html:link></li>
<li><html:link page="/programManagerDA.do?method=acessProgram"><bean:message key="link.program"/></html:link></li>
<li><html:link page="/viewEvaluation.do?method=viewEvaluation"><bean:message key="link.evaluation"/></html:link></li>
<li><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference&page=0"><bean:message key="link.bibliography"/></html:link></li>
<li><html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship"><bean:message key="link.teachers"/></html:link></li>
<li><html:link page="/prepareInsertSection.do?method=prepareInsertRootSection"><bean:message key="link.createRootSection"/></html:link></li>
</ul>
<ul>
<logic:present name="<%= SessionConstants.SECTIONS %>" >
	<logic:present name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" renderer="teacher" />
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" renderer="teacher" />
	</logic:notPresent>		
</logic:present>
<br />
<br />
<li><html:link forward="logoff"><bean:message key="link.logout"/></html:link></li>
</ul>