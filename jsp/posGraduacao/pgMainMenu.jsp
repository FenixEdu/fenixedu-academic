<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" />
    </html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link></li>
    <li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link></li>
     <li><html:link page="/certificateSection.do"><bean:message key="label.coordinator.student" /> 
    </html:link></li>
    <li><html:link page="/marksSection.do"><bean:message key="link.masterDegree.administrativeOffice.marks" />
    </html:link></li>
</ul>
     
     