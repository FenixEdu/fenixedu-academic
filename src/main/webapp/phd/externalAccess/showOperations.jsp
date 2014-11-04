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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="org.fenixedu.academic.domain.phd.access.PhdProcessAccessType"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>

<br/>
<h2><bean:message key="label.phd.process" bundle="PHD_RESOURCES" /></h2>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="participant">
	<jsp:include page="processDetails.jsp" />
	<br/>
</logic:notEmpty>

<bean:define id="hash" name="participant" property="accessHashCode" />
<bean:define id="mainProcess" name="participant" property="individualProcess" type="org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess" />

<ul>
	<logic:iterate id="accessType" name="participant" property="accessTypes.types" type="org.fenixedu.academic.domain.phd.access.PhdProcessAccessType">
		<bean:define id="methodName" >prepare<bean:write name="accessType" property="descriptor"/></bean:define>
		
		<phd:accessTypeAvailable mainProcess="<%= mainProcess %>" accessType="<%= accessType %>">
			<li>
			<html:link action="<%= "/phdExternalAccess.do?method=" + methodName + "&amp;hash=" + hash.toString() %>">
				<bean:write name="accessType" property="localizedName"/>
			</html:link>
			</li>
		</phd:accessTypeAvailable>
	</logic:iterate>
</ul>
