<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="executionCourse" name="component" property="executionCourse" />

<br/>
<br/>
<ul>
<li><html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.home"/></html:link></li>
</ul>
<p class="invisible"><strong><bean:message key="title.tests"/></strong></p>
<ul>
<li><html:link page="/testsManagement.do?method=prepareCreateTest" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.createTest"/></html:link></li>
<li><html:link page="/testsManagement.do?method=showTests" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showTests"/></html:link></li>
<br/>
<li><html:link page="/testDistribution.do?method=showDistributedTests" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showDistributedTests"/></html:link></li>
</ul>
<br/>
<p class="invisible"><strong><bean:message key="title.exercises"/></strong></p>
<ul>
<li><html:link page="/exercisesManagement.do?method=insertNewExercise" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.importExercise"/></html:link></li>
<li><html:link page="/exercisesManagement.do?method=exercisesFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.showExercises"/></html:link></li>
</ul>
<br/>
<br/>
<br/>
<ul>
<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.executionCourseAdministration"/></html:link></li>
</ul>
