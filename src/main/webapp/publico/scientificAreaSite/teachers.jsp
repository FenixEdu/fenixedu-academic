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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="actual$site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="label.teachers" bundle="SITE_RESOURCES"/></h2>


<logic:iterate id="category" name="categories" type="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory">
	<h2 class="greytxt mtop2">
		<fr:view name="category" property="name.content"/>
	</h2>

	<bean:define id="byCategory" value="true" toScope="request"/>
	<logic:iterate id="teacher" name="teachers" property="<%= category.getExternalId() %>" type="net.sourceforge.fenixedu.domain.Person">
		<fr:view name="teacher">
			<fr:layout name="person-presentation-card">
				<fr:property name="subLayout" value="values-as-list"/>
				<fr:property name="subSchema" value="present.research.member"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:iterate>