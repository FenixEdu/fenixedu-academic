<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:outputFormat value="<h2>#{bundle['list.students']}</h2/><hr/>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorStudentsBackingBean.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.sortBy' id='sortBy' name='sortBy' type='hidden' value='#{CoordinatorStudentsBackingBean.sortBy}'/>"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
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

			<h:outputText value="#{bundle['label.student.number']}: " />
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.minStudentNumberString']}" id="minStudentNumberString" value="#{CoordinatorStudentsBackingBean.minStudentNumberString}" size="6"/>
				<h:outputText value=" - " />
				<h:inputText alt="#{htmlAltBundle['inputText.maxStudentNumberString']}" id="maxStudentNumberString" value="#{CoordinatorStudentsBackingBean.maxStudentNumberString}" size="6"/>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.average']}: " />
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

			<h:outputText value="#{bundle['label.viewPhoto']}: " />
			<h:selectBooleanCheckbox id="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}" />
		</h:panelGrid>

		<h:commandButton alt="#{htmlAltBundle['commandButton.search']}" styleClass="inputbutton" value="#{bundle['button.search']}"/>
	</h:form>

	<h:outputText value="<br/>#{bundle['label.number.results']}: " escape="false"/>
	<h:outputText value="#{CoordinatorStudentsBackingBean.numberResults}"/>
	<h:outputText value="<br/><br/>" escape="false"/>

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
						<c:param name="studentCurricularPlanStateString" value="${CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
						<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
						<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
						<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
						<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
						<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
						<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
						<c:param name="showPhoto" value="${CoordinatorStudentsBackingBean.showPhoto}"/>
						<c:param name="minIndex" value="${CoordinatorStudentsBackingBean.minIndex - CoordinatorStudentsBackingBean.resultsPerPage}"/>
						<c:param name="maxIndex" value="${CoordinatorStudentsBackingBean.maxIndex - CoordinatorStudentsBackingBean.resultsPerPage}"/>
					</c:url>
					<a href='<c:out value="${pageURL}"/>'>
						<c:out value="<<" />
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
					<c:param name="studentCurricularPlanStateString" value="${CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
					<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
					<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
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
					<c:param name="studentCurricularPlanStateString" value="${CoordinatorStudentsBackingBean.studentCurricularPlanStateString}"/>
					<c:param name="minGradeString" value="${CoordinatorStudentsBackingBean.minGradeString}"/>
					<c:param name="maxGradeString" value="${CoordinatorStudentsBackingBean.maxGradeString}"/>
					<c:param name="minNumberApprovedString" value="${CoordinatorStudentsBackingBean.minNumberApprovedString}"/>
					<c:param name="maxNumberApprovedString" value="${CoordinatorStudentsBackingBean.maxNumberApprovedString}"/>
					<c:param name="minStudentNumberString" value="${CoordinatorStudentsBackingBean.minStudentNumberString}"/>
					<c:param name="maxStudentNumberString" value="${CoordinatorStudentsBackingBean.maxStudentNumberString}"/>
					<c:param name="showPhoto" value="${CoordinatorStudentsBackingBean.showPhoto}"/>
					<c:param name="minIndex" value="${CoordinatorStudentsBackingBean.minIndex + CoordinatorStudentsBackingBean.resultsPerPage}"/>
					<c:param name="maxIndex" value="${CoordinatorStudentsBackingBean.maxIndex + CoordinatorStudentsBackingBean.resultsPerPage}"/>
				</c:url>
				<c:out value="&nbsp;&nbsp;" escapeXml="false" />
				<a href='<c:out value="${pageURL}"/>'>
					<c:out value=">>"/>
				</a>
			</c:if>
			</c:if>
		</c:forEach>
	</f:verbatim>
	<h:outputText value="</center>" escape="false"/>
	</h:panelGroup>

	<h:panelGroup>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
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
					<f:param name="showPhoto" value="#{CoordinatorStudentsBackingBean.showPhoto}"/>
					<h:outputText value="#{bundle['label.average']}" />
				</h:outputLink>
			</f:facet>
			<h:outputText value="#{studentCurricularPlan.student.arithmeticMean}">
				<f:converter converterId="net.sourceforge.fenixedu.presentationTier.jsf.converter.SimpleRoundConverter"/>
			</h:outputText>
		</h:column>
		<h:column rendered="#{CoordinatorStudentsBackingBean.showPhoto == true}">
			<f:facet name="header">
				<h:outputText value="#{bundle['label.person.photo']}" />
			</f:facet>
			<h:form>
				<h:outputText value="<img src='#{CoordinatorStudentsBackingBean.contextPath}/person/retrievePersonalPhoto.do?method=retrieveByID&personCode=#{studentCurricularPlan.student.person.idInternal}'/> alt='<bean:message key='personPhoto' bundle='IMAGE_RESOURCES' />'" escape="false"/>
			</h:form>
		</h:column>
	</h:dataTable>
	</h:panelGroup>
	</h:panelGrid>

</ft:tilesView>