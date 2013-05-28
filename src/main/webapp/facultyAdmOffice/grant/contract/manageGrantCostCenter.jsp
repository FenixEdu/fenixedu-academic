<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<logic:present name="grantCenterList">
	<%-- Create a new Grant CostCenter --%>
	<p>
		<bean:message key="message.grant.costcenter.creation"/>:
		<html:link page="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm">
			<bean:message key="link.create.grant.costcenter"/>
		</html:link>
	</p>


	<fr:view name="grantCenterList" schema="show.grantCostCenter">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
			<fr:property name="columnClasses" value="acenter" />
			<fr:property name="headerClasses" value="acenter" />
            <fr:property name="link(edit)" value="/editGrantCostCenter.do?method=prepareEditGrantCostCenterForm" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="param(edit)" value="externalId/idGrantCostCenter" />
		</fr:layout>
	</fr:view> 

</logic:present>
    
<%-- If there are no grant cost centers --%>
<logic:notPresent name="grantCenterList">
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