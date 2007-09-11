<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.costcenter.information"/></h2>

<div class="infoop2">
	<p><bean:message key="info.grant.manage.grantcostcenter.information"/></p>
	<p><bean:message key="info.grant.manage.grantcostcenter.edit"/>"<bean:message key="link.edit"/>".</p>
	<p><bean:message key="info.grant.manage.grantcostcenter.create"/>"<bean:message key="link.create.grant.costcenter"/>".</p>
</div>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>


<logic:messagesNotPresent>

<logic:present name="infoGrantCostCenterList">
	<%-- Create a new Grant CostCenter --%>
	<p>
		<bean:message key="message.grant.costcenter.creation"/>:
		<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
			<bean:message key="link.create.grant.costcenter"/>
		</html:link>
	</p>


    <table class="tstyle4">
    <%-- Table with grant type description rows --%>
    <tr>
        <th>
            <bean:message key="label.grant.costcenter.number"/>
        </th>
        <th>
            <bean:message key="label.grant.costcenter.designation"/>
        </th>
        <th>
            <bean:message key="label.grant.costcenter.responsibleTeacher.number"/>
        </th>
        <th></th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantCostCenter" name="infoGrantCostCenterList">
        <tr>
            <td>
	            <bean:write name="infoGrantCostCenter" property="number"/>
            </td>
            <td>
	            <bean:write name="infoGrantCostCenter" property="designation"/>
            </td>
            <td class="acenter">
               	<logic:present name="infoGrantCostCenter" property="infoResponsibleTeacher">
		            <bean:write name="infoGrantCostCenter" property="infoResponsibleTeacher.teacherNumber"/>
		        </logic:present>
            </td>
            <td>
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
    <p><bean:message key="message.grant.costcenter.nonExistentGrantTypes" /></p>
</logic:notPresent>
    

<%-- Create a new Grant CostCenter --%>
<p>
	<bean:message key="message.grant.costcenter.creation"/>:
	<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
		<bean:message key="link.create.grant.costcenter"/>
	</html:link>
</p>

</logic:messagesNotPresent>