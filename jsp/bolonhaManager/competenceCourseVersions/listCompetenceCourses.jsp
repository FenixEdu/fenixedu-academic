<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.competenceCourse.list" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<strong class='highlight1'><bean:message key="groupMembers" bundle="BOLONHA_MANAGER_RESOURCES"/></strong> <bean:message key="label.group.members.explanation" bundle="BOLONHA_MANAGER_RESOURCES"/></strong>

<logic:notEmpty name="department" property="competenceCourseMembersGroup.elements">
<ul>
<logic:iterate id="person" type="net.sourceforge.fenixedu.domain.Person" name="department" property="competenceCourseMembersGroup.elements">
	<li><fr:view name="person" layout="name-with-alias"/>
</logic:iterate>
</ul>
</logic:notEmpty>

<logic:empty name="department" property="competenceCourseMembersGroup.elements">
	<bean:message key="label.empty.group.members" bundle="BOLONHA_MANAGER_RESOURCES"/>
</logic:empty>

<p>
<logic:equal name="department" property="currentUserMemberOfCompetenceCourseMembersGroup" value="true">
	<fr:view name="department">
		<fr:layout name="competence-course-list">
			<fr:property name="scientificAreaNameClasses" value="boldFontClass"/>
			<fr:property name="tableClasses" value="showinfo1 smallmargin mtop05 width600px"/>
			<fr:property name="link(manageVersions)" value="/competenceCourses/manageVersions.do?method=showVersions"/>
			<fr:property name="key(manageVersions)" value="label.manage.versions"/>
			<fr:property name="bundle(manageVersions)" value="BOLONHA_MANAGER_RESOURCES" />
			<fr:property name="param(manageVersions)" value="idInternal/competenceCourseID" />
			<fr:property name="order(manageVersions)" value="1"/>
			<fr:property name="filterBy" value="APPROVED"/>
		</fr:layout>
	</fr:view>
</logic:equal>

<logic:equal name="department" property="currentUserMemberOfCompetenceCourseMembersGroup" value="false">
	<em><bean:message key="notMemberInCompetenceCourseManagementGroup" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
</logic:equal>
</p>