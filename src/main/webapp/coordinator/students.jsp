<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%
	org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex.setCoordinatorContext(request);
%>
<fp:select actionClass="org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex" />
<jsp:include page="/coordinator/context.jsp" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:outputFormat value="<h2>#{bundle['list.students']}</h2/><hr/>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.sortBy' id='sortBy' name='sortBy' type='hidden' value='#{CoordinatorStudentsBackingBean.sortBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionDegreeId' id='executionDegreeId' name='executionDegreeId' type='hidden' value='#{CoordinatorStudentsBackingBean.executionDegreeId}'/>"/>

		<h:panelGrid columns="2" styleClass="infoop">
			<h:outputText value="#{bundle['label.student.curricular.plan.state']}: " id="studentCurricularPlanState" />
			<h:selectOneMenu id="registrationStateTypeString" value="#{CoordinatorStudentsBackingBean.registrationStateTypeString}">
				<f:selectItem itemLabel="#{bundle['message.all']}" itemValue="SHOWALL"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.REGISTERED']}" itemValue="REGISTERED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.MOBILITY']}" itemValue="MOBILITY"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.CANCELED']}" itemValue="CANCELED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.CONCLUDED']}" itemValue="CONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.FLUNKED']}" itemValue="FLUNKED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.INTERRUPTED']}" itemValue="INTERRUPTED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.SCHOOLPARTCONCLUDED']}" itemValue="SCHOOLPARTCONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.INTERNAL_ABANDON']}" itemValue="INTERNAL_ABANDON"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.EXTERNAL_ABANDON']}" itemValue="EXTERNAL_ABANDON"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.TRANSITED']}" itemValue="TRANSITED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.STUDYPLANCONCLUDED']}" itemValue="STUDYPLANCONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.INACTIVE']}" itemValue="INACTIVE"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.student.number']}: " id="studentNumber" />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minStudentNumberString']}" id="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}" size="6"/>
				<h:outputText value=" - " id="textSeparator" />
				<h:inputText alt="#{htmlAltBundle['inputText.maxStudentNumberString']}" id="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}" size="6"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.arithmetricAverage']}: " id="arithmetricAverage"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minGradeString']}" id="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}" size="4"/>
				<h:outputText value=" - " id="textSeparator2" />
				<h:inputText alt="#{htmlAltBundle['inputText.maxGradeString']}" id="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}" size="4"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.number.approved.curricular.courses']}: " id="approvedCurricularCourses"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minNumberApprovedString']}" id="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}" size="4"/>
				<h:outputText value=" - " id="textSeparator3"/>
				<h:inputText alt="#{htmlAltBundle['inputText.maxNumberApprovedString']}" id="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}" size="4"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.student.curricular.year']}: " id="curricularYear"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minimumYear']}" id="minimumYearString" value="#{CoordinatorStudentsBackingBean.minimumYearString}" size="4"/>
				<h:outputText value=" - " id="textSeparator4"/>
				<h:inputText alt="#{htmlAltBundle['inputText.maximumYear']}" id="maximumYearString" value="#{CoordinatorStudentsBackingBean.maximumYearString}" size="4"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.viewPhoto']}: " id="photo"/>
			<h:selectBooleanCheckbox id="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}" />
		</h:panelGrid>

		<fc:commandButton alt="#{htmlAltBundle['commandButton.search']}" styleClass="inputbutton" value="#{bundle['button.search']}"/>

		<h:outputText value="<br/><br/>#{bundle['label.number.results']}: " escape="false" id="number"/>
		<h:outputText value="#{CoordinatorStudentsBackingBean.numberResults}" id="numberResults"/>
		<h:outputText value="<br/><br/>" escape="false"/>
	
		<h:graphicImage id="image" alt="Excel" url="/images/excel.gif" />
		<h:outputText value="&nbsp;" escape="false" id="textSeparator5"/>
		<fc:commandLink value="#{bundle['link.exportToExcel']}" action="#{CoordinatorStudentsBackingBean.exportStudentsToExcel}"/>
		<h:outputText value="<br/><br/>" escape="false" id="textSeparator6"/>
		
 		<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/teacher/searchECAttends.do?method=sendEmail&amp;searchGroup=#{CoordinatorStudentsBackingBean.serializedFilteredStudents}&amp;executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}&contentContextPath_PATH=/comunicacao/comunicacao'>#{bundle['link.sendEmailToAllStudents']}</a>" id="email" escape="false"/>

	</h:form>


	<h:panelGrid>
	<h:panelGroup>
	<h:outputText value="<center>" escape="false" id="textSeparator7"/>
	<f:verbatim>
		<c:forEach items="${CoordinatorStudentsBackingBean.indexes}" var="pageIndex" varStatus="status">
			<c:if test="${status.first}">
				<c:if test="${pageIndex != CoordinatorStudentsBackingBean.minIndex}">
					<c:url value="${facesContext.externalContext.requestContextPath}/coordinator/students.faces" var="pageURL">
						<c:param name="degreeCurricularPlanID" value="${CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
						<c:param name="sortBy" value="${CoordinatorStudentsBackingBean.sortBy}"/>
						<c:param name="registrationStateTypeString" value="${CoordinatorStudentsBackingBean.registrationStateTypeString}"/>
						<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
						<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
						<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
						<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
						<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
						<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
						<c:param name="minimumYearString" value="${CoordinatorStudentsBackingBean.minimumYearString}"/>
						<c:param name="maximumYearString" value="${CoordinatorStudentsBackingBean.maximumYearString}"/>
						<c:param name="showPhoto" value="${CoordinatorStudentsBackingBean.showPhoto}"/>
						<c:param name="minIndex" value="${CoordinatorStudentsBackingBean.minIndex - CoordinatorStudentsBackingBean.resultsPerPage}"/>
						<c:param name="maxIndex" value="${CoordinatorStudentsBackingBean.maxIndex - CoordinatorStudentsBackingBean.resultsPerPage}"/>
					</c:url>
					<a href='<c:out value="${pageURL}"/>'>
						<c:out value="&laquo;" escapeXml="false" />
					</a>
					<c:out value="&nbsp;&nbsp;" escapeXml="false" />
				</c:if>
			</c:if>
			<c:if test="${pageIndex == CoordinatorStudentsBackingBean.minIndex}">
				<c:out value="${status.index + 1}"/>
			</c:if>
			<c:if test="${pageIndex != CoordinatorStudentsBackingBean.minIndex}">
				<c:url value="${facesContext.externalContext.requestContextPath}/coordinator/students.faces" var="pageURL">
					<c:param name="degreeCurricularPlanID" value="${CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<c:param name="sortBy" value="${CoordinatorStudentsBackingBean.sortBy}"/>
					<c:param name="registrationStateTypeString" value="${CoordinatorStudentsBackingBean.registrationStateTypeString}"/>
					<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
					<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
					<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<c:param name="minimumYearString" value="${CoordinatorStudentsBackingBean.minimumYearString}"/>
					<c:param name="maximumYearString" value="${CoordinatorStudentsBackingBean.maximumYearString}"/>
					<c:param name="showPhoto" value="${CoordinatorStudentsBackingBean.showPhoto}"/>
					<c:param name="minIndex" value="${pageIndex}"/>
					<c:param name="maxIndex" value="${pageIndex + CoordinatorStudentsBackingBean.resultsPerPage - 1}"/>
				</c:url>
				<a href='<c:out value="${pageURL}"/>'>
					<c:out value="${status.index + 1}"/>
				</a>
			</c:if>
			<c:if test="${!status.last}">
				<c:out value=" - "/>
			</c:if>
			<c:if test="${status.last}">
			<c:if test="${pageIndex != CoordinatorStudentsBackingBean.minIndex}">
				<c:url value="${facesContext.externalContext.requestContextPath}/coordinator/students.faces" var="pageURL">
					<c:param name="degreeCurricularPlanID" value="${CoordinatorStudentsBackingBean.degreeCurricularPlanID}"/>
					<c:param name="sortBy" value="${CoordinatorStudentsBackingBean.sortBy}"/>
					<c:param name="registrationStateTypeString" value="${CoordinatorStudentsBackingBean.registrationStateTypeString}"/>
					<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
					<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
					<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<c:param name="minimumYearString" value="${CoordinatorStudentsBackingBean.minimumYearString}"/>
					<c:param name="maximumYearString" value="${CoordinatorStudentsBackingBean.maximumYearString}"/>
					<c:param name="showPhoto" value="${CoordinatorStudentsBackingBean.showPhoto}"/>
					<c:param name="minIndex" value="${CoordinatorStudentsBackingBean.minIndex + CoordinatorStudentsBackingBean.resultsPerPage}"/>
					<c:param name="maxIndex" value="${CoordinatorStudentsBackingBean.maxIndex + CoordinatorStudentsBackingBean.resultsPerPage}"/>
				</c:url>
				<c:out value="&nbsp;&nbsp;" escapeXml="false" />
				<a href='<c:out value="${pageURL}"/>'>
					<c:out value="&raquo;" escapeXml="false"/>
				</a>
			</c:if>
			</c:if>
		</c:forEach>
	</f:verbatim>
	<h:outputText value="</center>" escape="false" id="textSeparator8"/>
	</h:panelGroup>

	<h:dataTable value="#{CoordinatorStudentsBackingBean.studentCurricularPlans}" var="mapEntry" cellpadding="0"
			styleClass="table table-bordered table-striped text-center">
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=student.number&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.number']}</a>" id="showPhotoNumber" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewStudentCurriculumSearch.do?method=showStudentCurriculum&degreeCurricularPlanId=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&registrationOID=#{mapEntry.key.registration.externalId}&studentNumber=#{mapEntry.key.registration.student.number}&executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}'>" escape="false"/>
				<h:outputText value="#{mapEntry.key.registration.number}"/>
			<h:outputText value="</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.person.name&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.name']}</a>" id="showPhotoName" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewStudentCurriculumSearch.do?method=showStudentCurriculum&degreeCurricularPlanId=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&registrationOID=#{mapEntry.key.registration.externalId}&studentNumber=#{mapEntry.key.registration.student.number}&executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}'>" id="executionDegree" escape="false"/>
				<h:outputText value="#{mapEntry.key.registration.person.name}" id="personName"/>
			<h:outputText value="</a>" escape="false" id="textSeparator9"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.person.email&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.email']}</a>" id="showPhotoEmail" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='mailto:#{mapEntry.key.registration.person.email}'>" escape="false" id="personEmailLink"/>
			<h:outputText value="#{mapEntry.key.registration.person.email}</a>" escape="false" id="personEmail"/>
		</h:column>
		<h:column>
			<f:facet name="header">
					<h:outputText value="<a href='#{facesContext.externalContext.requestContextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=currentState&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.student.curricular.plan.state']}</a>" id="studentCurricularPlanState" escape="false"/>
			</f:facet>
			<%-- Kept the mailto:REGISTRATION_TYPE for legacy purposes only. To Luis 'Goofy' Cruz just one word: LMAO! :P --%>
			<h:outputText value="<a href='mailto:#{mapEntry.value}'>" escape="false" id="mailValue"/>
			<h:outputText value="#{bundleEnum[mapEntry.value.qualifiedName]}</a>" escape="false" id="name"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.numberOfCurriculumEntries&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.number.approved.curricular.courses']}</a>" id="numberApprovedCurricularCourses" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.numberOfCurriculumEntries}" id="numberOfCurricularEntries"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.ectsCredits&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.ects']}</a>" id="ects" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.ectsCredits}" id="ectsCredits"/>
		</h:column>
		<h:column>
			<f:facet name="header">
					<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.rawGrade&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.arithmetricAverage']}</a>" id="arithmetricAverage" escape="false"/>
			</f:facet>
			<h:panelGroup rendered="#{mapEntry.key.registration.concluded}">
				<h:outputText rendered="#{mapEntry.key.registration.registrationConclusionProcessed && (mapEntry.key.internalCycleCurriculumGroupsSize eq 1)}" value="#{mapEntry.key.registration.rawGrade.value}" id="registrationAverage">
					<f:convertNumber maxFractionDigits="0" minIntegerDigits="1" maxIntegerDigits="2"/>
				</h:outputText>
				<h:outputText rendered="#{ ! mapEntry.key.registration.registrationConclusionProcessed}" value=" - " id="textSeparator10" />
			</h:panelGroup>
			<h:panelGroup rendered="#{ ! mapEntry.key.registration.concluded}">
				<h:outputText value="#{mapEntry.key.registration.rawGrade.value}" id="registrationAverageConcluded">
					<f:convertNumber maxFractionDigits="2" minFractionDigits="2" minIntegerDigits="1" maxIntegerDigits="2" />
				</h:outputText>
			</h:panelGroup>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.curricularYear&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.student.curricular.year']}</a>" id="year" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.curricularYear}" id="registrationCurricularYear"/>
		</h:column>
		<h:column>
			<f:facet name="header">	
				<c:if test="${mapEntry.key.activeTutorship != null}">
					<h:outputText value="#{mapEntry.key.activeTutorship.teacher.person.name}" id="personName"/>
				</c:if>
			</f:facet>
		</h:column>
		
		<h:column rendered="#{CoordinatorStudentsBackingBean.showPhoto == true}">
			<f:facet name="header">
				<h:outputText value="#{bundle['label.person.photo']}" id="personPhoto" />
			</f:facet>
			<h:form>
				<h:outputText value="<img src='#{CoordinatorStudentsBackingBean.contextPath}/user/photo/#{mapEntry.key.registration.person.username}'/> alt='<bean:message key='personPhoto' bundle='IMAGE_RESOURCES' />'" id="personPhotoImg" escape="false"/>
			</h:form>
		</h:column>
	</h:dataTable>
	</h:panelGrid>

</f:view>
