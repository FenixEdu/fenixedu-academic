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
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value="<h3>#{CompetenceCourseManagement.competenceCourse.name}</h3>" escape="false"/>
	<h:outputText value="#{bolonhaBundle['department']}: " style="font-weight: bold"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}" style="font-style:italic"/><br/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.unit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.unit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<br/>
	<h:form>
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['state']}: " style="font-weight: bold"/>
			<h:selectOneMenu value="#{CompetenceCourseManagement.stage}">
				<f:selectItem itemValue="DRAFT" itemLabel="#{enumerationBundle['DRAFT']}"/>
				<f:selectItem itemValue="PUBLISHED" itemLabel="#{enumerationBundle['PUBLISHED']}"/>
				<f:selectItem itemValue="APPROVED" itemLabel="#{enumerationBundle['APPROVED']}"/>
			</h:selectOneMenu>
		</h:panelGrid>
		<br/>
		<h:messages infoClass="infoMsg" errorClass="error" layout="table"/>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>				
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
			
			<h:outputText value="#{bolonhaBundle['acronym']} (en): "/>
			<h:panelGroup>
				<h:inputText id="acronym" required="true" maxlength="40" size="10" value="#{CompetenceCourseManagement.acronym}"/>
				<h:message styleClass="error" for="acronym" />
			</h:panelGroup>
			
			<h:outputText value="#{bolonhaBundle['basic']}: "/>
			<h:selectBooleanCheckbox value="#{CompetenceCourseManagement.basic}"></h:selectBooleanCheckbox>
		</h:panelGrid>
		<br/><hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['edit']}"
	 			action="#{CompetenceCourseManagement.editCompetenceCourse}"/> 
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
				action="editCompetenceCourseMainPage"/>
	</h:form>
</ft:tilesView>
