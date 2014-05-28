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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.chooseDissertation.chooseThesis"/></h2>

<bean:define id="personExternalId" name="student" property="person"/>

<html:link page="/viewStudent.do?method=showStats" paramName="personExternalId" paramProperty="externalId" paramId="personId">
	<bean:message key="link.back" bundle="EXTERNAL_SUPERVISION_RESOURCES"/>
</html:link>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:iterate id="dissertations" name="dissertations">
	<strong><bean:write name="dissertations" property="executionYear.qualifiedName"/></strong>
	<bean:write name="dissertations" property="curricularCourse.name"/>
	<bean:define id="theses" name="dissertations" property="theses"/>
	<logic:empty name="theses">
		<ul class="nobullet"><li>
			<em><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="label.chooseDissertation.notAssigned"/></em>
		</li></ul>
	</logic:empty>
	<logic:notEmpty name="theses">
		<ul>
		<logic:iterate id="theses" name="theses">
			<li>
				<html:link page="/viewDissertation.do?method=viewThesisForSupervisor" paramName="theses" paramProperty="externalId" paramId="thesisID">
					<bean:write name="theses" property="finalTitle"/>
				</html:link>
				&nbsp;<strong><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="label.chooseDissertation.state"/>:</strong> <em><fr:view name="theses" property="state"/></em>
			</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	<br/>
</logic:iterate>
