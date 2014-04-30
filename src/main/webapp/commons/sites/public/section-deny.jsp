<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<logic:present name="section">
    
    
    <h2>
        ${section.name.content}
        <span class="permalink1">(<a href="${section.fullPath}"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>
    
    
    <p>
       <em><bean:message key="message.section.view.notAllowed" bundle="SITE_RESOURCES"/></em>
    </p>
    
    <bean:message key="label.item.file.availableFor" bundle="SITE_RESOURCES"/>

    ${section.permittedGroup.presentationName}
    
</logic:present>
