<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<%
	net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex.setCoordinatorContext(request);
%>
<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex" />
<jsp:include page="/coordinator/context.jsp" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

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

				<h:outputText value="#{bundle['property.curricularYear']}: " escape="false" />
				<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.curricularYearID}" onchange="this.form.submit()">
					<f:selectItems value="#{CoordinatorEvaluationsBackingBean.curricularYearSelectItems}"/>
				</h:selectOneMenu>

				<h:outputText value="#{bundle['label.evaluation.type']}: "/>
				<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.evaluationType}" onchange="this.form.submit()">
					<f:selectItem itemLabel="#{bundle['message.all']}" itemValue=""/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.writtenTest']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.exam']}" itemValue="net.sourceforge.fenixedu.domain.Exam"/>
				</h:selectOneMenu>
			</h:panelGrid>
		<h:outputText value="</div>" escape="false"/>


	<h:outputText value="<br/>" escape="false" />
	<h:outputText value="<strong>#{bundle['label.coordinator.instructions']}</strong>" escape="false" />
	<h:outputText value="<ul><li>#{bundle['label.coordinator.instruction1']}</li>" escape="false" />
	<h:outputText value="<li>#{bundle['label.coordinator.instruction2']}</li>" escape="false" />
	<h:outputText value="</ul>" escape="false" />
	<h:outputText value="<br/>" escape="false" />

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
					<c:url var="createEvaluationURL" value="#{facesContext.externalContext.requestContextPath}/coordinator/evaluation/createEvaluation.faces">
						<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
						<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
						<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
						<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.externalId}"/>
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
								<c:url var="editEvaluationURL" value="#{facesContext.externalContext.requestContextPath}/coordinator/evaluation/editEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.externalId}"/>
									<c:param name="description" value="${evaluation.description}"/>
									<c:param name="date" value="${date}"/>
									<c:param name="beginTime" value="${begin}"/>
									<c:param name="endTime" value="${end}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="#{facesContext.externalContext.requestContextPath}/coordinator/evaluation/deleteEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.externalId}"/>
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
								<c:url var="editEvaluationURL" value="#{facesContext.externalContext.requestContextPath}/coordinator/evaluation/editEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.externalId}"/>
									<c:param name="name" value="${evaluation.name}"/>
									<c:param name="begin" value="${begin}"/>
									<c:param name="end" value="${end}"/>
									<c:param name="description" value="${evaluation.description}"/>
								</c:url>
								<a href='<c:out value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="#{facesContext.externalContext.requestContextPath}/coordinator/evaluation/deleteEvaluation.faces">
									<c:param name="degreeCurricularPlanID" value="${CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}"/>
									<c:param name="executionPeriodID" value="${CoordinatorEvaluationsBackingBean.executionPeriodID}"/>
									<c:param name="curricularYearID" value="${CoordinatorEvaluationsBackingBean.curricularYearID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationType" value="${evaluation.class.name}"/>
									<c:param name="executionCourseID" value="${executionCourseEvaluationEntry.key.externalId}"/>
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

</f:view>