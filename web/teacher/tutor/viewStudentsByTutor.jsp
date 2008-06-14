<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.teacher.tutor.operations" /></em>
<h2><bean:message key="label.teacher.tutor.viewStudentsByTutor"/></h2>


<!-- AVISOS E ERROS -->
<p>
	<em>
		<html:errors/>
		<logic:notPresent name="tutorshipHistory">
			<bean:message key="error.tutor.noStudent" />
		</logic:notPresent>
	</em>
</p>

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


