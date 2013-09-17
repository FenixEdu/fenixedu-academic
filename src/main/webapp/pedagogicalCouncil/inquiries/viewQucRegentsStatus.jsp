<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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