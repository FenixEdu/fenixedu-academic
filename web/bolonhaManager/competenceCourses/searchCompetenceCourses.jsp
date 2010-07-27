<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="bolonhaManager" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.search.competenceCourses" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<fr:form action="/competenceCourses/searchCompetenceCourses.do?method=search">
	<table>
		<tr><td>
			<fr:edit id="searchBean" name="searchBean">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager.SearchCompetenceCoursesDA$SearchCompetenceCourseBean" bundle="BOLONHA_MANAGER_RESOURCES">
					<fr:slot name="searchName"/>
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
	<fr:view name="searchResults" layout="tabular">
		<fr:schema type="net.sourceforge.fenixedu.domain.CompetenceCourse" bundle="BOLONHA_MANAGER_RESOURCES">
			<fr:slot name="name"/>
			<fr:slot name="departmentUnit.acronym"/>
			<fr:slot name="curricularStage"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
			<fr:property name="sortBy" value="curricularStage=desc, departmentUnit.acronym=asc, name=asc"/>
			<fr:link name="showApprovedCompetenceCourse" label="show" link="/competenceCourses/showCompetenceCourse.faces?action=ccm&competenceCourseID=${idInternal}" target="_blank" condition="loggedPersonAllowedToView"/>
		</fr:layout>
	</fr:view>
</logic:present>
