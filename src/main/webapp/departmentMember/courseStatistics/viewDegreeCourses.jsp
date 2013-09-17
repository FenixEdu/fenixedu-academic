<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>
	
	<h:outputText value="<em>#{bundle['label.departmentMember']}</em>" escape="false" />
	<h:outputText value="<h2>#{bundle['label.courseStatistics.degreeStatistics']}</h2>" escape="false" />
	<h:outputText value="<h3>#{courseStatistics.competenceCourse.name}</h3>" escape="false" />

	<h:form>
		<fc:viewState binding="#{courseStatistics.viewState}" />

		<h:outputText value="<ul><li>" escape="false" />
		<fc:commandLink value="#{bundle['link.back']}" action="backToCompetenceCourses" />
		<h:outputText value="</li></ul>" escape="false" />

		<h:outputText value="<table class='tstyle5 mtop05 mbottom15'>" escape="false" />
		<h:outputText value="<tr><td>" escape="false" />
		<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="aright" />
		<h:outputText value="<td>" escape="false" />
			<fc:selectOneMenu
				value="#{courseStatistics.executionPeriodId}"
				valueChangeListener="#{courseStatistics.onExecutionPeriodChangeForDegreeCourses}"
				onchange="this.form.submit();">
				<f:selectItems value="#{courseStatistics.executionPeriods}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</td>" escape="false" />
		<h:outputText value="</td></tr>" escape="false" />
		<h:outputText value="</table>" escape="false" />



		<f:verbatim>
			<table class="tstyle1">
				<tr>
		</f:verbatim>
		
		<h:outputText value="<th>&nbsp;</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.firstCount']}</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.restCount']}</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.totalCount']}</th>" escape="false" />
		<h:outputText value="<th>&nbsp;</th>" escape="false" />
		<h:outputText value="<th>&nbsp;</th>" escape="false" />
		<h:outputText value="</tr><tr>" escape="false" />
		
		<h:outputText value="<th>#{bundle['label.courseStatistics.degree']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.enrolled']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.approved']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.average']}</th>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.approvedPercentage']}</th>" escape="false" />
		<h:outputText value="<th>&nbsp;</th>" escape="false" />

		<f:verbatim>
					</tr>
				<tbody>
		</f:verbatim>
		
		<fc:dataRepeater value="#{courseStatistics.degreeCourses}" var="degreeCourse">
			<h:outputText value="<tr><td>" escape="false"/>
				
				<fc:commandLink value="#{degreeCourse.name}"	
					action="viewExecutionCourses"
					actionListener="#{courseStatistics.onDegreeCourseSelect}">
					<f:param id="degreeId" name="degreeId" value="#{degreeCourse.externalId}" />
				</fc:commandLink>

				<h:outputText value="</td>" escape="false" />
				<h:outputText value="<td class='aright'>#{degreeCourse.firstEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.firstApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.firstApprovedCount == 0 ? bundle['label.common.notAvailable'] : degreeCourse.firstApprovedAverage.gradeValue}</td>" escape="false"/>

				<h:outputText value="<td class='aright'>#{degreeCourse.restEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.restApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.restApprovedCount == 0 ? bundle['label.common.notAvailable'] : degreeCourse.restApprovedAverage.gradeValue}</td>" escape="false"/>

				<h:outputText value="<td class='aright'>#{degreeCourse.totalEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.totalApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td class='aright'>#{degreeCourse.totalApprovedCount == 0 ? bundle['label.common.notAvailable'] : degreeCourse.totalApprovedAverage.gradeValue}</td>" escape="false"/>
				
				<h:outputText value="<td class='aright'>" escape="false" />
				<h:outputText value="#{(degreeCourse.totalEnrolledCount == 0)  ? bundle['label.common.notAvailable'] : (degreeCourse.totalApprovedCount / degreeCourse.totalEnrolledCount)}" escape="false">
					<f:convertNumber maxFractionDigits="2" type="percent"/>
 		  		</h:outputText>
 		  		<h:outputText value="</td><td>" escape="false" />
 		  		
 		  			<h:graphicImage id="image" alt="Excel" url="/images/excel.gif" />
					<h:outputText value="&nbsp;" escape="false" />
					
					<fc:commandLink value="#{bundle['label.teacherService.exportToExcel']}" 
									action="#{courseStatistics.exportStudentsToExcel}"
									actionListener="#{courseStatistics.onDegreeCourseSelect}">
						<f:param id="degreeToExportId" name="degreeId" value="#{degreeCourse.externalId}" />
					</fc:commandLink>
					
				<h:outputText value="</td></tr>" escape="false"/>
		</fc:dataRepeater>
		<f:verbatim>
				</tbody>
			</table>
		</f:verbatim>
			
	</h:form>

</ft:tilesView>
