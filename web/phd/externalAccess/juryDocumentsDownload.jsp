<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>


<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>

<br/>
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdExternalAccess.do?method=prepare" paramId="hash" paramName="participant" paramProperty="accessHashCode">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=operationBean" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="participant">
	<jsp:include page="processDetails.jsp" />
	
	<fr:form action="/phdExternalAccess.do?method=juryDocumentsDownload">
	
		<fr:edit id="operationBean" name="operationBean">
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdExternalOperationBean.class.getName() %>">
				<fr:slot name="email" required="true" validator="<%= EmailValidator.class.getName() %>"/>
				<fr:slot name="password" required="true" layout="password" />
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="/phdExternalAccess.do?method=prepareJuryDocumentsDownloadInvalid"/>
			
		</fr:edit>
		
		<html:submit><bean:message key="label.submit" /></html:submit>
	
	</fr:form>
	
</logic:notEmpty>


