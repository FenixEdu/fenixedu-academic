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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

<!-- showAlumniList.jsp -->
<h2><bean:message key="link.search.alumni" bundle="ALUMNI_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<bean:define id="schema" value="alumni.search.bean" />
<logic:notEmpty name="searchAlumniBean" property="degreeType" >
	<bean:define id="schema" value="alumni.search.bean.full" />
</logic:notEmpty>

<fr:form id="searchAlumniForm" action="/searchAlumni.do?method=showAlumniList">
	
	<fr:edit id="searchAlumniBean" name="searchAlumniBean" schema="<%= schema.toString() %>" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05 thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="degreeTypePostback" path="/searchAlumni.do?method=degreeTypePostback"/>
		<fr:destination name="success" path="/searchAlumni.do?method=showAlumniList"/>
		<fr:destination name="invalid" path="/searchAlumni.do?method=showAlumniList"/>
	</fr:edit>
	<p class="mtop05">
		<html:submit>
			<bean:message key="label.filter" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>
</fr:form>	

<logic:present name="searchAlumniBean" property="alumni">
	<logic:notEmpty name="searchAlumniBean" property="alumni">
	<bean:define id="bean" name="searchAlumniBean" type="net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean"/>
	<p class="mtop15 mbottom05"><bean:message key="label.hitCount" bundle="ALUMNI_RESOURCES" />: <strong><fr:view name="searchAlumniBean" property="totalItems"/></strong></p>
		<fr:view name="searchAlumniBean" property="alumni" schema="alumni.registration.search.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2 mtop05" />
				<fr:property name="columnClasses" value=",,acenter,acenter,acenter" />
				<fr:property name="sortParameter" value="sort" />
				<fr:property name="sortUrl" value="<%= "/searchAlumni.do?method=showAlumniList" + bean.getSearchElementsAsParameters() %>" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<logic:empty name="searchAlumniBean" property="alumni">
		<bean:message key="label.search.noResultsFound" bundle="ALUMNI_RESOURCES" /> 
	</logic:empty>
</logic:present>
