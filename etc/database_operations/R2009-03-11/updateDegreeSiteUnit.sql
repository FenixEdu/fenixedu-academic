
-- updates DegreeSite with DegreeSite->Degree->Unit
UPDATE CONTENT,DEGREE
SET CONTENT.KEY_UNIT = DEGREE.KEY_UNIT
WHERE CONTENT.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.DegreeSite"
  AND CONTENT.KEY_DEGREE  = DEGREE.ID_INTERNAL