<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<%-- Presenting errors --%>
<logic:messagesPresent>

<center><b><bean:message key="label.grant.part.information"/></b></center>
<span class="error">
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>
<p class="infoselected">
	<b><bean:message key="label.grant.subsidy.information"/></b><br/>
	<logic:present name="subsidyValue">
    	<bean:message key="label.grant.subsidy.value"/>:&nbsp;<bean:write name="subsidyValue"/><br/>
    </logic:present>
    <logic:present name="subsidyTotalCost">
    	<bean:message key="label.grant.subsidy.totalCost"/>:&nbsp;<bean:write name="subsidyTotalCost"/>
    </logic:present>
</p>

<p><b><bean:message key="label.grant.part.information"/></b></p><br/>

<logic:present name="infoGrantPartList">
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant part description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.part.percentage"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.part.grantPaymentEntity.designation"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.part.responsibleTeacher.number"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantPart" name="infoGrantPartList">
        <tr>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantPart" property="percentage">
		            <bean:write name="infoGrantPart" property="percentage"/>
	            </logic:present>
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantPart" property="infoGrantPaymentEntity">
		            <bean:write name="infoGrantPart" property="infoGrantPaymentEntity.designation"/>
	            </logic:present>
            </td>
            <td class="listClasses">&nbsp;
	        	<logic:present name="infoGrantPart" property="infoResponsibleTeacher">
		            <bean:write name="infoGrantPart" property="infoResponsibleTeacher.teacherNumber"/>
	            </logic:present>
            </td>
            <td class="listClasses">
	            <%-- Edit a Grant Part --%>
                <bean:define id="idGrantPart" name="infoGrantPart" property="idInternal"/>
                <html:link page='<%= "/editGrantPart.do?method=prepareEditGrantPartForm&amp;idGrantPart=" + idGrantPart.toString() + "&amp;loaddb=" + 1 %>' > 
                	<bean:message key="link.edit" />
                </html:link>
            </td>       
            <td class="listClasses">
	            <%-- Delete a Grant Part --%>
                <bean:define id="idGrantPart" name="infoGrantPart" property="idInternal"/>
                <html:link page='<%= "/editGrantPart.do?method=doDelete&amp;idGrantPart=" + idGrantPart.toString() + "&amp;idSubsidy=" + request.getAttribute("idSubsidy").toString() %>' > 
                	<bean:message key="link.delete"/>
                </html:link>
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<%-- If there are no grant part --%>
<logic:notPresent name="infoGrantPartList">
	<p align="center"><bean:message key="message.grant.part.nonExistent" /></p>
</logic:notPresent>
    
<br/><br/>

<%-- Create a new Grant Part --%>
<bean:message key="message.grant.type.creation"/>:&nbsp;
	<html:link page='<%= "/editGrantPart.do?method=prepareEditGrantPartForm&amp;idSubsidy=" + request.getAttribute("idSubsidy").toString() + "&amp;loaddb=" + 1 %>' >
<bean:message key="link.create.grant.part"/>
</html:link>

<br/><br/>

<center>
<html:form action="/manageGrantSubsidy" style="display:inline">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantSubsidyForm"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idContract" property="idContract" value='<%= request.getAttribute("idContract").toString() %>'/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantSubsidy"/>
	</html:submit>
</html:form>		
</center>

</logic:messagesNotPresent>