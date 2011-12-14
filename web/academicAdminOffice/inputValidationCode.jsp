<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
 <h2>Validação de Contactos</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="success0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

A pessoa em questão terá que validar o contacto introduzido para este ser considerado válido.