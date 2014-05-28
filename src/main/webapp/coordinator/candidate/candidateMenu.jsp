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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<bean:message key="label.coordinator.candidate" />
<br/>
<ul>
	<li><html:link
		page='<%= "/candidateOperation.do?method=getCandidates&action=visualize&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message key="link.coordinator.visualizeCandidate" />
	</html:link></li>
	
	<li><html:link
		page='<%= "/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message key="link.coordinator.approveCandidates" />
	</html:link></li>
	
	<li><html:link titleKey="link.masterDegree.administrativeOffice.makeStudyPlan.title" 
		page='<%= "/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&amp;page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message 
			key="link.masterDegree.administrativeOffice.makeStudyPlan" />
	</html:link></li>
	
	<li><html:link titleKey="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu.title" 
		page='<%= "/printAllCandidatesList.do?method=prepare&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message
			key="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu" />
	</html:link></li>
</ul>
