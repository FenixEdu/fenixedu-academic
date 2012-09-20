<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="title.phdProgramInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="phdProgramId" name="phdProgram" property="externalId"/>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdProgramInformation.do?method=listPhdProgramInformations" paramId="phdProgramId" paramName="phdProgramId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phdProgramInformation.create" bundle="PHD_RESOURCES"/></strong></p>

<fr:form action="<%= "/phdProgramInformation.do?method=create&amp;phdProgramId=" + phdProgramId %>" >
	<fr:edit id="phdProgramInformationBean" name="phdProgramInformationBean" visible="false" />
		
	<fr:edit id="phdProgramInformationBean.create" name="phdProgramInformationBean">	

		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramInformationBean" >
			<fr:slot name="beginDate" required="true" />
			
			<fr:slot name="minThesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />
			</fr:slot>
			<fr:slot name="maxThesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="minStudyPlanEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="maxStudyPlanEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="numberOfYears" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />
			</fr:slot>
			<fr:slot name="numberOfSemesters" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />
			</fr:slot>			
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />			
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/phdProgramInformation.do?method=listPhdProgramInformations&amp;phdProgramId=" + phdProgramId %>" />
		<fr:destination name="invalid" path="<%= "/phdProgramInformation.do?method=createInvalid&phdProgramId=" + phdProgramId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>

</logic:present>
