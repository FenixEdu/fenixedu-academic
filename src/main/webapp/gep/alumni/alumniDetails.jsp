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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<!-- alumniDetails.jsp -->
<h2><bean:message key="title.alumni.details" bundle="GEP_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>



<fr:form id="searchAlumniForm" action="/alumni.do?method=searchAlumni">
	<fr:edit id="searchAlumniBean" name="searchAlumniBean" schema="alumni.gep.search.bean" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05 thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="success" path="/alumni.do?method=searchAlumni"/>
		<fr:destination name="invalid" path="/alumni.do?method=searchAlumniError"/>
	</fr:edit>
	<p class="mtop05">
		<html:submit>
			<bean:message key="label.filter" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>
</fr:form>	

<logic:present name="alumniResultItems">
	<logic:notEmpty name="alumniResultItems">
		<bean:define id="bean" name="searchAlumniBean" type="net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean"/>
		<fr:view name="alumniResultItems" schema="alumni.gep.search.result">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop05" />
				<fr:property name="columnClasses" value=",,acenter,acenter,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<logic:empty name="searchAlumniBean" property="alumni">
		<bean:message key="label.search.noResultsFound" bundle="ALUMNI_RESOURCES" /> 
	</logic:empty>
</logic:present>