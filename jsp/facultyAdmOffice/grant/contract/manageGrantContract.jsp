<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.contract.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%--  CONTRATOS --%>    
<p class="infoselected">
	<b><bean:message key="label.grant.contract.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="grantOwnerNumber"/><br/>
    <bean:message key="label.grant.owner.name"/>:&nbsp;<bean:write name="grantOwnerName"/>
</p>

<logic:present name="infoGrantContractList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with contract description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.number"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.orientationTeacher"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.contract.state"/>
        </td>
        <td class="listClasses-header">&nbsp;</td>
        <td class="listClasses-header">&nbsp;</td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantContract" name="infoGrantContractList">
        <tr>
            <td class="listClasses">
                <bean:write name="infoGrantContract" property="contractNumber"/>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantContract" property="grantOrientationTeacherInfo">
                    <bean:write name="infoGrantContract" property="grantOrientationTeacherInfo.orientationTeacherInfo.infoPerson.nome"/>
                </logic:present>
                <logic:notPresent name="infoGrantContract" property="grantOrientationTeacherInfo">
                    ---
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:equal name="infoGrantContract" property="contractActive" value="true">
                    <bean:message key="label.grant.contract.state.open"/>
                </logic:equal>
                <logic:equal name="infoGrantContract" property="contractActive" value="false">
                     <bean:message key="label.grant.contract.state.close"/>
                </logic:equal>
            </td>
            <td class="listClasses">
                    <bean:define id="idContract" name="infoGrantContract" property="idInternal"/>
                    <html:link page='<%= "/editGrantContract.do?method=prepareEditGrantContractForm&amp;idContract=" + idContract %>' > 
                        <bean:message key="link.grant.contract.edit" />
                    </html:link>        
            </td>
            <td class="listClasses">
                    <bean:define id="idContract" name="infoGrantContract" property="idInternal"/>
                    <bean:define id="idGrantOwner" name="infoGrantContract" property="grantOwnerInfo.idInternal"/>
                    <html:link page='<%= "/manageGrantSubsidy.do?method=prepareManageGrantSubsidyForm&amp;idContract=" + idContract %>' > 
                        <bean:message key="link.manage.grant.subsidy" />
                    </html:link>        
            </td>
			<td class="listClasses">
                    <bean:define id="idContract" name="infoGrantContract" property="idInternal"/>
                    <bean:define id="idGrantOwner" name="infoGrantContract" property="grantOwnerInfo.idInternal"/>
                    <html:link page='<%= "/manageGrantContractRegime.do?method=prepareManageGrantContractRegime&amp;idContract=" + idContract %>' > 
                        <bean:message key="link.manage.grant.contract.regime" />
                    </html:link>        
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no contracts --%>
<logic:notPresent name="infoGrantContractList">
    <p align="center"><bean:message key="message.grant.contract.nonExistentContracts" /></p>
</logic:notPresent>
    
<br/><br/>

<bean:message key="message.grant.contract.creation"/>:&nbsp;
<html:link page='<%= "/editGrantContract.do?method=prepareEditGrantContractForm&amp;idInternal=" + request.getAttribute("idInternal").toString() %>'>
	<bean:message key="link.grant.contract.create"/>
</html:link>

<br/><br/><br/>
<center>
<html:form action="/manageGrantOwner" style="display:inline">
	<html:hidden property="method" value="prepareManageGrantOwnerForm"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
	<html:submit styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantOwner"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>