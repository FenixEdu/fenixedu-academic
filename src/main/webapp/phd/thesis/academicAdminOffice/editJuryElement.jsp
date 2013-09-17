<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>

<html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.jury.elements" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdThesisProcess.do?method=manageThesisJuryElements&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<strong><bean:message  key="label.phd.edit.thesis.jury.element" bundle="PHD_RESOURCES"/></strong><br/>

<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>">

	<input type="hidden" name="method" value="" />
	<fr:edit id="thesisJuryElementBean" name="thesisJuryElementBean" visible="false" />

	<fr:edit id="thesisJuryElementBean.jury.type" name="thesisJuryElementBean" >
		
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisJuryElementBean.class.getName() %>">

			<fr:slot name="name" readOnly="true" />

			<%-- common fields --%>
			<fr:slot name="title">
				<fr:property name="size" value="30" />
			</fr:slot>
			<fr:slot name="category" required="true" >
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="workLocation">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="institution">
				<fr:property name="size" value="50" />
			</fr:slot>

			<logic:notEqual name="thesisJuryElementBean" property="internal" value="true">
				<fr:slot name="name" required="true" >
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="qualification" required="true" >
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="address" >
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="email" required="true" >
					<fr:validator name="<%= EmailValidator.class.getName() %>" />
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="phone" >
					<fr:property name="size" value="50" />
				</fr:slot>
			</logic:notEqual>

			<fr:slot name="reporter" />
			<fr:slot name="expert" />
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=editJuryElementInvalid&processId=" + processId.toString() %>"/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='editJuryElement';return true;"><bean:message bundle="PHD_RESOURCES" key="label.save"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageThesisJuryElements';return true;"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>


<%--  ### End of Operation Area  ### --%>
