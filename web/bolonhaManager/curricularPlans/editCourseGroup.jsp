<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CourseGroupManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['courseGroup']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.courseGroupID' id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{CourseGroupManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>

			<h:outputText value="<p>#{bolonhaBundle['name']} (pt):" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" size="60" maxlength="100" value="#{CourseGroupManagement.name}"/>
				<h:message for="name" styleClass="error0"/>
			</h:panelGroup>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['name']} (en):" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" required="true" size="60" maxlength="100" value="#{CourseGroupManagement.nameEn}"/>
				<h:message for="nameEn" styleClass="error0"/>
			</h:panelGroup>			
			<h:outputText value="</p>" escape="false"/>
			
			
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{bolonhaBundle['save']}"
			action="#{CourseGroupManagement.editCourseGroup}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="editCurricularPlanStructure"/>	
		<h:outputText value="</p>" escape="false"/>	
	</h:form>
</ft:tilesView>