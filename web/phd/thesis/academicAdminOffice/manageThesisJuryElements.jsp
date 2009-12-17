<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.thesis.jury.elements" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
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

<bean:define id="processId" name="process" property="externalId" />

<logic:notEmpty name="process" property="juryElementsDocument">
	
	<strong><bean:message  key="label.phd.thesis.jury.elements.document" bundle="PHD_RESOURCES"/>: </strong>
	<bean:define id="finalThesisDownloadUrl" name="process" property="juryElementsDocument.downloadUrl" />
	<a href="<%= finalThesisDownloadUrl.toString() %>">
		<bean:write name="process" property="juryElementsDocument.documentType.localizedName"/> 
		(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryElementsDocument.documentVersion"/>)
	</a>
</logic:notEmpty>

<br/>
<br/>
<br/>

<strong>Presidente:</strong> Presidente do Conselho Científico do IST

<br/>
<strong><bean:message  key="label.phd.thesis.elements" bundle="PHD_RESOURCES"/>:</strong>
<fr:view name="process" property="thesisJuryElements">

	<fr:schema bundle="PHD_RESOURCES" type="<%= ThesisJuryElement.class.getName() %>">
		<fr:slot name="elementOrder" />
		<fr:slot name="name" />
		<fr:slot name="title" />
		<fr:slot name="qualification" />
		<fr:slot name="category" />
		<fr:slot name="workLocation" />
		<fr:slot name="email" />
		<fr:slot name="reporter" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />

		<fr:property name="linkFormat(top)" value="<%= "/phdThesisProcess.do?method=moveTop&juryElementId=${externalId}&processId=" + processId.toString() %>"/>
		<fr:property name="key(top)" value="label.move.top"/>
		<fr:property name="bundle(top)" value="PHD_RESOURCES"/>
		<fr:property name="visibleIfNot(top)" value="topElement"/>
		<fr:property name="order(top)" value="1"/>

		<fr:property name="linkFormat(up)" value="<%= "/phdThesisProcess.do?method=moveUp&juryElementId=${externalId}&processId=" + processId.toString() %>"/>
		<fr:property name="key(up)" value="label.move.up"/>
		<fr:property name="bundle(up)" value="PHD_RESOURCES"/>
		<fr:property name="visibleIfNot(up)" value="topElement"/>
		<fr:property name="order(up)" value="2"/>

		<fr:property name="linkFormat(down)" value="<%= "/phdThesisProcess.do?method=moveDown&juryElementId=${externalId}&processId=" + processId.toString() %>"/>
		<fr:property name="key(down)" value="label.move.down"/>
		<fr:property name="bundle(down)" value="PHD_RESOURCES"/>
		<fr:property name="visibleIfNot(down)" value="bottomElement"/>
		<fr:property name="order(down)" value="3"/>

		<fr:property name="linkFormat(bottom)" value="<%= "/phdThesisProcess.do?method=moveBottom&juryElementId=${externalId}&processId=" + processId.toString() %>"/>
		<fr:property name="key(bottom)" value="label.move.bottom"/>
		<fr:property name="bundle(bottom)" value="PHD_RESOURCES"/>
		<fr:property name="visibleIfNot(bottom)" value="bottomElement"/>
		<fr:property name="order(bottom)" value="4"/>

		<fr:property name="linkFormat(delete)" value="<%= "/phdThesisProcess.do?method=deleteJuryElement&juryElementId=${externalId}&processId=" + processId.toString() %>"/>
		<fr:property name="key(delete)" value="label.delete"/>
		<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
		<fr:property name="order(delete)" value="5"/>
		<fr:property name="confirmationKey(delete)" value="label.phd.thesis.process.remove.jury.element.confirmation" />
		<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />

		<fr:property name="sortBy" value="elementOrder=asc"/>
	</fr:layout>
</fr:view>
<logic:empty name="process" property="thesisJuryElements">
	<em><bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.no.jury.elements"/></em><br/>
	<br/>
</logic:empty>

<html:link action="/phdThesisProcess.do?method=prepareAddJuryElement" paramId="processId" paramName="process" paramProperty="externalId"> 
	<bean:message bundle="PHD_RESOURCES" key="label.add"/>
</html:link>,&nbsp;

-> presidente do júri
<html:link action="/phdThesisProcess.do?method=prepareAddJuryElement" paramId="processId" paramName="process" paramProperty="externalId"> 
	<bean:message bundle="PHD_RESOURCES" key="label.add"/>
</html:link>,&nbsp;

-> pedir data de validação do júri
<html:link action="/phdThesisProcess.do?method=prepareValidateThesisJury" paramId="processId" paramName="process" paramProperty="externalId"> 
	<bean:message bundle="PHD_RESOURCES" key="label.add"/>
</html:link>,&nbsp;

Se o júri está validado .... é que pode imprimir
<html:link action="/phdThesisProcess.do?method=printJuryElementsDocument" paramId="processId" paramName="process" paramProperty="externalId"> 
	<bean:message bundle="PHD_RESOURCES" key="label.add"/>
</html:link>


<br/><br/>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:present>