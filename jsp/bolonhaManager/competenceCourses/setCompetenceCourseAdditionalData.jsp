<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h2><h:outputText value="#{bolonhaBundle['createCompetenceCourse']}"/></h2> 
	
	<h:outputText value="#{bolonhaBundle['step']} 1: #{bolonhaBundle['createCompetenceCourse']} > "/>
	<h:outputText value="#{bolonhaBundle['step']} 2: #{bolonhaBundle['setData']}"  style="font-weight: bold"/>
	<br/>
	<h:outputText styleClass="error" rendered="#{!empty CompetenceCourseManagement.errorMessage}"
		value="#{bundle[CompetenceCourseManagement.errorMessage]}"/>
	<br/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.personDepartmentName}"/><br/>
	<h:outputText value="#{bolonhaBundle['scientificArea']}:" style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnit.name}"/><br/>
	<h:outputText value="#{bolonhaBundle['group']}: " style="font-weight: bold"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourseGroupUnit.name}"/><br/>
	<br/>
	<h:outputText value="#{bolonhaBundle['name']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}" /><br/>
	<h:outputText value="#{bolonhaBundle['ectsCredits']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.ectsCredits}" /><br/>
	<h:outputText value="#{bolonhaBundle['type']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle['basic']}<br/>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}<br/>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['lessonHours']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.totalLessonHours} h" /><br/>
	<h:outputText value="#{bolonhaBundle['regime']}: " style="font-weight: bold"/>
	<h:outputText value="#{bolonhaBundle[CompetenceCourseManagement.competenceCourse.regime]}" /><br/>
	<br/>
	<h:form>
		<h:outputText escape="false" value="<input id='scientificAreaUnitID' name='scientificAreaUnitID' type='hidden' value='#{CompetenceCourseManagement.scientificAreaUnit.idInternal}'" />
		<h:outputText escape="false" value="<input id='competenceCourseGroupUnitID' name='competenceCourseGroupUnitID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroupUnit.idInternal}'" />
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'" /><br/>
		
		<h:outputText value="#{bolonhaBundle['portuguese']}: " />
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['program']}: " />
			<h:inputTextarea required="true" cols="80" rows="5"/>
		</h:panelGrid>
		<br/>
		<h:outputText value="#{bolonhaBundle['english']}: " />
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['nameEn']}: " />
			<h:inputText required="true" maxlength="100" size="40"/>
	
			<h:outputText value="#{bolonhaBundle['programEn']}: " />
			<h:inputTextarea required="true" cols="80" rows="5"/>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
			action="#{CompetenceCourseManagement.setCompetenceCourseAdditionalData}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>