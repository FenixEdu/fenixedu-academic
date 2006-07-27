<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="EUREKA" escape="false"/>

	<h:outputFormat value="<h2>#{bundleSOP['link.writtenEvaluation.map']}</h2>" escape="false">
		<f:param value="#{bundleSOP['label.writtenTests']}" />
	</h:outputFormat>

	<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
		value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
	<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

	<h:form>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>

		<h:panelGrid columns="2" styleClass="infotable">
			<h:outputText value="<b><i>#{bundleSOP['title.selected.degree']}:</b></i>" escape="false"/>
			<h:outputText value="&nbsp;" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: " />
	 		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.chosenExecutionPeriodID}" 
	 						onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.enableDropDowns}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionPeriods}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
			<h:selectOneMenu id="chosenExecutionDegreeID" value="#{SOPEvaluationManagementBackingBean.chosenExecutionDegreeID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValue}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
			<h:selectOneMenu id="chosenCurricularYearID" value="#{SOPEvaluationManagementBackingBean.chosenCurricularYearID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValue}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.change']}" styleClass="inputbutton" action="#{SOPEvaluationManagementBackingBean.continueToCalendar}" value="#{bundle['button.change']}" />
	</h:form>

</ft:tilesView>
