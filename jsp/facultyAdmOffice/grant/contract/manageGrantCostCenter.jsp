<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p><b><bean:message key="label.grant.costcenter.information"/></b></p>

	<table class="infoop">
		<tr>
			<td rowspan=4><p class="emphasis-box">i</p></td>
		    <td><bean:message key="info.grant.manage.grantcostcenter.information"/></td>
		</tr>
		<tr>
			<td><bean:message key="info.grant.manage.grantcostcenter.edit"/>"<bean:message key="link.edit"/>".</td>
		</tr>
		<tr>
			<td><bean:message key="info.grant.manage.grantcostcenter.create"/>"<bean:message key="link.create.grant.costcenter"/>".</td>
		</tr>
	</table><br/><br/>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>


<logic:messagesNotPresent>

<logic:present name="infoGrantCostCenterList">
	<%-- Create a new Grant CostCenter --%>
	<bean:message key="message.grant.costcenter.creation"/>:&nbsp;
	<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
		<bean:message key="link.create.grant.costcenter"/>
	</html:link><br/><br/>


    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant type description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.costcenter.number"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.costcenter.designation"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.costcenter.responsibleTeacher.number"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantCostCenter" name="infoGrantCostCenterList">
        <tr>
            <td class="listClasses">&nbsp;
	            <bean:write name="infoGrantCostCenter" property="number"/>
            </td>
            <td class="listClasses">&nbsp;
	            <bean:write name="infoGrantCostCenter" property="designation"/>
            </td>
            <td class="listClasses">&nbsp;
               	<logic:present name="infoGrantCostCenter" property="infoResponsibleTeacher">
		            <bean:write name="infoGrantCostCenter" property="infoResponsibleTeacher.teacherNumber"/>
		        </logic:present>
            </td>
            <td class="listClasses">
		            <%-- Edit a Grant CostCenter --%>
                    <bean:define id="idGrantCostCenter" name="infoGrantCostCenter" property="idInternal"/>
                    <html:link page='<%= "/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm&amp;idGrantCostCenter=" + idGrantCostCenter.toString() %>' > 
                        <bean:message key="link.edit" />
                    </html:link>        
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no grant cost centers --%>
<logic:notPresent name="infoGrantCostCenterList">
    <p align="center"><bean:message key="message.grant.costcenter.nonExistentGrantTypes" /></p>
</logic:notPresent>
    
<br/><br/>

<%-- Create a new Grant CostCenter --%>
<bean:message key="message.grant.costcenter.creation"/>:&nbsp;
<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
	<bean:message key="link.create.grant.costcenter"/>
</html:link>

</logic:messagesNotPresent>