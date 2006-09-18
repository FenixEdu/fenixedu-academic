<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>
	
	<h:outputText value="<h2>#{bundle['label.courseStatistics.executionStatistics']}</h2>" escape="false" />
	<h:outputText value="<h3>#{courseStatistics.competenceCourse.name}</h3>" escape="false" />

	<h:form>
		<fc:viewState binding="#{courseStatistics.viewState}" />

		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="aright"/>
			<fc:selectOneMenu
				value="#{courseStatistics.executionPeriodId}"
				valueChangeListener="#{courseStatistics.onExecutionPeriodChangeForExecutionCourses}"
				onchange="this.form.submit();">
				<f:selectItems value="#{courseStatistics.executionPeriods}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>

		<h:outputText value="<br />" escape="false" />
		
		<fc:commandLink value="#{bundle['link.back']}" action="backToDegreeCourses" />

		<h:outputText value="<br /><br />" escape="false" />
		
		<f:verbatim>
			<table class="vtsbc">
				<thead>
				<tr>
		</f:verbatim>
		
		<h:outputText value="<th>&nbsp;</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.firstCount']}</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.restCount']}</th>" escape="false" />
		<h:outputText value="<th colspan=\"3\">#{bundle['label.courseStatistics.totalCount']}</th>" escape="false" />
		<h:outputText value="<th>&nbsp;</th>" escape="false" />
		<h:outputText value="</tr><tr>" escape="false" />
		<h:outputText value="<th>#{bundle['label.courseStatistics.executionPeriod']}</th>" escape="false" />
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

		<f:verbatim>
					</tr>
				</thead>
				<tbody>
		</f:verbatim>
		

		<fc:dataRepeater value="#{courseStatistics.executionCourses}" var="executionCourse">
				<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th colspan=\"11\">#{bundle['label.courseStatistics.teacher']}: #{executionCourse.teacher}</th>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
				
				<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th colspan=\"11\">#{bundle['label.courseStatistics.hadExecutionTogether']}:</th>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
				
				<h:outputText value="<tr><td colspan=\"11\">" escape="false"/>
				<fc:dataRepeater value="#{executionCourse.degrees}" var="degree" >
					<h:outputText value="#{degree}<br />" escape="false" />
				</fc:dataRepeater>
				<h:outputText value="</td></tr>" escape="false"/>
				
				<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<td class=\"courses\">#{executionCourse.executionPeriod} de #{executionCourse.executionYear}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.firstEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.firstApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.firstApprovedCount == 0 ? bundle['label.common.notAvailable'] : executionCourse.firstApprovedAverage.grade}</td>" escape="false"/>

				<h:outputText value="<td>#{executionCourse.restEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.restApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.restApprovedCount == 0 ? bundle['label.common.notAvailable'] : executionCourse.restApprovedAverage.grade}</td>" escape="false"/>

				<h:outputText value="<td>#{executionCourse.totalEnrolledCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.totalApprovedCount}</td>" escape="false"/>
				<h:outputText value="<td>#{executionCourse.totalApprovedCount == 0 ? bundle['label.common.notAvailable'] : executionCourse.totalApprovedAverage.grade}</td>" escape="false"/>
				
				<h:outputText value="<td>" escape="false" />
				<h:outputText value="#{(executionCourse.totalEnrolledCount == 0)  ? bundle['label.common.notAvailable'] : (executionCourse.totalApprovedCount / executionCourse.totalEnrolledCount)}" escape="false">
					<f:convertNumber maxFractionDigits="2" type="percent"/>
 		  		</h:outputText>
				<h:outputText value="</td></tr>" escape="false"/>
		
		</fc:dataRepeater>
		<f:verbatim>
				</tbody>
			</table>
		</f:verbatim>
		
		<fc:commandLink value="#{bundle['link.back']}" action="backToDegreeCourses" />
		
	</h:form>

</ft:tilesView>
