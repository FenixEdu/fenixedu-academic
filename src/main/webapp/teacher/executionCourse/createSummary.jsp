<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<html:xhtml/>

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
		<h2><bean:message key="title.summary.insert"/></h2>
	</logic:empty>
	<logic:notEmpty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.edit"/></h2>
	</logic:notEmpty>
	
	<p class="mbottom05"><span class="dnone" id="instructionsButton" onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;"><a href="#"><bean:message key="link.home"/></a></span></p>
	<div id="instructions" class="dblock">
		<div class="infoop2 mtop025">
			<ul class="mvert025">
				<li><bean:message key="label.summary.management.instructions1" /></li>
				<li><bean:message key="label.summary.management.instructions2" /></li>
				<li><bean:message key="label.summary.management.instructions3" /></li>
				<li><bean:message key="label.summary.management.instructions4" /></li>
			</ul>
		</div>
	</div>


	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dblock";
	</script>

	<bean:define id="executionCourseID" name="summariesManagementBean" property="executionCourse.externalId" />
	
	<fr:hasMessages for="summariesManagementBeanWithSummary" type="conversion">
		<p><span class="error0">			
			<fr:message for="summariesManagementBeanWithSummary" show="message"/>
		</span></p>
	</fr:hasMessages>	
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" >
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>
		
	<%-- Lesson --%>	
	<h3 class="mbottom0"><bean:message key="label.lesson" /></h3>
	<table class="tstyle5">
		<tr>
			<%-- SummaryType --%>				
			<td class="aright"><bean:message key="label.type"/>:</td>
			<td>
				<div style="display: inline;">
					<fr:form action="/summariesManagement.do">
						<fr:edit id="summariesManagementBeanWithSummaryType" name="summariesManagementBean" schema="ChooseSummaryType" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseSummaryType"/>
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
				<fr:form action="/summariesManagement.do?method=chooseShift">
					<fr:edit id="summariesManagementBeanWithShifts" name="summariesManagementBean" schema="ListShiftsToCreateSummary" nested="true">
						<fr:destination name="postBack" path="/summariesManagement.do?method=chooseShift"/>				
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
				<td class="aright"><bean:message key="label.lesson" />:</td>
				<td>
					<%-- Lesson --%>
					<fr:form action="/summariesManagement.do?method=chooseLesson">
						<fr:edit id="summariesManagementBeanWithLessons" name="summariesManagementBean" schema="ListShiftLessonsToCreateSummary" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLesson"/>				
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>
						</fr:edit>
					</fr:form>
				</td>
			</tr>
			<tr>
				<td class="aright"><bean:message key="label.lessonPlanning.lessonType"/>:</td>
				<td>
					<fr:form action="/summariesManagement.do?method=chooseLessonType">
						<fr:edit id="summariesManagementBeanWithLessonTypes" name="summariesManagementBean" schema="ListLessonTypesToCreateSummary" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLessonType"/>				
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>
						</fr:edit>
					</fr:form>
				</td>				
			</tr>					
			<tr>
				<td class="aright"><bean:message key="label.date" />:</td>
				<td>
					<%-- Date --%>
					<fr:form action="/summariesManagement.do?method=chooseDate">
						<fr:edit id="summariesManagementBeanWithDate" name="summariesManagementBean" schema="LisPossibleDatesToCreateSummary" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseDate"/>	
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
	<logic:empty name="notShowLessonPlanningsAndSummaries">
		<h3 class="mbottom0"> <bean:message key="label.associate"/></h3>
		<table class="tstyle5 thright">
			<%-- LessonPlannings --%>
			<tr>
				<td class="aright"><bean:message key="label.lessonPlanning" />:</td>
				<td>				
					<fr:form action="/summariesManagement.do?method=chooseLessonPlanning">
						<fr:edit id="summariesManagementBeanWithLessonPlanning" name="summariesManagementBean" schema="ListLessonPlanningsToSummariesManagement" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLessonPlanning"/>	
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
				<td class="aright"><bean:message key="message.summaryText.last"/>:</td>
				<td>				
					<fr:form action="/summariesManagement.do?method=chooseLastSummary">
						<fr:edit id="summariesManagementBeanWithLastSummary" name="summariesManagementBean" schema="ListLastSummariesToSummariesManagement" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLastSummary"/>	
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
			
	<bean:define id="invalidLink">/summariesManagement.do?method=goToInsertSummaryAgain&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
				
	<fr:form action="/summariesManagement.do">		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="summariesManagementForm" value="createSummary"/>	
		
		<logic:equal name="summariesManagementBean" property="summaryType" value="NORMAL_SUMMARY">							
			
			<%-- Teacher --%>
			<jsp:include page="chooseTeacher.jsp"/>							
			
			<%-- Summary --%>
				
			<h3 class="mbottom0"><bean:message key="message.summaryText"/></h3>			
<%-- 			<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToNormalSummary">								 --%>
<%-- 				<fr:destination name="invalid" path="<%= invalidLink %>"/> --%>
<%-- 				<fr:destination name="exception" path="<%= invalidLink %>"/> --%>
<%-- 				<fr:destination name="input" path="<%= invalidLink %>"/> --%>
<%-- 				<fr:layout name="tabular"> --%>
<%-- 					<fr:property name="classes" value="tstyle5 thlight"/> --%>
<%-- 				</fr:layout> --%>
<%-- 			</fr:edit> --%>
			
			
			<logic:equal name="summariesManagementBean" property="taught" value="true">
				<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToNormalSummary">
					<fr:destination name="input" path="<%= invalidLink %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:destination name="exception" path="<%= invalidLink %>"/>
					<fr:destination name="changeSummaryType" path="/summariesManagement.do?method=prepareInsertNotTaughtSummary" />
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>
				</fr:edit>
			</logic:equal>
			
			<logic:equal name="summariesManagementBean" property="taught" value="false">
				<fr:edit nested="true" id="summariesManagementBeanWithNotTaughtSummary" name="summariesManagementBean" schema="CreateSummaryToNormalNotTaughtSummary">
					<fr:destination name="input" path="<%= invalidLink %>"/>
					<fr:destination name="invalid" path="<%= invalidLink %>"/>
					<fr:destination name="exception" path="<%= invalidLink %>"/>
					<fr:destination name="changeSummaryType" path="/summariesManagement.do?method=prepareInsertTaughtSummary" />
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>
				</fr:edit>
			</logic:equal>
				
			
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>
			<logic:empty name="summariesManagementBean" property="summary">						
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:cancel>
		</logic:equal>	
		
		<logic:equal name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">		
			
			<%-- Teacher --%>
			<jsp:include page="chooseTeacher.jsp"/>			
			
			<%-- Summary --%>
			<h3 class="mbottom0"><bean:message key="message.summaryText"/></h3>								
			
		<logic:equal name="summariesManagementBean" property="taught" value="true">
			<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToExtraSummary">
				<fr:destination name="input" path="<%= invalidLink %>"/>
				<fr:destination name="invalid" path="<%= invalidLink %>"/>
				<fr:destination name="exception" path="<%= invalidLink %>"/>
				<fr:destination name="changeSummaryType" path="/summariesManagement.do?method=prepareInsertNotTaughtSummary" />
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<logic:equal name="summariesManagementBean" property="taught" value="false">
			<fr:edit nested="true" id="summariesManagementBeanWithNotTaughtSummary" name="summariesManagementBean" schema="CreateSummaryToExtraNotTaughtSummary">
				<fr:destination name="input" path="<%= invalidLink %>"/>
				<fr:destination name="invalid" path="<%= invalidLink %>"/>
				<fr:destination name="exception" path="<%= invalidLink %>"/>
				<fr:destination name="changeSummaryType" path="/summariesManagement.do?method=prepareInsertTaughtSummary" />
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>
			</fr:edit>
		</logic:equal>
			
			
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>
			<logic:empty name="summariesManagementBean" property="summary">							
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:cancel>
		</logic:equal>	
	</fr:form>				
</logic:present>