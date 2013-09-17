<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumni/inquiries/alumniManageProfessionalInformation.jsp -->

<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2>IV Inquérito ao Percurso Sócio-Profissional dos Diplomados</h2>
<h3>
	<bean:message key="label.create.professional.information" bundle="ALUMNI_RESOURCES" />
</h3>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p class="mbottom05 mtop15"><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:present name="jobCreateBean">
	<bean:define id="schema" name="jobCreateBean" property="schema" type="java.lang.String" />
	<fr:edit id="jobCreateBean" name="jobCreateBean" schema="<%= schema.toString() %>" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="updateBusinessAreaPostback" path="/alumniInquiries.do?method=createBusinessAreaPostback"/>
		<fr:destination name="success" path="/alumniInquiries.do?method=createProfessionalInformation"/>
		<fr:destination name="invalid" path="/alumniInquiries.do?method=prepareProfessionalInformationCreation"/>
		<fr:destination name="cancel" path="/alumniInquiries.do?method=viewProfessionalInformation"/>
	</fr:edit>
</logic:present>

<logic:present name="jobUpdateBean">
	<bean:define id="schema" name="jobUpdateBean" property="schema" type="java.lang.String" />
	<fr:edit id="jobUpdateBean" name="jobUpdateBean" schema="<%= schema %>" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="updateBusinessAreaPostback" path="/alumniInquiries.do?method=updateBusinessAreaPostback"/>
		<fr:destination name="success" path="/alumniInquiries.do?method=updateProfessionalInformation"/>
		<fr:destination name="invalid" path="/alumniInquiries.do?method=updateProfessionalInformationError"/>
		<fr:destination name="cancel" path="/alumniInquiries.do?method=viewProfessionalInformation"/>
	</fr:edit>	
</logic:present>

<p class="mbottom0">
	<em>Nota: <bean:message key="date.note" bundle="ALUMNI_RESOURCES" /></em>
</p>
