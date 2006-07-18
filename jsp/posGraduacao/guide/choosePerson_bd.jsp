<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<html:form action="/guideListingByPerson.do?method=getPersonGuideList" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
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
         	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" size="5" maxlength="5"/></td>
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
         	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.identificationDocumentNumber" property="identificationDocumentNumber"/></td>
       	</tr>
       	<%-- Identification Document Type --%>
       	<tr>
        	<td><bean:message key="label.candidate.identificationDocumentType"/>:</td>
         	<td>
         		<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
         		<html:select bundle="HTMLALT_RESOURCES" altKey="select.identificationDocumentType" property="identificationDocumentType">
         			<html:option key="dropDown.Default" value=""/>
            		<html:options collection="values" property="value" labelProperty="label"/>
             	</html:select>
         	</td>
       	</tr>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</html:form>