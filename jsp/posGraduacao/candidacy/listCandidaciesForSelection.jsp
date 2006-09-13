<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.selectCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:form action="/selectDFACandidacies.do?method=listCandidacies" >
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="degreePostBack" path="/selectDFACandidacies.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/selectDFACandidacies.do?method=chooseDegreeCurricularPlanPostBack"/>	
		<fr:destination name="invalid" path="/selectDFACandidacies.do?method=chooseExecutionDegreeInvalid"/>		
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,,"/>
		</fr:layout>
	</fr:edit>	
	<html:submit><bean:message key="button.submit" /></html:submit>
</fr:form>
<br/>
<br/>
<br/>
<logic:present name="candidacies">
	<logic:empty name="candidacies">
		<bean:message key="label.noCandidacies.found" bundle="ADMIN_OFFICE_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="candidacies">
		<fr:form action="/selectDFACandidacies.do?method=selectCandidacies" >
			<bean:size id="numberOfCandidacies" name="candidacies" />
			<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
			<fr:edit name="candidacies" schema="candidacy.show.listForSelection" id="candidaciesListForSelection">
				<fr:layout name="tabular-editable">
			        <fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value=",,,acenter"/>
					<fr:property name="sortBy" value="candidacy.number"/>
			    </fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.proceed" bundle="ADMIN_OFFICE_RESOURCES" /></html:submit>	
		</fr:form>
	</logic:notEmpty>
</logic:present>