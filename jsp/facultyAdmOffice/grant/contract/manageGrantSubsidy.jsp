<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.subsidy.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<%--  SUBSIDIOS --%>    
<p class="infoselected">
	<b><bean:message key="label.grant.subsidy.information"/></b><br/>
    <bean:message key="label.grant.owner.number"/>:&nbsp;<bean:write name="grantOwnerNumber"/><br/>
    <bean:message key="label.grant.contract.contractnumber"/>:&nbsp;<bean:write name="grantContractNumber"/>
</p>

<logic:present name="infoGrantSubsidyList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with subsidy description rows --%>
    <tr>
        <td class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateBeginSubsidy"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateEndSubsidy"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.subsidy.value"/>
        </td>
        <td class="listClasses-header">
            <bean:message key="label.grant.subsidy.totalCost"/>
        </td>
        <td class="listClasses-header">&nbsp;</td>
        <td class="listClasses-header">&nbsp;</td>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantSubsidy" name="infoGrantSubsidyList">
        <tr>
        <td class="listClasses">
               <logic:present name="infoGrantSubsidy" property="dateBeginSubsidy">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantSubsidy" property="dateBeginSubsidy.time"/>
                    </dt:format>
			   </logic:present>
                <logic:notPresent name="infoGrantSubsidy" property="dateBeginSubsidy">
                    ---
                </logic:notPresent>
            </td>
            <td class="listClasses">
                <logic:present name="infoGrantSubsidy" property="dateEndSubsidy">
                    <dt:format pattern="dd-MM-yyyy">
                        <bean:write name="infoGrantSubsidy" property="dateEndSubsidy.time"/>
                    </dt:format>
                </logic:present>
                <logic:notPresent name="infoGrantSubsidy" property="dateEndSubsidy">
                    ---
                </logic:notPresent>
            </td>
            <td class="listClasses">
                € <bean:write name="infoGrantSubsidy" property="value"/>
            </td>
            <td class="listClasses">
                € <bean:write name="infoGrantSubsidy" property="totalCost"/>
            </td>
            <td class="listClasses">
                    <bean:define id="idSubsidy" name="infoGrantSubsidy" property="idInternal"/>
                    <html:link page='<%= "/editGrantSubsidy.do?method=prepareEditGrantSubsidyForm&amp;idSubsidy=" + idSubsidy %>' > 
                        <bean:message key="link.grant.subsidy.edit" />
                    </html:link>        
            </td>
            <td class="listClasses">
            		<bean:define id="idSubsidy" name="infoGrantSubsidy" property="idInternal"/>
                    <html:link page='<%= "/manageGrantPart.do?method=prepareManageGrantPart&amp;idSubsidy=" + idSubsidy %>' > 
                        <bean:message key="link.manage.grant.part" />
                    </html:link>        
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no subsidies --%>
<logic:notPresent name="infoGrantSubsidyList">
    <p align="center"><bean:message key="message.grant.subsidy.nonExistent" /></p>
</logic:notPresent>
    
<br/><br/>

<logic:notPresent name="infoGrantSubsidyList">
	<bean:message key="message.grant.subsidy.creation"/>:&nbsp;
	<html:link page='<%= "/editGrantSubsidy.do?method=prepareEditGrantSubsidyForm&amp;idContract=" + request.getAttribute("idContract").toString() %>'>
		<bean:message key="link.grant.subsidy.create"/>
	</html:link>
</logic:notPresent>

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