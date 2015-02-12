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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="title.candidacies" /></h2>


<table class="tstyle4 thlight">
	<tr>
		<th><bean:message key="org.fenixedu.academic.domain.candidacy.Candidacy.number" /></th>
		<th><bean:message key="org.fenixedu.academic.domain.candidacy.StudentCandidacy.executionDegree.degreeCurricularPlan.degree.name" /></th>
		<th><bean:message key="org.fenixedu.academic.domain.candidacy.StudentCandidacy.executionDegree.executionYear.year" /></th>
		<th><bean:message key="org.fenixedu.academic.domain.candidacy.Candidacy.activeCandidacySituation.candidacySituationType" /></th>
		<th></th>
	</tr>
	
<logic:iterate id="candidacy" name="LOGGED_USER_ATTRIBUTE" property="person.candidacies">
	<logic:notEmpty name="candidacy" property="candidacySituations">
	<bean:define id="externalId" name="candidacy" property="externalId" />
	<tr>
		<td class="acenter">
			<bean:write name="candidacy" property="number" />
		</td>
		<logic:notEmpty name="candidacy" property="executionDegree">
			<td>			
				<bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.degree.name" /> - <bean:write name="candidacy" property="executionDegree.degreeCurricularPlan.name" />
			</td>
			<td>
				<bean:write name="candidacy" property="executionDegree.executionYear.year" />			
			</td>
		</logic:notEmpty>
		<logic:empty name="candidacy" property="executionDegree">
			<td></td>
			<td></td>
		</logic:empty>
		<td>
			<bean:message name="candidacy" property="activeCandidacySituation.candidacySituationType.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
		</td>
		<td>
			<logic:equal name="candidacy" property="class.name" value="org.fenixedu.academic.domain.candidacy.DegreeCandidacy">
				<html:link action="<%="/degreeCandidacyManagement.do?method=showCandidacyDetails&amp;candidacyID=" + externalId%>">
					<bean:message key="link.viewCandidacyDetails"/>
				</html:link>
			</logic:equal>
			<logic:equal name="candidacy" property="class.name" value="org.fenixedu.academic.domain.candidacy.IMDCandidacy">
				<html:link action="<%="/degreeCandidacyManagement.do?method=showCandidacyDetails&amp;candidacyID=" + externalId%>">
					<bean:message key="link.viewCandidacyDetails"/>
				</html:link>
			</logic:equal>
			<logic:equal name="candidacy" property="class.name" value="org.fenixedu.academic.domain.candidacy.DFACandidacy">
				<html:link action="<%="/viewCandidacies.do?method=viewDetail&amp;candidacyID=" + externalId%>">
					<bean:message key="link.viewCandidacyDetails"/>
				</html:link>
			</logic:equal>
		</td>
	</tr>
	</logic:notEmpty>
</logic:iterate>
</table>