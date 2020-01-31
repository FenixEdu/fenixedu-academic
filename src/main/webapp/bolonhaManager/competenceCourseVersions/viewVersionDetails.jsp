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

<bean:define id="competenceCourseID" name="changeRequest" property="competenceCourse.externalId"/>

<em><bean:message key="bolonhaManager" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
<h2><bean:message key="label.view.versions" bundle="BOLONHA_MANAGER_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="<%= "/competenceCourses/manageVersions.do?method=showVersions&competenceCourseID=" + competenceCourseID %>"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	</li>
</ul>

<logic:notEmpty name="changeRequest">
		
	<p class="mtop15 mbottom05"><strong><bean:message key="label.generalInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
	<fr:view name="changeRequest" schema="present.competenceCourseInformation.change.request">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright thtop mtop05"/>
			<fr:property name="columnClasses" value="width150px,"/>
			<fr:property name="rowClasses" value=",,,bold,,,,,,tdhl1,,,,,,,,,,,,,,,"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop2 mbottom05"><strong><bean:message key="label.loadInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
	<fr:view name="changeRequest" schema="present.semester1.loads">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
			<fr:property name="columnClasses" value=",aright"/>
		</fr:layout>
	</fr:view>
	
	<logic:equal name="changeRequest" property="regime" value="ANUAL">
		<fr:view name="changeRequest" schema="present.semester2.loads">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value=",aright"/>
			</fr:layout>
		</fr:view>			
	</logic:equal>

	<%--
		<p class="mtop15 mbottom05"><strong><bean:message key="label.bibliographyInformation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong></p>
	--%>

	<logic:notEmpty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">

		<logic:notEmpty name="changeRequest"  property="bibliographicReferences.mainBibliographicReferences">
			<p class="mtop2 mbottom05"><strong><bean:message key="label.primaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="changeRequest" property="bibliographicReferences.mainBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences">		
			<p class="mtop2 mbottom05"><strong><bean:message key="label.secondaryBibliography" bundle="BOLONHA_MANAGER_RESOURCES"/></strong></p>
			<fr:view name="changeRequest" property="bibliographicReferences.secondaryBibliographicReferences" schema="view.reference">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright thcenter mtop05"/>
					<fr:property name="columnClasses" value=",,acenter,,"/>
				</fr:layout>
			</fr:view>
	   </logic:notEmpty>

	</logic:notEmpty>

	<logic:empty name="changeRequest" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
		<p>
			<em><bean:message key="label.no.bibliography" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
		</p>
	</logic:empty>
	
</logic:notEmpty>

<logic:empty name="changeRequest">
	<p>
		<em><bean:message key="label.changeRequest.notAvailable" bundle="BOLONHA_MANAGER_RESOURCES"/></em>
	</p>
</logic:empty>