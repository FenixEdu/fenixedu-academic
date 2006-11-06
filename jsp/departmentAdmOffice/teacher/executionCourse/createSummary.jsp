<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

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

	<logic:empty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.insert" bundle="DEFAULT"/></h2>
	</logic:empty>
	<logic:notEmpty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.edit" bundle="DEFAULT"/></h2>
	</logic:notEmpty>


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
	
	<bean:define id="executionCourseID" name="summariesManagementBean" property="executionCourse.idInternal" />
	<bean:define id="teacherNumber" name="loggedTeacherProfessorship" property="teacher.teacherNumber" />

	<fr:hasMessages for="summariesManagementBeanWithSummary" type="conversion">
		<p><span class="error0">			
			<fr:message for="summariesManagementBeanWithSummary" show="message"/>
		</span></p>
	</fr:hasMessages>	
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>

	<%-- Lesson --%>			
	<h3 class="mbottom0"><bean:message key="label.lesson" bundle="DEFAULT"/></h3>
	<table class="tstyle5">
		<tr>
			<%-- SummaryType --%>				
			<td class="aright"><bean:message key="label.type"/></td>
			<td>
				<div style="display: inline;">
					<fr:form>
						<bean:define id="chooseSummaryTypeUrl">/summariesManagement.do?method=chooseSummaryType&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
						<fr:edit id="summariesManagementBeanWithSummaryType" name="summariesManagementBean" schema="ChooseSummaryType" nested="true">
							<fr:destination name="postBack" path="<%= chooseSummaryTypeUrl %>"/>
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>	
						</fr:edit>
					</fr:form>
				</div>				
			</td>	
		</tr>	
		<tr>
			<td class="aright"><bean:message key="label.shift"/>:</td>
			<td>
				<%-- Shift --%>						
				<fr:form>
					<bean:define id="chooseShiftUrl">/summariesManagement.do?method=chooseShift&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
					<fr:edit id="summariesManagementBeanWithShifts" name="summariesManagementBean" schema="ListShiftsToCreateSummary" nested="true">
						<fr:destination name="postBack" path="<%= chooseShiftUrl %>"/>				
						<fr:layout name="flow">
							<fr:property name="labelTerminator" value=""/>
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
					</fr:edit>
				</fr:form>					
			</td>	
		</tr>	
		<logic:notEqual name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">
			<tr>
				<td class="aright"><bean:message key="label.lesson" bundle="DEFAULT"/>:</td>
				<td>
					<%-- Lesson --%>
					<fr:form>
						<bean:define id="chooseLessonUrl">/summariesManagement.do?method=chooseLesson&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
						<fr:edit id="summariesManagementBeanWithLessons" name="summariesManagementBean" schema="ListShiftLessonsToCreateSummary" nested="true">
							<fr:destination name="postBack" path="<%= chooseLessonUrl %>"/>				
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>
						</fr:edit>
					</fr:form>
				</td>
			</tr>		
			<tr>
				<td class="aright"><bean:message key="label.date" bundle="DEFAULT"/>:</td>
				<td>
					<%-- Date --%>
					<fr:form>		
						<bean:define id="chooseDateUrl">/summariesManagement.do?method=chooseDate&teacherNumber__=<bean:write name="teacherNumber"/></bean:define>				
						<fr:edit id="summariesManagementBeanWithDate" name="summariesManagementBean" schema="LisPossibleDatesToCreateSummary" nested="true">
							<fr:destination name="postBack" path="<%= chooseDateUrl %>"/>	
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>			
						</fr:edit>
					</fr:form>
				</td>
			</tr>
		</logic:notEqual>
	</table>	
	
	<%-- Associate --%>
	<h3 class="mbottom0"> <bean:message key="label.use" bundle="DEFAULT"/></h3>
	<table class="tstyle5">
		<%-- LessonPlannings --%>
		<tr>
			<td class="aright"><bean:message key="label.lessonPlanning" bundle="DEFAULT"/>:</td>
			<td>				
				<fr:form>
					<bean:define id="chooseLessonPlanningURL">/summariesManagement.do?method=chooseLessonPlanning&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
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
			<td class="aright"><bean:message key="message.summaryText.last" bundle="DEFAULT"/>:</td>
			<td>				
				<fr:form>
					<bean:define id="chooseLastSummaryURL">/summariesManagement.do?method=chooseLastSummary&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
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
	
	<bean:define id="invalidLink">/summariesManagement.do?method=goToInsertSummaryAgain&executionCourseID=<bean:write name="executionCourseID"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>									
	<bean:define id="createSummaryURL">/summariesManagement.do?teacherNumber_=<bean:write name="teacherNumber"/></bean:define>		

	<fr:form action="<%= createSummaryURL %>">		
		<html:hidden property="method" name="summariesManagementForm" value="createSummary"/>	
				
		<logic:equal name="summariesManagementBean" property="summaryType" value="NORMAL_SUMMARY">				
		
			<%-- Teacher --%>
			<jsp:include page="../../../teacher/executionCourse/chooseTeacher.jsp"/>						
		
			<%-- Summary --%>	
			<h3 class="mbottom0"><bean:message key="message.summaryText" bundle="DEFAULT"/></h3>
			<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToNormalSummary">
				<fr:destination name="invalid" path="<%= invalidLink %>"/>
				<fr:destination name="exception" path="<%= invalidLink %>"/>			
				<fr:destination name="input" path="<%= invalidLink %>"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>
			</fr:edit>
			<html:submit titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>
			<logic:empty name="summariesManagementBean" property="summary">	
				<html:submit titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:cancel onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:cancel>
		</logic:equal>
		
		<logic:equal name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">		
			
			<%-- Teacher --%>
			<jsp:include page="../../../teacher/executionCourse/chooseTeacher.jsp"/>						
			
			<%-- Summary --%>	
			<h3 class="mbottom0"><bean:message key="message.summaryText" bundle="DEFAULT"/></h3>
			<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToExtraSummary" >				
				<fr:destination name="invalid" path="<%= invalidLink %>"/>
				<fr:destination name="exception" path="<%= invalidLink %>"/>
				<fr:destination name="input" path="<%= invalidLink %>"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>
			</fr:edit>	
			<html:submit titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>			
			<logic:empty name="summariesManagementBean" property="summary">				
				<html:submit titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:cancel onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:cancel>
		</logic:equal>		
	</fr:form>
	
</logic:present>
