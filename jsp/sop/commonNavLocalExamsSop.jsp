<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p><strong>&raquo; Gest&atilde;o de Exames</strong></p>
<ul>
  <li><html:link page="/chooseExamsContextDA.do?method=prepare&amp;nextPage=createExam"><bean:message key="link.exams.create"/></html:link></li>
  <li><html:link page="/chooseDayAndShiftForm.do?method=prepare"><bean:message key="link.exams.listByDayAndShift"/></html:link></li>
  <li><html:link page="/chooseExamsContextDA.do?method=prepare"><bean:message key="link.exams.listByDegreeAndAcademicYear"/></html:link></li>
</ul>
