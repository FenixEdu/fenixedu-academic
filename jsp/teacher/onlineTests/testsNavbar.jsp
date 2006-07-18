<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<bean:define id="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>

<ul>
	<li><html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="objectCode"><bean:message key="link.home"/></html:link></li>
	<li class="navheader"><bean:message key="title.tests"/></li>
	<li><html:link page="/testsManagement.do?method=prepareCreateTest" paramId="objectCode" paramName="objectCode"><bean:message key="link.createTest"/></html:link></li>
	<li><html:link page="/testsManagement.do?method=showTests" paramId="objectCode" paramName="objectCode"><bean:message key="link.showTests"/></html:link></li>
	<br/>
	<li><html:link page="/testDistribution.do?method=showDistributedTests" paramId="objectCode" paramName="objectCode"><bean:message key="link.showDistributedTests"/></html:link></li>

	<li class="navheader"><bean:message key="title.exercises"/></li>
	<li><html:link page="/exercisesManagement.do?method=chooseNewExercise" paramId="objectCode" paramName="objectCode"><bean:message key="link.createExercise"/></html:link></li>
	<li><html:link page="/exercisesManagement.do?method=insertNewExercise" paramId="objectCode" paramName="objectCode"><bean:message key="link.importExercise"/></html:link></li>
	<li><html:link page="/exercisesManagement.do?method=exercisesFirstPage" paramId="objectCode" paramName="objectCode"><bean:message key="link.showExercises"/></html:link></li>
	<br/>
	<li><html:link page="/evaluation/evaluationIndex.faces" paramId="executionCourseID" paramName="objectCode"><bean:message key="link.evaluation"/></html:link></li>
	<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="objectCode"><bean:message key="link.executionCourseAdministration"/></html:link></li>
</ul>