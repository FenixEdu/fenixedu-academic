<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
  <li><strong><html:link page="/chooseClassSearchContextDA.do?method=prepare" > <bean:message key="link.classes.consult"/> </html:link></strong></li>
  <li><strong><html:link page="/chooseShiftSearchContextDA.do?method=prepare"> <bean:message key="link.shifts.consult"/> </html:link></strong></li>
  <li><strong><html:link page="/prepareConsultRooms.do"> <bean:message key="link.rooms.consult"/> </html:link></strong></li>  
</ul>