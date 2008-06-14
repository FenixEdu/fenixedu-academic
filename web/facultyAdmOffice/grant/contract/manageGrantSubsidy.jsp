<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>
<center><b><bean:message key="label.grant.subsidy.information"/></b></center>
<span class="error"><!-- Error messages go here -->
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


<%-- LISTA DE SUBSIDIOS ACTIVOS --%>

<logic:present name="infoGrantActiveSubsidyList">
	<b><bean:message key="label.grant.subsidy.state.actual"/></b><br/>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with subsidy description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateBeginSubsidy"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateEndSubsidy"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.value"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.totalCost"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantSubsidy" name="infoGrantActiveSubsidyList">
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
                 <bean:write name="infoGrantSubsidy" property="value"/>
            </td>
            <td class="listClasses">
                 <bean:write name="infoGrantSubsidy" property="totalCost"/>
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

<br/><br/>

<%-- If there are no subsidies --%>
<logic:notPresent name="infoGrantActiveSubsidyList">
    <p align="center"><bean:message key="message.grant.subsidy.nonExistent" /></p><br/><br/>
</logic:notPresent>

<bean:message key="message.grant.subsidy.creation"/>:&nbsp;
<html:link page='<%= "/editGrantSubsidy.do?method=prepareEditGrantSubsidyForm&amp;idContract=" + request.getAttribute("idContract").toString() %>'>
	<bean:message key="link.grant.subsidy.create"/>
</html:link>
<br/><bean:message key="message.grant.subsidy.creation.note"/><br/><br/>

<%-- LISTA DE SUBSIDIOS ACTIVOS --%>

<logic:present name="infoGrantNotActiveSubsidyList">
<br/><br/>
	<b><bean:message key="label.grant.subsidy.notactive.list"/></b><br/>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with subsidy description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateBeginSubsidy"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.dateEndSubsidy"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.value"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.subsidy.totalCost"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantSubsidy" name="infoGrantNotActiveSubsidyList">
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
                 <bean:write name="infoGrantSubsidy" property="value"/>
            </td>
            <td class="listClasses">
                 <bean:write name="infoGrantSubsidy" property="totalCost"/>
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

<br/><br/><br/>
<center>
<html:form action="/manageGrantContract" style="display:inline">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantContractForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantContract"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>