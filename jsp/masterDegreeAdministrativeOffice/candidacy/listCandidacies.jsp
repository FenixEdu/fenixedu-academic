<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.listCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:form action="/listDFACandidacy.do?method=listCandidacies">
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="degreePostBack" path="/listDFACandidacy.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/listDFACandidacy.do?method=chooseDegreeCurricularPlanPostBack"/>		
		<fr:destination name="executionDegreePostBack" path="/listDFACandidacy.do?method=chooseExecutionDegreePostBack"/>				
		<fr:destination name="invalid" path="/listDFACandidacy.do?method=chooseExecutionDegreeInvalid"/>		
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
		<bean:size id="numberOfCandidacies" name="candidacies" />
		<bean:message key="label.numberOfFoundCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/>: <bean:write name="numberOfCandidacies" />
		<fr:view name="candidacies" schema="candidacy.show.list.candidady">
			<fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value=",,,acenter"/>
		        <fr:property name="linkFormat(view)" value="/listDFACandidacy.do?method=viewCandidacy&candidacyID=${idInternal}"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="sortBy" value="number"/>
		    </fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>