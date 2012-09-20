<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<br/>
<h4>Lista de Regentes com comentários e/ou questões obrigatórias por responder:</h4>
<logic:present name="regentInquiryOID">	
	<p><html:link action="qucRegentsStatus.do?method=dowloadReport" paramId="regentInquiryOID" paramName="regentInquiryOID">Ver ficheiro</html:link></p>
</logic:present>

<logic:notPresent name="regentInquiryOID">
	<p>O inquérito ao Regente encontra-se fechado.</p>
</logic:notPresent>