<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="component" property="executionCourse" />

<ul>
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
</ul>
<br/>
<br/>
<br/>
<br/>
<ul>
<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.executionCourseAdministration"/></html:link></li>
</ul>