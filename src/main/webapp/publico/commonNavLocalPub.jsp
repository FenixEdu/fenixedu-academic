<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<ul>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>html/instituto/"><bean:message key="label.institution" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>html/estrutura/"><bean:message key="label.structure" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>html/ensino/"><bean:message key="label.education" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>html/id/"><bean:message key="label.research.and.development.acronyms" bundle="GLOBAL_RESOURCES"/></a></li>
	<li><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>html/viverist/"><bean:message key="label.life.at.ist" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="GLOBAL_RESOURCES"/></a></li>
</ul>