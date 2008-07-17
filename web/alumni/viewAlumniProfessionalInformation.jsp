<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- viewAlumniProfessionalInformation.jsp -->

<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES" /></h2>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:empty name="alumniPerson" property="jobs">
	<p>
		<em><bean:message key="alumni.no.professional.information" bundle="ALUMNI_RESOURCES" />.</em>
	</p>
</logic:empty>

<ul class="mtop15">
	<li>
		<html:link action="/professionalInformation.do?method=prepareProfessionalInformationCreation">
			<bean:message key="label.create.professional.information" bundle="ALUMNI_RESOURCES" />
		</html:link>
	</li>
</ul>


<logic:notEmpty name="alumniPerson" property="jobs">
	<logic:iterate id="eachJob" indexId="jobIndex" name="alumniPerson" property="jobs">
		<bean:define id="jobID" name="eachJob" property="idInternal" />
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

		<div id="<%= "deleteConfirm" + jobIndex %>" class="infoop2 width300px switchInline">
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