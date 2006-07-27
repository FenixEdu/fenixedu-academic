<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:outputText value="<h2>#{bundle['label.teacherExpectationDefinitionPeriodManagement.title']}</h2>" escape="false" style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;" />
	
	<h:form>
	
		<fc:viewState binding="#{teacherExpectationDefinitionPeriodManagement.viewState}"/>
		<h:inputHidden value="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriodID}" />
	
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['label.common.chooseExecutionYear']}:" />
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{teacherExpectationDefinitionPeriodManagement.selectedExecutionYearID}" onchange="this.form.submit();" valueChangeListener="#{teacherExpectationDefinitionPeriodManagement.onExecutionYearChange}">
				<f:selectItems value="#{teacherExpectationDefinitionPeriodManagement.executionYears}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />
				
		<h:panelGrid columns="1" rendered="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriod == null && teacherExpectationDefinitionPeriodManagement.selectedExecutionYearID != null}">
			<h:outputText value="#{bundle['label.teacherExpectationDefinitionPeriodManagement.noPeriodDefined']}" />
			<fc:commandLink value="#{bundle['link.teacherExpectationDefinitionPeriodManagement.define']}" action="create"/>
		</h:panelGrid>
				
		<h:panelGrid columns="2" styleClass="infoop" rendered="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriod != null}">
			<h:outputText value="<strong>#{bundle['label.teacherExpectationDefinitionPeriodManagement.startDate']}&nbsp;<i>(#{bundle['label.teacherExpectationDefinitionPeriodManagement.dateInstructions']})</i>:</strong>"  escape="false"/>
			<h:outputFormat value="{0, date, dd / MM / yyyy}<br/>" escape="false">
				<f:param value="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriod.startDate}"/>
			</h:outputFormat>
			<h:outputText value="<strong>#{bundle['label.teacherExpectationDefinitionPeriodManagement.endDate']}&nbsp;<i>(#{bundle['label.teacherExpectationDefinitionPeriodManagement.dateInstructions']})</i>:</strong>" escape="false" />
			<h:outputFormat value="{0, date, dd / MM / yyyy}<br/>" escape="false">
				<f:param value="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriod.endDate}"/>
			</h:outputFormat>
		</h:panelGrid>

		
		<h:outputText value="<br/>" escape="false"/>

		<h:panelGrid columns="1" rendered="#{teacherExpectationDefinitionPeriodManagement.teacherExpectationDefinitionPeriod != null}">
			<fc:commandLink value="#{bundle['link.edit']}" action="#{teacherExpectationDefinitionPeriodManagement.preparePeriodForEdit}" />
		</h:panelGrid>
		
	</h:form>

</ft:tilesView>