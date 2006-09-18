<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>

	<h:outputText value="<h2>#{bundle['label.courseStatistics.competenceStatistics']}</h2>" escape="false" />
	<h:outputText value="<h3>#{courseStatistics.department.realName}</h3>" escape="false" />

	<h:form>

		<fc:viewState binding="#{courseStatistics.viewState}" />
		
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="aright" />
			<fc:selectOneMenu
				value="#{courseStatistics.executionPeriodId}"
				valueChangeListener="#{courseStatistics.onExecutionPeriodChangeForCompetenceCourses}"
				onchange="this.form.submit();">
				<f:selectItems value="#{courseStatistics.executionPeriods}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>

		<h:outputText value="<br />" escape="false" />
		
		<f:verbatim>
			<table class="vtsbc">
				<thead>
				<tr>
		</f:verbatim>
		
		<h:outputText value="<th class=\"listClasses-header\">&nbsp;</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\" colspan=\"3\">#{bundle['label.courseStatistics.firstCount']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\" colspan=\"3\">#{bundle['label.courseStatistics.restCount']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\" colspan=\"3\">#{bundle['label.courseStatistics.totalCount']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">&nbsp;</th>" escape="false" />
		<h:outputText value="</tr><tr>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.common.courseName']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th class=\"listClasses-header\">#{bundle['label.courseStatistics.approvedPercentage']}</th>" escape="false" />

		<f:verbatim>
					</tr>
				</thead>
				<tbody>
		</f:verbatim>
		

		<fc:dataRepeater value="#{courseStatistics.competenceCourses}" var="competenceCourse">
				
				<h:outputText value="<tr><td class=\"courses\">" escape="false"/>
				
				<fc:commandLink value="#{competenceCourse.name}"	
					action="viewDegreeCourses"
					actionListener="#{courseStatistics.onCompetenceCourseSelect}">

					<f:param id="competenceCourseId" name="competenceCourseId"
						value="#{competenceCourse.idInternal}" />
				</fc:commandLink>
				
				<h:outputText value="</td><td class=\"listClasses\">#{competenceCourse.firstEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.firstApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.firstApprovedCount == 0 ? bundle['label.common.notAvailable'] : competenceCourse.firstApprovedAverage.grade}</td>" escape="false" />
				
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.restEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.restApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.restApprovedCount == 0 ? bundle['label.common.notAvailable'] : competenceCourse.restApprovedAverage.grade}</td>" escape="false" />

				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.totalEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.totalApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class=\"listClasses\">#{competenceCourse.totalApprovedCount == 0 ? bundle['label.common.notAvailable'] : competenceCourse.totalApprovedAverage.grade}</td>" escape="false" />
				
				<h:outputText value="<td class=\"listClasses\">" escape="false" />
				<h:outputText value="#{(competenceCourse.totalEnrolledCount == 0)  ? bundle['label.common.notAvailable'] : (competenceCourse.totalApprovedCount / competenceCourse.totalEnrolledCount)}" escape="false">
					<f:convertNumber maxFractionDigits="2" type="percent"/>
 		  		</h:outputText>
				<h:outputText value="</td></tr>" escape="false"/>
		
		</fc:dataRepeater>
		<f:verbatim>
				</tbody>
			</table>
		</f:verbatim>
	</h:form>

</ft:tilesView>
