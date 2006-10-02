<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.selectCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<br/>
<fr:form action="/selectDFACandidacies.do?method=confirmCandidacies">

	<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
		<br/>
	</html:messages>	

	<logic:present name="admittedCandidacies">
		<h2><strong><bean:message key="label.admittedCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
		<logic:empty name="admittedCandidacies">
			<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
		</logic:empty>
		<logic:notEmpty name="admittedCandidacies">
			<bean:size id="numberOfCandidacies" name="admittedCandidacies" />
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<fr:edit name="admittedCandidacies" schema="candidacy.show.listForSelection" visible="false" id="admittedCandidacies" />
			<fr:view name="admittedCandidacies" schema="candidacy.show.listForSelection" >
				<fr:layout name="tabular">
			        <fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value=",,,acenter"/>
					<fr:property name="sortBy" value="candidacy.number"/>
			    </fr:layout>
			</fr:view>
		</logic:notEmpty>
		<br/><br/>
	</logic:present>
	
    <input id="tree-structure" type="hidden" name="substituteCandidaciesOrder" value="" />
	<logic:present name="substituteCandidacies">
		<h2><strong><bean:message key="label.substituteCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
		<logic:empty name="substituteCandidacies">
			<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
		</logic:empty>
		<logic:notEmpty name="substituteCandidacies">
			<p class="infoop"><bean:message key="message.substitutes.order.explanation" bundle="ADMIN_OFFICE_RESOURCES" /></p>
			<bean:size id="numberOfCandidacies" name="substituteCandidacies" />			
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<br/><br/>
			<fr:edit name="substituteCandidacies" schema="candidacy.show.listForSelection" visible="false" id="substituteCandidacies" />			
			<fr:view name="substituteCandidacies"  >
				<fr:layout name="tree">
			        <fr:property name="listClass" value=""/>

			        <fr:property name="treeId" value="substituteCandidaciesOrderTree"/>
			        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
			        
			        <fr:property name="eachLayout" value="values-dash"/>
			        <fr:property name="eachSchema" value="candidacy.show.listForSelection"/>
			    </fr:layout>
			</fr:view>
			
			<script type="text/javascript">
			    document.getElementById("tree-controls").style.display = 'block';
			</script> 
		</logic:notEmpty>
		<br/><br/>
	</logic:present>
	
	<logic:present name="notAdmittedCandidacies">
		<h2><strong><bean:message key="label.notAdmittedCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
		<logic:empty name="notAdmittedCandidacies">
			<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
		</logic:empty>
		<logic:notEmpty name="notAdmittedCandidacies">
			<bean:size id="numberOfCandidacies" name="notAdmittedCandidacies" />
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<fr:edit name="notAdmittedCandidacies" schema="candidacy.show.listForSelection" visible="false" id="notAdmittedCandidacies" />
			<fr:view name="notAdmittedCandidacies" schema="candidacy.show.listForSelection" >
				<fr:layout name="tabular">
			        <fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value=",,,acenter"/>
					<fr:property name="sortBy" value="candidacy.number"/>
			    </fr:layout>
			</fr:view>
		</logic:notEmpty>
		<br/><br/>
	</logic:present>
	
	<p>
		<html:submit onclick="treeRenderer_saveTree('substituteCandidaciesOrderTree');"><bean:message key="button.confirm" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
	</p>
	
</fr:form>