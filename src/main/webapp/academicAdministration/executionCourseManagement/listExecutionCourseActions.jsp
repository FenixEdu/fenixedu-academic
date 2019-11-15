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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.fenixedu.academic.domain.degree.DegreeType"%>
<%@page import="org.fenixedu.academic.domain.AcademicProgram"%>
<%@page import="org.fenixedu.academic.domain.ExecutionDegree"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<bean:define id="executionPeriodId" name="sessionBean" property="executionPeriod.externalId" />
<bean:define id="executionPeriodQName" name="sessionBean" property="executionPeriod.qualifiedName" />

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="info">
	<p>
		<span class="infoop4">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="info">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<bean:define id="linkGetRequestBigMessage" value="" /> <%-- bean redefined ahead --%>
<bean:define id="linkGetRequestLilMessage" value="" /> <%-- bean redefined ahead --%>
<bean:define id="parametersForManageSeparation" value="" /> <%-- bean redefined ahead --%>
<bean:define id="executionDegreeId" name="sessionBean" /> <%-- bean redefined ahead --%>
<bean:define id="curricularYearId" name="sessionBean" /> <%-- bean redefined ahead --%>
<bean:define id="notLinked" name="sessionBean" property="chooseNotLinked"/>
<logic:equal name="sessionBean" property="chooseNotLinked" value="false">
	<bean:define id="executionDegreeId" name="sessionBean" property="executionDegree.externalId" />
	<bean:define id="executionDegreePName" name="sessionBean" property="executionDegree.presentationName" />
	<bean:define id="curricularYear" name="sessionBean" property="curricularYear.year" />
	<bean:define id="curricularYearId" name="sessionBean" property="curricularYear.externalId" />
	<bean:define id="yearTag">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= curricularYear.toString() + ".ordinal.short" %>"/>
		<bean:message bundle="ENUMERATION_RESOURCES" key="YEAR" />
	</bean:define>
	<bean:define id="notLinkedHardCoded" value="&executionCoursesNotLinked=null" />

	<fr:view name="sessionBean" property="executionPeriod.qualifiedName" /> &nbsp;&gt;&nbsp;
	<b><bean:write name="executionDegreePName"/></b>  &nbsp;&gt;&nbsp;
	<bean:write name="yearTag"/>
	
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + executionPeriodId.toString()
			+ "&executionDegree=" + executionDegreePName.toString() + "~" + executionDegreeId.toString()
			+ "&curYear=" + curricularYear.toString() + "~" + curricularYearId.toString() + notLinkedHardCoded.toString() %>" />

	<bean:define id="linkGetRequestLilMessage"
		value="<%= "&executionPeriodId=" + executionPeriodId.toString() +
	"&executionDegreeId=" + executionDegreeId + "&curYearId=" + curricularYearId.toString() + notLinkedHardCoded.toString()%>" />
	
	<bean:define id="parametersForManageSeparation"
		value="<%="&amp;executionPeriodId=" + executionPeriodId.toString()
				+ "&amp;originExecutionDegreeId="  + executionDegreeId.toString()
				+ "&amp;curricularYearId=" + curricularYearId.toString()
				+ notLinkedHardCoded.toString() %>"/>
</logic:equal>
<logic:equal name="sessionBean" property="chooseNotLinked" value="true">

	<p>
		<fr:view name="sessionBean" property="executionPeriod.qualifiedName" /> &nbsp;&gt;&nbsp;
		<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.chooseNotLinked" /></b>
	</p>

	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + executionPeriodId.toString() +
	"&executionDegree=null~null" + "&curYear=null~null" + "&executionCoursesNotLinked=" + notLinked %>" />

	<bean:define id="linkGetRequestLilMessage"
		value="<%= "&executionPeriodId=" + executionPeriodId.toString() +
	"&executionDegreeId=null" + "&curYearId=null~null" + "&executionCoursesNotLinked=" + notLinked %>" />
	
	<bean:define id="parametersForManageSeparation"
		value="<%="&amp;executionPeriodId=" + executionPeriodId.toString()
				+ "&amp;executionCoursesNotLinked=" + notLinked	%>"/>
	
	<bean:define id="executionDegree" name="sessionBean" />
</logic:equal>


<fr:view name="sessionBean" property="executionCourses">
	<fr:layout name="tabular">
		<fr:property name="linkGroupSeparator" value="&nbsp&nbsp|&nbsp&nbsp" />

		<fr:property name="linkFormat(edit)" value="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=${externalId}" + linkGetRequestBigMessage.toString() %>" />
		<fr:property name="order(edit)" value="1" />
		<fr:property name="key(edit)" value="label.manager.executionCourseManagement.edit" />
		<fr:property name="bundle(edit)" value="MANAGER_RESOURCES" />

		<fr:property name="linkFormat(manageCurricularSeparation)"
					 value="<%="/seperateExecutionCourse.do?method=manageCurricularSeparation"
					 		+ "&amp;executionCourseId=${externalId}"
							+ parametersForManageSeparation.toString() %>"/> 
		<fr:property name="order(manageCurricularSeparation)" value="3" />
		<fr:property name="key(manageCurricularSeparation)" value="link.executionCourseManagement.curricular.manageCurricularSeparation" />
		<fr:property name="bundle(manageCurricularSeparation)" value="MANAGER_RESOURCES" />

		<logic:present name="sessionBean" property="executionDegree">
			<bean:define id="degree" name="sessionBean" property="executionDegree.degree"/>
			<academic:allowed operation="MANAGE_EXECUTION_COURSES_ADV" permission="ACADEMIC_PLANNING_EXECUTIONS" program="<%= (AcademicProgram) degree %>">
		<fr:property name="linkFormat(delete)"
					 value="<%="/editExecutionCourse.do?method=deleteExecutionCourse"
					 		+ "&amp;executionCourseId=${externalId}"
					 		+ linkGetRequestBigMessage.toString() %>" />
		<fr:property name="order(delete)" value="4" />
		<fr:property name="key(delete)" value="label.manager.executionCourseManagement.delete" />
		<fr:property name="visibleIf(delete)" value="deletable" />
		<fr:property name="bundle(delete)" value="MANAGER_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.manager.delete.selected.executionCourses.certainty"/>
		<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="${nome},${sigla}"/>
			</academic:allowed>
		</logic:present>
		<logic:notPresent name="sessionBean" property="executionDegree">
			<academic:allowed operation="MANAGE_EXECUTION_COURSES_ADV" permission="ACADEMIC_PLANNING_EXECUTIONS" program="<%= null %>">
		<fr:property name="linkFormat(delete)"
					 value="<%="/editExecutionCourse.do?method=deleteExecutionCourse"
					 		+ "&amp;executionCourseId=${externalId}"
					 		+ linkGetRequestBigMessage.toString() %>" />
		<fr:property name="order(delete)" value="4" />
		<fr:property name="key(delete)" value="label.manager.executionCourseManagement.delete" />
		<fr:property name="visibleIf(delete)" value="deletable" />
		<fr:property name="bundle(delete)" value="MANAGER_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.manager.delete.selected.executionCourses.certainty"/>
		<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="${nome},${sigla}"/>
			</academic:allowed>
		</logic:notPresent>

		<fr:property name="linkFormat(sentEmails)" value="/viewSentEmails.do?method=viewSentEmails&senderId=${sender.externalId}" />
		<fr:property name="order(sentEmails)" value="5" />
		<fr:property name="key(sentEmails)" value="link.manager.email.sender" />
		<fr:property name="bundle(sentEmails)" value="MANAGER_RESOURCES" />
		<fr:property name="visibleIf(sentEmails)" value="hasSender" />
		<fr:property name="module(sentEmails)" value="/messaging" />
		
	
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,,tdclear tderror1" />

	</fr:layout>

	<fr:schema type="org.fenixedu.academic.domain.ExecutionCourse" bundle="MANAGER_RESOURCES">
		<fr:slot name="nome" key="label.manager.teachersManagement.executionCourseName" />
		<fr:slot name="sigla" key="label.manager.curricularCourse.code" />
	</fr:schema>
</fr:view>
