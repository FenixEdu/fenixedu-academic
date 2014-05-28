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
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />

<html:link action='<%= "/caseHandlingOver23CandidacyProcess.do?method=listProcesses&amp;processId=" + processId.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
</html:link>
<br/>
<br/>

<h3><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></h3>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<ul>
	<li>
		<html:link action='<%= "/caseHandlingOver23CandidacyProcess.do?method=prepareIntroduceCandidacyResults&amp;processId=" + processId.toString() %>'>
			« <bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</li>
</ul>
<br/>

<fr:view name="over23IndividualCandidacyResultBeans" schema="Over23IndividualCandidacyResultBean.manage.all.students">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 mtop025"/>
	</fr:layout>
</fr:view>
