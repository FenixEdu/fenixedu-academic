<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<html:form action="/searchInquiriesResultPage.do">
	<html:hidden property="method" value="prepare"/>
	<html:select property="executionSemesterID" onchange="this.form.method.value='selectExecutionSemester';this.form.submit();">
		<html:option value=""></html:option>
 		<html:options collection="executionSemesters" property="idInternal" labelProperty="qualifiedName"/>
	</html:select>
	<br/>
	<html:select property="executionDegreeID" onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
		<html:option value=""></html:option>
 		<html:options collection="executionDegrees" property="idInternal" labelProperty="presentationName"/>
	</html:select>
	<br/>
	<html:select property="executionCourseID" onchange="this.form.method.value='selectExecutionCourse';this.form.submit();">
		<html:option value=""></html:option>
 		<html:options collection="executionCourses" property="idInternal" labelProperty="nome"/>
	</html:select>	
</html:form>

<c:if test="${not empty executionCourse}">
	<br/>
	<bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${executionCourse.site.reversePath}" /></bean:define>
	<!-- NO_CHECKSUM --><!-- HAS_CONTEXT --><a href="<%= executionCourseLink %>" target="_blank">Página de Resultados</a>
</c:if>