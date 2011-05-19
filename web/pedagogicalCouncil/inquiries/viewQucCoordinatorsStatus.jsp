<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<br/>
<h4>Lista de Coordenadores com relatório por responder:</h4>
<logic:present name="coordinatorInquiryOID">	
	<p><html:link action="qucCoordinatorsStatus.do?method=dowloadReport" paramId="coordinatorInquiryOID" paramName="coordinatorInquiryOID">Ver ficheiro</html:link></p>
</logic:present>

<logic:notPresent name="coordinatorInquiryOID">
	<p>O inquérito ao Coordenador encontra-se fechado.</p>
</logic:notPresent>