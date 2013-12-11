<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
		<h:outputText value="<em>#{bolonhaBundle['competenceCourse']}</em>" escape="false"/>
		<h:outputText value="<h2>#{bolonhaBundle['delete']}: #{CompetenceCourseManagement.competenceCourse.name} " style="font-weight: bold" escape="false"/>
		<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}" value="(#{CompetenceCourseManagement.competenceCourse.acronym})" style="font-weight: bold" escape="false"/>
		<h:outputText value="</h2>" style="font-weight: bold" escape="false"/>		
	<h:form>

		<h:outputText value="<p class='mtop15'><span class='bold'>#{bolonhaBundle['department']}: </span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</p>" escape="false"/>

		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
			<h:outputText value="<p><span class='bold'>#{bolonhaBundle['area']}: </span>" escape="false"/>
			<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</p>" escape="false"/>
		</fc:dataRepeater>		

		<h:outputText value="<p class='mtop15'><span class='bold'>#{bolonhaBundle['name']} (pt):</span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</p>" escape="false"/>
		<h:outputText value="<p><span class='bold'>#{bolonhaBundle['nameEn']} (en):</span>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</p>" escape="false"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}">
			<h:outputText value="<p><span class='bold'>#{bolonhaBundle['acronym']}:</span>" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</p>" escape="false"/>	
		</h:panelGroup>
		
		<h:messages infoClass="success0" errorClass="error0" layout="table"/>
		
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>

		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['confirmDeleteMessage']}" styleClass="warning0" escape="false"/>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p>" escape="false"/>	
			<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" styleClass="inputbutton" value="#{bolonhaBundle['yes']}" action="#{CompetenceCourseManagement.deleteCompetenceCourse}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}" action="competenceCoursesManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>