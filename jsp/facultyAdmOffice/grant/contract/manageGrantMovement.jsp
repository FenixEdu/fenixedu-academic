<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>

<center><b><bean:message key="label.grant.contract.movement.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>
<p class="infoselected">
	<b><bean:message key="label.grant.owner.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="grantOwnerNumber"/><br/>
    <bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="contractNumber"/>
</p>

<center><p><b><bean:message key="label.grant.contract.movement.information"/></b></p></center><br/>

<logic:present name="infoGrantContractMovementsList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant movement description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.movement.location"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.movement.departureDate"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.movement.arrivalDate"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantMovement" name="infoGrantContractMovementsList">
        <tr>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantMovement" property="location">
		            <bean:write name="infoGrantMovement" property="location"/>
	            </logic:present>
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantMovement" property="departureDate">
                    <dt:format pattern="dd-MM-yyyy">
    		            <bean:write name="infoGrantMovement" property="departureDate.time"/>
                    </dt:format>
                </logic:present>
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantMovement" property="arrivalDate">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantMovement" property="arrivalDate.time"/>
                    </dt:format>
	            </logic:present>
            </td>
            <td class="listClasses">
	            <%-- Edit a Grant Movement --%>
                <bean:define id="idGrantMovement" name="infoGrantMovement" property="idInternal"/>
                <html:link page='<%= "/editGrantContractMovement.do?method=prepareEditGrantContractMovementForm&amp;idGrantMovement=" + idGrantMovement.toString() + "&amp;loaddb=" + 1 %>' > 
                	<bean:message key="link.edit" />
                </html:link>
            </td>       
            <td class="listClasses">
	            <%-- Delete a Grant Movement --%>
                <bean:define id="idGrantMovement" name="infoGrantMovement" property="idInternal"/>
                <html:link page='<%= "/editGrantContractMovement.do?method=doDelete&amp;idGrantMovement=" + idGrantMovement.toString() + "&amp;idContract=" + request.getAttribute("idContract").toString() %>' > 
                	<bean:message key="link.delete"/>
                </html:link>
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<%-- If there are no grant movement --%>
<logic:notPresent name="infoGrantContractMovementsList">
	<p align="center"><bean:message key="message.grant.contract.movement.nonExistent" /></p>
</logic:notPresent>
    
<br/><br/>

<%-- Create a new Grant Contract Movement --%>
<bean:message key="message.grant.contract.movement.creation"/>:&nbsp;
	<html:link page='<%= "/editGrantContractMovement.do?method=prepareEditGrantContractMovementForm&amp;idContract=" + request.getAttribute("idContract").toString() + "&amp;loaddb=" + 1 %>' >
<bean:message key="link.create.grant.contract.movement"/>
</html:link>

<br/><br/>

<center>
<html:form action="/editGrantInsurance" style="display:inline">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEditGrantInsuranceForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
		<bean:message key="button.editGrantInsurance"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>