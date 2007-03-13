<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.realName}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourseGroupUnit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:outputText value="<div class='simpleblock4'> " escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['state']}: </label>" escape="false"/>
		<h:selectOneMenu value="#{CompetenceCourseManagement.stage}">
			<f:selectItem itemValue="DRAFT" itemLabel="#{enumerationBundle['DRAFT']}"/>
			<f:selectItem itemValue="PUBLISHED" itemLabel="#{enumerationBundle['PUBLISHED']}"/>
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label class='lempty'>.</label>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt): </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
		<h:message styleClass="error0" for="name"/>
		<h:outputText value="</p>" escape="false"/>
				
		<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en): </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
		<h:message styleClass="error0" for="nameEn"/>
		<h:outputText value="</p>" escape="false"/>	
		
		<h:outputText value="<p><label>#{bolonhaBundle['acronym']}: </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" id="acronym" disabled="true" required="true" maxlength="40" size="10" value="#{CompetenceCourseManagement.acronym}"/>
		<h:message styleClass="error0" for="acronym" />
		<h:outputText value="</p>" escape="false"/>	
		
		<h:outputText value="<p><label>#{bolonhaBundle['competenceCourseLevel']}: </label>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.competenceCourseLevel}">
			<f:selectItem itemValue="" itemLabel="#{enumerationBundle['dropDown.Default']}"/>
			<f:selectItem itemValue="FIRST_CYCLE" itemLabel="#{enumerationBundle['FIRST_CYCLE']}"/>
			<f:selectItem itemValue="SECOND_CYCLE" itemLabel="#{enumerationBundle['SECOND_CYCLE']}"/>
			<f:selectItem itemValue="FORMATION" itemLabel="#{enumerationBundle['FORMATION']}"/>
			<f:selectItem itemValue="DOCTORATE" itemLabel="#{enumerationBundle['DOCTORATE']}"/>
			<f:selectItem itemValue="SPECIALIZATION" itemLabel="#{enumerationBundle['SPECIALIZATION']}"/>
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
        <h:outputText value="<p><label>#{bolonhaBundle['competenceCourseType']}: </label>" escape="false"/>
        <fc:selectOneMenu value="#{CompetenceCourseManagement.competenceCourseType}">
            <f:selectItem itemValue="" itemLabel="#{enumerationBundle['dropDown.Default']}"/>
            <f:selectItem itemValue="REGULAR" itemLabel="#{enumerationBundle['REGULAR']}"/>
            <f:selectItem itemValue="DISSERTATION" itemLabel="#{enumerationBundle['DISSERTATION']}"/>
        </fc:selectOneMenu>
        <h:outputText value="</p>" escape="false"/>
        
		<h:outputText value="<p><label>#{bolonhaBundle['basic']}: </label>" escape="false"/>
		<h:selectBooleanCheckbox value="#{CompetenceCourseManagement.basic}"></h:selectBooleanCheckbox>
		<h:outputText value="</p>" escape="false"/>	

		<h:outputText value="</div>" escape="false"/>	

		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{bolonhaBundle['save']}"
	 			action="#{CompetenceCourseManagement.editCompetenceCourse}"/> 
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
				action="editCompetenceCourseMainPage"/>
		<h:outputText value="</p></fieldset>" escape="false"/>	
	</h:form>

</ft:tilesView>
