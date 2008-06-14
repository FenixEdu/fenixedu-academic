<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<script language="Javascript" type="text/javascript">
<!--
function selectAll(){
	var all=document.getElementsByTagName("input");
	for (var i=0; i<all.length; i++){
		all[i].checked = true;
	}
}

function selectNone(){
	var all=document.getElementsByTagName("input");
	for (var i=0; i<all.length; i++){
		all[i].checked = false;
	}
}

// -->
</script>

<em class="invisible"><bean:message key="DIRECTIVE_COUNCIL" /></em>
<h2><bean:message key="link.assiduousnessStructure" /></h2>

<p>
	<span class="error0">
		<html:messages id="message" message="true"><bean:write name="message" /></html:messages>
	</span>
</p>

<logic:present name="assiduousnessPersonFunctionFactory">
	<logic:equal name="assiduousnessPersonFunctionFactory" property="byPersons" value="true">
		<fr:view name="assiduousnessPersonFunctionFactory" schema="show.assiduousnessPersonFunctionResponsible">
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright"/>
			</fr:layout>
		</fr:view>
		<bean:size id="employeesNumber" name="assiduousnessPersonFunctionFactory"  property="party.allCurrentNonTeacherEmployees"/>
		<logic:equal name="employeesNumber" value="0">
			
			<p class="mbottom15"><em><bean:message key="message.noEmployees"/></em>.</p>
			
			<fr:form action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
				<fr:edit visible="false" id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory" schema="create.assiduousnessPersonFunctionFactory">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>				
					<fr:destination name="invalid" path="/assiduousnessStructure.do?method=prepareCreateAssiduousnessPersonFunction"/>
				</fr:edit>		
				<html:cancel><bean:message key="button.cancel"/></html:cancel>
			</fr:form>
		</logic:equal>
		
		<logic:notEqual name="employeesNumber" value="0">
			<fr:form action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
				<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory" 
				schema="create.assiduousnessPersonFunctionFactory">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
					<fr:destination name="invalid" path="/assiduousnessStructure.do?method=prepareCreateAssiduousnessPersonFunction"/>
				</fr:edit>
				<p class="mbottom05">
					Seleccionar: 
					<html:link href="javascript:selectAll();" onclick=""><bean:message key="label.showBy.all"/></html:link> | 
					<html:link href="javascript:selectNone();" onclick=""><bean:message key="label.none"/></html:link>
				</p>
				<fr:edit id="assiduousnessPersonFunctionFactory1" name="assiduousnessPersonFunctionFactory" schema="create.assiduousnessPersonFunctionFactory.employees">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright thmiddle inobullet ulnomargin mtop05"/>
				        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
					<fr:destination name="invalid" path="/assiduousnessStructure.do?method=prepareCreateAssiduousnessPersonFunction"/>
				</fr:edit>
				<p>
					<html:submit><bean:message key="button.confirm"/></html:submit>
					<html:cancel><bean:message key="button.cancel"/></html:cancel>
				</p>
			</fr:form>
		</logic:notEqual>
	</logic:equal>
	<logic:equal name="assiduousnessPersonFunctionFactory" property="byPersons" value="false">
		<fr:view name="assiduousnessPersonFunctionFactory" schema="show.assiduousnessPersonFunctionParties">
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright"/>
			</fr:layout>
		</fr:view>
		<logic:equal name="assiduousnessPersonFunctionFactory" property="isPartyAnUnit" value="true" >
			<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory"
				schema="create.assiduousnessPersonFunctionFactory"
				action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		        	<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:destination name="invalid" path="/assiduousnessStructure.do?method=prepareCreateAssiduousnessPersonFunction"/>
			</fr:edit>
		</logic:equal>
		<logic:equal name="assiduousnessPersonFunctionFactory" property="isPartyAnUnit" value="false" >
			<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory"
				schema="create.assiduousnessPersonFunctionFactory"
				action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		    	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
				<fr:destination name="invalid" path="/assiduousnessStructure.do?method=prepareCreateAssiduousnessPersonFunction"/>
			</fr:edit>
		</logic:equal>
	</logic:equal>
</logic:present>
