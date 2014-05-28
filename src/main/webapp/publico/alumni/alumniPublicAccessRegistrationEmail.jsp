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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<div class="alumnilogo">

	<h2><bean:message key="label.confirm.email" bundle="ALUMNI_RESOURCES" /> <span class="color777 fwnormal"><bean:message key="label.step.2.3" bundle="ALUMNI_RESOURCES" /></span></h2>
	
	<p><bean:message key="message.confirm.email" bundle="ALUMNI_RESOURCES" /></p>
	<logic:present name="alumniEmailErrorMessage">
		<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
		</html:messages>
		<bean:write name="alumniEmailErrorMessage"  scope="request" />
	</logic:present>

</div>