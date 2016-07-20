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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="org.fenixedu.academic.domain.degree.DegreeType"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.payments.postingRules.category"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<p>
	<html:link
		action="/postingRules.do?method=managePostGraduationRules">
		<bean:message	key="label.payments.postingRules.category.postGraduate"
			bundle="MANAGER_RESOURCES" />
	</html:link>
</p>
<p>
	<html:link
		action="/postingRules.do?method=manageGraduationRules">
		<bean:message	key="label.payments.postingRules.category.graduation"
			bundle="MANAGER_RESOURCES" />
	</html:link>
</p>

<p>
	<html:link
		action="/postingRules.do?method=showInsurancePostingRules">
		<bean:message key="label.payments.postingRules.category.insurance"
			bundle="MANAGER_RESOURCES" />
	</html:link></td>	
</p>

<p>
	<html:link
		action="/postingRules.do?method=showFCTScolarshipPostingRules">
		<bean:message key="label.payments.postingRules.external.entity.scholarship" bundle="MANAGER_RESOURCES" />
	</html:link></td>	
</p>
