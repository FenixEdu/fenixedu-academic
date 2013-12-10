<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml />

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="label.careerWorkshopApplication.title" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<div class="CareerWorkshop-Terms"><bean:message key="label.careerWorkshopApplication.termsAcceptance" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></div>

<div class="CareerWorkshop-Form">
	<fr:form action="/careerWorkshopApplication.do?method=acceptConfirmation">
		<fr:edit name="confirmationBean" id="confirmationBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.student.careerWorkshop.CareerWorkshopConfirmationBean" bundle="STUDENT_RESOURCES">
				<fr:slot name="confirmationCode" key="label.careerWorkshopApplication.confirmationCode">
					<fr:property name="required" value="true"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
				<fr:property name="columnClasses" value=",,,,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="cancel" path="/careerWorkshopApplication.do?method=declineConfirmation"/>
		</fr:edit>
		<div class="CareerWorkshop-FormButtons">
			<html:submit><bean:message bundle="STUDENT_RESOURCES" key="button.accept" /></html:submit>
			<html:cancel><bean:message bundle="STUDENT_RESOURCES" key="button.decline" /></html:cancel>
		</div>
	</fr:form>
</div>


<style type="text/css">
	.CareerWorkshop-Terms {
		width: 300px;
		margin-top: 80px;
		margin-left: 225px;
		margin-bottom: 10px;
		border-style: solid;
		border-width: 1px;
		padding: 8px;
		background-color: #F6F4FA;
		font-size: 12px;
	}
	
	.CareerWorkshop-Form {
		width: 300px;
		margin-top: 20px;
		margin-left: 235px;
		margin-bottom: 10px;
	}
	
	.CareerWorkshop-FormButtons {
		margin-top: 10px;
		margin-left: 80px;
	}
</style>