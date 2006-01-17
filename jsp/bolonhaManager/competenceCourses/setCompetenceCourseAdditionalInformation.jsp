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
	
	<h:outputText value="<p class='breadcumbs'><span>#{bolonhaBundle['step']} 1: </strong>" escape="false"/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}</span>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value=" > "/>
	<h:outputText value="<span><strong>#{bolonhaBundle['step']} 2:</strong> #{bolonhaBundle['setCompetenceCourseLoad']}</span>" escape="false"/>
	<h:outputText value=" > <span  class='actual'><strong>#{bolonhaBundle['step']} 3:</strong> #{bolonhaBundle['setData']}</span>" escape="false"/>
	<h:outputText value="</p>" escape="false"/>
	<h:messages infoClass="infoMsg" errorClass="error" layout="table"/>
	
	<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['name']} (pt): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['nameEn']} (en): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</li>" escape="false" />
	<h:outputText value="<li><strong>#{bolonhaBundle['acronym']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</li>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['type']}: </strong>" escape="false"/>
	<h:outputText value="#{bolonhaBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>	
	<h:form>				
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<a name='portuguese'><h4 class='first'>#{bolonhaBundle['portuguese']}: </h4></a><fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['objectives']}: </label>" escape="false"/>
		<h:inputTextarea id="generalObjectives" cols="80" rows="5" value="#{CompetenceCourseManagement.objectives}"/>
		<h:outputText value="</p>" escape="false"/>			
		<h:outputText value="<p><label>#{bolonhaBundle['program']}: </label>" escape="false"/>
		<h:inputTextarea id="program" cols="80" rows="5" value="#{CompetenceCourseManagement.program}"/>			
		<h:outputText value="</p></fieldset></div>" escape="false"/>
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<a name='english'><h4 class='first'>#{bolonhaBundle['english']}: </h4></a><fieldset class='lfloat'>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['objectivesEn']}: </a></label>" escape="false"/>
		<h:inputTextarea id="generalObjectivesEn" cols="80" rows="5" value="#{CompetenceCourseManagement.objectivesEn}"/>
		<h:outputText value="</p>" escape="false"/>			
		<h:outputText value="<p><label>#{bolonhaBundle['programEn']}: </label>" escape="false"/>
		<h:inputTextarea id="programEn" cols="80" rows="5" value="#{CompetenceCourseManagement.programEn}"/>			
		<h:outputText value="</p></fieldset></div>" escape="false"/>
		
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>		
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