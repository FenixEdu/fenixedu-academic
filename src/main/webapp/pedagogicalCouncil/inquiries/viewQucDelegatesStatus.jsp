<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<br/>
<h4>Lista de Delegados com questões obrigatórias por responder:</h4>
<logic:present name="delegateInquiryOID">	
	<p><html:link action="qucDelegatesStatus.do?method=dowloadReport" paramId="delegateInquiryOID" paramName="delegateInquiryOID">Ver ficheiro</html:link></p>
</logic:present>

<logic:notPresent name="delegateInquiryOID">
	<p>O inquérito ao Delegado encontra-se fechado.</p>
</logic:notPresent>