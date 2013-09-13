<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo"%>

<%
	CoordinatedDegreeInfo.setCoordinatorContext(request);
%>


<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:outputFormat value="<h2>#{bundle['list.students']}</h2/><hr/>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.sortBy' id='sortBy' name='sortBy' type='hidden' value='#{CoordinatorStudentsBackingBean.sortBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionDegreeId' id='executionDegreeId' name='executionDegreeId' type='hidden' value='#{CoordinatorStudentsBackingBean.executionDegreeId}'/>"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
			<h:outputText value="#{bundle['label.student.curricular.plan.state']}: " />
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
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.TRANSITION']}" itemValue="TRANSITION"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.TRANSITED']}" itemValue="TRANSITED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.STUDYPLANCONCLUDED']}" itemValue="STUDYPLANCONCLUDED"/>
				<f:selectItem itemLabel="#{bundleEnum['RegistrationStateType.INACTIVE']}" itemValue="INACTIVE"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.student.number']}: " />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minStudentNumberString']}" id="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}" size="6"/>
				<h:outputText value=" - " />
				<h:inputText alt="#{htmlAltBundle['inputText.maxStudentNumberString']}" id="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}" size="6"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.arithmetricAverage']}: " />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minGradeString']}" id="minGradeString" value="#{CoordinatorStudentsBackingBean.minGradeString}" size="4"/>
				<h:outputText value=" - " />
				<h:inputText alt="#{htmlAltBundle['inputText.maxGradeString']}" id="maxGradeString" value="#{CoordinatorStudentsBackingBean.maxGradeString}" size="4"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.number.approved.curricular.courses']}: " />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minNumberApprovedString']}" id="minNumberApprovedString" value="#{CoordinatorStudentsBackingBean.minNumberApprovedString}" size="4"/>
				<h:outputText value=" - " />
				<h:inputText alt="#{htmlAltBundle['inputText.maxNumberApprovedString']}" id="maxNumberApprovedString" value="#{CoordinatorStudentsBackingBean.maxNumberApprovedString}" size="4"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['label.student.curricular.year']}: " />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minimumYear']}" id="minimumYearString" value="#{CoordinatorStudentsBackingBean.minimumYearString}" size="4"/>
				<h:outputText value=" - " />
				<h:inputText alt="#{htmlAltBundle['inputText.maximumYear']}" id="maximumYearString" value="#{CoordinatorStudentsBackingBean.maximumYearString}" size="4"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.viewPhoto']}: " />
			<h:selectBooleanCheckbox id="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}" />
		</h:panelGrid>

		<fc:commandButton alt="#{htmlAltBundle['commandButton.search']}" styleClass="inputbutton" value="#{bundle['button.search']}"/>

		<h:outputText value="<br/><br/>#{bundle['label.number.results']}: " escape="false"/>
		<h:outputText value="#{CoordinatorStudentsBackingBean.numberResults}"/>
		<h:outputText value="<br/><br/>" escape="false"/>
	
		<h:graphicImage id="image" alt="Excel" url="/images/excel.gif" />
		<h:outputText value="&nbsp;" escape="false" />
		<fc:commandLink value="#{bundle['link.exportToExcel']}" action="#{CoordinatorStudentsBackingBean.exportStudentsToExcel}"/>
		<h:outputText value="<br/><br/>" escape="false"/>
		
 		<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/teacher/searchECAttends.do?method=sendEmail&amp;searchGroup=#{CoordinatorStudentsBackingBean.serializedFilteredStudents}&amp;executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}&contentContextPath_PATH=/comunicacao/comunicacao'>#{bundle['link.sendEmailToAllStudents']}</a>" escape="false"/>

	</h:form>


	<h:panelGrid>
	<h:panelGroup>
	<h:outputText value="<center>" escape="false"/>
	<f:verbatim>
		<c:forEach items="${CoordinatorStudentsBackingBean.indexes}" var="pageIndex" varStatus="status">
			<c:if test="${status.first}">
				<c:if test="${pageIndex != CoordinatorStudentsBackingBean.minIndex}">
					<c:url value="students.faces" var="pageURL">
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
				<c:url value="students.faces" var="pageURL">
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
				<c:url value="students.faces" var="pageURL">
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
	<h:outputText value="</center>" escape="false"/>
	</h:panelGroup>

	<h:panelGroup>
	<h:dataTable value="#{CoordinatorStudentsBackingBean.studentCurricularPlans}" var="mapEntry" cellpadding="0"
			headerClass="listClasses-header" columnClasses="listClasses">
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=student.number&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.number']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewStudentCurriculumSearch.do?method=showStudentCurriculum&degreeCurricularPlanId=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&registrationOID=#{mapEntry.key.registration.externalId}&studentNumber=#{mapEntry.key.registration.student.number}&executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}'>" escape="false"/>
				<h:outputText value="#{mapEntry.key.registration.number}"/>
			<h:outputText value="</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.person.name&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.name']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/viewStudentCurriculumSearch.do?method=showStudentCurriculum&degreeCurricularPlanId=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&registrationOID=#{mapEntry.key.registration.externalId}&studentNumber=#{mapEntry.key.registration.student.number}&executionDegreeId=#{CoordinatorStudentsBackingBean.executionDegreeId}'>" escape="false"/>
				<h:outputText value="#{mapEntry.key.registration.person.name}"/>
			<h:outputText value="</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.person.email&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.email']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="<a href='mailto:#{mapEntry.key.registration.person.email}'>" escape="false"/>
			<h:outputText value="#{mapEntry.key.registration.person.email}</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
					<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=currentState&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.student.curricular.plan.state']}</a>" escape="false"/>
			</f:facet>
			<%-- Kept the mailto:REGISTRATION_TYPE for legacy purposes only. To Luis 'Goofy' Cruz just one word: LMAO! :P --%>
			<h:outputText value="<a href='mailto:#{mapEntry.value}'>" escape="false"/>
			<h:outputText value="#{bundleEnum[mapEntry.value.qualifiedName]}</a>" escape="false"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.numberOfCurriculumEntries&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.number.approved.curricular.courses']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.numberOfCurriculumEntries}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.ectsCredits&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.ects']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.ectsCredits}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
					<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.average&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.arithmetricAverage']}</a>" escape="false"/>
			</f:facet>
			<h:panelGroup rendered="#{mapEntry.key.registration.concluded}">
				<h:outputText rendered="#{mapEntry.key.registration.registrationConclusionProcessed && (!mapEntry.key.registration.bolonha || (mapEntry.key.internalCycleCurriculumGroupsSize eq 1))}" value="#{mapEntry.key.registration.average}">
					<f:convertNumber maxFractionDigits="0" minIntegerDigits="1" maxIntegerDigits="2"/>
				</h:outputText>
				<h:outputText rendered="#{ ! mapEntry.key.registration.registrationConclusionProcessed}" value=" - "  />
			</h:panelGroup>
			<h:panelGroup rendered="#{ ! mapEntry.key.registration.concluded}">
				<h:outputText value="#{mapEntry.key.registration.average}">
					<f:convertNumber maxFractionDigits="2" minFractionDigits="2" minIntegerDigits="1" maxIntegerDigits="2" />
				</h:outputText>
			</h:panelGroup>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="<a href='#{CoordinatorStudentsBackingBean.contextPath}/coordinator/students.faces?degreeCurricularPlanID=#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}&amp;sortBy=registration.curricularYear&amp;registrationStateTypeString=#{CoordinatorStudentsBackingBean.registrationStateTypeString}&amp;studentNumber=#{mapEntry.key.registration.student.number}&amp;minGradeString=#{CoordinatorStudentsBackingBean.minGradeString}&amp;maxGradeString=#{CoordinatorStudentsBackingBean.maxGradeString}&amp;minNumberApprovedString=#{CoordinatorStudentsBackingBean.minNumberApprovedString}&amp;maxNumberApprovedString=#{CoordinatorStudentsBackingBean.maxNumberApprovedString}&amp;minStudentNumberString=#{CoordinatorStudentsBackingBean.minStudentNumberString}&amp;maxStudentNumberString=#{CoordinatorStudentsBackingBean.maxStudentNumberString}&amp;showPhoto=#{CoordinatorStudentsBackingBean.showPhoto}'>#{bundle['label.student.curricular.year']}</a>" escape="false"/>
			</f:facet>
			<h:outputText value="#{mapEntry.key.registration.curricularYear}"/>
		</h:column>
		<h:column>
			<f:facet name="header">	
				<c:if test="${mapEntry.key.activeTutorship != null}">
					<h:outputText value="#{mapEntry.key.activeTutorship.teacher.person.name}"/>
				</c:if>
			</f:facet>
		</h:column>
		
		<h:column rendered="#{CoordinatorStudentsBackingBean.showPhoto == true}">
			<f:facet name="header">
				<h:outputText value="#{bundle['label.person.photo']}" />
			</f:facet>
			<h:form>
				<h:outputText value="<img src='#{CoordinatorStudentsBackingBean.contextPath}/person/retrievePersonalPhoto.do?method=retrieveByID&personCode=#{mapEntry.key.registration.person.externalId}'/> alt='<bean:message key='personPhoto' bundle='IMAGE_RESOURCES' />'" escape="false"/>
			</h:form>
		</h:column>
	</h:dataTable>
	</h:panelGroup>
	</h:panelGrid>

</ft:tilesView>
