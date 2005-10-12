<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->

<h:outputText value="<ul><li class=\"navheader\">#{bundle['link.evaluation']}</li>" escape="false"/>

<h:form>
	<h:inputHidden binding="#{evaluationManagementMenuBackingBean.executionCourseIdHidden}" />

	<h:outputText value="<li>" escape="false"/>
	<h:outputLink value="#{evaluationManagementMenuBackingBean.contextPath}/teacher/evaluation/onlineTestsIndex.faces?executionCourseID=#{evaluationManagementMenuBackingBean.executionCourseID}">
		<h:outputText value="#{bundle['link.onlineTests']}"/>
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>

	<h:outputText value="<li>" escape="false"/>
	<h:outputLink value="#{evaluationManagementMenuBackingBean.contextPath}/teacher/evaluation/writtenTestsIndex.faces?executionCourseID=#{evaluationManagementMenuBackingBean.executionCourseID}">
		<h:outputText value="#{bundle['link.writtenTests']}"/>
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>

	<h:outputText value="<li>" escape="false"/>
	<h:outputLink value="#{evaluationManagementMenuBackingBean.contextPath}/teacher/evaluation/examsIndex.faces?executionCourseID=#{evaluationManagementMenuBackingBean.executionCourseID}">
		<h:outputText value="#{bundle['link.exams']}"/>		
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>

	<h:outputText value="<li>" escape="false"/>
	<h:outputLink value="#{evaluationManagementMenuBackingBean.contextPath}/teacher/evaluation/finalEvaluationIndex.faces?executionCourseID=#{evaluationManagementMenuBackingBean.executionCourseID}">
		<h:outputText value="#{bundle['link.finalEvaluation']}"/>		
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>

	<h:outputText value="<br/><li>" escape="false"/>
	<h:outputLink value="#{evaluationManagementMenuBackingBean.contextPath}/teacher/teacherAdministrationViewer.do?method=instructions&objectCode=#{evaluationManagementMenuBackingBean.executionCourseID}">
		<h:outputText value="#{bundle['link.manage.executionCourse']}"/>		
	</h:outputLink>
	<h:outputText value="</li></ul>" escape="false"/>

</h:form>
