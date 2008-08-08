<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- showTutorPerformanceGrid.jsp -->

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.tutor.tutorships" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="/tutorTutorship.do?method=viewStudentsByTutor">

	<fr:edit id="filterForm" name="tutorateBean" schema="tutorship.tutor.number">
		<fr:edit id="tutorateBean" name="tutorateBean" visible="false" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/tutorTutorship.do?method=prepareTutorSearch" />
	</fr:edit>

	<html:submit>
		<bean:message key="label.submit" bundle="PEDAGOGICAL_COUNCIL" />
	</html:submit>

</fr:form>


<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
<br/><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>

<logic:present name="tutor">
	<logic:notPresent name="tutorshipHistory">
	<br/><em><bean:message key="error.tutor.noStudent" bundle="APPLICATION_RESOURCES" /></em>
	</logic:notPresent>
</logic:present>

<logic:present name="tutorshipHistory">
	<fr:view name="tutorshipHistory" schema="teacher.tutorshipHistory.resume">
		<fr:layout name="tabular">
	  	    <fr:property name="classes" value="tstyle2 thlight thright"/>
	   </fr:layout>
	</fr:view>
	
	<logic:notEmpty name="tutorshipHistory" property="activeTutorshipsByEntryYear">
		<p class="mtop2 mbottom1 separator2"/>
			<b><bean:message key="label.tutor.tutorshipInfo.currentTutoredStudents" bundle="APPLICATION_RESOURCES"/></b>
		</p>
		<logic:iterate id="studentsByTutorBean" name="tutorshipHistory" property="activeTutorshipsByEntryYear">
			<ul>
				<li>
					<bean:message key="label.studentsEntryYear" />
					<b><bean:write name="studentsByTutorBean" property="studentsEntryYear.year"/></b>
				</li>
			</ul>
			<bean:define id="students" name="studentsByTutorBean" property="studentsList" />
			<fr:view name="students" layout="tabular" schema="teacher.viewStudentsByTutor">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 thlight mtop0 mbottom15 tdcenter"/>
					<fr:property name="columnClasses" value=",aleft,,,"/>
				</fr:layout>
			</fr:view>
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:empty name="tutorshipHistory" property="activeTutorshipsByEntryYear">
		<p>
			<em><bean:message key="error.tutor.noActiveTutorships" /></em>
		</p>
	</logic:empty>

	<logic:notEmpty name="tutorshipHistory" property="pastTutorshipsByEntryYear">
		<p class="mtop2 mbottom1 separator2"/>
			<b><bean:message key="label.tutor.tutorshipInfo.pastTutoredStudents" bundle="APPLICATION_RESOURCES"/></b>
		</p>
		<logic:iterate id="studentsByTutorBean" name="tutorshipHistory" property="pastTutorshipsByEntryYear">
			<ul>
				<li>
					<bean:message key="label.studentsEntryYear" />
					<b><bean:write name="studentsByTutorBean" property="studentsEntryYear.year"/></b>
				</li>
			</ul>
			<bean:define id="students" name="studentsByTutorBean" property="studentsList" />
			<fr:view name="students" layout="tabular" schema="teacher.viewStudentsByTutor">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 thlight mtop0 mbottom15 tdcenter"/>
					<fr:property name="columnClasses" value=",aleft,,,"/>
				</fr:layout>
			</fr:view>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>



