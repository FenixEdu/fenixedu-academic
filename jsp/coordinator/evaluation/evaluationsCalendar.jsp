<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>
.boldFontClass { 
	font-weight: bold
}
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

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	<h:outputText value="<h2>#{bundle['link.evaluations.calendar']}</h2>" escape="false"/>

		<h:form>
	<h:outputText value="<div class='infoop' style='width: 30em'>" escape="false" />

			<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'"/>
			<h:panelGrid columns="2">
				<h:outputText value="#{bundle['label.coordinator.selectedExecutionPeriod']}:" styleClass="boldFontClass"/>
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
					<f:selectItem itemLabel="" itemValue=""/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.writtenTest']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
					<f:selectItem itemLabel="#{bundle['label.evaluation.type.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
				</h:selectOneMenu>
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

	<h:outputText value="<br/><i>#{bundle['label.coordinator.definedProjects']}</i><br/>" escape="false" styleClass="boldFontClass"/>
	<f:verbatim>
		<table class="style2u">
			<tr>			
				<th><c:out value="${bundle['label.coordinator.identification']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.publish.date']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.delivery.date']}" escapeXml="false"/></th>
				<th><c:out value="${bundle['label.description']}" escapeXml="false"/></th>
				<th style="width: 9em;"></th>				
			</tr>			
			<c:forEach items="${CoordinatorEvaluationsBackingBean.executionCourses}" var="executionCourse">
				<tr class="space"><td></td></tr>
				<tr><td colspan="5" class="header"><c:out value="${executionCourse.sigla} - ${executionCourse.nome}" /></td></tr>
				<c:forEach items="${executionCourse.associatedEvaluations}" var="evaluation">
					<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
						<tr>
							<td><c:out value="${evaluation.description}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy" value="${evaluation.dayDate}"/></td>
							<td><fmt:formatDate pattern="HH:mm" value="${evaluation.beginningDate}"/></td>
							<td><fmt:formatDate pattern="HH:mm" value="${evaluation.endDate}"/></td>
							<td></td>
						</tr>
					</c:if>
				</c:forEach>
				<c:forEach items="${executionCourse.associatedEvaluations}" var="evaluation">
					<c:if test="${evaluation.class.name == 'net.sourceforge.fenixedu.domain.Project'}">
						<tr>
							<td><c:out value="${evaluation.name}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.begin}"/></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${evaluation.end}"/></td>
							<td></td>
							<td></td>
						</tr>
					</c:if>
				</c:forEach>
			</c:forEach>			
		</table>
	</f:verbatim>

</ft:tilesView>