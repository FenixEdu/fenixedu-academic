<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<!-- viewAlumniQualifications.jsp -->
<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
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
		<bean:define id="formationID" name="eachFormation" property="idInternal" />
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

		<div id="<%= "deleteConfirm" + formationIndex %>" class="infoop2 width300px switchInline">
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
