<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayoutStudent_2col_photo.jsp" flush="true">
  <tiles:put name="title" value=".IST - portalEstudante" />
  <tiles:put name="serviceName" value="Portal do Estudante" />
  <tiles:put name="navGeral" value="" />
  <tiles:put name="photos" value="/student/commonEntrPhotosStudent.jsp" />
  <tiles:put name="contextBody" value="  "/>
  <tiles:put name="body" value="/student/showShiftEnrolmentOptions_bd.jsp"/>
  <tiles:put name="footer" value="/student/commonFooterStudent.jsp" />
</tiles:insert>
