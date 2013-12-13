<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean"%>


<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>
<br/>

<logic:notEmpty name="operationBean" >

	<%-- Print access type name --%>
	<h2><bean:write name="operationBean" property="accessType.localizedName" /></h2>

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
	
	<%-- Process Details --%>
	<jsp:include page="processDetails.jsp" />
	
	<bean:define id="accessTypeDescriptor" name="operationBean" property="accessType.descriptor" type="java.lang.String"/>
	<% request.setAttribute("actionMethodName", accessTypeDescriptor.substring(0,1).toLowerCase() + accessTypeDescriptor.substring(1)); %>
	
	<bean:define id="actionMethodName" name="actionMethodName" />
	
	<fr:form action="<%= "/phdExternalAccess.do?method=" + actionMethodName %>">
	
		<fr:edit id="operationBean" name="operationBean">
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdExternalOperationBean.class.getName() %>">
			
				<%@include file="/phd/externalAccess/common/accessInformation.jsp" %>
			
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= String.format("/phdExternalAccess.do?method=prepare%sInvalid", accessTypeDescriptor) %>"/>
		</fr:edit>
		
		<html:submit><bean:message key="label.submit" /></html:submit>
	
	</fr:form>
	
</logic:notEmpty>

</logic:notEmpty>
