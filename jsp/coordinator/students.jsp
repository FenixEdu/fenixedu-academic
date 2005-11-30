<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
.alignright {
text-align: right;
}
.valigntop {
vertical-align: top;
}
</style>
<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="bundleEnum"/>

	<h:outputFormat value="<h2>#{bundle['list.students']}</h2/><hr>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='sortBy' name='sortBy' type='hidden' value='#{CoordinatorStudentsBackingBean.sortBy}'"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
			<h:outputText value="#{bundle['label.student.curricular.plan.state']}: " />
			<h:selectOneMenu value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}">
				<f:selectItem itemLabel="#{bundle['message.all']}" itemValue=""/>
				<f:selectItem itemLabel="#{bundleEnum['ACTIVE']}" itemValue="ACTIVE"/>
				<f:selectItem itemLabel="#{bundleEnum['CONCLUDED']}" itemValue="CONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['INCOMPLETE']}" itemValue="INCOMPLETE"/>
				<f:selectItem itemLabel="#{bundleEnum['SCHOOLPARTCONCLUDED']}" itemValue="SCHOOLPARTCONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['INACTIVE']}" itemValue="INACTIVE"/>
				<f:selectItem itemLabel="#{bundleEnum['PAST']}" itemValue="PAST"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.average.lower.bound']}: " />
			<h:inputText id="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}" />

			<h:outputText value="#{bundle['label.average.upper.bound']}: " />
			<h:inputText id="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}" />

			<h:outputText value="#{bundle['label.number.approved.lower.bound']}: " />
			<h:inputText id="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}" />

			<h:outputText value="#{bundle['label.number.approved.upper.bound']}: " />
			<h:inputText id="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}" />

			<h:outputText value="#{bundle['label.student.number.lower.bound']}: " />
			<h:inputText id="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}" />

			<h:outputText value="#{bundle['label.student.number.upper.bound']}: " />
			<h:inputText id="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}" />
		</h:panelGrid>

		<h:commandButton styleClass="inputbutton" value="#{bundle['button.search']}"/>
	</h:form>

	<h:outputText value="<br/><br/>#{bundle['label.number.results']}: " escape="false"/>
	<h:outputText value="#{CoordinatorStudentsBackingBean.numberResults}"/>

	<h:dataTable value="#{CoordinatorStudentsBackingBean.indexes}" var="index">
		<h:column>
			<h:outputLink>
				<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
				<f:param name="sortBy" value="student.number"/>
				<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
				<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
				<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
				<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
				<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
				<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
				<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
				<f:param name="minIndex" value="#{index}"/>
				<h:outputText value="#{index}" />
			</h:outputLink>
		</h:column>
	</h:dataTable>

	<h:dataTable value="#{CoordinatorStudentsBackingBean.studentCurricularPlans}" var="studentCurricularPlan" cellpadding="0"
			headerClass="listClasses-header" columnClasses="listClasses">
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.number"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.number']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewCurriculum.do?method=getStudentCP&degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&studentNumber=#{studentCurricularPlan.student.number}'>" escape="false"/>
				<h:outputText value="#{studentCurricularPlan.student.number}"/>
			<h:outputText value="</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.person.nome"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.name']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewCurriculum.do?method=getStudentCP&degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&studentNumber=#{studentCurricularPlan.student.number}'>" escape="false"/>
				<h:outputText value="#{studentCurricularPlan.student.person.nome}"/>
			<h:outputText value="</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.person.email"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.email']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="<a href='mailto:#{studentCurricularPlan.student.person.email}'>" escape="false"/>
			<h:outputText value="#{studentCurricularPlan.student.person.email}</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.person.email"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.student.curricular.plan.state']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="<a href='mailto:#{studentCurricularPlan.currentState}'>" escape="false"/>
			<h:outputText value="#{bundleEnum[studentCurricularPlan.currentState]}</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.approvedEnrollmentsNumber"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.number.approved.curricular.courses']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.student.approvedEnrollmentsNumber}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.arithmeticMean"/>
					<f:param name="studentCurricularPlanStateString" value="#{CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<f:param name="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}"/>
					<f:param name="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}"/>
					<f:param name="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<f:param name="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<f:param name="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<f:param name="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<h:outputText value="#{bundle['label.average']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.student.arithmeticMean}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="#{bundle['label.person.photo']}" />
			</f:facet>
			<h:form>
				<h:graphicImage url="#{CoordinatorStudentsBackingBean.contextPath}/person/viewPhoto.do?personCode=#{studentCurricularPlan.student.person.idInternal}"/>
			</h:form>
		</h:column>
	</h:dataTable>

</ft:tilesView>