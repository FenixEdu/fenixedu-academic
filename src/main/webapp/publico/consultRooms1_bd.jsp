<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message key="title.search.rooms"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<br />
<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="message.search.rooms" /></td>
	</tr>
</table>
<logic:present name="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>" scope="request">

	<html:form action="/prepareConsultRoomsNew.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="<%="hidden." + PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td nowrap="nowrap" width="125">
			    	<bean:message key="property.executionPeriod"/>:
			    </td>
			</tr>
			<tr>
			    <td nowrap="nowrap">
			    	<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1"
			    				 onchange='this.form.submit();'>
						<html:options property="value"
							labelProperty="label" 
							collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>"/>
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			    </td>
			</tr>
		</table>
	</html:form> 
	<br />
</logic:present>
<html:form action="/chooseRoomsFormNew" >
	<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>"  property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />

    <table border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td class="tall-td"><bean:message key="property.room.name"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="11" maxlength="20"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.building"/>:</td>
            <td>
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building" size="1">
                    <html:options collection="publico.buildings" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.floor"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" property="floor" size="2" maxlength="2"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.type"/>:</td>
            <td>
            	<html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type" size="1">
                	<html:options collection="publico.types" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.capacity.normal"/>:</td>
            <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/></td>
        </tr>
        <tr>
            <td class="tall-td"><bean:message key="property.room.capacity.exame"/>:</td>
            <td height="0"><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/></td>
        </tr>
    </table>
    <br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.submit"/></html:submit>
    <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>