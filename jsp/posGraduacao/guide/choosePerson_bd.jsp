<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<html:form action="/guideListingByPerson.do?method=getPersonGuideList" focus="studentNumber">
	<html:hidden property="page" value="1"/>
	<bean:define id="identificationDocumentTypeList" name="<%= SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST %>"/>
   	<table width="100%">
       	<tr>
        	<td style="text-align:left" class="listClasses">
        		<bean:message key="message.masterDegree.guide.listing.options"/>
        	</td>
       	</tr>
	</table>
	<br />
	<span class="error"><html:errors/></span>  
	<table>
       	<%-- Student Number --%>
       	<tr>
        	<td><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/>:</td>
         	<td><html:text property="studentNumber" size="5" maxlength="5"/></td>
       	</tr>
       	<%-- Or --%>
       	<tr>
        	<td colspan="2" style="text-align:center">
	        	<br /><i><bean:message key="label.masterDegree.gratuity.or"/></i><br /><br />
        	</td>
       	</tr>
		<%-- Identification Document Number --%>
       	<tr>
        	<td><bean:message key="label.candidate.identificationDocumentNumber"/>:</td>
         	<td><html:text property="identificationDocumentNumber"/></td>
       	</tr>
       	<%-- Identification Document Type --%>
       	<tr>
        	<td><bean:message key="label.candidate.identificationDocumentType"/>:</td>
         	<td>
         		<html:select property="identificationDocumentType">
            		<html:options collection="identificationDocumentTypeList" property="value" labelProperty="label"/>
             	</html:select>
         	</td>
       	</tr>
	</table>
	<br />
	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
	<html:reset value="Limpar" styleClass="inputbutton"/>
</html:form>