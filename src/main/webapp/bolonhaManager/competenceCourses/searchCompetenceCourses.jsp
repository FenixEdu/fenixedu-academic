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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="bolonhaManager" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.search.competenceCourses" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<fr:form action="/competenceCourses/searchCompetenceCourses.do?method=search">
	<table>
		<tr><td>
			<fr:edit id="searchBean" name="searchBean">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.SearchCompetenceCoursesDA$SearchCompetenceCourseBean" bundle="BOLONHA_MANAGER_RESOURCES">
					<fr:slot name="searchName"/>
					<fr:slot name="searchCode"/>
				</fr:schema>
			   	<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thmiddle mvert05"/>
				</fr:layout>
			</fr:edit>
		</td><td>			
			<html:submit><bean:message key="label.search" bundle="BOLONHA_MANAGER_RESOURCES"/></html:submit>
		</td></tr>
	</table>
</fr:form>

<logic:present name="searchResults">
	<bean:define id="searchName" name="searchBean" property="searchName"/>
	<bean:define id="searchCode" name="searchBean" property="searchCode"/>
	<fr:view name="searchResults">
		<fr:schema type="net.sourceforge.fenixedu.domain.CompetenceCourse" bundle="BOLONHA_MANAGER_RESOURCES">
			<fr:slot name="name"/>
			<fr:slot name="code"/>
			<fr:slot name="departmentUnit.acronym"/>
			<fr:slot name="curricularStage"/>
		</fr:schema>
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
			<fr:property name="sortParameter" value="sortBy"/>
       		<fr:property name="sortUrl" value="<%= "/competenceCourses/searchCompetenceCourses.do?method=sortSearch&searchName=" + searchName + "&searchCode=" + searchCode %>"/>
			<fr:property name="sortableSlots" value="name, code"/>
			<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "curricularStage=desc, departmentUnit.acronym=asc, name=asc" : request.getParameter("sortBy") %>"/>
			<fr:link name="showApprovedCompetenceCourse" label="show" link="/competenceCourses/showCompetenceCourse.faces?action=ccm&competenceCourseID=${externalId}" target="_blank" condition="loggedPersonAllowedToView"/>
		</fr:layout>
	</fr:view>
</logic:present>
