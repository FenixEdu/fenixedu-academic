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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<table cellpadding="0" cellspacing="0" border="0">
	<%--	<tr>
		<td>
		    <html:link page="/studentShiftEnrolmentManager.do?method=initializeEnrolment&divideMethod=courses"> Inscrição por disciplinas</html:link>
		</td>
	</tr> --%>
	<tr>
		<td>
			<html:link page="/studentShiftEnrolmentManager.do?method=initializeEnrolment&divideMethod=classes">
				Inscrição por Turmas
			</html:link>
		</td>
	</tr>
</table>