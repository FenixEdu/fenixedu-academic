<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.insurance.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%--  INSURANCES --%>    
<p class="infoselected">
	<b><bean:message key="label.grant.insurance.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="grantOwnerNumber"/><br/>
    <bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="grantContractNumber"/>
</p>


<%-- LISTA DE SEGUROS ACTIVOS --%>

<logic:present name="infoGrantActiveInsuranceList">
	<b><bean:message key="label.grant.insurance.active.list"/></b><br/>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with insurance description rows --%>
    <tr>
        <td class="listClasses-header">
        </td>
        <td class="listClasses-header">
        </td>
        <td class="listClasses-header">
        </td>
        <td class="listClasses-header">
        </td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantSubsidy" name="infoGrantActiveSubsidyList">
        <tr>
	        <td class="listClasses">
	        </td>
            <td class="listClasses">
            
            </td>
            <td class="listClasses">
            
            </td>
            <td class="listClasses">
            
            </td>
            <td class="listClasses">
                    <bean:define id="idInsurance" name="infoGrantInsurance" property="idInternal"/>
                    <html:link page='<%= "/editGrantInsurance.do?method=prepareEditGrantInsuranceForm&amp;idInsurance=" + idInsurance %>' > 
                        <bean:message key="link.grant.subsidy.edit" />
                    </html:link>        
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<br/><br/>

<%-- If there are no insurances --%>
<logic:notPresent name="infoGrantActiveInsuranceList">
    <p align="center"><bean:message key="message.grant.insurance.nonExistent" /></p><br/><br/>
</logic:notPresent>

<bean:message key="message.grant.insurance.creation"/>:&nbsp;
<html:link page='<%= "/editGrantInsurance.do?method=prepareEditGrantInsuranceForm&amp;idContract=" + request.getAttribute("idContract").toString() %>'>
	<bean:message key="link.grant.insurance.create"/>
</html:link>
<br/><bean:message key="message.grant.insurance.creation.note"/><br/><br/>



<br/><br/><br/>
<center>
<html:form action="/manageGrantContract" style="display:inline">
	<html:hidden property="method" value="prepareManageGrantContractForm"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/>
	<html:submit styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantContract"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>