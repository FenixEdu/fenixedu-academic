<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<html:xhtml/>


<%-- 
 This is a JSP to be only included, should never be refered directly
--%>

<p><em><!-- Error messages go here --><html:errors /></em></p>

	<html:messages id="message" message="true" bundle="DEFAULT">
		<p>
			<em><!-- Error messages go here -->
				<bean:write name="message"/>
			</em>
		</p>
	</html:messages>


	<logic:present name="executionSemesters"><!-- Dropdown box -->					
		<fr:form action="/summariesControl.do?method=listSummariesControl">			
			<fr:edit id="executionSemester" name="executionSemesters">
				<fr:layout name="tabular">
					<fr:property name="classes" value="mtop05 mbottom1 thlight thmiddle"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement">
					<fr:slot name="executionSemester" layout="menu-select-postback" key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"/>
						<fr:property name="format" value="${executionYear.year} - ${semester}º Semestre" />
					</fr:slot>										
				</fr:schema>
			</fr:edit>											
		</fr:form>
		<logic:present name="currentSemester">
			<p class="mtop0">
				<em>
					<bean:message key="message.summary.data.reportsAWeekAgo" bundle="PEDAGOGICAL_COUNCIL"/>
					<fr:view name="oneWeekBeforeDate"/>.
				</em>
			</p>
		</logic:present> 
	</logic:present>


	<logic:present name="summariesResumeMap"><!-- All departments resume -->
		<fr:view name="summariesResumeMap" schema="summary.control.allDepartments">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",acenter,acenter,acenter,acenter,acenter,acenter bgcolorf5f5f5"/>
			</fr:layout>
		</fr:view>															
	</logic:present>
			
	
	<logic:present name="departmentResume"><!-- Department resume -->
		<bean:define id="executionSemesterID" name="departmentResume" property="executionSemester.externalId"/>
		<fr:form action="/summariesControl.do?method=listDepartmentSummariesControl">

			<fr:edit id="departmentResume" name="departmentResume">
				<fr:layout name="tabular">
					<fr:property name="classes" value="mtop05 mbottom1 thlight thmiddle"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>	
				<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement">
					<fr:slot name="executionSemester" layout="menu-select-postback" key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"/>
						<fr:property name="format" value="${executionYear.year} - ${semester}º Semestre" />
					</fr:slot>									
				</fr:schema>
			</fr:edit>
			
			<p class="mtop05 mbottom1">
				<html:link page="<%= "/summariesControl.do?method=listSummariesControl&amp;executionSemesterID=" + executionSemesterID %>">
					<bean:message key="button.back" />
				</html:link>
			</p>
												
		</fr:form>
	</logic:present>
	

<style type="text/css">
.anchorBiggerPadding a {
padding-left: 3px;
padding-right: 3px;
}
</style>

	
	<logic:present name="departmentResumeList"><!-- Department mini resume -->
		<bean:define id="columnClasses" value="acenter,acenter,acenter,acenter,acenter,highlight1 acenter"/>
		<logic:equal name="departmentResume" property="summaryControlCategoryString" value="BETWEEN_0_20">
			<bean:define id="columnClasses" value="highlight1 acenter,acenter,acenter,acenter,acenter,acenter"/>
		</logic:equal>
		<logic:equal name="departmentResume" property="summaryControlCategoryString" value="BETWEEN_20_40">
			<bean:define id="columnClasses" value="acenter,highlight1 acenter,acenter,acenter,acenter,acenter"/>
		</logic:equal>
		<logic:equal name="departmentResume" property="summaryControlCategoryString" value="BETWEEN_40_60">
			<bean:define id="columnClasses" value="acenter,acenter,highlight1 acenter,acenter,acenter,acenter"/>
		</logic:equal>
		<logic:equal name="departmentResume" property="summaryControlCategoryString" value="BETWEEN_60_80">
			<bean:define id="columnClasses" value="acenter,acenter,acenter,highlight1 acenter,acenter,acenter"/>
		</logic:equal>
		<logic:equal name="departmentResume" property="summaryControlCategoryString" value="BETWEEN_80_100">
			<bean:define id="columnClasses" value="acenter,acenter,acenter,acenter,highlight1 acenter,acenter"/>
		</logic:equal>
			
		<fr:view name="departmentResumeList">
			<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement">													
				<fr:slot name="numberOfExecutionCoursesWithin020" layout="link" key="label.summary.0to20">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink020"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl=BETWEEN_0_20"/>
				</fr:slot>
				<fr:slot name="numberOfExecutionCoursesWithin2040" layout="link" key="label.summary.20to40">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink2040"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl=BETWEEN_20_40"/>
				</fr:slot>
				<fr:slot name="numberOfExecutionCoursesWithin4060" layout="link" key="label.summary.40to60">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink4060"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl=BETWEEN_40_60"/>
				</fr:slot>
				<fr:slot name="numberOfExecutionCoursesWithin6080" layout="link" key="label.summary.60to80">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink6080"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl=BETWEEN_60_80"/>
				</fr:slot>
				<fr:slot name="numberOfExecutionCoursesWithin80100" layout="link" key="label.summary.80to100">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink80100"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl=BETWEEN_80_100"/>
				</fr:slot>
				<fr:slot name="numberOfExecutionCoursesWithin0100" layout="link" key="label.summary.total">				
					<fr:property name="contextRelative" value="true"/>
				    <fr:property name="moduleRelative" value="true"/>
					<fr:property name="useParent" value="true"/>
					<fr:property name="linkIf" value="toDisplayCategoryLink0100"/>
					<fr:property name="linkFormat" value="/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID=${executionSemester.externalId}&amp;departmentID=${department.externalId}&amp;categoryControl="/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05 anchorBiggerPadding"/>
				<fr:property name="columnClasses" value="<%= columnClasses %>"/>
			</fr:layout>							
		</fr:view>
	</logic:present>


	<logic:present name="departmentResume"><!-- Department executuin courses resume -->
		<bean:define id="departmentID" name="departmentResume" property="department.externalId" type="java.lang.String"/>
		<bean:define id="executionSemesterID" name="departmentResume" property="executionSemester.externalId" type="java.lang.String"/>
		<bean:define id="categoryControl" value=""/>		
		<logic:notEmpty name="departmentResume" property="summaryControlCategory">
			<bean:define id="categoryControl" name="departmentResume" property="summaryControlCategoryString" type="java.lang.String"/>
		</logic:notEmpty>
		<h3 class="mtop1"><fr:view name="departmentResume" property="department.realName"/></h3>
		<logic:empty name="departmentResume" property="executionCourses">
			<p><em><bean:message key="message.summary.noExecutionCourses" bundle="PEDAGOGICAL_COUNCIL"/></em></p>
		</logic:empty>
		<logic:notEmpty name="departmentResume" property="executionCourses">
			<bean:define id="departmentResume" name="departmentResume" type="net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.DepartmentSummaryElement"/>
			
			<%
				String sortCriteria = request.getParameter("sortBy");

				if (sortCriteria == null) {
				    sortCriteria = "executionCourse.nome=ascending";
				}
			%>
 		<html:link page="<%= "/summariesControl.do?method=exportInfoToExcel&amp;departmentID=" + departmentID.toString() + "&amp;executionSemesterID=" 
 				+ executionSemesterID.toString() + "&amp;categoryControl=" + categoryControl.toString() %>">
			<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"/>
			<bean:message key="label.excel.link" bundle="PEDAGOGICAL_COUNCIL"/>
		</html:link>					
			<fr:view name="departmentResume" property="executionCourses">
				<fr:layout name="tabular-sortable">
					<fr:property name="classes" value="tstyle4 thlight mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,acenter,acenter,acenter,acenter"/>
					<fr:property name="sortUrl" value="<%= "/summariesControl.do?method=departmentSummariesResume&amp;executionSemesterID="+executionSemesterID+"&amp;departmentID="+departmentID+"&amp;categoryControl="+categoryControl.toString() %>"/>
					<fr:property name="sortParameter" value="sortBy"/>
					<fr:property name="sortableSlots" value="numberOfLessonInstances,numberOfLessonInstancesWithSummary,percentageOfLessonsWithSummary,numberOfLessonInstancesWithNotTaughtSummary,percentageOfLessonsWithNotTaughtSummary"/>
					<fr:property name="sortBy" value="<%= sortCriteria %>"/>
				</fr:layout>
				<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.dataTransferObject.directiveCouncil.ExecutionCourseSummaryElement">
					<fr:slot name="executionCourse.nome" layout="link" key="label.executionCourse.name">
						<fr:property name="contextRelative" value="true"/>
				        <fr:property name="moduleRelative" value="true"/>
				        <fr:property name="useParent" value="true"/>
						<fr:property name="linkFormat" value="<%= "/summariesControl.do?method=executionCourseSummariesControl&amp;executionCourseID=${executionCourse.externalId}&amp;departmentID="+departmentID+"&amp;categoryControl="+categoryControl.toString() %>"/>
					</fr:slot>
					<fr:slot name="persons" key="label.teachers" layout="flowLayout" bundle="GLOBAL_RESOURCES" >
						<fr:property name="eachSchema" value="summary.control.teacher"/>
						<fr:property name="eachLayout" value="values-comma"/>
						<fr:property name="htmlSeparator" value=","/>						
					</fr:slot>
					<fr:slot name="numberOfLessonInstances" key="label.summary.numberOfLessonInstances"/>
					<fr:slot name="numberOfLessonInstancesWithSummary" key="label.summary.numberOfLessonInstancesWithSummary"/>
					<fr:slot name="percentageOfLessonsWithSummary" key="label.summary.percentageOfLessonsWithSummary"/>
					<fr:slot name="numberOfLessonInstancesWithNotTaughtSummary" key="label.summary.numberOfLessonInstancesWithNotTaughtSummary"/>
					<fr:slot name="percentageOfLessonsWithNotTaughtSummary" key="label.summary.percentageOfLessonsWithNotTaughtSummary"/>
				</fr:schema>
				<fr:destination name="teacherLink" path="<%= "/summariesControl.do?method=teacherSummariesControl&amp;executionSemesterID=" + departmentResume.getExecutionSemester().getExternalId() + "&amp;personID=${externalId}&amp;departmentID="+departmentID.toString()+"&amp;categoryControl="+categoryControl.toString() %>"/>
			</fr:view>
	 		<html:link page="<%= "/summariesControl.do?method=exportInfoToExcel&amp;departmentID=" + departmentID.toString() + "&amp;executionSemesterID=" 
	 				+ executionSemesterID.toString() + "&amp;categoryControl=" + categoryControl.toString() %>">
				<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"/>
				<bean:message key="label.excel.link" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</logic:notEmpty>
	</logic:present>	


	<logic:present name="executionCoursesResume"><!-- Execution course resume -->
		<bean:define id="executionSemesterID" name="executionCourse" property="executionPeriod.externalId"/>		
		<bean:define id="departmentID" name="departmentID" type="java.lang.String"/>		
		<bean:define id="categoryControl" name="categoryControl" type="java.lang.String"/>
		
		<p class="mtop15 mbottom15">
			<html:link page="<%= "/summariesControl.do?method=departmentSummariesResume&amp;departmentID="+ departmentID +"&amp;categoryControl="+ categoryControl + "&amp;executionSemesterID=" + executionSemesterID %>">
				<bean:message key="button.back" />
			</html:link>
		</p>
		
		<h3 class="mtop1 mbottom1">				 
			<fr:view name="executionCourse" property="executionPeriod">
				<fr:layout>
					<fr:property name="format" value="${executionYear.year} - ${semester}º Sem" />
				</fr:layout>
			</fr:view> - 
			<fr:view name="executionCourse" property="nome"/>
		</h3>					

		<bean:define id="year" name="executionCourse" property="executionPeriod.executionYear.year" type="java.lang.String"/>
		<% year = year.replace('/','-'); %>
		<bean:define id="semester" name="executionCourse" property="executionPeriod.semester"/>
		<bean:define id="sigla" name="executionCourse" property="sigla"/>
		<bean:define id="urlContext" value="<%= request.getContextPath() + "/disciplinas/"+ sigla +"/"+ year + "/" + semester + "-semestre/sumarios" %>"/>		
		
		<% 
		   StringBuilder urlStart = new StringBuilder("http://");
		   urlStart.append(request.getServerName()); 
		   int serverPort = request.getServerPort();
		   if(serverPort != 0 && serverPort != 443) {
			  urlStart.append(":").append(serverPort); 
		   }
		%>
		
		<fr:view name="executionCoursesResume" schema="summaries.control.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mvert05"/>
				<fr:property name="columnClasses" value=",acenter,acenter,,smalltxt,aright,aright,bold aright, aright,bold aright"/>
				<fr:property name="suffixes" value=",,,,,,,%,,%"/>
			</fr:layout>
		</fr:view>				

		<p>
			<a href="<%= urlStart + urlContext %>" target="_blank"><bean:message key="label.executionCourse.summaries" bundle="PEDAGOGICAL_COUNCIL"/></a>
		</p>

	</logic:present>


	<logic:present name="last4SemestersSummaryControl"><!-- Teacher resume -->
		<bean:define id="executionSemesterID" name="executionSemesterID"/>		
		<bean:define id="departmentID" name="departmentID" type="java.lang.String"/>		
		<bean:define id="categoryControl" name="categoryControl" type="java.lang.String"/>
		
		<p class="mvert15">
			<html:link page="<%= "/summariesControl.do?method=departmentSummariesResume&amp;departmentID="+ departmentID +"&amp;categoryControl="+ categoryControl + "&amp;executionSemesterID=" + executionSemesterID %>">
				<bean:message key="button.back" />
			</html:link>
		</p>
		
		<h3 class="mvert1">			
			<fr:view name="person" property="name"/>
		</h3>		
		<logic:iterate id="executionCoursesPair" name="last4SemestersSummaryControl">
			<bean:define id="executionSemester" name="executionCoursesPair" property="key"/>
			<bean:define id="executionCoursesResume" name="executionCoursesPair" property="value"/>
			<p class="mtop15 mbottom05">				 
				<fr:view name="executionSemester">
					<fr:layout>
						<fr:property name="format" value="${executionYear.year} - ${semester}º Sem" />
					</fr:layout>
				</fr:view>
			</p>
			<logic:notEmpty name="executionCoursesResume">
				<fr:view name="executionCoursesResume" schema="summaries.control.list">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thlight mvert05"/>
						<fr:property name="columnClasses" value=",acenter,acenter,,smalltxt,aright,aright,bold aright, aright,bold aright, aright,bold aright"/>
						<fr:property name="suffixes" value=",,,,,,,%,,%"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="executionCoursesResume">
				<em><bean:message key="message.summary.noDataForThisSemester" bundle="PEDAGOGICAL_COUNCIL"/></em>
			</logic:empty>
		</logic:iterate>			
	</logic:present>
	
	