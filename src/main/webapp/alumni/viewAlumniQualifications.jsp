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


<!-- viewAlumniQualifications.jsp -->
<h2><bean:message key="link.qualification" bundle="ALUMNI_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<logic:empty name="educationFormationList">
	<p>
		<em><bean:message key="label.no.post.graduation.formations" bundle="ALUMNI_RESOURCES" />.</em>
	</p>
</logic:empty>

<ul class="mtop15">
	<li>
		<html:link action="/formation.do?method=initFormationCreation" >
			<bean:message key="label.create.post.graduation.formation" bundle="ALUMNI_RESOURCES" />
		</html:link>
	</li>
</ul>

<logic:notEmpty name="educationFormationList">
	<logic:iterate id="eachFormation" indexId="formationIndex" name="educationFormationList">
		<bean:define id="formationID" name="eachFormation" property="externalId" />
		<fr:view name="eachFormation" layout="tabular" schema="alumni.formation.list">
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="alumni.formation.list"/>
				<fr:property name="classes" value="tstyle2 thlight thright mbottom025" />
			</fr:layout>
		</fr:view>
		<p class="mtop025">
			<html:link page="<%= "/formation.do?method=prepareFormationEdit&formationId=" + formationID  %>">
				<bean:message key="label.edit" bundle="ALUMNI_RESOURCES"/> 
			</html:link> | 
			<html:link href="#" onclick="<%= "document.getElementById('deleteConfirm" + formationIndex + "').style.display='block'" %>" >
				<bean:message key="label.delete" bundle="ALUMNI_RESOURCES"/> 
			</html:link>
		</p>

		<div id="<%= "deleteConfirm" + formationIndex %>" class="infoop2 switchInline">
			<fr:form id="<%= "deleteForm" + formationIndex %>" action="<%= "/formation.do?method=deleteFormation&formationId=" + formationID %>" >

				<p class="mvert05"><bean:message key="label.confirm.delete" bundle="ALUMNI_RESOURCES" /></p>
				<p class="mvert05">
					<html:submit>
						<bean:message key="label.delete" bundle="ALUMNI_RESOURCES" />
					</html:submit>
					<html:cancel property="cancel" onclick="<%= "document.getElementById('deleteConfirm" + formationIndex + "').style.display='none'; return false;'" %>">
						<bean:message key="label.cancel" bundle="ALUMNI_RESOURCES" />
					</html:cancel>
				</p>

			</fr:form>
		</div>
		
	</logic:iterate>
</logic:notEmpty>
