<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<script language="Javascript" type="text/javascript">
<!--
function addShift(){
	document.forms[0].method.value='insertAssiduousnessExemptionShift';
	document.forms[0].submit();
	return true;
}

function removeShift(index){
	document.forms[0].method.value='removeAssiduousnessExemptionShift';
	document.forms[0].rowIndex.value=index;
	document.forms[0].submit();
	return true;
}
// -->
</script>
<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.assiduousnessExemptions" /></h2>

<p>
<span class="error0">
	<html:messages id="message" message="true">
		<bean:write name="message" />
	</html:messages>
</span>
</p>

<logic:present name="assiduousnessExemptionBean">
	<fr:form action="/assiduousnessParametrization.do">
		<html:hidden bundle="HTMLALT_RESOURCES" name="parametrizationForm" property="method" value="insertAssiduousnessExemption"/>
		<html:hidden bundle="HTMLALT_RESOURCES" name="parametrizationForm" property="rowIndex" value="0"/>
		<logic:notEmpty name="assiduousnessExemptionBean" property="assiduousnessExemptionShifts">
			<fr:edit name="assiduousnessExemptionBean" schema="edit.assiduousnessExemption">
			</fr:edit>
			<html:link href="javascript:addShift()"><bean:message key="label.addShiftDate"/></html:link>
			<br/>
			<logic:iterate id="shift" name="assiduousnessExemptionBean" property="assiduousnessExemptionShifts" indexId="index">
				<fr:edit id="<%=index.toString()%>" name="shift" schema="edit.assiduousnessExemption.shifts" layout="flow">
				</fr:edit>
				
				<html:link href="<%="javascript:removeShift("+index+")"%>"><bean:message key="label.delete"/></html:link>
				<br/>
			</logic:iterate>
		</logic:notEmpty>
		<p><html:submit>
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form>
</logic:present>
