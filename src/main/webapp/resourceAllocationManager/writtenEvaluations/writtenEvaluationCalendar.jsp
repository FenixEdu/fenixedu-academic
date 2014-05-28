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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams.MainExamsDA" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundleSOP['link.writtenEvaluation.map']}</h2>" escape="false"/>

	<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
		value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
	<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedBegin}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedEnd}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText value="<p class='mbottom05'>#{bundleSOP['title.selected.degree']}:</p>" escape="false"/>

		<h:outputText value="<table class='tstyle5 thlight thright thmiddle mtop05 mbottom2'>" escape="false"/>			
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.academicInterval']}: " />
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
	 	<h:selectOneMenu disabled="true" value="#{SOPEvaluationManagementBackingBean.academicInterval}" 
	 						onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.enableDropDowns}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.academicIntervals}" />
			</h:selectOneMenu>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:selectOneMenu id="executionDegreeID" value="#{SOPEvaluationManagementBackingBean.executionDegreeID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionDegreeID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</h:selectOneMenu>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.period']}: " />
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:selectOneMenu id="calendarPeriod" value="#{SOPEvaluationManagementBackingBean.calendarPeriod}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCalendarPeriod}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.calendarPeriodItems}"/>
			</h:selectOneMenu>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td class='tstylebordernone'>" escape="false"/>
			<h:selectManyCheckbox id="curricularYearIDs"  value="#{SOPEvaluationManagementBackingBean.curricularYearIDs}"
					disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
					onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCurricularYearIDs}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}"/>
			</h:selectManyCheckbox>
		<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>

	</h:form>

	 	<h:panelGroup rendered="#{SOPEvaluationManagementBackingBean.renderContextSelection}">
		 	<fc:fenixCalendar 
		 		begin="#{SOPEvaluationManagementBackingBean.writtenEvaluationsCalendarBegin}" 
		 		end="#{SOPEvaluationManagementBackingBean.writtenEvaluationsCalendarEnd}"
		 		createLink="#{facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/showExecutionCourses.faces?academicInterval=#{SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}&executionDegreeID=#{SOPEvaluationManagementBackingBean.executionDegreeID}&curricularYearIDsParameterString=#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"
		 		editLinkPage="#{facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/editWrittenTest.faces"
		 		editLinkParameters="#{SOPEvaluationManagementBackingBean.writtenTestsCalendarLink}"
		 	/>

			<h:outputText value="<p class='mtop2'><b>#{bundleSOP['label.defined.written.evaluations']}:</b></p>" escape="false"/>
			<h:panelGroup rendered="#{empty SOPEvaluationManagementBackingBean.executionCoursesWithWrittenEvaluations}">
				<h:outputText value="<p><em>#{bundleSOP['label.no.defined.written.evaluations']}</em></p>" escape="false" />
			</h:panelGroup>
		<h:panelGroup rendered="#{!empty SOPEvaluationManagementBackingBean.executionCoursesWithWrittenEvaluations}">
			<f:verbatim>
				<table class="style2u">
				<tr>			
					<th><c:out value="${bundle['label.coordinator.identification']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.evaluationDate']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enroledStudents']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.missingPlaces']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.enrolmentPeriod']}" escapeXml="false"/></th>
					<th><c:out value="${bundle['label.coordinator.rooms']}"/></th>
					<th style="width: 9em;"></th>				
				</tr>			
				<c:forEach items="${SOPEvaluationManagementBackingBean.executionCourseWrittenEvaluationAgregationBeans}" var="executionCourseWrittenEvaluationAgregationBean">
					<tr class="space"><td></td></tr>
					<tr>
						<td colspan="2" class="header" style="font-weight: bold">
							<c:out value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.sigla} - ${executionCourseWrittenEvaluationAgregationBean.executionCourse.nome}"/>
							<c:out value=": ${executionCourseWrittenEvaluationAgregationBean.curricularYear} ${bundleSOP['label.year.simple']}"/>
						</td>
						<td colspan="2" class="header" style="font-weight: bold">
							<c:url var="creationURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/showExecutionCourses.faces">
								<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
								<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
								<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.externalId}"/>
								<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
							</c:url>
							<a href='<c:out value="${creationURL}"/>' style="text-decoration:none">
								<c:out value="${bundleSOP['link.create.evaluation']}"/>
							</a>
						</td>
						<td colspan="3" class="header">
							<c:url var="commentURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/commentExecutionCourse.faces">
								<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
								<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
								<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.externalId}"/>
								<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
							</c:url>
							<a href='<c:out value="${commentURL}"/>' style="text-decoration:none">
								<c:if test="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment != null && executionCourseWrittenEvaluationAgregationBean.executionCourse.comment != ''}">
									<c:out value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment}"/>
								</c:if>
								<c:if test="${executionCourseWrittenEvaluationAgregationBean.executionCourse.comment == null || executionCourseWrittenEvaluationAgregationBean.executionCourse.comment == ''}">
									<c:out value="(${bundleSOP['link.define.comment']})"/>
								</c:if>
							</a>
						</td>
					</tr>						
					<c:forEach items="${executionCourseWrittenEvaluationAgregationBean.writtenEvaluations}" var="evaluation">
						<tr>
							<td class="description">
								<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
									<c:out value="${evaluation.description}"/>
								</c:if>
								<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
									<c:out value="${evaluation.season}"/>
								</c:if>
							</td>
							<td>
								<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/><br/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/>
								<c:out value=" ${bundle['label.coordinator.to']} "/>
								<fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/>
							</td> <!--  enrolled students  -->
							<td><c:out value="${SOPEvaluationManagementBackingBean.executionCoursesEnroledStudents[evaluation.externalId]}"/></td>
							<td>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.externalId] != 0}">
									<c:out value="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.externalId]}"/>
								</c:if>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsFreeSpace[evaluation.externalId] == 0}">
									<c:out value="-"/>
								</c:if>
							</td>
							<c:if test="${evaluation.enrollmentBeginDayDate != null && evaluation.enrollmentEndDayDate != null}">
								<td>
									<c:out value=" ${bundle['label.coordinator.enrolmentBegin']}: "/>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentBeginDayDate}"/>
									<c:out value=" - "/>
									<fmt:formatDate pattern="HH:mm" value="${evaluation.enrollmentBeginTimeDate}"/><br/>
									<c:out value=" ${bundle['label.coordinator.enrolmentEnd']}: "/>
									<fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.enrollmentEndDayDate}"/>
									<c:out value=" - "/>
								</td>
							</c:if>
							<c:if test="${evaluation.enrollmentBeginDayDate == null && evaluation.enrollmentEndDayDate == null}">
								<td>
									<c:out value=" - "/>
								</td>
							</c:if>
							<td> <!-- rooms  -->
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.externalId] != ''}">
									<c:out value="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.externalId]}"/>
								</c:if>
								<c:if test="${SOPEvaluationManagementBackingBean.writtenEvaluationsRooms[evaluation.externalId] == ''}">
									<c:out value="-"/>
								</c:if>
							</td>
							<td> <!-- links -->
								<c:url var="editEvaluationURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/editWrittenTest.faces">
									<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationTypeClassname" value="${evaluation.class.name}"/>
									<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
									<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.externalId}"/>
									<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
								</c:url>
								<a href='<c:out escapeXml="false" value="${editEvaluationURL}"/>'>
									<c:out value="${bundle['label.edit']}"/>
								</a>
								<c:out value=" | "/>
								<c:url var="deleteEvaluationURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/deleteWrittenEvaluation.faces">
									<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
									<c:param name="evaluationID" value="${evaluation.externalId}"/>
									<c:param name="evaluationTypeClassname" value="${evaluation.class.name}"/>
									<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
									<c:param name="executionCourseID" value="${executionCourseWrittenEvaluationAgregationBean.executionCourse.externalId}"/>
									<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
								</c:url>
								<a href='<c:out value="${deleteEvaluationURL}"/>' onclick="return confirm('#{bundle['message.confirm.written.test']}')">
									<c:out value="${bundle['label.remove']}"/>
								</a>
							</td>
						</tr>
					</c:forEach>
				</c:forEach>			
				</table>		
			</f:verbatim>
			</h:panelGroup>

			<h:outputText value="<p class='mtop2'><b>#{bundleSOP['label.execution.courses.without.written.evaluations']}:</b></p>" escape="false"/>
			<h:panelGroup rendered="#{empty SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}">
				<h:outputText value="<p><em>#{bundleSOP['label.no.execution.courses.without.written.evaluations']}</em></p>" escape="false" />
			</h:panelGroup>
			<h:panelGroup rendered="#{!empty SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}">
				<f:verbatim>
					<table class="executionCoursesWithoutWrittenEvaluations">
						<c:forEach items="${SOPEvaluationManagementBackingBean.executionCoursesWithoutWrittenEvaluations}" var="executionCourse">
							<tr>
								<td>
									<c:out value="${executionCourse.sigla} - ${executionCourse.nome}"/>
								</td>
								<td>
									<c:url var="creationURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/showExecutionCourses.faces">
										<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
										<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
										<c:param name="executionCourseID" value="${executionCourse.externalId}"/>										
										<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>
									</c:url>
									<a href='<c:out value="${creationURL}"/>' style="text-decoration:none">
										<c:out value="${bundleSOP['link.create.evaluation']}"/>
									</a>
								</td>
								<td>
									<c:url var="commentURL" value="${facesContext.externalContext.requestContextPath}/resourceAllocationManager/writtenEvaluations/commentExecutionCourse.faces">
										<c:param name="executionDegreeID" value="${SOPEvaluationManagementBackingBean.executionDegreeID}"/>
										<c:param name="academicInterval" value="${SOPEvaluationManagementBackingBean.academicIntervalEscapeFriendly}"/>
										<c:param name="executionCourseID" value="${executionCourse.externalId}"/>							
										<c:param name="curricularYearIDsParameterString" value="${SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}"/>			
									</c:url>
									<a href='<c:out value="${commentURL}"/>' style="text-decoration:none">
										<c:if test="${executionCourse.comment != null && executionCourse.comment != ''}">
											<c:out value="${executionCourse.comment}"/>
										</c:if>
										<c:if test="${executionCourse.comment == null || executionCourse.comment == ''}">
											<c:out value="(${bundleSOP['link.define.comment']})"/>
										</c:if>
									</a>
								</td>
							</tr>
						</c:forEach>				
					</table>
				</f:verbatim>
			</h:panelGroup>

	 	</h:panelGroup>

	

</f:view>
