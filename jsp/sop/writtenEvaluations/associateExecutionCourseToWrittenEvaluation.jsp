<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResourcesSOP" var="bundleSOP"/>

	<h:outputText value="<h2>#{bundleSOP['property.exam.associate']}</h2><br/>" escape="false" />
	
	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		<h:outputText escape="false" value="<input id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'"/>
		
		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

		<h:panelGrid columns="2" styleClass="infotable">
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionDegreeChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionDegreeID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
--%>

			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedCurricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onCurricularYearChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}"/>
			</fc:selectOneMenu>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.curricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCurricularYearID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}" />
			</fc:selectOneMenu>
--%>

			<h:outputText value="#{bundleSOP['lable.execution.course']}: " />
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionCourseID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionCourseChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesItems}"/>
			</fc:selectOneMenu>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionCourseID}" 
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionCourseID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesLabels}" />
			</fc:selectOneMenu> 
--%>			
		</h:panelGrid>
		
		

		<h:outputText value="<br/><br/>" escape="false" />
		<h:commandButton action="#{SOPEvaluationManagementBackingBean.associateExecutionCourse}" value="#{bundleSOP['button.associate']}" styleClass="inputbutton"/>
		<h:commandButton action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" value="#{bundleSOP['button.cancel']}" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>
