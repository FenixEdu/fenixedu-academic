<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<script language="JavaScript">	
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>

<logic:present name="summariesManagementBean">
			
	<h2><bean:message key="title.summaries.insert" bundle="DEFAULT"/></h2>
		
	<p class="mbottom05"><a href="#" class="dnone" id="instructionsButton" onclick="check(document.getElementById('instructions'), document.getElementById('instructionsButton'));"><bean:message key="link.home" bundle="DEFAULT"/></a></p>
	<div id="instructions" class="dblock">
		<div class="infoop2 mtop025">
			<ul class="mvert025">
				<li><bean:message key="label.summary.management.instructions1" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions2" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions3" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions4" bundle="DEFAULT"/></li>
			</ul>
		</div>
	</div>

	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dblock";
	</script>

	<bean:define id="executionCourseID" name="summariesManagementBean" property="executionCourse.externalId" />
	<bean:define id="teacherNumber" name="loggedTeacherProfessorship" property="teacher.teacherNumber" />
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>			
	
	<logic:empty name="notShowLessonPlanningsAndSummaries">
		<%-- Associate --%>
		<h3 class="mbottom0"> <bean:message key="label.use" bundle="DEFAULT"/></h3>
		<table class="tstyle5">
			<%-- LessonPlannings --%>
			<tr>
				<td><bean:message key="label.lessonPlanning" bundle="DEFAULT"/>:</td>
				<td>				
					<bean:define id="chooseLessonPlanningURL">/summariesManagement.do?method=chooseLessonPlanningToCreateComplexSummary&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
					<fr:form action="<%= chooseLessonPlanningURL %>">						
						<fr:edit id="summariesManagementBeanWithLessonPlanning" name="summariesManagementBean" schema="ListLessonPlanningsToSummariesManagement" nested="true">
							<fr:destination name="postBack" path="<%= chooseLessonPlanningURL %>"/>	
								<fr:layout name="flow">
									<fr:property name="labelTerminator" value=""/>
									<fr:property name="labelExcluded" value="true"/>
								</fr:layout>	
						</fr:edit>							
					</fr:form>
				</td>
			</tr>	
			<%-- LastSummaries --%>
			<tr>
				<td><bean:message key="message.summaryText.last" bundle="DEFAULT"/>:</td>
				<td>				
					<bean:define id="chooseLastSummaryURL">/summariesManagement.do?method=chooseLastSummaryToCreateComplexSummary&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
					<fr:form action="<%= chooseLastSummaryURL %>">						
						<fr:edit id="summariesManagementBeanWithLastSummary" name="summariesManagementBean" schema="ListLastSummariesToSummariesManagement" nested="true">
							<fr:destination name="postBack" path="<%= chooseLastSummaryURL %>"/>	
								<fr:layout name="flow">
									<fr:property name="labelTerminator" value=""/>
									<fr:property name="labelExcluded" value="true"/>
								</fr:layout>	
						</fr:edit>							
					</fr:form>	
				</td>
			</tr>
		</table>		
	</logic:empty>	
		
	<bean:define id="invalidLink">/summariesManagement.do?method=prepareCreateComplexSummary&executionCourseID=<bean:write name="executionCourseID"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>					
	<bean:define id="createSummaryURL">/summariesManagement.do?teacherNumber_=<bean:write name="teacherNumber"/></bean:define>		
	
	<fr:form action="<%= createSummaryURL %>">		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="summariesManagementForm" value="createComplexSummary"/>	

		<%-- Lessons --%>
		<h3 class="mbottom0"><bean:message key="label.lesson.or.lessons" bundle="DEFAULT"/></h3>
		<logic:notEmpty name="summariesManagementBean" property="nextPossibleSummaryLessonsAndDatesBean">
			<logic:iterate name="summariesManagementBean" property="nextPossibleSummaryLessonsAndDatesBean" id="lesson" indexId="index" type="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean">				
				<fr:hasMessages for="<%= "nextPossibleLessonsDatesBean" + String.valueOf(index.intValue() + 1) %>" type="conversion">
					<p>
						<span class="error0">			
							<fr:message for="<%= "nextPossibleLessonsDatesBean" + String.valueOf(index.intValue() + 1) %>" show="message"/>
						</span>
					</p>
				</fr:hasMessages>				
				<fr:view name="lesson" schema="PossibleNextSummaryLessonAndDate">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mtop1 mbottom0"/>
						<fr:property name="columnClasses" value="width8em aright,width30em"/>
					</fr:layout>
				</fr:view>					
				<fr:edit nested="true" name="lesson" id="<%= "nextPossibleLessonsDatesBean" + String.valueOf(index.intValue() + 1) %>" schema="EditPossibleNextSummaryLessonAndDates">									
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mbottom1 mtop0"/>
						<fr:property name="columnClasses" value="width8em aright,width30em,tdclear"/>
					</fr:layout>
				</fr:edit>				
			</logic:iterate>			
		</logic:notEmpty>
					
		<%-- Teacher --%>
		<jsp:include page="../../../teacher/executionCourse/chooseTeacher.jsp"/>						
		
		<%-- Summary --%>	
		<h3 class="mbottom0"><bean:message key="message.summaryText" bundle="DEFAULT"/></h3>
		<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToNormalComplexSummary">
			<fr:destination name="input" path="<%= invalidLink %>"/>
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight"/>
			</fr:layout>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>		
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:cancel>
	</fr:form>			
</logic:present>