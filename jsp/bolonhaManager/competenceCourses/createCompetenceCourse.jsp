<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
.alignLeft {
	text-align: left;
}
.alignRight {
	text-align: right;
}
.backgroundColor {
	background-color: #f5f5f5;
}
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['create.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>		
	<h:outputText value="#{bolonhaBundle['step']} 1: " style="font-weight: bold"/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}" style="font-weight: bold">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>	
	<h:outputText value=" > #{bolonhaBundle['step']} 2: #{bolonhaBundle['setCompetenceCourseLoad']}"/>
	<h:outputText value=" > #{bolonhaBundle['step']} 3: #{bolonhaBundle['setData']}"/>
	<br/>
	<h:messages infoClass="infoMsg" errorClass="error" layout="table" globalOnly="true"/>
	<br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourseGroupUnit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<br/>
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input id='competenceCourseGroupUnitID' name='competenceCourseGroupUnitID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroupUnit.idInternal}'/>"/>				
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='create'/>"/>
		<h:panelGrid columnClasses="alignRight infocell,infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['name']} (pt): "/>
			<h:panelGroup>
				<h:inputText id="name" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
				<h:message styleClass="error" for="name"/>
			</h:panelGroup>		
			
			<h:outputText value="#{bolonhaBundle['nameEn']} (en): "/>
			<h:panelGroup>
				<h:inputText id="nameEn" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
				<h:message styleClass="error" for="nameEn" />
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['acronym']}: "/>
			<h:panelGroup>
				<h:inputText id="acronym" required="true" maxlength="40" size="10" value="#{CompetenceCourseManagement.acronym}"/>
				<h:message styleClass="error" for="acronym" />
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['basic']}: "/>
			<h:selectBooleanCheckbox value="#{CompetenceCourseManagement.basic}"></h:selectBooleanCheckbox>
			
			<h:outputText value="#{bolonhaBundle['regime']}: "/>
			<fc:selectOneMenu value="#{CompetenceCourseManagement.regime}">
				<f:selectItem itemValue="SEMESTER" itemLabel="#{enumerationBundle['SEMESTER']}"/>
				<f:selectItem itemValue="ANUAL" itemLabel="#{enumerationBundle['ANUAL']}"/>
			</fc:selectOneMenu>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['create']}"
	 			action="#{CompetenceCourseManagement.createCompetenceCourse}"/> 
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
				action="competenceCoursesManagement"/>
	</h:form>
</ft:tilesView>
