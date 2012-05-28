<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">

	<h2>Referências SIBS para candidaturas</h2>

	<ul>
		<li>
			Maiores de 23 anos - 
			<bean:write name="over23Size" /> - 
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=OVER_23_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Grau Médio Superior Externos - 
			<bean:write name="externalDegreeCandidacyForGraduatedPersonSize" /> - 
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Grau Médio Superior Internos - 
			<bean:write name="internalDegreeCandidacyForGraduatedPersonSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Transferências Externos - 
			<bean:write name="externalDegreeChangeSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Transferências Internos - 
			<bean:write name="internalDegreeChangeSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Mudanças de Curso Externos - 
			<bean:write name="externalDegreeTransferSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Mudanças de Curso Internos - 
			<bean:write name="internalDegreeTransferSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Segundo Ciclo - 
			<bean:write name="secondCycleSize" /> - 
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
	</ul>

</logic:present>