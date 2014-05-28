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

<!-- viewAlumniProfessionalInformation.jsp -->

<h2><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:empty name="alumni" property="jobs">
	<p>
		<em><bean:message key="alumni.no.professional.information" bundle="ALUMNI_RESOURCES" />.</em>
	</p>
</logic:empty>

<div class="infoop7 dinlineinside">
	<fr:form id="alumniEmployment" action="/professionalInformation.do?method=updateIsEmployedPostback">
		<fr:edit id="employed" name="alumni" schema="alumni.isEmployed">
			<fr:layout name="flow" >
				<fr:property name="classes" value="inobullet asd2"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:property name="labelTerminator" value=""/>
			</fr:layout>
			<fr:destination name="updateIsEmployedPostback" path="/professionalInformation.do?method=updateIsEmployedPostback"/>
		</fr:edit>	
	</fr:form>
</div>


<ul class="mtop15">
	<li>
		<html:link action="/professionalInformation.do?method=prepareProfessionalInformationCreation">
			<bean:message key="label.create.professional.information" bundle="ALUMNI_RESOURCES" />
		</html:link>
	</li>
</ul>


<logic:notEmpty name="alumni" property="jobs">
	<logic:iterate id="eachJob" indexId="jobIndex" name="alumni" property="jobs">
		<bean:define id="jobID" name="eachJob" property="externalId" />
		<fr:view name="eachJob" layout="tabular" schema="alumni.professional.information.job">
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="alumni.professional.information.job"/>
				<fr:property name="classes" value="tstyle2 thlight thright mbottom025" />
			</fr:layout>
		</fr:view>
		<p class="mtop025">
			<html:link page="<%= "/professionalInformation.do?method=prepareUpdateProfessionalInformation&jobId=" + jobID  %>">
				<bean:message key="label.edit" bundle="ALUMNI_RESOURCES"/> 
			</html:link> | 
			<html:link href="#" onclick="<%= "document.getElementById('deleteConfirm" + jobIndex + "').style.display='block'" %>" >
				<bean:message key="label.delete" bundle="ALUMNI_RESOURCES"/> 
			</html:link>
		</p>

		<div id="<%= "deleteConfirm" + jobIndex %>" class="infoop2 switchInline">
			<fr:form id="deleteForm" action="<%= "/professionalInformation.do?method=deleteProfessionalInformation&jobId=" + jobID  %>" >
				<p class="mvert05"><bean:message key="label.confirm.delete" bundle="ALUMNI_RESOURCES" /></p>
				<p class="mvert05">
					<html:submit>
						<bean:message key="label.delete" bundle="ALUMNI_RESOURCES" />
					</html:submit>
					<html:cancel property="cancel" onclick="<%= "document.getElementById('deleteConfirm" + jobIndex + "').style.display='none'; return false;'" %>">
						<bean:message key="label.cancel" bundle="ALUMNI_RESOURCES" />
					</html:cancel>
				</p>
			</fr:form>
		</div>
		
	</logic:iterate>
</logic:notEmpty>

