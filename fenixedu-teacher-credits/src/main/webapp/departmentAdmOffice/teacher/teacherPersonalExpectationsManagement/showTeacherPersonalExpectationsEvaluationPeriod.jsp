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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.defineTeacherPersonalExpectationsEvaluationPeriod"/></h2>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">

	<logic:messagesPresent message="true">
		<p class="mtop2">
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<logic:notEmpty name="bean">
		
		<fr:form action="/teacherPersonalExpectationsEvaluationPeriod.do?method=showPeriodWithSelectedYear">
			<div class="mtop2 mbottom1">
			<bean:message key="label.common.executionYear"/>:
			<fr:edit id="executionYear" name="bean" slot="executionYear"> 
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:destination name="postback" path="/teacherPersonalExpectationsEvaluationPeriod.do?method=showPeriodWithSelectedYear"/>
				</fr:layout>
			</fr:edit>
			</div>
		</fr:form>

		<logic:notEmpty name="period">
			<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
			<fr:view name="period" schema="editTeacherPersonalExpectationsEvaluationPeriod">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright"/>
				</fr:layout>
			</fr:view>
			<ul class="list5">
				<li>
					<html:link page="<%= "/teacherPersonalExpectationsEvaluationPeriod.do?method=editPeriod&executionYearId=" + executionYearId.toString() %>">
					  <bean:message key="link.edit"/>
					</html:link>
				</li>
			</ul>
		</logic:notEmpty>
		
		<logic:notEmpty name="bean" property="executionYear">
			<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
			<logic:empty name="period">
				<p class="mtop15 mbottom1"><em><bean:message key="label.teacherPersonalExpectationsEvaluationPeriodNotAvailable"/></em></p>
				<ul class="list5">
					<li>
						<html:link page="<%= "/teacherPersonalExpectationsEvaluationPeriod.do?method=createPeriod&executionYearId=" + executionYearId.toString() %>">
							<bean:message key="label.teacher-institution-working-time.create"/>
						</html:link>
					</li>
				</ul>
			</logic:empty>
		</logic:notEmpty>	
		
	</logic:notEmpty>
	
</logic:present>