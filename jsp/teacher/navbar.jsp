<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="component" property="executionCourse" />
<ul>
<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.home"/></html:link></li>
<br />
<br />
<li><html:link page="/alternativeSite.do?method=prepareCustomizationOptions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.personalizationOptions"/></html:link></li>
<li><html:link page="/announcementManagementAction.do?method=showAnnouncements" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.announcements"/></html:link></li>
<li><html:link page="/objectivesManagerDA.do?method=viewObjectives" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.objectives"/></html:link></li>
<li><html:link page="/programManagerDA.do?method=viewProgram" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.program"/></html:link></li>
<li><html:link page="/viewEvaluation.do?method=viewEvaluation" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluation"/></html:link></li>
<li><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.bibliography"/></html:link></li>
<li><html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.teachers"/></html:link></li>
<li><html:link page="/viewExams.do" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.exams"/></html:link></li>
<li><html:link page="/sectionManagement.do?method=prepareCreateRootSection" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.createRootSection"/></html:link></li>
</ul>
<ul>
<bean:define id="sections" name="component" property="sections"/>
<logic:notEmpty name="sections" >
	<logic:present name="infoSection" >
	<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection" renderer="teacher"/>
	</logic:present>
	<logic:notPresent name="infoSection" >
	<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" renderer="teacher"/>
	</logic:notPresent>		
</logic:notEmpty>		
<br />
<br />
<li><html:link forward="logoff"><bean:message key="link.logout"/></html:link></li>
</ul>