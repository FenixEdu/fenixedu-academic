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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2 class="mtop15 mbottom025"><bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processName" name="processName"/>
<bean:define id="processId" name="process" property="externalId" />

<br/>

<academic:allowed operation="MANAGE_STUDENT_PAYMENTS">
<logic:equal name="process" property="isCandidacyInternal" value="true">
	<bean:define id="personId" name="process" property="candidacy.personalDetails.person.externalId" />
	<html:link action='<%= "payments.do?method=showOperations&amp;personId=" + personId.toString() %>' target="_blank">
		<bean:message key="label.payments.management" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</logic:equal>
</academic:allowed>

<logic:equal name="process" property="isCandidacyInternal" value="false">
	<bean:message key="label.payments.no.payments"/>
	<bean:message key="message.candidacy.not.bind.person.create.payment" bundle="CANDIDATE_RESOURCES"/>
	
	<p>
	<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteBindPersonToCandidacy&amp;processId=" + processId.toString() %>'>
		<bean:message key="label.bind.person" bundle="CANDIDATE_RESOURCES"/>	
	</html:link>
	</p>
	
</logic:equal>

<p>
<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
</p>