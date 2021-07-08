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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%@page import="org.fenixedu.academic.ui.struts.action.BolonhaManager.CompetenceCourseInformationRequestBean"%><html:xhtml/>
<%@page import="org.fenixedu.academic.domain.ExecutionSemester"%><html:xhtml/>


<logic:notEmpty name="departments">

<h2><bean:write name="department" property="realName"/></h2>
<div class="dropdown">
  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    <bean:write name="department" property="realName"/>
    <span class="caret"></span>
  </button>
  <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
    <logic:iterate id="dep" name="departments">
        <li>
            <html:link action="/competenceCourses/manageVersions.do?method=prepare&departmentID=${dep.externalId}">
                <bean:write name="dep" property="realName"/>
            </html:link>
        </li>
    </logic:iterate>
  </ul>
</div>
<h3><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h3>

<logic:notEmpty name="competenceCourseMembersGroupMembers">

<p class="mtop15 mbottom05"><strong class='highlight1'><bean:message key="groupMembers" bundle="BOLONHA_MANAGER_RESOURCES"/></strong> <bean:message key="label.group.members.explanation" bundle="BOLONHA_MANAGER_RESOURCES"/></p>

	<ul>
		<logic:iterate id="user" name="competenceCourseMembersGroupMembers">
			<logic:notEmpty name="user" property="person">
				<li><fr:view name="user" property="person" layout="name-with-alias"/></li>
			</logic:notEmpty>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<logic:empty name="competenceCourseMembersGroupMembers">
	<p>
		<em><bean:message key="label.empty.group.members" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
	</p>
</logic:empty>

<bean:define id="showOldCompetenceCourses" name="requestBean" property="isShowOldCompetenceCourses" />
<p>
<fr:form id="requestForm" action="/competenceCourses/manageVersions.do?method=prepare">
	<fr:edit id="requestBean" name="requestBean">
		<fr:schema bundle="BOLONHA_MANAGER_RESOURCES" type="org.fenixedu.academic.ui.struts.action.BolonhaManager.CompetenceCourseInformationRequestBean">
			<fr:slot name="showOldCompetenceCourses" layout="option-select-postback"/>
		</fr:schema>
		<fr:layout>
			<fr:property name="classes" value="thlight thpadding0px mtop05"/>
		</fr:layout>
	</fr:edit>
</fr:form>
</p>

<div class="mtop15">
<logic:equal name="department" property="currentUserMemberOfCompetenceCourseMembersGroup" value="true">
	<fr:view name="department">
		<fr:layout name="competence-course-list">
			<fr:property name="scientificAreaNameClasses" value="bold"/>
			<fr:property name="showOldCompetenceCourses" value="<%= ((Boolean) showOldCompetenceCourses) ? "true" : "false" %>"/>
			<fr:property name="tableClasses" value="smallmargin mtop05 table-condensed"/>
			<fr:property name="link(manageVersions)" value="/competenceCourses/manageVersions.do?method=showVersions"/>
			<fr:property name="key(manageVersions)" value="label.view.versions"/>
			<fr:property name="bundle(manageVersions)" value="BOLONHA_MANAGER_RESOURCES" />
			<fr:property name="param(manageVersions)" value="externalId/competenceCourseID" />
			<fr:property name="order(manageVersions)" value="1"/>
			<fr:property name="filterBy" value="APPROVED"/>
		</fr:layout>
	</fr:view>
</logic:equal>
</div>

<logic:equal name="department" property="currentUserMemberOfCompetenceCourseMembersGroup" value="false">
	<em><bean:message key="notMemberInCompetenceCourseManagementGroup" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
</logic:equal>

</logic:notEmpty>

<logic:empty name="departments">
    <p>
        <em><bean:message key="no.current.department.working.place" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
    </p>
</logic:empty>
