<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/StudentResources" var="bundle"/>
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<h:outputText value="<h2>#{bundle['label.evaluations.enrolment']}</h2>" escape="false" />
	<h:outputText value="<p><em>#{bundle['label.do.enrolment']}:</em> " escape="false" />
	<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1">
		<h:outputText value="#{bundle['label.page']} #{bundle['link.exams.enrolment']}" />
	</h:outputLink>
	<h:outputText value=" | " />
	<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2">
		<h:outputText value="#{bundle['label.page']} #{bundle['link.writtenTests.enrolment']}"/>
	</h:outputLink>	
	<h:outputText value="</p>" escape="false" />
	<h:form>
		<h:outputText value="<div class='evalcontainer'>" escape="false" />
		<h:panelGrid columns="2" columnClasses="alignright,," styleClass="search">
			<h:outputText value="#{bundle['label.student.enrollment.executionPeriod']}: " styleClass="boldFontClass" />
			<h:panelGroup>			
				<fc:selectOneMenu value="#{displayEvaluationsToEnrol.executionPeriodID}" onchange="this.form.submit();" 
				   valueChangeListener="#{displayEvaluationsToEnrol.changeExecutionPeriod}">
					<f:selectItems value="#{displayEvaluationsToEnrol.executionPeriodsLabels}" />
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			</h:panelGroup>
			<h:outputText value="#{bundle['link.evaluations.enrolment']}: " styleClass="boldFontClass" />
			<h:panelGroup>
				<fc:selectOneMenu value="#{displayEvaluationsToEnrol.evaluationType}" onchange="this.form.submit();"
				   valueChangeListener="#{displayEvaluationsToEnrol.changeEvaluationType}">
					<f:selectItems value="#{displayEvaluationsToEnrol.evaluationTypes}" />
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="</div>" escape="false" />
		<h:outputText value="<br/>" escape="false"/>		
		<%-- Evaluations Table --%>
		<h:outputText value="<table class='evallist'>" escape="false"/>
		<h:outputText value="<tr><th>#{bundle['label.type']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.executionCourse']}</th>" escape="false" />		
		<h:outputText value="<th>#{bundle['label.identification']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.evaluationDate']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPeriod']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.enrolmentPage']}</th></tr>" escape="false" />				
		<%-- Not Enroled Evaluations --%>
		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>

		<h:outputText value="<tr><td colspan='6' class='title'>#{bundle['label.notEnroledEvaluations']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.notEnroledEvaluations}">
			<h:outputText value="<tr><td colspan='6' class='eval_error'>(#{bundle['message.no.evaluations.to.enroll']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.notEnroledEvaluations}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.notEnroledEvaluations}" var="evaluation">
				<h:outputText value="<tr class='el_highlight'>" escape="false" rendered="#{evaluation.isInEnrolmentPeriod}" />
				<h:outputText value="<tr>" escape="false" rendered="#{!evaluation.isInEnrolmentPeriod}"/>
				<h:outputText value="<td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td class='el_courses'>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} " escape="false" />
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
				
				<h:panelGroup rendered="#{evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td><span class='green'>#{bundle['label.opened']}</span></td>" escape="false" />
					<h:outputText value="<td>" escape="false" />
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1"
					   rendered="#{evaluation.class.simpleName == 'Exam'}">
						<h:outputText value="#{bundle['label.enrolment']}" escape="false" />
					</h:outputLink>
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2"
					   rendered="#{evaluation.class.simpleName == 'WrittenTest'}">
						<h:outputText value="#{bundle['label.enrolment']}" escape="false" />
					</h:outputLink>
					<h:outputText value="</td></tr>" escape="false" />					
				</h:panelGroup>
				<h:panelGroup rendered="#{!evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.closed']}</td>" escape="false" />
					<h:outputText value="<td></td></tr>" escape="false" />				
				</h:panelGroup>		
			</fc:dataRepeater>
		</h:panelGroup>				
		<%-- Enroled Evaluations --%>
		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='6' class='title'>#{bundle['label.enroledEvaluations']}: </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.enroledEvaluations}">
			<h:outputText value="<tr><td colspan='6' class='eval_error'>(#{bundle['message.no.enroled.evaluations']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.enroledEvaluations}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.enroledEvaluations}" var="evaluation">
				<h:outputText value="<tr class='el_highlight'>" escape="false" rendered="#{evaluation.isInEnrolmentPeriod}" />
				<h:outputText value="<tr>" escape="false" rendered="#{!evaluation.isInEnrolmentPeriod}"/>			
				<h:outputText value="<td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td class='el_courses'>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} "/>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
				
				<h:panelGroup rendered="#{evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td><span class='green'>#{bundle['label.opened']}</span></td>" escape="false" />
					<h:outputText value="<td>" escape="false" />
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1"
					   rendered="#{evaluation.class.simpleName == 'Exam'}">
						<h:outputText value="#{bundle['label.enrolment']}" escape="false" />
					</h:outputLink>
					<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2"
					   rendered="#{evaluation.class.simpleName == 'WrittenTest'}">
						<h:outputText value="#{bundle['label.enrolment']}" escape="false" />
					</h:outputLink>		
					<h:outputText value="</td></tr>" escape="false" />					
				</h:panelGroup>
				<h:panelGroup rendered="#{!evaluation.isInEnrolmentPeriod}">
					<h:outputText value="<td>#{bundle['label.closed']}</td>" escape="false" />
					<h:outputText value="<td></td></tr>" escape="false" />				
				</h:panelGroup>		
			</fc:dataRepeater>
		</h:panelGroup>		
		<%-- Evaluations Without Enrolment Period --%>
		<h:outputText value="<tr><td colspan='6' class='evallist_empty'></td></tr>" escape="false"/>
		<h:outputText value="<tr><td colspan='6' class='title'>#{bundle['label.evaluationsWithoutEnrolmentPeriod']}:* </td></tr>" escape="false"/>
		<h:panelGroup rendered="#{empty displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}">
			<h:outputText value="<tr><td colspan='6' class='eval_error'>(#{bundle['message.no.evaluations.without.enrolment.period']})</td></tr>" escape="false" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}">
			<fc:dataRepeater value="#{displayEvaluationsToEnrol.evaluationsWithoutEnrolmentPeriod}" var="evaluation">
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.exam']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<tr><td>#{bundle['label.evaluation.shortname.test']}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>			
				
				<h:outputText value="<td class='el_courses'>" escape="false" />
	 			<fc:dataRepeater value="#{displayEvaluationsToEnrol.executionCourses[evaluation.idInternal]}" var="executionCourse">
					<h:outputText value="#{executionCourse.nome}" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td>" escape="false" />
							
				<h:outputText value="<td>#{evaluation.season}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'Exam'}"/>
				<h:outputText value="<td>#{evaluation.description}</td>" escape="false" rendered="#{evaluation.class.simpleName == 'WrittenTest'}"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputFormat value="{0, date, dd/MM/yyyy} " >
					<f:param value="#{evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.to']} " escape="false" />
				<h:outputFormat value="{0, date, HH:mm}" >
					<f:param value="#{evaluation.endDate}" />
				</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
				
				<h:outputText value="<td>-</td>" escape="false" />
				<h:outputText value="<td></td></tr>" escape="false" />							
			</fc:dataRepeater>
		</h:panelGroup>		
		<h:outputText value="</table>" escape="false"/>
		<h:outputText value="* #{bundle['label.evaluationNote']}<br/><br/>" escape="false"/>	
		<h:outputText value="<p><em>#{bundle['label.do.enrolment']}:</em> " escape="false" />
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1">
			<h:outputText value="#{bundle['label.page']} #{bundle['link.exams.enrolment']}" />
		</h:outputLink>
		<h:outputText value=" | " />
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2">
			<h:outputText value="#{bundle['label.page']} #{bundle['link.writtenTests.enrolment']}"/>
		</h:outputLink>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>