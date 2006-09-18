<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<html>
    <head>
    	<title><bean:message key="title.masterDegree.administrativeOffice.printGuide"/></title>
    </head>

    <body>
    
    <bean:define id="candidate" name="candidacy"/>     
    
    <table width="100%" height="100%" border="0">
    <tr height="30"><td>
     <table width="100%" border="0" valign="top">
      <tr> 
        <td height="100" colspan="2">
          <table border="0" width="100%" height="100" align="center" cellpadding="0" cellspacing="0">
            <tr> 
              <td width="50" height="100">
               <img src="<%= request.getContextPath() %>/posGraduacao/guide/images/istlogo.gif" alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />" width="50" height="104" border="0"/> 
              </td>
              <td>
                &nbsp;
              </td>
              <td>
                <table border="0" width="100%" height="100%">
                  <tr valign="top" align="left"> 
                    <td>&nbsp;<b><bean:message bundle="GLOBAL_RESOURCES" key="institution.nameUpperCase" /></b><br/>
                      &nbsp;<b>Secretaria da Pós-Graduação</b><br/>
                      <hr size="1">
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
    <tr valign="top"><td>

	<table width="100%" border="0">
	 <tr>
	 <td>
      <table width="100%" border="0">
          <tr>
            <td width="40%"><strong>Processo de:</strong></td>
            <td width="60%">&nbsp;</td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
            <td> <bean:write name="candidate" property="person.name"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="candidate" property="executionDegree.degreeCurricularPlan.degree.name"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.candidateNumber"/> </td>
            <td> <bean:write name="candidate" property="number"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.executionYear"/> </td>
            <td> <bean:write name="candidate" property="executionDegree.executionYear.year"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.identificationDocumentNumber"/> </td>
            <td> <bean:write name="candidate" property="person.documentIdNumber"/> </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.identificationDocumentType"/> </td>
            <td> 
            	<bean:define id="idType" name="candidate" property="person.idDocumentType"/>
            	<bean:message key='<%=idType.toString()%>'/> 
            </td>
          </tr>
          <tr>
            <td> <bean:message key="label.candidate.username"/> </td>
            <td>
            	<logic:empty name="candidate" property="person.istUsername">
	            	<bean:write name="candidate" property="person.username"/>             	
            	</logic:empty>
            	<logic:notEmpty name="candidate" property="person.istUsername">
    	        	<bean:write name="candidate" property="person.istUsername"/> 
            	</logic:notEmpty>
            </td>
          </tr>
          
	      <tr>
	        <td> <bean:message key="label.candidate.password"/> </td>
	        <td> <font face="Verdana"><bean:write name="password" /> </font></td>
	      </tr>

          
          <tr>
            <td> <bean:message key="label.candidate.accessAddress"/> </td>
            <td> <bean:message key="label.candidate.url"/> </td>
          </tr>

          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          



	  </table>

	  <tr>
	  	<td>
		  <jsp:include page="./warning.jsp" flush="true" />
		</td>
	  </tr>

	 </td>
	 </tr>
	</table>
	</td>
	</tr>




	<tr height="30">
	<td>

    <table width="100%" border="0">
     <tr>	 
	 <td>
	 	<table align="center" width="100%" valign="bottom">
	      <tr>
          <td colspan="2" valign="bottom" >
            <div align="center">
              <font size="2"><bean:message bundle="GLOBAL_RESOURCES" key="footer.computer.processedDocument"/></font> 
            </div>
            <hr size="1" color="#000000" width="100%">
            <div align="center">
              <font size="2"><bean:message bundle="GLOBAL_RESOURCES" key="institution.address"/></font>
            </div>
          </td>
          </tr>
        </table>
     </td>	 
	 </tr>
	</table>

    </td>
    </tr>
    </table>
    
    </body>
</html>
