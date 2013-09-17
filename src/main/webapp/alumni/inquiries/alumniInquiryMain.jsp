<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<!-- alumni/inquiries/alumniInquiryMain.jsp -->

<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="inquiries.alumni.title" bundle="ALUMNI_RESOURCES" /></h2>


<p>
	Há mais de 15 anos que o IST acompanha com regularidade o percurso sócio-profissional dos seus diplomados no âmbito dos processos de avaliação e acreditação de cada um dos seus cursos. Desde 1998, que este acompanhamento foi sistematizado a todos os cursos, estando neste momento a decorrer a IV edição do inquérito, lançado pelo Observatório de Empregabilidade do IST (OEIST) em estreita colaboração com o projecto Alumni.
</p>
<p>
	A sua colaboração é fundamental para a instituição, dado que os requisitos de Bolonha atribuiram à empregabilidade um lugar de destaque e de grande relevo na avaliação dos cursos. Por conseguinte, é muito importante para o IST compreender o percurso dos seus diplomados, de forma não só a responder a estes requisitos mas também de modo a aproximar a escola do mercado de trabalho.
</p>
<p>
	Dada a relação entre o projecto Alumni e o OEIST, fica feito o convite ao preenchimento do inquérito (no link que se segue).
</p>


<p>
	<strong>
		<html:link page="/alumniInquiries.do?method=initInquiry">
			Preencher o inquérito »
		</html:link>
	</strong>
</p>

<%--
<p>
	<strong>
		<a target="_blank" href="http://groups.ist.utl.pt/gep/inqweb/index.php?sid=85246">
			<bean:message key="inquiries.alumni.link.two" bundle="ALUMNI_RESOURCES" />
		</a>
	</strong>
</p>
--%>

