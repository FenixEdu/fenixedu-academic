<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.realName}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['create.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>

	<h:outputText value="<p class='breadcumbs'><span class='actual'><strong>#{bolonhaBundle['step']} 1: </strong>" escape="false"/>
	<h:outputFormat value="#{bolonhaBundle['create.param']}">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<h:outputText value="</span>" escape="false"/>
	<h:outputText value=" > " escape="false"/>
	<h:outputText value="<span><strong>#{bolonhaBundle['step']} 2:</strong> #{bolonhaBundle['setCompetenceCourseLoad']}</span>" escape="false"/>
	<h:outputText value=" > " escape="false"/>
	<h:outputText value="<span><strong>#{bolonhaBundle['step']} 3:</strong> #{bolonhaBundle['setCompetenceCourseAdditionalInformation']}</span></p>" escape="false"/>	
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<p><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} &gt; #{CompetenceCourseManagement.competenceCourseGroupUnit.name}</p>" escape="false"/>
	</fc:dataRepeater>
	
	
	<h:outputText value="<div class='simpleblock4'> " escape="false"/>
	<h:outputFormat value="<h4 class='first'>#{bolonhaBundle['create.param']}</h4>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>	
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseGroupUnitID' id='competenceCourseGroupUnitID' name='competenceCourseGroupUnitID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseGroupUnit.idInternal}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='create'/>"/>

		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt): </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
		<h:message styleClass="error0" for="name"/>
		<h:outputText value="</p>" escape="false"/>
				
		<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en): </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
		<h:message styleClass="error0" for="nameEn"/>
		<h:outputText value="</p>" escape="false"/>	
		
<%-- 		<h:outputText value="<p><label>#{bolonhaBundle['acronym']}: </label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" id="acronym" required="true" maxlength="10" size="9" value="#{CompetenceCourseManagement.acronym}"/>
		<h:message styleClass="error0" for="acronym" />
		<h:outputText value="</p>" escape="false"/>
--%>		
		<h:outputText value="<p><label>#{bolonhaBundle['competenceCourseLevel']}: </label>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.competenceCourseLevel}">
			<f:selectItem itemValue="" itemLabel="#{enumerationBundle['dropDown.level.Default']}"/>
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

		<h:outputText value="</p></fieldset>" escape="false"/>	
		<h:outputText value="</div>" escape="false"/>	
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}"
	 		action="#{CompetenceCourseManagement.createCompetenceCourse}"/> 
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="competenceCoursesManagement"/>
	</h:form>

</ft:tilesView>
