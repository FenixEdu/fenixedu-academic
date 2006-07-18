<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
		<f:param value="#{bundle['label.exam']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:panelGrid styleClass="infoselected">
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: #{SOPEvaluationManagementBackingBean.executionPeriodLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: #{SOPEvaluationManagementBackingBean.executionDegreeLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: #{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.courses']}: " escape="false"/>
			<h:dataTable value="#{SOPEvaluationManagementBackingBean.associatedExecutionCourses}" var="associatedExecutionCourseID">
				<h:column>
					<h:outputText value="#{SOPEvaluationManagementBackingBean.associatedExecutionCoursesNames[associatedExecutionCourseID]}<br/> " escape="false"/>
				</h:column>
			</h:dataTable>
			<h:outputText rendered="#{empty SOPEvaluationManagementBackingBean.associatedExecutionCourses}" value="<b>#{bundleSOP['label.no.associated.curricular.courses']}</b>" escape="false"/>		
			
		</h:panelGrid>
		<h:outputText value="<br/><br/>" escape="false"/>
	
	
		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.day']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.month']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.year']}" required="true" maxlength="4" size="4" value="#{SOPEvaluationManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" required="true" maxlength="2" size="2" value="#{SOPEvaluationManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.description']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{bundle['property.exam.season']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{SOPEvaluationManagementBackingBean.description}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.season}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}">
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.seasonLabels}" />
				</h:selectOneMenu>
			</h:panelGroup>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
		<h:panelGrid columns="1">
			<h:commandLink action="associateRoomToWrittenEvaluation">
				<h:outputText value="#{bundleSOP['property.exam.associateRooms']}" escape="false"/>
				<f:param name="executionPeriodOID" value="#{SOPEvaluationManagementBackingBean.executionPeriodOID}" />
			</h:commandLink>
			<h:commandLink action="associateExecutionCourseToWrittenEvaluation">
				<h:outputText value="#{bundleSOP['property.exam.associate']}" escape="false"/>
				<f:param name="executionPeriodOID" value="#{SOPEvaluationManagementBackingBean.executionPeriodOID}" />
			</h:commandLink>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
		<h:outputFormat value="<i>#{bundleSOP['lable.associated.rooms']}:</i> <br/><b>#{SOPEvaluationManagementBackingBean.associatedRooms}</b><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
			<f:param value="#{bundle['label.written.test']}"/>
		</h:outputFormat>
		<h:outputFormat value="<i>#{bundleSOP['lable.associated.rooms']}:</i> <br/><b>#{SOPEvaluationManagementBackingBean.associatedRooms}</b><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
			<f:param value="#{bundle['label.exam']}"/>
		</h:outputFormat>

		<h:outputText value="<br/>" escape="false"/>
		<h:outputFormat value="<i>#{bundleSOP['lable.associated.curricular.courses']}:</i><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
			<f:param value="#{bundle['label.written.test']}"/>
		</h:outputFormat>
		<h:outputFormat value="<i>#{bundleSOP['lable.associated.curricular.courses']}:</i><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
			<f:param value="#{bundle['label.exam']}"/>
		</h:outputFormat>
		<h:dataTable value="#{SOPEvaluationManagementBackingBean.associatedExecutionCourses}" var="associatedExecutionCourseID">
			<h:column>
				<h:outputText value="<b>#{SOPEvaluationManagementBackingBean.associatedExecutionCoursesNames[associatedExecutionCourseID]}</b> " escape="false"/>
				<h:commandLink action="#{SOPEvaluationManagementBackingBean.disassociateExecutionCourse}">
					<h:outputText value="#{bundleSOP['property.exam.dissociate']}" escape="false"/>
					<f:param name="executionCourseToDisassociate" value="#{associatedExecutionCourseID}" />
				</h:commandLink>
				<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.curricularCourseScopesToAssociate[associatedExecutionCourseID]}" layout="pageDirection" >
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularCourseScopesSelectItems[associatedExecutionCourseID]}" />	
				</h:selectManyCheckbox>
				<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.curricularCourseContextToAssociate[associatedExecutionCourseID]}" layout="pageDirection" >
					<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularCourseContextSelectItems[associatedExecutionCourseID]}" />	
				</h:selectManyCheckbox>
			</h:column>
		</h:dataTable>
		<h:outputText rendered="#{empty SOPEvaluationManagementBackingBean.associatedExecutionCourses}" value="<b>#{bundleSOP['label.no.associated.curricular.courses']}</b><br/>" escape="false"/>		

		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{SOPEvaluationManagementBackingBean.createWrittenEvaluation}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="writtenEvaluationCalendar" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</ft:tilesView>
