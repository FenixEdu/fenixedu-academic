<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.selectCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/> - <bean:message key="message.success.candidaciesSelection" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<br/>
<fr:form action="/selectDFACandidacies.do?method=printAcceptanceDispatch" target="_blank" >

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
	</logic:present>
	
	<logic:present name="substituteCandidacies">
		<h2><strong><bean:message key="label.substituteCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
		<logic:empty name="substituteCandidacies">
			<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
		</logic:empty>
		<logic:notEmpty name="substituteCandidacies">
			<bean:size id="numberOfCandidacies" name="substituteCandidacies" />
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<fr:edit name="substituteCandidacies" schema="candidacy.show.listSubstitutesForSelection" visible="false" id="substituteCandidacies" />
			<fr:view name="substituteCandidacies" schema="candidacy.show.listSubstitutesForSelection" >
				<fr:layout name="tabular">
			        <fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value=",,,acenter"/>
					<fr:property name="sortBy" value="order"/>
			    </fr:layout>
			</fr:view>
		</logic:notEmpty>
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
	</logic:present>
	
	<br/>
	<p>
		<html:submit><bean:message key="button.printAcceptanceDispatch" bundle="ADMIN_OFFICE_RESOURCES" /></html:submit>
	</p>
	
</fr:form>