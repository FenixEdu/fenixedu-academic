<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.searchMarkSheet"/></strong></h2>

<fr:form action="/dfaCandidacy.do?method=createCandidacy">
	<fr:edit id="executionDegree"
			 name="createCandidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="postBack" path="/dfaCandidacy.do?method=chooseExecutionDegreePostBack"/>
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<logic:present name="createCandidacyBean" property="executionDegree">
		<fr:edit id="person"
			 name="createCandidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.person">
		</fr:edit>
	</logic:present>
	<html:submit/>
</fr:form>