<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="departmentMember.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources" var="bundle"/>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #ccc;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #ddd;
border-bottom: 1px solid #ccc;
background-color: #eaeaea;
font-weight: normal;
}
table.vtsbc td {
background-color: #fafafa;
border: none;
border: 1px solid #eee;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #ffe;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
.center {
text-align: center;
}
.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite a {
color: #888;
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0 0.5em;
}
.backwhite ul li a {
text-decoration: none;
border-bottom: 1px solid #ddd;
}
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
</style>

	<h:outputText value="<h2>#{bundle['label.courseStatistics.competenceStatistics']}</h2>" escape="false" />
	<h:outputText value="<h3>#{courseStatistics.department.realName}</h3>" escape="false" />

	<h:form>

		<fc:viewState binding="#{courseStatistics.viewState}" />
		
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}&nbsp;" escape="false" styleClass="leftColumn" />
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
