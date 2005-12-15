<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
<!--
.alignRight {
	text-align: right;
}
-->
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<h:form>
		<h2><h:outputText value="#{bolonhaBundle['delete']} #{bolonhaBundle['competenceCourse']}"/></h2>
		<h3><h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}" style="font-weight: bold"/></h3>
		<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
			<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
		<h:outputText value="#{bolonhaBundle['scientificArea']}:" style="font-weight: bold"/>
			<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnit.name}"/><br/>
		<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourseGroupUnit.name}"/><br/>
		<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CompetenceCourseManagement.errorMessage]}<br/>" escape="false"/>
		<br/>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'"/><br/>
		<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" styleClass="error"/>
		<br/><br/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CompetenceCourseManagement.deleteCompetenceCourse}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>