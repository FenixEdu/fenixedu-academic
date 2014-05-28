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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><strong><bean:message key="label.candidacy.dfa.periodsManagement.candidacyPeriod" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error">
		<bean:write name="message" />
	</span>
	<br />
</html:messages>

<h3><strong><bean:write name="executionDegree" property="degree.name"/> - <bean:write name="executionDegree" property="degreeCurricularPlan.name"/> </strong></h3>



<bean:define id="executionYearId" name="executionDegree" property="executionYear.externalId" />
<logic:present name="candidacyPeriod">
	<fr:hasMessages for="editCandidacyPeriod" type="conversion">
		<ul>
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
		</ul>
	</fr:hasMessages>
	<fr:edit id="editCandidacyPeriod"
			name="candidacyPeriod"
			schema="CandidacyPeriodInDegreeCurricularPlan.edit"
			action="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:destination name="cancel" path="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="candidacyPeriod">
	<fr:hasMessages for="createCandidacyPeriod" type="conversion">
		<ul>
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
		</ul>
	</fr:hasMessages>
	<fr:create 	id="createCandidacyPeriod"
				schema="CandidacyPeriodInDegreeCurricularPlan.create"
				type="net.sourceforge.fenixedu.domain.CandidacyPeriodInDegreeCurricularPlan"
				action="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:hidden slot="degreeCurricularPlan" name="executionDegree" property="degreeCurricularPlan"/>
		<fr:hidden slot="executionYear" name="executionDegree" property="executionYear"/>
		<fr:destination name="cancel" path="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>"/>
	</fr:create>
</logic:notPresent>

