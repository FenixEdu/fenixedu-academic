<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="executionDegreeId" name="executionDegreeId" />
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />
<bean:define id="parameters" value="<%= "executionDegreeId=" + executionDegreeId + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" />

<h2><bean:message key="label.coordinator.createTutorships" bundle="APPLICATION_RESOURCES" /></h2>

<logic:notPresent name="selectedStudentsBean">
	<p class="mvert2 breadcumbs">
		<span class="actual">
		<strong><bean:message key="label.coordinator.tutor.createTutorship.step" bundle="APPLICATION_RESOURCES"/> 1 : </strong>
		<bean:message key="label.coordinator.tutor.createTutorship.students" bundle="APPLICATION_RESOURCES" /></span>
			 > 
		<span>
		<strong><bean:message key="label.coordinator.tutor.createTutorship.step" bundle="APPLICATION_RESOURCES" /> 2 : </strong>
		<bean:message key="label.coordinator.tutor.createTutorship.tutor" bundle="APPLICATION_RESOURCES" /></span>
 	</p>

	<p class="mtop2 mbottom05">
		<b><bean:message key="label.coordinator.tutor.createTutorship.selectEntryYear" bundle="APPLICATION_RESOURCES" /></b></p>

	<p class="color888 mvert05">
		<bean:message key="label.coordinator.tutor.createTutorship.selectEntryYear.help" bundle="APPLICATION_RESOURCES" /></p>
	<fr:view name="studentsWithoutTutor" schema="coordinator.createTutorship.studentsWithoutTutorship">
		<fr:layout name="tabular">
	   	    <fr:property name="classes" value="tstyle2 thlight thright mvert05 acenter"/>
	    </fr:layout>
	</fr:view>
</logic:notPresent>
<logic:present name="selectedStudentsBean">

	<ul>
		<li>
			<html:link page="<%= "/createTutorship.do?method=prepareCreateTutorships&" + parameters %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link>
		</li>
	</ul>

	<p class="mvert15 breadcumbs">
		<span>
		<strong><bean:message key="label.coordinator.tutor.createTutorship.step" bundle="APPLICATION_RESOURCES"/> 1 : </strong>
		<bean:message key="label.coordinator.tutor.createTutorship.students" bundle="APPLICATION_RESOURCES" /></span>
			 > 
		<span class="actual">
		<strong><bean:message key="label.coordinator.tutor.createTutorship.step" bundle="APPLICATION_RESOURCES" /> 2 : </strong>
		<bean:message key="label.coordinator.tutor.createTutorship.tutor" bundle="APPLICATION_RESOURCES" /></span>
 	</p>
</logic:present>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<!-- TUTORSHIP CREATION FORMS -->
<!-- IT SHOWS ALL STUDENTS FROM SELECTED ENTRY YEAR BEAN -->
<logic:present name="filteredStudentsBean">
	<bean:define id="entryYear" name="filteredStudentsBean" property="executionYear.year" />
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.coordinator.tutor.createTutorship.selectStudents" bundle="APPLICATION_RESOURCES" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.coordinator.tutor.createTutorship.selectStudents.randomNumber.help" bundle="APPLICATION_RESOURCES" /></p>
		
	<!-- Specify a number of students to select -->
	<fr:form action="/createTutorship.do?method=prepareSelectGivenNumberOfTutorships">
		<fr:edit id="studentsWithoutTutor" name="studentsWithoutTutor" visible="false" />
		<fr:edit id="filteredStudentsBean" name="filteredStudentsBean" visible="false" />
		<fr:edit id="numberOfStudentsBean" name="filteredStudentsBean" schema="coordinator.createTutorship.randomNumber">
			<fr:destination name="invalid" path="<%= "/createTutorship.do?method=prepareCreateTutorships&selectedEntryYear=" + entryYear + "&" +  parameters %>" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 mbottom0" />
				<fr:property name="columnClasses" value="width100px,width100px,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width100px">
					<html:submit><bean:message key="button.coordinator.tutor.select" bundle="APPLICATION_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
	<br />
	<!-- Select, one by one, each student -->
	<fr:form action="/createTutorship.do?method=selectStudentsToCreateTutorships">
		<html:submit><bean:message key="button.coordinator.tutor.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="clearSelection"><bean:message key="button.coordinator.tutor.clear" bundle="APPLICATION_RESOURCES" /></html:submit>
		<fr:edit id="studentsWithoutTutor" name="studentsWithoutTutor" visible="false" />
		<fr:edit id="selectedStudentsBean" name="filteredStudentsBean" schema="coordinator.createTutorship.studentsListTabularOption">
			<fr:destination name="invalid" path="<%= "/createTutorship.do?method=prepareCreateTutorships&selectedEntryYear=" + entryYear + "&" +  parameters %>" />
			<fr:destination name="cancel" path="<%= "/chooseDegree.do?degreeCurricularPlanID=" + degreeCurricularPlanID %>" />
			<fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.coordinator.tutor.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="clearSelection"><bean:message key="button.coordinator.tutor.clear" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>
</logic:present>

<!-- SELECTED STUDENTS -->
<!-- CHOOSE TUTOR TO ASSOCIATE TO SELECTED STUDENTS -->
<logic:present name="selectedStudentsBean">
	<bean:define id="entryYear" name="selectedStudentsBean" property="executionYear.year" />
	<fr:view name="selectedStudentsBean" schema="coordinator.createTutorship.selectedStudentsInfo">
		<fr:layout name="tabular">
	  	    <fr:property name="classes" value="tstyle2 thlight thright"/>
	  	    <fr:property name="columnClasses" value=", aleft"/>
	   </fr:layout>
	</fr:view>
	
	<!-- Students list -->
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.coordinator.tutor.createTutorship.selectedStudents" bundle="APPLICATION_RESOURCES" /></b></p>
	<fr:view name="selectedStudentsBean" property="studentsToCreateTutorshipList" schema="coordinator.createTutorship.studentsList">
		<fr:layout name="tabular">
	  	    <fr:property name="classes" value="tstyle2 thlight tdcenter mtop05"/>
	  	    <fr:property name="columnClasses" value=",aleft,,,"/>
	   </fr:layout>
	</fr:view>
	
	<!-- Get teachers list -->
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.coordinator.tutor.createTutorship.selectTutor" bundle="APPLICATION_RESOURCES" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.coordinator.tutor.createTutorship.selectTutor.help" bundle="APPLICATION_RESOURCES" /></p>
	<ul>
		<li><p class="mtop1 mbottom2">
			<html:link target="_blank" page="<%= "/tutorManagement.do?method=prepare&forwardTo=prepareChooseTutorHistory&" + parameters %>">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.tutor.chooseTutorFromListLink" /></html:link></p>
		</li>
	</ul>
	
	<!-- CHOOSE TUTOR -->
	<fr:form action="/createTutorship.do?method=prepareCreateTutorshipForSelectedStudents#confirmTutor">
		<fr:edit id="selectedStudentsBean" name="selectedStudentsBean" schema="coordinator.createTutorship.tutorNumber">
			<fr:destination name="invalid" path="/createTutorship.do?method=prepareSelectTutor"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
				<fr:property name="columnClasses" value="width110px,width300px,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width110px"></td>
				<td class="width300px">
					<html:submit><bean:message key="button.coordinator.tutor.chooseTutor" bundle="APPLICATION_RESOURCES"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
	
	<!-- CONFIRM TUTOR -->
	<logic:notEqual name="selectedStudentsBean" property="teacherNumber" value="">
		<p class="mbottom05"><span class="warning0" id="confirmTutor">
			<bean:message key="label.coordinator.tutor.createTutorship.confirmTutor" bundle="APPLICATION_RESOURCES"/></span></p>
		<fr:form action="/createTutorship.do?method=createTutorshipForSelectedStudentsAndTutor">
			<fr:edit id="selectedStudentsAndTutorBean" name="selectedStudentsBean" schema="coordinator.createTutorship.tutorName">
				<fr:destination name="invalid" path="/createTutorship.do?method=prepareSelectTutor"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
					<fr:property name="columnClasses" value="width110px,width300px,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
			<table class="tstyle5 gluetop mtop0">
				<tr>
					<td class="width110px"></td>
					<td class="width300px">
						<html:submit><bean:message key="button.coordinator.tutor.createTutorships" bundle="APPLICATION_RESOURCES"/></html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
	</logic:notEqual>
</logic:present>

