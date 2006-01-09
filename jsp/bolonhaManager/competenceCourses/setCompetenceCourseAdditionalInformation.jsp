<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
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
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/>
	<h:outputText value="<h2>#{bolonhaBundle['setData']}</h2>" escape="false"/>	
	<h:outputText value="#{bolonhaBundle['step']} 1: "/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value=" > #{bolonhaBundle['step']} 2: #{bolonhaBundle['setCompetenceCourseLoad']}"/>
	<h:outputText value=" > "/>
	<h:outputText value="#{bolonhaBundle['step']} 3: #{bolonhaBundle['setData']}" style="font-weight: bold"/>
	<br/>
	<h:messages infoClass="infoMsg" errorClass="error" layout="table"/>
	<br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<br/>
	<h:outputText value="#{bolonhaBundle['name']} (pt): " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}"/><br/>
	<h:outputText value="#{bolonhaBundle['nameEn']} (en): " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}" /><br/>
	<h:outputText value="#{bolonhaBundle['acronym']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}" /><br/>	
	<h:outputText value="#{bolonhaBundle['type']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle['basic']}<br/>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}<br/>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/><br/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		
		<h:outputText value="#{bolonhaBundle['portuguese']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" >
			<h:outputText value="<a name='objectives'>#{bolonhaBundle['objectives']}: </a>" escape="false"/>
			<h:inputTextarea id="generalObjectives" cols="80" rows="5" value="#{CompetenceCourseManagement.objectives}"/>
			
			<h:outputText value="<a name='program'>#{bolonhaBundle['program']}: </a>" escape="false"/>
			<h:inputTextarea id="program" cols="80" rows="5" value="#{CompetenceCourseManagement.program}"/>
			
			<h:outputText value="<a name='evaluationMethod'>#{bolonhaBundle['evaluationMethod']}: </a>" escape="false"/>
			<h:inputTextarea id="evaluationMethod" cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethod}"/>			
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['english']}: " />
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0" >
			<h:outputText value="<a name='objectivesEn'>#{bolonhaBundle['objectivesEn']}: </a>" escape="false"/>
			<h:inputTextarea id="generalObjectivesEn" cols="80" rows="5" value="#{CompetenceCourseManagement.objectivesEn}"/>
			
			<h:outputText value="<a name='programEn'>#{bolonhaBundle['programEn']}: </a>" escape="false"/>
			<h:inputTextarea id="programEn" cols="80" rows="5" value="#{CompetenceCourseManagement.programEn}"/>
			
			<h:outputText value="<a name='evaluationMethodEn'>#{bolonhaBundle['evaluationMethodEn']}: </a>" escape="false"/>
			<h:inputTextarea id="evaluationMethodEn" cols="80" rows="5" value="#{CompetenceCourseManagement.evaluationMethodEn}"/>		
		</h:panelGrid>
		<br/><hr>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
				action="#{CompetenceCourseManagement.createCompetenceCourseAdditionalInformation}"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['edit']}"
				action="#{CompetenceCourseManagement.editCompetenceCourseAdditionalInformation}"/>
		</h:panelGroup>		
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="competenceCoursesManagement"/>			
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="editCompetenceCourseMainPage"/>			
		</h:panelGroup>
	</h:form>
</ft:tilesView>