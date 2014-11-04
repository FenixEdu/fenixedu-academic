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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<span class="error0">
	<html:messages id="message" message="true">
		<bean:write name="message" />
	</html:messages>
</span>
		
<logic:present name="nonRegularTeacherBean">
	<fr:edit id="nonRegularTeacherBean" name="nonRegularTeacherBean" action="/manageNonRegularTeachingService.do?method=showNonRegularTeachingService">
		<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES" type="org.fenixedu.academic.dto.teacher.credits.NonRegularTeacherBean">
			<fr:slot name="username"/>
		</fr:schema>
	</fr:edit>

</logic:present>