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
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.selectPerson" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 3</strong>: <bean:message key="label.candidacy.commonCandidacyInformation" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 4</strong>: <bean:message key="label.candidacy.candidacyInformation" bundle="APPLICATION_RESOURCES" /> </span>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />

<fr:form action='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?parentProcessId=" + parentProcessId.toString() %>'>

 	<html:hidden property="method" value="createNewProcess" />
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
	
		<fr:edit id="individualCandidacyProcessBean.candidacyDate" 
			 name="individualCandidacyProcessBean"
			 schema="StandaloneIndividualCandidacyProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
		</fr:edit>
	
		<h3 class="mtop15 mbottom025"><bean:message key="label.curricularCourses" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="individualCandidacyProcessBean.searchCurricularCourseByDegree" 
				 name="individualCandidacyProcessBean" property="searchCurricularCourseByDegree"
				 schema="StandaloneIndividualCandidacyProcessBean.add.curricularCourse">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="searchCurricularCourseByDegreePostBack" path='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=searchCurricularCourseByDegreePostBack&amp;parentProcessId=" + parentProcessId.toString() %>' />
			<fr:destination name="invalid" path='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
		</fr:edit>
		<html:submit onclick="this.form.method.value='addCurricularCourse'; return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<br/>
		<br/>		

		<logic:notEmpty name="individualCandidacyProcessBean" property="curricularCourseBeans">
			<h3 class="mtop15 mbottom025"><bean:message key="label.curricularCourses" bundle="APPLICATION_RESOURCES"/>:</h3>
			<table class="tstyle1 mtop025">
				<logic:iterate id="curricularCourseBean" name="individualCandidacyProcessBean" property="curricularCourseBeans">
					<tr>
						<td class="nowrap"><bean:write name="curricularCourseBean" property="curricularCourseName"/></td>
						<td><bean:write name="curricularCourseBean" property="curricularCourseEcts"/></td>
						<td class="nowrap"><bean:write name="curricularCourseBean" property="degreeName"/></td>
						<td>
							<bean:define id="curricularCourseBeanKey" name="curricularCourseBean" property="key" />
							<html:hidden property="curricularCourseBeanKey" value="<%= curricularCourseBeanKey.toString() %>" />
							<html:submit onclick="this.form.method.value='removeCurricularCourse';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
						</td>
					</tr>
				</logic:iterate>
				<tr align="right">
					<td colspan="4"><em><strong><bean:message key="label.total.ects" bundle="APPLICATION_RESOURCES" />:</strong> <bean:write name="individualCandidacyProcessBean" property="totalEctsCredits" /></em></td>
				</tr>
			</table>
		</logic:notEmpty>
		<logic:empty name="individualCandidacyProcessBean" property="curricularCourseBeans">
			<strong><em><bean:message key="label.candidacy.standalone.no.curricularCourses" bundle="APPLICATION_RESOURCES"/></em></strong>
		</logic:empty>

	</logic:notEmpty>

	<br/>		
	<br/>
	
	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
