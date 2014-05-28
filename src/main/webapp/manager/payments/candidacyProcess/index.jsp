<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="role(MANAGER)">

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
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Transferências Internos - 
			<bean:write name="internalDegreeChangeSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Mudanças de Curso Externos - 
			<bean:write name="externalDegreeTransferSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Mudanças de Curso Internos - 
			<bean:write name="internalDegreeTransferSize" /> -
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
		<li>
			Segundo Ciclo - 
			<bean:write name="secondCycleSize" /> - 
			<html:link action="/candidacyPaymentCodes.do?method=prepareCreatePaymentCodes&type=SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS" >Criar Referências</html:link>
		</li>
	</ul>

</logic:present>