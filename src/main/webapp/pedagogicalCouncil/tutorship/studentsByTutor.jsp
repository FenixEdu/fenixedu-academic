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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.tutorship.tutor.tutorships" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="/viewStudentsByTutor.do?method=viewStudentsByTutor">
    <fr:edit id="tutorateBean" name="tutorateBean" schema="tutorship.teacher.search">
        <fr:layout>
            <fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        <fr:destination name="departmentPostBack" path="/viewStudentsByTutor.do?method=viewStudentsByTutor"/>
        <fr:destination name="teacherSelection" path="/viewStudentsByTutor.do?method=viewStudentsByTutor"/>
        <fr:destination name="invalid" path="/viewStudentsByTutor.do?method=prepareTutorSearch" />
    </fr:edit>
</fr:form>

<br/>

<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>

<logic:present name="tutor">
	<logic:notPresent name="tutorshipHistory">
	<em><bean:message key="error.tutor.noStudent" bundle="APPLICATION_RESOURCES" /></em>
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
			<fr:view name="students" layout="tabular" schema="tutorship.tutorate.student.byTutor">
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



