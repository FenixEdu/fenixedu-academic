<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<html>
    <head>
    	<title><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.guide"/></title>
    </head>

    <body>

    <table width="100%" height="100%" border="0">
    <tr height="30"><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="104" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="LogoIST" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr align="left"> 
                    <td>&nbsp;<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.printTemplates.institutionName.upper.case"/></b><br/>
                      &nbsp;<b><bean:write name="currentUnit" property="name"/></b><br/>
                      &nbsp;<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
                      <hr size="1">
                    </td>
                  </tr>
                  <tr> 
                    <td align="right" valign="top"> <b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.guide"/></b>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
	</table>

	</td>
	</tr>
    <tr valign="top" >
    <td>
	
	<table width="100%" border="0">
	 	<tr>
	 		<td>
	 			<bean:define id="person" name="createOtherPartyPayment" property="event.person" />
		    	<table width="100%" border="0">
		          <tr>
		            <td width="20%"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.processFrom"/>:</strong></td>
		            <td width="80%">&nbsp;</td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" /> </td>
		            <td> <bean:write name="person" property="name"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES" /> </td>
		            <td> <bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" /> </td>
		            <td> <bean:write name="person" property="documentIdNumber"/> </td>
		          </tr>
			  </table>
	 		</td>
	 	</tr>
	 	<tr>
	 		<td> </td>
	 	</tr>
	 	<tr>
	 		<td>
	 			<bean:define id="contributor" name="createOtherPartyPayment" property="contributorParty" />
		        <table width="100%" border="0">
		          <tr>
		            <td width="20%"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.guideForOtherPartyPayments.contributor"/>:</strong></td>
		            <td width="80%">&nbsp;</td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.name" bundle="APPLICATION_RESOURCES" /> </td>
		            <td> <bean:write name="contributor" property="name"/> </td>
		          </tr>
		          <tr>
		            <td> <bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.socialSecurityNumber" bundle="APPLICATION_RESOURCES" /> </td>
		            <td> <bean:write name="contributor" property="socialSecurityNumber"/> </td>
		          </tr>
			  </table>
	 		</td>
	 	</tr>
	 </table>
	 </td>
	 </tr>
	 
	 <tr>
	 <td> 
	   <table align="right">
       		<tr>
       			<td>
       				<app:labelFormatter name="createOtherPartyPayment" property="event.description">
       					<app:property name="enum" value="ENUMERATION_RESOURCES"/>
       					<app:property name="application" value="APPLICATION_RESOURCES"/>
						<app:property name="default" value="APPLICATION_RESOURCES"/>	
       				</app:labelFormatter>
       			</td>
       			<td>...................................&nbsp;</td>
       			<td> <bean:define id="amountToPay" name="createOtherPartyPayment" property="amount" type="Money" /> <%= amountToPay.toPlainString() %> &nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/></td>
       		</tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    	<tr>
    	  	<td><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.totalAmountToPay"/></strong></td>
   			<td>_________________&nbsp;</td>
   			<td> <bean:define id="amountToPay" name="createOtherPartyPayment" property="amount" type="Money" /> <%= amountToPay.toPlainString() %> &nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currencySymbol"/></td>
    	</tr>
	   </table>
	 </td>
	 </tr>	
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr>
	 <td>&nbsp;
	 </td>
	 </tr>
	 <tr valign="bottom">
	 <td>
     <table valign="bottom" width="100%" border="0">
       <tr>
         <td>
         	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
         </td>
       </tr>
       
       <tr>
        <td>&nbsp;</td>
         <td colspan="2" valign="bottom">
           &nbsp;<div align="center">&nbsp;</div>
           <div align="center">&nbsp;</div>
           <div align="center"><b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.payments.printTemplates.theEmployee"/></b> <br/>
            <br/>
            <br/>
           </div>
          <hr align="center" width="300" size="1">
         </td>
       </tr>

	 </table>
	 </td>
	 </tr>
	 
     <tr>	 
	 <td>
		 <jsp:include page="/academicAdminOffice/payments/commons/footer.jsp" flush="true" />
    </td>
    </tr>
    </table>
	
    </body>
</html>

</logic:present>
