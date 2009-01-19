<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniPublicAccessInformationInquiry.jsp -->

<%-- <div class="col_right_alumni"><img src="alumni_reg_01.gif" alt="[Image] Alumni" /></div> --%>

<h1>Alumni</h1>

<div class="alumnilogo">

<h2>
	<bean:message key="label.update.personal.data" bundle="ALUMNI_RESOURCES" />
	<span class="color777 fwnormal">
		<bean:message key="label.step.2.3" bundle="ALUMNI_RESOURCES" />
	</span>
</h2>

<p class="greytxt" style="margin-bottom: 1em;"><bean:message key="label.update.personal.data.steps" bundle="ALUMNI_RESOURCES" /></p>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<div class="reg_form">
<fr:form action="/alumni.do?method=updateAlumniInformation&isEmployed=true">

	<fr:edit id="publicAccessBean" name="publicAccessBean" visible="false" />

<div id="hackAddress" class="block mtop1">
	<fieldset style="margin-bottom: 1em;">

		<legend><bean:message key="label.contact" bundle="ALUMNI_RESOURCES" /></legend>

		<!-- PHONE -->
		<label for="" class="">
			<bean:message key="label.phone" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="phone-validated" name="publicAccessBean" slot="phone" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="phone-validated" /></span>
	
	
		<!-- ADDRESS -->
		<label for="" class="">
			<bean:message key="label.address" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="address-validated" name="publicAccessBean" slot="addressBean.address" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="50"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="address-validated" /></span>
	
		<!-- AREA CODE -->
		<label for="" class="">
			<bean:message key="label.areaCode" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="areaCode-validated" name="publicAccessBean" slot="addressBean.areaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="8"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		&nbsp;
		<span class="error0"><fr:message for="areaCode-validated" /></span>
		<fr:edit id="areaOfAreaCode-validated" name="publicAccessBean" slot="addressBean.areaOfAreaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="areaOfAreaCode-validated" /></span>
	
	
		<!-- COUNTRY -->
		<label for="" class="">
			<bean:message key="label.country" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="country-validated" name="publicAccessBean" slot="addressBean.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider" />
				<fr:property name="format" value="${name}"/>
				<fr:property name="sortBy" value="name"/>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="country-validated" /></span>

	</fieldset>
</div>
	
	<div class="ie67margin20px"></div>

	<div id="questions1" class="block mtop1"> 
		<p>
			<bean:message key="label.isEmployed.question" bundle="ALUMNI_RESOURCES" />
			<logic:present name="professionalInformationPostback">
				<input id="yes1" type="radio" name="a" checked="checked" onclick="document.getElementById('isEmployed').style.display='block';document.getElementById('hiddenButton').style.display='none';document.getElementById('hackAddress').style.display='block';document.getElementById('yes2').checked='checked'"/> Sim
			</logic:present>
			<logic:notPresent name="professionalInformationPostback">
				<input id="yes1" type="radio" name="a" onclick="document.getElementById('isEmployed').style.display='block';document.getElementById('hiddenButton').style.display='none';document.getElementById('hackAddress').style.display='block';document.getElementById('yes2').checked='checked'"/> Sim
			</logic:notPresent>
			<input id="no1" type="radio" name="a" onclick="document.getElementById('hiddenButton').style.display='block';document.getElementById('isEmployed').style.display='none';document.getElementById('hackAddress').style.display='none';document.getElementById('questions1').style.display='none';document.getElementById('no2').checked='checked'"/> Não
		</p>
	</div>

<logic:present name="professionalInformationPostback">
	<div id="isEmployed" class="block mtop1">
</logic:present>
<logic:notPresent name="professionalInformationPostback">
	<div id="isEmployed" class="switchInline mtop1">
</logic:notPresent>
	<fieldset style="margin-bottom: 1em;"> 
		
		<legend><bean:message key="label.current.job" bundle="ALUMNI_RESOURCES" /></legend>
	
		<!-- EMPLOYER NAME -->
		<label for="" class="">
			<bean:message key="label.employerName" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="employerName-validated" name="publicAccessBean" slot="jobBean.employerName" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="updateBusinessAreaPostback" path="/alumni.do?method=professionalInformationPostback"/>
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="40"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="employerName-validated" /></span>
		

		<!-- CITY -->
		<label for="" class="">
			<bean:message key="label.city" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="city-validated" name="publicAccessBean" slot="jobBean.city" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="updateBusinessAreaPostback" path="/alumni.do?method=professionalInformationPostback"/>
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="city-validated" /></span>
		

		<!-- COUNTRY -->
		<label for="" class="">
			<bean:message key="label.country" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="country-validated" name="publicAccessBean" slot="jobBean.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="updateBusinessAreaPostback" path="/alumni.do?method=professionalInformationPostback"/>
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider" />
				<fr:property name="format" value="${name}"/>
				<fr:property name="sortBy" value="name"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="country-validated" /></span>


		<!-- BEGIN DATE -->
		<label for="" class="">
			<bean:message key="label.beginDate" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<bean:define id="teste" name="publicAccessBean" property="jobBean" />
		<fr:edit id="beginDate-validated" name="teste" schema="job.begin" >
			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformationError"/>
			<fr:layout name="tabular-break">
				<fr:property name="style" value="margin-left: -3px; margin-top: -6px; margin-bottom: -4px;" />
				<fr:property name="displayLabel" value="false" />
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="beginDate-validated" /></span>

		<!-- POSITION -->
		<label for="" class="">
			<bean:message key="label.job.position" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="position-validated" name="publicAccessBean" slot="jobBean.position" >
			<fr:destination name="updateBusinessAreaPostback" path="/alumni.do?method=professionalInformationPostback"/>
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="30"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<logic:present name="position-validated">
			<span class="error0"><bean:write name="position-validated" scope="request" bundle="ALUMNI_RESOURCES" /></span>
		</logic:present>


		<!-- PARENT BUSINESS AREA -->
		<label for="" class="">
			<bean:message key="label.parent.businessArea" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<bean:define id="tempBean" name="publicAccessBean" property="jobBean" type="net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean" />
		<%-- the bean is necessary because the dot in the slot="jobBean.parentBusinessArea" is misinterpreted --%>
		<%-- <fr:edit id="parentBusinessArea-validated" name="publicAccessBean" slot="jobBean.parentBusinessArea" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > --%>
		<fr:edit id="parentBusinessArea-validated" name="tempBean" slot="parentBusinessArea" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.BusinessAreaParentProvider" />
				<fr:property name="format" value="${description}"/>
				<fr:property name="sortBy" value="code"/>
				<fr:property name="style" value="display: inline;"/>
				<fr:destination name="postback" path="/alumni.do?method=professionalInformationPostback"/>
				<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="parentBusinessArea-validated" /></span>
		

		<!-- CHILD BUSINESS AREA -->
		<label for="" class="">
			<bean:message key="label.child.businessArea" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="childBusinessArea-validated" name="publicAccessBean" slot="jobBean.childBusinessArea" >
			<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.BusinessAreaChildProvider" />
				<fr:property name="format" value="${description}"/>
				<fr:property name="sortBy" value="code"/>
				<fr:property name="style" value="display: inline;"/>
				<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			</fr:layout>
		</fr:edit>
		<logic:present name="childBusinessArea-validated">
			<span class="error0"><bean:write name="childBusinessArea-validated" scope="request" bundle="ALUMNI_RESOURCES" /></span>
		</logic:present>

		
		<!-- APPLICATION TYPE -->
		<label for="" class="">
			<bean:message key="label.application.type" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="applicationType-validated" name="publicAccessBean" slot="jobBean.applicationType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:layout name="menu-select">
				<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.JobApplicationType" />
				<fr:property name="format" value="${localizedName}"/>
				<fr:property name="style" value="display: inline;"/>
				<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="applicationType-validated" /></span>
		
		<!-- CONTRACT TYPE -->
		<label for="" class="">
			<bean:message key="label.contract.type" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="contractType-validated" name="publicAccessBean" slot="jobBean.contractType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:layout name="menu-select">
				<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.ContractType" />
				<fr:property name="format" value="${localizedName}"/>
				<fr:property name="style" value="display: inline;"/>
				<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="contractType-validated" /></span>
		
		<!-- SALARY TYPE -->
		<label for="" class="">
			<bean:message key="label.salary.type" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="salaryType-validated" name="publicAccessBean" slot="jobBean.salaryType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:layout name="menu-select">
				<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.SalaryType" />
				<fr:property name="format" value="${localizedName}"/>
				<fr:property name="style" value="display: inline;"/>
				<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="salaryType-validated" /></span>

	</fieldset>
	
	<p class="mtop15">
		<html:submit>
			<bean:message key="label.continue" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>
</div>

</fr:form>
</div>

<div id="hiddenButton" class="switchInline mtop1">
	
<div class="reg_form">
<fr:form action="/alumni.do?method=updateAlumniInformation&isEmployed=false">
		
	<fr:edit id="publicAccessBean" name="publicAccessBean" visible="false" />

	
	<fieldset style="margin-bottom: 1em;">

		<legend><bean:message key="label.contact" bundle="ALUMNI_RESOURCES" /></legend>

		<!-- PHONE -->
		<label for="" class="">
			<bean:message key="label.phone" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="phone-validated2" name="publicAccessBean" slot="phone" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="phone-validated" /></span>
	
	
		<!-- ADDRESS -->
		<label for="" class="">
			<bean:message key="label.address" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="address-validated2" name="publicAccessBean" slot="addressBean.address" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="50"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="address-validated" /></span>
	
		<!-- AREA CODE -->
		<label for="" class="">
			<bean:message key="label.areaCode" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="areaCode-validated2" name="publicAccessBean" slot="addressBean.areaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="8"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		&nbsp;
		<span class="error0"><fr:message for="areaCode-validated" /></span>
		<fr:edit id="areaOfAreaCode-validated2" name="publicAccessBean" slot="addressBean.areaOfAreaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="areaOfAreaCode-validated" /></span>
	
	
		<!-- COUNTRY -->
		<label for="" class="">
			<bean:message key="label.country" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="country-validated2" name="publicAccessBean" slot="addressBean.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider" />
				<fr:property name="format" value="${name}"/>
				<fr:property name="sortBy" value="name"/>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="country-validated" /></span>

	</fieldset>


	<div class="ie67margin20px"></div>

	<p>
		<bean:message key="label.isEmployed.question" bundle="ALUMNI_RESOURCES" />
		<input id="yes2" type="radio" name="a" onclick="document.getElementById('isEmployed').style.display='block';document.getElementById('hiddenButton').style.display='none';document.getElementById('hackAddress').style.display='block';document.getElementById('questions1').style.display='block';document.getElementById('yes1').checked='checked'"/> Sim
		<input id="no2" type="radio" name="a" onclick="document.getElementById('hiddenButton').style.display='block';document.getElementById('isEmployed').style.display='none';document.getElementById('hackAddress').style.display='none';document.getElementById('no1').checked='checked'"/> Não
	</p>

			
		<p class="dinline">
		<html:submit>
			<bean:message key="label.continue" bundle="ALUMNI_RESOURCES" />
		</html:submit>
		</p>
</fr:form>
</div>

</div>

<!-- END CONTENTS -->
</div>

<script type="text/javascript">
	function hugo() { alert("FDP"); }
</script>