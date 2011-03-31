<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<br/>
<h4>Lista de Docentes com comentários e/ou questões obrigatórias por responder:</h4>
<logic:present name="teacherInquiryOID">	
	<p><html:link action="qucTeachersStatus.do?method=dowloadReport" paramId="teacherInquiryOID" paramName="teacherInquiryOID">Ver ficheiro</html:link></p>
</logic:present>

<logic:notPresent name="teacherInquiryOID">
	<p>O inquérito ao Docente encontra-se fechado.</p>
</logic:notPresent>