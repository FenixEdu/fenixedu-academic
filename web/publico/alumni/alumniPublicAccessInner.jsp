<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniPublicAccessInner.jsp -->


<!-- START MAIN PAGE CONTENTS HERE -->

<%-- <div class="col_right_alumni"><img src="alumni_reg_01.gif" alt="[Image] Alumni" /></div> --%>
<h1>Alumni</h1>

<div class="alumnilogo">


<h2>
	<bean:message key="label.confirm.identity" bundle="ALUMNI_RESOURCES" />
	<span class="color777 fwnormal">
		<bean:message key="label.step.1.3" bundle="ALUMNI_RESOURCES" />
	</span>
</h2>

<p class="greytxt"><bean:message key="label.confirm.identity.steps" bundle="ALUMNI_RESOURCES" /></p>


<fr:form id="reg_form" action="/alumni.do?&method=initPasswordGenerationInquiry">

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


<!-- END CONTENTS -->
</div>