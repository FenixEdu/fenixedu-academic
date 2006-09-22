<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="showSummariesBean">
	<h2><bean:message key="label.summaries.management" bundle="DEFAULT"/></h2>

	<bean:define id="executionCourseID" name="showSummariesBean" property="executionCourse.idInternal" />
	<bean:define id="teacherNumber" name="loggedTeacherProfessorship" property="teacher.teacherNumber" />
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>
	
	<logic:notEmpty name="nextPossibleLessonsDates">
		<p class="mbottom025"><bean:message key="label.last.lessons.without.summaries" bundle="DEFAULT"/></p>	
		<bean:define id="createComplexSummaryUrl">/summariesManagement.do?method=prepareCreateComplexSummary&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
		<fr:form action="<%= createComplexSummaryUrl %>">			
			<fr:edit id="showSummariesBeanWithChoicesHidden" name="showSummariesBean" nested="true" visible="false" />
			<fr:view name="nextPossibleLessonsDates" schema="PossibleNextSummaryLessonAndDate">
				<fr:layout name="tabular">
					<fr:property name="style" value="width: 500px;"/>
					<fr:property name="checkable" value="true"/>
					<fr:property name="checkboxName" value="selectedLessonAndDate"/>
					<fr:property name="checkboxValue" value="checkBoxValue"/>
					<fr:property name="classes" value="tstyle1 mtop025 mbottom0"/>
					<fr:property name="columnClasses" value="acenter,,,"/>
				</fr:layout>
			</fr:view>

			<table style="width: 500px;" class="tstyle1 mtop0">
				<tr><td><html:submit><bean:message key="label.fill" bundle="DEFAULT"/></html:submit></td></tr>
			</table>

		</fr:form>
	</logic:notEmpty>	


	<bean:define id="insertSummaryLink">/summariesManagement.do?method=prepareInsertSummary&page=0&executionCourseID=<bean:write name="showSummariesBean" property="executionCourse.idInternal"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
	<div class="gen-button mtop1 mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="<%= insertSummaryLink %>">
			<bean:message key="label.insertSummary" bundle="DEFAULT"/>
		</html:link>	
		<bean:message key="link.summary.insert.info" bundle="DEFAULT"/>
	</div>
	
		
	<bean:define id="showSummariesPostBackUrl">/summariesManagement.do?method=showSummariesPostBack&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
	<fr:form>
		<fr:edit id="showSummariesBeanWithChoices" name="showSummariesBean" schema="ShowSummariesFilterToExecutionCourseManagementToDepartmentAdmOffice" nested="true">
			<fr:destination name="postBack" path="<%= showSummariesPostBackUrl %>"/>		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	
	
	<logic:notEmpty name="summaries">
		<logic:iterate name="summaries" id="summary">
			
			<%-- Summary --%>
			<h3 class="mtop2 mbottom05">
				<fr:view name="summary" property="title" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />
				<%-- Order --%>	
				<span class="greytxt1 fwnormal" style="font-size: 0.75em"><bean:write name="summary" property="order"/></span>
			</h3>			
			<logic:equal name="showSummariesBean" property="listSummaryType" value="ALL_CONTENT">				
				<p class="mvert05">
					<fr:view name="summary" property="summaryText" type="net.sourceforge.fenixedu.util.MultiLanguageString" layout="html" />					
				</p>
			</logic:equal>	
			
			<%-- Summary Info --%>		
			<p class="smalltxt greytxt1 mvert025">
				<em>
					<logic:empty name="summary" property="isExtraLesson">
					  	<!-- Normal Summary -->
						<bean:message key="label.lesson" bundle="DEFAULT"/>
					</logic:empty>				
					<logic:notEmpty name="summary" property="isExtraLesson">
						<bean:message key="label.lesson" bundle="DEFAULT"/>
					 	<logic:notEmpty name="summary" property="summaryType">
							<bean:message name="summary" property="summaryType.name" bundle="ENUMERATION_RESOURCES"/>						   	
						</logic:notEmpty>
						<logic:equal name="summary" property="isExtraLesson" value="true">		     
					   		<!-- Extra Summary -->
							<bean:message key="label.extra" bundle="DEFAULT"/>
						</logic:equal>
					</logic:notEmpty>
									       	
					<fr:view name="summary" property="summaryDateYearMonthDay" />
					<fr:view name="summary" property="summaryHourHourMinuteSecond" />
					
					 - 															 
	
					<%-- Students Number --%>
					<logic:present name="summary" property="studentsNumber">			
						<logic:notEmpty name="summary" property="studentsNumber">			
							<bean:define id="studentsAttended" name="summary" property="studentsNumber" />
							<logic:greaterThan name="studentsAttended" value="0">
								<bean:message key="message.studentsnumber.attended.lesson" bundle="DEFAULT" arg0="<%= studentsAttended.toString() %>"/>
							</logic:greaterThan>
							<logic:lessEqual name="studentsAttended" value="0">
								<bean:message key="message.studentsnumber.attended.lesson.no" bundle="DEFAULT"/>				
							</logic:lessEqual>
						</logic:notEmpty>
						<logic:empty name="summary" property="studentsNumber">			
								<bean:message key="message.studentsnumber.attended.lesson.no" bundle="DEFAULT"/>								
						</logic:empty>
					</logic:present>				
					<logic:notPresent name="summary" property="studentsNumber">			
						<bean:message key="message.studentsnumber.attended.lesson.no" bundle="DEFAULT"/>								
					</logic:notPresent>
					
					 - 
						
					<%-- Teacher --%>						
					<logic:notEmpty name="summary" property="professorship">
						<bean:message key="label.teacher" bundle="DEFAULT"/>:&nbsp;				
						<bean:write name="summary" property="professorship.teacher.person.name" /> 
					</logic:notEmpty>
					<logic:notEmpty name="summary" property="teacher">
						<bean:message key="label.teacher" bundle="DEFAULT"/>:&nbsp;								
						<bean:write name="summary" property="teacher.person.name" /> 
					</logic:notEmpty>
					<logic:notEmpty name="summary" property="teacherName">
						<bean:message key="label.teacher" bundle="DEFAULT"/>:&nbsp;								
						<bean:write name="summary" property="teacherName" /> 
					</logic:notEmpty>			

					- 														
					
					<%-- Last Modification Date --%>	
					<span class="px9">
						<bean:message key="label.lastModificationDate" bundle="DEFAULT"/>:&nbsp;	 				
						<fr:view name="summary" property="lastModifiedDateDateTime" />		
					</span>		
				</em>
			</p>
			
			<div class="gen-button">
				<bean:define id="editSummaryLink">/summariesManagement.do?method=prepareEditSummary&page=0&executionCourseID=<bean:write name="showSummariesBean" property="executionCourse.idInternal"/>&summaryID=<bean:write name="summary" property="idInternal"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>				
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
				<html:link page="<%= editSummaryLink %>">
					<bean:message key="button.edit" /> 
				</html:link>				 
				<bean:define id="deleteSummaryLink">/summariesManagement.do?method=deleteSummary&page=0&executionCourseID=<bean:write name="showSummariesBean" property="executionCourse.idInternal"/>&summaryID=<bean:write name="summary" property="idInternal"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
				<html:link page="<%= deleteSummaryLink %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
					<bean:message key="button.delete" />
				</html:link>
			</div>
			
		</logic:iterate>
	</logic:notEmpty>	

</logic:present>