<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.layout.two-column.contents" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="<em>#{BranchCourseGroupManagement.degreeCurricularPlan.name}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['create.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['branchCourseGroup']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{BranchCourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.parentCourseGroupID' id='parentCourseGroupID' name='parentCourseGroupID' type='hidden' value='#{BranchCourseGroupManagement.parentCourseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.toOrder}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>#{bolonhaBundle['name']} (pt): </th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" size="60" maxlength="100" value="#{BranchCourseGroupManagement.name}"/>
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
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" required="true" size="60" maxlength="100" value="#{BranchCourseGroupManagement.nameEn}"/>
			<h:outputText value=" " escape="false"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>		
		<h:outputText value="<td class='tderror1 tdclear'>" escape="false"/>
			<h:message for="nameEn" styleClass="error0"/>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		
		
		<%-- Patched for BranchType selection --%>
		<h:outputText value="<tr><th>" escape="false"/>
		<h:outputText value="#{bolonhaBundle['branchType']}: " escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
		<h:panelGroup>
			<h:selectOneMenu id="branchTypeName" required="true" value="#{BranchCourseGroupManagement.branchTypeName}">
				<f:selectItems value="#{BranchCourseGroupManagement.branchTypes}"/>
			</h:selectOneMenu>
			<h:outputText value=" " escape="false"/>
		</h:panelGroup>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<td class='tderror1 tdclear'>" escape="false"/>
			<h:message for="branchType" styleClass="error0"/>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
		



		<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}"
			action="#{BranchCourseGroupManagement.createBranchCourseGroup}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="editCurricularPlanStructure"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>