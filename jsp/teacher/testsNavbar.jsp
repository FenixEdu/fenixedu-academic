<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="component" property="executionCourse" />

<p class="invisible"><strong><bean:message key="title.tests"/></strong></p>
<ul>
<li><html:link page="/testsManagement.do?method=prepareCreateTest" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.createTest"/></html:link></li>
<li><html:link page="/testsManagement.do?method=showTests" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showTests"/></html:link></li>
<br/>
<li><html:link page="/testDistribution.do?method=showDistributedTests" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showDistributedTests"/></html:link></li>
</ul>
<br/>
<p class="invisible"><strong><bean:message key="title.exercices"/></strong></p>
<ul>
<li><html:link page="/exercicesManagement.do?method=insertNewExercice" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.importExercice"/></html:link></li>
<li><html:link page="/exercicesManagement.do?method=exercicesFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showExercices"/></html:link></li>
</ul>
<br/>
<br/>
<br/>
<ul>
<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.executionCourseAdministration"/></html:link></li>
</ul>
