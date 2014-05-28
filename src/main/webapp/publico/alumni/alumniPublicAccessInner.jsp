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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniPublicAccessInner.jsp -->


<!-- START MAIN PAGE CONTENTS HERE -->

<%-- <div class="col_right_alumni"><img src="alumni_reg_01.gif" alt="[Image] Alumni" /></div> --%>
<h1>Inscrição Alumni</h1>

<div class="alumnilogo">


<h2>
	<bean:message key="label.confirm.identity" bundle="ALUMNI_RESOURCES" />
	<span class="color777 fwnormal">
		<bean:message key="label.step.3.3" bundle="ALUMNI_RESOURCES" />
	</span>
</h2>

<p class="greytxt"><bean:message key="label.confirm.identity.steps" bundle="ALUMNI_RESOURCES" /></p>


<div class="reg_form">	
	<fr:form action="/alumni.do?&method=registrationConclusion">
	
		<fieldset style="margin-bottom: 1em;">
	
			<legend><bean:message key="label.identification" bundle="ALUMNI_RESOURCES" /></legend>
			
				<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
					<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
				</html:messages>
	
				<fr:edit id="alumniBean" name="alumniBean" visible="false" />
			
				<label for="" class="">
					<bean:message key="label.student.number" bundle="ALUMNI_RESOURCES" />:
				</label>
				<fr:edit id="studentNumber-validated" name="alumniBean" slot="studentNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
					<fr:destination name="invalid" path="<%= "/alumni.do?method=innerFenixPublicAccessValidation&alumniId=" + request.getAttribute("alumniId") + "&urlToken=" + request.getAttribute("urlToken") %>"/>
					<fr:layout>
						<fr:property name="size" value="30"/>
						<fr:property name="style" value="display: inline;"/>
					</fr:layout>
				</fr:edit>
				<span class="error0"><fr:message for="studentNumber-validated" /></span>
	
				<label for="" class="">
					<bean:message key="label.document.id.number" bundle="ALUMNI_RESOURCES" />:
				</label>
				<fr:edit id="documentIdNumber-validated" name="alumniBean" slot="documentIdNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:destination name="invalid" path="<%= "/alumni.do?method=innerFenixPublicAccessValidation&alumniId=" + request.getAttribute("alumniId") + "&urlToken=" + request.getAttribute("urlToken") %>"/>
					<fr:layout>
						<fr:property name="size" value="30"/>
						<fr:property name="style" value="display: inline;"/>
					</fr:layout>
				</fr:edit>
				<span class="error0"><fr:message for="documentIdNumber-validated" /></span>
	
			</fieldset>
	
			<p>
				<html:submit>
					<bean:message key="label.continue" bundle="ALUMNI_RESOURCES" />
				</html:submit>
			</p>
	
	</fr:form>
</div>

<!-- END CONTENTS -->
</div>