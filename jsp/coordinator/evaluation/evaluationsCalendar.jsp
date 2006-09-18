<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

<style type="text/css">
table.style2u {
	margin-bottom: 1em;
	border: 2px solid #ccc;
	border-collapse: collapse;
	margin-top: 0.5em;
}
table.style2u th {
	padding: 0.2em 1em;
	border-bottom: 2px solid #ccc;
	background-color: #ddd;
	border-left: 1px solid #ccc;
	text-align: center;
}
table.style2u td.header {
	padding: 0.2em 0.5em;
	border: 1px solid #ddd;
	background-color: #eee;
	font-weight: bold;
	text-align: left;
	margin-top: 1em;
}
table.style2u td {
	padding: 0.2em 0.5em;
	background-color: #fafafa;
	border: none;
	border: 1px solid #ddd;
	text-align: center;
}
table.style2u td.courses {
	border-right: 1px solid #ddd;
	background-color: #ffe;
	padding: 0 1em;
}
table.style2u tr.space {
	padding: 1em;
}
table.style2u tr.space td {
	padding: 0.5em;
	background: none;
	border: none;
}
table.executionCoursesWithoutWrittenEvaluations {
	margin-bottom: 1em;
	border: 2px solid #ccc;
	border-collapse: collapse;
	margin-top: 0.5em;
}
table.executionCoursesWithoutWrittenEvaluations td {
	padding: 0.2em 0.5em;
	background-color: #fafafa;
	border: none;
	border: 1px solid #ddd;
	text-align: left;
}
</style>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:outputText value="<h2>#{bundle['link.evaluations.calendar']}</h2>" escape="false"/>

	<h:form>
		<h:outputText value="<div class='infoop' style='width: 30em'>" escape="false" />
			<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'/>"/>
			<h:panelGrid columns="2">
				<h:outputText value="#{bundle['label.coordinator.selectedExecutionPeriod']}:" styleClass="bold"/>
				<h:outputText value=""/>

				<h:outputText value="#{bundle['property.executionPeriod']}: " escape="false"/>
				<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.executionPeriodID}" onchange="this.form.submit()">
					<f:selectItems value="#{CoordinatorEvaluationsBackingBean.executionPeriodSelectItems}"/>
				</h:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>

				<h:outputText value="#{bundle['property.curricularYear']}: " escape="false" />
				<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.curricularYearID}" onchange="this.form.submit()">
					<f:selectItems value="#{CoordinatorEvaluationsBackingBean.curricularYearSelectItems}"/>
				</h:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>

				<h:outputText value="#{bundle['label.evaluation.type']}: "/>
				<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.evaluationType}" onchange="this.form.submit()">
					<f:selectItem itemLabel="#{bundle['message.all']}" itemValue=""/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.writtenTest']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.exam']}" itemValue="net.sourceforge.fenixedu.domain.Exam"/>
				</h:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			</h:panelGrid>
		<h:outputText value="</div>" escape="false"/>


	<h:outputText value="<br/>" escape="false" />
	<h:outputText value="<strong>#{bundle['label.coordinator.instructions']}</strong>" escape="false" />
	<h:outputText value="<ul><li>#{bundle['label.coordinator.instruction1']}</li>" escape="false" />
	<h:outputText value="<li>#{bundle['label.coordinator.instruction2']}</li>" escape="false" />
	<h:outputText value="</ul>" escape="false" />
	<h:outputText value="<br/>" escape="false" />
	<style>@import url(<%= request.getContextPath() %>/CSS/dotist_calendars.css);</style>	

	<fc:fenixCalendar 
			begin="#{CoordinatorEvaluationsBackingBean.calendarBegin}"
			end="#{CoordinatorEvaluationsBackingBean.calendarEnd}"
			createLink="createEvaluation.faces?degreeCurricularPlanID=#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}&executionPeriodID=#{CoordinatorEvaluationsBackingBean.executionPeriodID}&curricularYearID=#{CoordinatorEvaluationsBackingBean.curricularYearID}&evaluationType=#{CoordinatorEvaluationsBackingBean.evaluationType}"
			editLinkPage="editEvaluation.faces"
			editLinkParameters="#{CoordinatorEvaluationsBackingBean.calendarLinks}"
			/>

	</h:form>		

	<h:outputText value="<br/><br/>" escape="false" styleClass="bold"/>
	<f:verbatim>
		<table class="style2u" width="80%">
			<tr>			
				<th><c:out value="${bundle['title.evaluation']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.begin']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.end']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.attending.students.count']}" escapeXml="false"/></th>
				<th style="width: 9em;"></th>
			</tr>			
			<c:forEach items="${CoordinatorEvaluationsBackingBean.executionCoursesMap}" var="executionCourseEvaluationEntry">
				<tr><td colspan="5" class="header">
					<c:out value="${executionCourseEvaluationEntry.key.sigla} - ${executionCourseEvaluationEntry.key.nome}" />
					<c:out value=" | "/>
					<c:url var="createEvaluationURL" value="createEvaluation.faces">
						<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
						<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
						<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
						<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.idInternal}"/>
					</c:url>
					<a href='<c:out value="${createEvaluationURL}"/>'>
						<c:out value="${bundle['label.create.evaluation']}"/>
					</a>
				</td></tr>
				<c:forEach items="${executionCourseEvaluationEntry.value}" var="evaluation">
					<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
						<tr>
							<td><c:out value="${evaluation.description}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/> <fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/> <fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/></td>
							<td><c:out value="${executionCourseEvaluationEntry.key.attendsCount}"/></td>
							<td>
								<fmt:formatDate var="date" pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/>
								<fmt:formatDate var="begin" pattern="dd/MM/yyyy" value="${evaluation.beginningDate}"/>
								<fmt:formatDate var="end" pattern="dd/MM/yyyy" value="${evaluation.endDate}"/>
								<c:url var="editEvaluationURL" value="editEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.idInternal}"/>
									<c:param name="description" value="${evaluation.description}"/>
									<c:param name="date" value="${date}"/>
									<c:param name="beginTime" value="${begin}"/>
									<c:param name="endTime" value="${end}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="deleteEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.idInternal}"/>
									<c:param name="description" value="${evaluation.description}"/>
									<c:param name="date" value="${date}"/>
									<c:param name="beginTime" value="${begin}"/>
									<c:param name="endTime" value="${end}"/>
								</c:url>
								<a href='<c:out value="${deleteEvaluationURL}"/>'>
									<c:out value="${bundle['label.delete']}"/>
								</a>
							</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:forEach items="${executionCourseEvaluationEntry.value}" var="evaluation">
					<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.Project'}">
						<tr>
							<td><c:out value="${evaluation.name}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.begin}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.end}"/></td>
							<td><c:out value="${executionCourseEvaluationEntry.key.attendsCount}"/></td>
							<td>
								<fmt:formatDate var="begin" pattern="dd/MM/yyyy HH:mm" value="${evaluation.begin}"/>
								<fmt:formatDate var="end" pattern="dd/MM/yyyy HH:mm" value="${evaluation.end}"/>
								<c:url var="editEvaluationURL" value="editEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.idInternal}"/>
									<c:param name="name" value="${evaluation.name}"/>
									<c:param name="begin" value="${begin}"/>
									<c:param name="end" value="${end}"/>
									<c:param name="description" value="${evaluation.description}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="deleteEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.idInternal}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.idInternal}"/>
									<c:param name="name" value="${evaluation.name}"/>
									<c:param name="begin" value="${begin}"/>
									<c:param name="end" value="${end}"/>
									<c:param name="description" value="${evaluation.description}"/>
								</c:url>
								<a href='<c:out value="${deleteEvaluationURL}"/>'>
									<c:out value="${bundle['label.delete']}"/>
								</a>
							</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:forEach items="${executionCourseEvaluationEntry.value}" var="evaluation">
					<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
						<tr>
							<td><c:out value="${evaluation.season}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/> <fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/> <fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/></td>
							<td><c:out value="${executionCourseEvaluationEntry.key.attendsCount}"/></td>
							<td>
								<fmt:formatDate var="date" pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/>
								<fmt:formatDate var="begin" pattern="dd/MM/yyyy" value="${evaluation.beginningDate}"/>
								<fmt:formatDate var="end" pattern="dd/MM/yyyy" value="${evaluation.endDate}"/>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>			
		</table>
	</f:verbatim>

</ft:tilesView>