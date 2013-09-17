<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="<em>#{CourseGroupManagement.degreeCurricularPlan.name}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['create.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['courseGroup']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.parentCourseGroupID' id='parentCourseGroupID' name='parentCourseGroupID' type='hidden' value='#{CourseGroupManagement.parentCourseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>#{bolonhaBundle['name']} (pt): </th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" size="60" maxlength="100" value="#{CourseGroupManagement.name}"/>
			<h:outputText value=" " escape="false"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<td class='tderror1 tdclear'>" escape="false"/>
			<h:message for="name" styleClass="error0"/>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr><th>" escape="false"/>
		<h:outputText value="#{bolonhaBundle['name']} (en): " escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" required="true" size="60" maxlength="100" value="#{CourseGroupManagement.nameEn}"/>
			<h:outputText value=" " escape="false"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<td class='tderror1 tdclear'>" escape="false"/>
			<h:message for="nameEn" styleClass="error0"/>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		



		<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}"
			action="#{CourseGroupManagement.createCourseGroup}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="editCurricularPlanStructure"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>