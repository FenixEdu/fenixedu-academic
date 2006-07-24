<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>

	<h:outputText value="<h2>#{bundleSOP['property.exam.associate']}</h2><br/>" escape="false" />
	
	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText escape="false" value="<input alt='input.year' id='year' name='year' type='hidden' value='#{SOPEvaluationManagementBackingBean.year}'/>"/>
		<h:outputText escape="false" value="<input alt='input.month' id='month' name='month' type='hidden' value='#{SOPEvaluationManagementBackingBean.month}'/>"/>
		<h:outputText escape="false" value="<input alt='input.day' id='day' name='day' type='hidden' value='#{SOPEvaluationManagementBackingBean.day}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginHour' id='beginHour' name='beginHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginMinute' id='beginMinute' name='beginMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginMinute}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endHour' id='endHour' name='endHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.endHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endMinute' id='endMinute' name='endMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.endMinute}'/>"/>

		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
<%-- 		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
--%>
		<h:panelGrid columns="2" styleClass="infotable">
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionDegreeChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionDegreeID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>

			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedCurricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onCurricularYearChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.curricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCurricularYearID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID4' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>

			<h:outputText value="#{bundleSOP['lable.execution.course']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionCourseID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionCourseChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesItems}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID5' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionCourseID}" 
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionCourseID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesLabels}" />
			</fc:selectOneMenu> 
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID6' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>			
		</h:panelGrid>
		
		

		<h:outputText value="<br/><br/>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" action="#{SOPEvaluationManagementBackingBean.associateExecutionCourse}" value="#{bundleSOP['button.associate']}" styleClass="inputbutton"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" value="#{bundleSOP['button.cancel']}" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>
