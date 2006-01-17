<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.realName}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="#{bolonhaBundle['area']}: " style="font-weight: bold"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourseGroupUnit.name}<br/>" escape="false"/>
	</fc:dataRepeater>
	<h:messages infoClass="infoMsg" errorClass="error" layout="table" globalOnly="true"/>
	
	<h:outputText value="<div class='simpleblock4'> " escape="false"/>
	<h:outputFormat value="<h4 class='first'>#{bolonhaBundle['edit.param']}</h4>" escape="false">
		<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
	</h:outputFormat>	
	<h:form>
		<h:outputText escape="false" value="<input id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['state']}: </label>" escape="false"/>
		<h:selectOneMenu value="#{CompetenceCourseManagement.stage}">
			<f:selectItem itemValue="DRAFT" itemLabel="#{enumerationBundle['DRAFT']}"/>
			<f:selectItem itemValue="PUBLISHED" itemLabel="#{enumerationBundle['PUBLISHED']}"/>
			<f:selectItem itemValue="APPROVED" itemLabel="#{enumerationBundle['APPROVED']}"/>
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label><span class='required'>*</span> #{bolonhaBundle['name']} (pt): </label>" escape="false"/>
		<h:inputText id="name" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.name}"/>
		<h:message styleClass="error0" for="name"/>
		<h:outputText value="</p>" escape="false"/>
				
		<h:outputText value="<p><label><span class='required'>*</span> #{bolonhaBundle['nameEn']} (en): </label>" escape="false"/>
		<h:inputText id="nameEn" required="true" maxlength="100" size="40" value="#{CompetenceCourseManagement.nameEn}"/>
		<h:message styleClass="error0" for="nameEn"/>
		<h:outputText value="</p>" escape="false"/>	
		
		<h:outputText value="<p><label><span class='required'>*</span> #{bolonhaBundle['acronym']}: </label>" escape="false"/>
		<h:inputText id="acronym" required="true" maxlength="40" size="10" value="#{CompetenceCourseManagement.acronym}"/>
		<h:message styleClass="error0" for="acronym" />
		<h:outputText value="</p>" escape="false"/>	
		
		<h:outputText value="<p><label>#{bolonhaBundle['basic']}: </label>" escape="false"/>
		<h:selectBooleanCheckbox value="#{CompetenceCourseManagement.basic}"></h:selectBooleanCheckbox>
		<h:outputText value="</p>" escape="false"/>	
				
		<h:outputText value="<label class='lempty'>.</label>#{bolonhaBundle['markedFields']} #{bolonhaBundle['with']} <span class='required'>*</span> #{bolonhaBundle['areRequiredFields']}</p>" escape="false"/>
		<h:outputText value="<p><label class='lempty'>.</label>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['edit']}"
	 			action="#{CompetenceCourseManagement.editCompetenceCourse}"/> 
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
				action="editCompetenceCourseMainPage"/>
		<h:outputText value="</p></fieldset>" escape="false"/>	
	</h:form>
	<h:outputText value="</div>" escape="false"/>	
</ft:tilesView>
