<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present name="candidacy">

<h2><bean:message key="label.internationalrelations.internship.candidacy.title" bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

	<p><strong>Processo concluído com sucesso.</strong></p>

	<p>Caro(a) <bean:write name="candidacy" property="name" />,</p>

	<p>a sua candidatura foi submetida com sucesso. Foi-lhe atribuído o código de inscrição nº <strong
		class="highlight1"><bean:write name="candidacyNumber" /></strong>, que deverá utilizar em
	contactos futuros. Uma cópia desta mensagem foi-lhe enviada para o email submetido na canditarura.</p>

</logic:present>