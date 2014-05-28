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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="error">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message"/></span>
	</p>
</html:messages>

<fr:edit id="uploadBean" name="uploadBean" schema="SIBSPaymentFileUpload" action="/SIBSPayments.do?method=uploadSIBSPaymentFiles">
	<fr:destination name="cancel" path="/SIBSPayments.do?method=prepareUploadSIBSPaymentFiles"/>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES" property="message">
	<p>
		<bean:write name="message" />
	</p>
</html:messages>
