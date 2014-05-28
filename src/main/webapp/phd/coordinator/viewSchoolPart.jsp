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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd"%>

<logic:present name="registrationConclusionBean">
	<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
	
	<table>
		<tr>
			<%-- view registration conclusion information --%>
			<td>
				<fr:view schema="PhdIndividualProgramProcess.view.registration.conclusion.bean" name="registrationConclusionBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10" />
					</fr:layout>
				</fr:view>
			</td>
			
			<%-- show operations --%>
			<td>
				<ul class="operations">
					<li>
						<html:link action="/phdIndividualProgramProcess.do?method=viewCurriculum" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.view.curriculum"/>
						</html:link>
					</li>
					<li>
						<html:link action="/phdIndividualProgramProcess.do?method=manageEnrolments" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.enrolments"/>
						</html:link>
					</li>
				</ul>
			</td>
		</tr>
	</table>
	<br/>
</logic:present>

<logic:notPresent name="registrationConclusionBean">
	<logic:notEmpty name="process" property="studyPlan">
		<strong><bean:message key="label.phd.studyPlan" bundle="PHD_RESOURCES" /></strong>
		<fr:view schema="PhdStudyPlan.description" name="process" property="studyPlan">
			<fr:layout name="tabular"><fr:property name="classes" value="tstyle2 thlight mtop10" /></fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:notPresent>
