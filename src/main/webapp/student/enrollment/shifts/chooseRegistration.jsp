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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="OR_CONDITION" value="false"/>
<logic:present role="role(STUDENT)">
	<bean:define id="OR_CONDITION" value="true"/>
</logic:present>
<logic:present role="role(RESOURCE_ALLOCATION_MANAGER)">
   <bean:define id="OR_CONDITION" value="true"/>
</logic:present>

<logic:equal name="OR_CONDITION" value="true">

    <h2><bean:message key="title.student.shift.enrollment" bundle="STUDENT_RESOURCES"/></h2>

	<logic:messagesPresent message="true">
		<ul class="mtop15 mbottom1 nobullet list2">
			<html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

    <logic:empty name="toEnrol">
    	<span class="error0">
	    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.notoEnrol"/>
    	</span>
    </logic:empty>

	<logic:notEmpty name="toEnrol">
	    <h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="APPLICATION_RESOURCES"/></h3>
		<fr:view name="toEnrol" schema="student.registrationDetail.short" >
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025"/>
				<fr:property name="linkFormat(enrol)" value="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning&registrationOID=${externalId}" />
				<fr:property name="key(enrol)" value="enrol.in.shift"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:equal>
