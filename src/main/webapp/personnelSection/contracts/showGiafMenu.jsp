<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.giaf.interface" bundle="APPLICATION_RESOURCES"/></h2>

<ul>

	<li><html:link action="/giafParametrization.do?method=showContractSituations">Situações Profissionais</html:link></li>
	<li><html:link action="/giafParametrization.do?method=showProfessionalCategories">Categorias Profissionais</html:link></li>
	<li><html:link action="/giafParametrization.do?method=showGrantOwnerEquivalences">Equiparações a Bolseiro</html:link></li>
	<li><html:link action="/giafParametrization.do?method=showServiceExemptions">Dispensas de Serviço</html:link></li>
	<li><html:link action="/giafParametrization.do?method=showAbsences">Faltas</html:link></li>

</ul>