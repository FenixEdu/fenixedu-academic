<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em class="invisible"><bean:message key="DIRECTIVE_COUNCIL" /></em>
<h2><bean:message key="link.assiduousnessStructure" /></h2>

<logic:present name="assiduousnessStructureSearch">
	<fr:form action="/assiduousnessStructure.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="assiduousnessForm" property="method" value="showAssiduousnessStructure" />
		<fr:edit id="assiduousnessStructureSearch" name="assiduousnessStructureSearch" schema="assiduousnessStructureSearch.responsible" layout="tabular">
		</fr:edit>
		<logic:notEmpty name="assiduousnessStructureSearch" property="searchPerson">
			<logic:equal name="assiduousnessStructureSearch" property="searchPerson" value="true">
				<fr:edit id="assiduousnessStructureSearchEmployee" name="assiduousnessStructureSearch" schema="assiduousnessStructureSearch.employee"/>
			</logic:equal>
			<logic:equal name="assiduousnessStructureSearch" property="searchPerson" value="false">
				<fr:edit id="assiduousnessStructureSearchUnit" name="assiduousnessStructureSearch" schema="assiduousnessStructureSearch.unit"/>
			</logic:equal>
		</logic:notEmpty>
		  <p><html:submit><bean:message key="button.confirm" /></html:submit></p>
		<logic:empty name="assiduousnessStructureSearch" property="personFunctionList">
			<logic:equal name="assiduousnessStructureSearch" property="hasEmployeeInSearch" value="true">
				<bean:message key="message.noResults"/>	
				<logic:notEmpty name="assiduousnessStructureSearch" property="responsible">
					<p><html:submit property="addPerson"><bean:message key="button.join"/></html:submit></p>			
				</logic:notEmpty>
			</logic:equal>
			<logic:equal name="assiduousnessStructureSearch" property="hasUnitInSearch"  value="true">
				<bean:message key="message.noResults"/>	
				<logic:notEmpty name="assiduousnessStructureSearch" property="responsible">
					<p><html:submit property="addUnit"><bean:message key="label.addUnit"/></html:submit></p>
					<p><html:submit property="addPersons"><bean:message key="label.addPersons"/></html:submit></p>				
				</logic:notEmpty>
			</logic:equal>
		</logic:empty>
		<logic:notEmpty name="assiduousnessStructureSearch" property="personFunctionList">
			<fr:view name="assiduousnessStructureSearch" property="personFunctionList" schema="show.personFunctionList">
				<fr:layout name="tabular">
					<fr:property name="link(edit)" value="/assiduousnessStructure.do?method=prepareEditPersonFunction" />
					<fr:property name="key(edit)" value="label.edit" />
					<fr:property name="param(edit)" value="idInternal" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</fr:form>
	
</logic:present>