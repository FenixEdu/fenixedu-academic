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

<em><bean:message key="bolonhaManager" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>
<p>&nbsp;</p>

<logic:iterate id="department" name="departments">

	<h2><bean:write name="department" property="realName"/></h2>
	<h3><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h3>
	
<%-- 	<logic:notEmpty name="department" property="competenceCourseMembersGroup.members"> --%>
	
<%-- 	<p class="mtop15 mbottom05"><strong class='highlight1'><bean:message key="groupMembers" bundle="BOLONHA_MANAGER_RESOURCES"/></strong> <bean:message key="label.group.members.explanation" bundle="BOLONHA_MANAGER_RESOURCES"/></p> --%>
	
<!-- 	<ul> -->
<%-- 		<logic:iterate id="user" name="department" property="competenceCourseMembersGroup.members"> --%>
<%-- 			<li><fr:view name="user" property="person" layout="name-with-alias"/></li> --%>
<%-- 		</logic:iterate> --%>
<!-- 	</ul> -->
<%-- 	</logic:notEmpty> --%>
	
<%-- 	<logic:empty name="department" property="competenceCourseMembersGroup.members"> --%>
<!-- 		<p> -->
<%-- 			<em><bean:message key="label.empty.group.members" bundle="BOLONHA_MANAGER_RESOURCES"/></em> --%>
<!-- 		</p> -->
<%-- 	</logic:empty> --%>
	
<%-- 	<bean:define id="showOldCompetenceCourses" name="requestBean" property="isShowOldCompetenceCourses" /> --%>
<!-- 	<p> -->
<%-- 	<fr:form id="requestForm" action="/competenceCourses/manageVersions.do?method=prepare"> --%>
<%-- 		<fr:edit id="requestBean" name="requestBean"> --%>
<%-- 			<fr:schema bundle="BOLONHA_MANAGER_RESOURCES" type="org.fenixedu.academic.ui.struts.action.BolonhaManager.CompetenceCourseInformationRequestBean"> --%>
<%-- 				<fr:slot name="showOldCompetenceCourses" layout="option-select-postback"/> --%>
<%-- 			</fr:schema> --%>
<%-- 			<fr:layout> --%>
<%-- 				<fr:property name="classes" value="thlight thpadding0px mtop05"/> --%>
<%-- 			</fr:layout> --%>
<%-- 		</fr:edit> --%>
<%-- 	</fr:form> --%>
<!-- 	</p> -->
	
	<div class="mtop15">
	<fr:view name="department">
		<fr:layout name="competence-course-list">
			<fr:property name="scientificAreaNameClasses" value="bold"/>
			<fr:property name="showOldCompetenceCourses" value="true"/>
			<fr:property name="tableClasses" value="smallmargin mtop05 table-condensed"/>
			<fr:property name="link(manageVersions)" value="/competenceCourses/manageVersions.do?method=showVersions"/>
			<fr:property name="key(manageVersions)" value="label.view.versions"/>
			<fr:property name="bundle(manageVersions)" value="BOLONHA_MANAGER_RESOURCES" />
			<fr:property name="param(manageVersions)" value="externalId/competenceCourseID" />
			<fr:property name="order(manageVersions)" value="1"/>
			<fr:property name="filterBy" value="APPROVED"/>
		</fr:layout>
	</fr:view>
	</div>
	
<%-- 	<logic:equal name="department" property="currentUserMemberOfCompetenceCourseMembersGroup" value="false"> --%>
<%-- 		<em><bean:message key="notMemberInCompetenceCourseManagementGroup" bundle="BOLONHA_MANAGER_RESOURCES"/></em> --%>
<%-- 	</logic:equal> --%>

</logic:iterate>


<table class="smallmargin mtop05 table-condensed table">
	<logic:iterate id="competenceCourse" name="competenceCourses">
		<logic:equal name="competenceCourse" property="approved" value="true">
			<tr>
				<td><span class="color2"><bean:write name="competenceCourse" property="code"/> - <bean:write name="competenceCourse" property="name"/></span></td>
				<td class="aright">
					<html:link action="/competenceCourses/manageVersions.do?method=showVersions" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="externalId">
						<bean:message bundle="BOLONHA_MANAGER_RESOURCES" key="label.view.versions"/>
					</html:link>
				</td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>

<logic:equal name="departments" property="empty" value="true">
	<logic:equal name="competenceCourses" property="empty" value="true">
		<em><bean:message key="notMemberInCompetenceCourseManagementGroup" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
	</logic:equal>
</logic:equal>

<%-- <logic:empty name="departments"> --%>
<!--     <p> -->
<%--         <em><bean:message key="no.current.department.working.place" bundle="BOLONHA_MANAGER_RESOURCES"/></em> --%>
<!--     </p> -->
<%-- </logic:empty> --%>
