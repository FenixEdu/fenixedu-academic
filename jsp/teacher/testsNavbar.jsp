<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="component" property="executionCourse" />
<ul>
<li><html:link page="/testsManagement.do?method=prepareCreateTest" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.createTest"/></html:link></li>
<li><html:link page="/testsManagement.do?method=showTests" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showTests"/></html:link></li>
</ul>
<br/>
<ul>
<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.executionCourseAdministration"/></html:link></li>
</ul>
