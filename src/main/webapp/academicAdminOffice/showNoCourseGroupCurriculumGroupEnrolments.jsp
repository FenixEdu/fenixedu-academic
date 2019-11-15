<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<bean:define id="type" name="enrolmentBean" property="groupType"/>
<bean:define id="actionName" name="actionName" />

<h2><strong><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="<%= type.toString() %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>
<html:messages id="error" message="true" bundle="APPLICATION_RESOURCES" property="enrolmentError" >
	<br/>
	<span class="error"><!-- Error messages go here --><bean:write name="error" /></span>
	<br/>
</html:messages>
<logic:messagesPresent message="true" property="warning" >
	<div class="warning0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.enrollment.warnings.in.enrolment" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
</logic:messagesPresent>

<bean:define id="bean" name="enrolmentBean" type="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean"/>

<fr:form action='<%= "/" + actionName + ".do" %>'>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseCurricular"/>
	<fr:edit id="enrolmentBean" name="enrolmentBean" visible="false"/>
	<logic:present name="enrolments">
		<bean:define id="url">/<%= actionName %>.do?method=delete&amp;enrolment=${externalId}&amp;scpID=<%= bean.getStudentCurricularPlan().getExternalId().toString() %>&amp;executionPeriodID=<%= bean.getExecutionPeriod().getExternalId().toString() %></bean:define>
		<fr:view name="enrolments" property="enrolments" schema="student.no.course.group.enrolments">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		      	<fr:property name="columnClasses" value="listClasses,,"/>

				<fr:property name="linkFormat(unenrol)" value="<%= url %>" />
				<fr:property name="key(unenrol)" value="link.student.unenrol"/>
				<fr:property name="bundle(unenrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(unenrol)" value="true"/>
				<fr:property name="confirmationKey(unenrol)" value="label.student.noCourseGroupCurriculumGroup.unenrol.confirmation.message" />
				<fr:property name="confirmationBundle(unenrol)" value="ACADEMIC_OFFICE_RESOURCES" />

				<fr:property name="sortBy" value="executionPeriod=desc,name=asc" />
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="enrolments">
		<p class="mtop2">
			<em><bean:message key="label.no.extra.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:notPresent>
	
	<p class="mtop15">
	
		<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.enrol"/></html:submit>
		
		<%-- For now only STANDALONE can enrol without rules 
			method chooseCurricularWithoutRules only exists in StudentStandaloneEnrolmentsDA. 
			If necessary can be moved to superclass
		--%>
		<logic:equal name="enrolmentBean" property="groupType" value="STANDALONE">
			<academic:allowed operation="ENROLMENT_WITHOUT_RULES" permission="ACADEMIC_OFFICE_CURRICULUM_MOVE_LINES_ADMIN" program="<%= bean.getStudentCurricularPlan().getDegree() %>">
				<html:submit onclick="this.form.method.value='chooseCurricularWithoutRules'; return true;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.enrol.without.rules"/></html:submit>
			</academic:allowed>
		</logic:equal>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='back'; return true;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="back"/></html:submit>
	</p>
</fr:form>
