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

	<h:outputFormat value="<h2>#{bundle['list.students']}</h2/><hr>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='sortBy' name='sortBy' type='hidden' value='#{CoordinatorStudentsBackingBean.sortBy}'"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
			<h:outputText value="#{bundle['label.name']}: " />
			<h:outputText value="#{bundle['label.name']}: " />

			<h:outputText value="#{bundle['label.name']}: " />
			<h:outputText value="#{bundle['label.name']}: " />
		</h:panelGrid>
	</h:form>

	<h:dataTable value="#{CoordinatorStudentsBackingBean.studentCurricularPlans}" var="studentCurricularPlan" cellpadding="0"
			headerClass="listClasses-header" columnClasses="listClasses">
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.number"/>
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
					<f:param name="sortBy" value="numberOfApprovedCurricularCourses"/>
					<h:outputText value="#{bundle['label.number.approved.curricular.courses']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.numberOfApprovedCurricularCourses}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputLink>
					<f:param name="degreeCurricularPlanID" value="#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<f:param name="sortBy" value="student.approvedEnrollmentsNumber"/>
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
					<h:outputText value="#{bundle['label.average']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.student.arithmeticMean}"/>
		</h:column>
	</h:dataTable>

</ft:tilesView>