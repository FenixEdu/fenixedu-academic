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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.autoEvaluation"/></h2>

<logic:present role="role(DEPARTMENT_MEMBER)">

	<logic:notEmpty name="expectation">
		
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="expectation" property="executionYear.year"/></em></p>
				
		<bean:define id="executionYearId" name="expectation" property="executionYear.externalId"/>
		<fr:edit name="expectation" slot="autoEvaluation" layout="rich-text">
			<fr:layout name="rich-text">
				<fr:property name="config" value="intermediateWithBreakLineInsteadOfParagraph"/>
				<fr:property name="columns" value="75"/>
				<fr:property name="rows" value="16"/>
			</fr:layout>
			<fr:destination name="success" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="<%= "/teacherExpectationAutoAvaliation.do?method=show&amp;executionYearId=" + executionYearId %>"/>
		</fr:edit>
		
	</logic:notEmpty>
	
</logic:present>

