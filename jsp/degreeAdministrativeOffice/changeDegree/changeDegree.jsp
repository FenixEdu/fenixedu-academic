<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.degreeAdministrativeOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/DegreeAdministrativeOfficeResources" var="bundle"/>
	<h:form>
		<h:outputText value="#{bundle['label.student.number']}"/>:
		<h:inputText value="#{changeDegree.studentNumber}" onchange="this.form.submit();"/>
	</h:form>

	<br />

	<c:if test="${changeDegree.studentNumber != null}">
		<br/>
		<h:outputText value="#{changeDegree.infoStudent.infoPerson.nome}"/>
		<br />
		<h:outputText value="#{bundle['label.student.curricular.plan']}"/>
		<br />
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['label.student.curricular.plan.state']}"/>
			<h:outputText value="#{changeDegree.activeInfoStudentCurricularPlan.currentState}" />
			<h:outputText value="#{bundle['label.degree.name']}"/>
			<h:outputText value="#{changeDegree.activeInfoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome}" />
		</h:panelGrid>
		<br/>
		<h:dataTable value="#{changeDegree.enrolments}" var="enrolment">
			<h:column>
				<h:panelGrid columns="6">
					<h:outputText value="#{enrolment.infoExecutionPeriod.infoExecutionYear.year}" />
					<h:outputText value="#{enrolment.infoExecutionPeriod.semester}" />
					<h:outputText value="#{enrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla}" />
					<h:outputText value="#{enrolment.infoCurricularCourse.code}" />
					<h:outputText value="#{enrolment.infoCurricularCourse.name}" />
					<h:outputText value="#{enrolment.infoEnrolmentEvaluation.grade}" />
				</h:panelGrid>
			</h:column>
		</h:dataTable>
	</c:if>

</ft:tilesView>