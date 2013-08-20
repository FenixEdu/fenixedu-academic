<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{scouncilBundle['competenceCourse']}</em>" escape="false" />
	<h:outputText value="<h2>#{CompetenceCourseManagement.name}</h2>" escape="false"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop15'>" escape="false"/>
	
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'/>"/>
		
		<h:outputText value="<p>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.executionSemesterID}"
			onchange="submit()">
			<f:selectItems binding="#{CompetenceCourseManagement.executionSemesterItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<p/>" escape="false"/>
	
		<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.departmentRealName}</li>" escape="false"/>
		<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnitName} > #{CompetenceCourseManagement.competenceCourseGroupUnitName}</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText value="#{scouncilBundle['transfer.to']}:<p>" escape="false"/>
		
		
		
		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToDepartmentUnitID}"
			onchange="submit()"
			valueChangeListener="#{CompetenceCourseManagement.onChangeDepartmentUnit}">
			<f:selectItems binding="#{CompetenceCourseManagement.departmentUnitItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="<p/><p>" escape="false"/>
		
		
		
  		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToScientificAreaUnitID}" 
			onchange="submit()"
			valueChangeListener="#{CompetenceCourseManagement.onChangeScientificAreaUnit}">
			<f:selectItems binding="#{CompetenceCourseManagement.scientificAreaUnitItems}"/>
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="<p/><p>" escape="false"/>
		
		
		
		
		<fc:selectOneMenu value="#{CompetenceCourseManagement.transferToCompetenceCourseGroupUnitID}">
			<f:selectItems binding="#{CompetenceCourseManagement.competenceCourseGroupUnitItems}"/>
		</fc:selectOneMenu>
		
		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.transfer']}" styleClass="inputbutton" action="#{CompetenceCourseManagement.transferCompetenceCourse}" value="#{scouncilBundle['transfer']}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{scouncilBundle['back']}" />
		<h:outputText value="<p/>" escape="false"/>
	</h:form>
</ft:tilesView>
