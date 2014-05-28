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

<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.view.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="<%= "/competenceCourses/manageVersions.do?method=showVersions&competenceCourseID="  + request.getParameter("competenceCourseID") %>">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>		
		</html:link>
	</li>
</ul>

<fr:view name="information" schema="view.competenceCourseInformation.details">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright thtop mtop05"/>
			<fr:property name="rowClasses" value=",,,bold,,,,,,,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

<logic:notEmpty name="information"  property="bibliographicReferences.mainBibliographicReferences">
		<p class="mbottom05"><strong><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
		<fr:view name="information" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright thcenter mtop05"/>
				<fr:property name="columnClasses" value=",,acenter,,"/>
			</fr:layout>
		</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="information" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mbottom05"><strong><bean:message key="label.secondaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="information" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>
</logic:notEmpty>

<logic:empty name="information" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
	<p>
		<em><bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
	</p>
</logic:empty>