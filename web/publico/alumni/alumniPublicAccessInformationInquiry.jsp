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


<fr:form id="reg_form" action="/alumni.do?method=updateAlumniInformation">

	<fr:edit id="publicAccessBean" name="publicAccessBean" visible="false" />

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
	
<%--
		<!-- AREA CODE -->
		<label for="" class="">
			<bean:message key="label.areaCode" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="areaCode-validated" name="publicAccessBean" slot="addressBean.areaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="areaCode-validated" /></span>
	
	
		<!-- AREA OF AREA CODE -->
		<label for="" class="">
			<bean:message key="label.areaOfAreaCode" bundle="ALUMNI_RESOURCES" />:&nbsp;<span class="red">*</span>
		</label>
		<fr:edit id="areaOfAreaCode-validated" name="publicAccessBean" slot="addressBean.areaOfAreaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:property name="size" value="20"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
		<span class="error0"><fr:message for="areaOfAreaCode-validated" /></span>
--%>
	
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
	
	<div class="ie67margin20px"></div>
	
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
			<fr:layout name="tabular-break">
				<fr:property name="style" value="margin-left: -3px; margin-top: -6px; margin-bottom: -4px;" />
				<fr:property name="displayLabel" value="false" />
			</fr:layout>
		</fr:edit>
<%-- 	
		<fr:edit id="beginDate-validated" name="publicAccessBean" slot="jobBean.beginDate" layout="picker">
			<fr:destination name="updateBusinessAreaPostback" path="/alumni.do?method=professionalInformationPostback"/>
			<fr:destination name="invalid" path="/alumni.do?method=updateAlumniInformation"/>
			<fr:layout>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
				<fr:property name="size" value="12"/>
				<fr:property name="maxLength" value="10"/>
				<fr:property name="style" value="display: inline;"/>
			</fr:layout>
		</fr:edit>
--%>
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

	</fieldset>
	
	<p class="mtop15">
		<html:submit>
				<bean:message key="label.continue" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>

</fr:form>

<!-- END CONTENTS -->
</div>
