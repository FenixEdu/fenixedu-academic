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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>

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

	<fr:form action="/viewStudentsByTutor.do?method=viewStudentsByTutor">
		<fr:edit id="performanceBean" name="performanceBean">
			<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean$StudentsPerformanceInfoNullEntryYearBean">
				<fr:slot name="studentsEntryYear" key="label.studentsEntryYear" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher.TutorshipEntryExecutionYearProvider$TutorshipEntryExecutionYearProviderByTeacher"/> 
					<fr:property name="format" value="${year}"/>
					<fr:property name="defaultText" value="<%= "-- " + BundleUtil.getMessageFromModuleOrApplication("application", "label.view.all") +  " --" %>"/>
				</fr:slot>
			</fr:schema>
			<fr:destination name="postBack" path="/viewStudentsByTutor.do?method=viewStudentsByTutor"/>
		</fr:edit>
	</fr:form>
	
	<logic:notEmpty name="tutorshipHistory" property="activeTutorshipsByEntryYear">
		<p class="mtop2 mbottom1 separator2"/>
			<b><bean:message key="label.tutor.tutorshipInfo.currentTutoredStudents" bundle="APPLICATION_RESOURCES"/></b>
		</p>
		<bean:define id="anyVisibleActiveTutorships" value="false"/>
		<logic:iterate id="studentsByTutorBean" name="tutorshipHistory" property="activeTutorshipsByEntryYear">
			<logic:notPresent name="performanceBean" property="studentsEntryYear">
				<bean:define id="anyVisibleActiveTutorships" value="true"/>
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
						<fr:property name="linkFormat(edit)" value="/viewStudentsByTutor.do?method=editStudent&studentID=${student.student.externalId}&registrationID=${student.externalId}" />
						<fr:property name="key(edit)" value="link.edit"/>
						<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES"/>
						<fr:property name="contextRelative(edit)" value="true"/>
						<fr:property name="visibleIf(edit)" value="tutorshipLogEditable"/>
					</fr:layout>
				</fr:view>
			</logic:notPresent>
			<bean:define id="entryYear" name="studentsByTutorBean" property="studentsEntryYear"/>
			<logic:equal name="performanceBean" property="studentsEntryYear" value="<%= entryYear.toString() %>">
				<bean:define id="anyVisibleActiveTutorships" value="true"/>
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
						<fr:property name="linkFormat(edit)" value="/viewStudentsByTutor.do?method=editStudent&studentID=${student.student.externalId}&registrationID=${student.externalId}" />
						<fr:property name="key(edit)" value="link.edit"/>
						<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES"/>
						<fr:property name="contextRelative(edit)" value="true"/>
						<fr:property name="visibleIf(edit)" value="tutorshipLogEditable"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:iterate>
		<logic:equal name="anyVisibleActiveTutorships" value="false">
			<p>
				<em><bean:message key="error.tutor.noActiveTutorships.for.selection" /></em>
			</p>
		</logic:equal>
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
		<bean:define id="anyVisiblePastTutorships" value="false"/>
		<logic:iterate id="studentsByTutorBean" name="tutorshipHistory" property="pastTutorshipsByEntryYear">
			<logic:notPresent name="performanceBean" property="studentsEntryYear">
				<bean:define id="anyVisiblePastTutorships" value="true"/>
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
			</logic:notPresent>
			
			<bean:define id="entryYear" name="studentsByTutorBean" property="studentsEntryYear"/>
			<logic:equal name="performanceBean" property="studentsEntryYear" value="<%= entryYear.toString() %>">
				<bean:define id="anyVisiblePastTutorships" value="true"/>
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
			</logic:equal>
		</logic:iterate>
		<logic:equal name="anyVisiblePastTutorships" value="false">
			<p>
				<em><bean:message key="error.tutor.noPastTutorships.for.selection" /></em>
			</p>
		</logic:equal>
	</logic:notEmpty>
</logic:present>


