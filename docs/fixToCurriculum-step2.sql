select CONCAT("insert into CURRICULUM (",
         CONCAT_WS(",","KEY_CURRICULAR_COURSE",
         		"KEY_EXECUTION_COURSE",	
                         "GENERAL_OBJECTIVES" ,
                         "OPERACIONAL_OBJECTIVES",
                         "PROGRAM",
                         "GENERAL_OBJECTIVES_EN",
                         "OPERACIONAL_OBJECTIVES_EN",
                         "PROGRAM_EN"
                 ),")",
         " values (",
         CONCAT_WS(",",
                 cc.id_internal,
                 ec.id_internal,
                 IF(c.GENERAL_OBJECTIVES IS NULL, "null", CONCAT("'", REPLACE(REPLACE(c.GENERAL_OBJECTIVES,"'","&quot;"),"\"","&quot;"),"'")),
                 IF(c.OPERACIONAL_OBJECTIVES IS NULL, "null", CONCAT("'",REPLACE(REPLACE(c.OPERACIONAL_OBJECTIVES,"\'","&quot;"),"\"","&quot;"),"'")),
                 IF(c.PROGRAM IS NULL, "null", CONCAT("'",REPLACE(REPLACE(c.PROGRAM,"\'","&quot;"),"\"","&quot;"),"'")),
                 IF(c.GENERAL_OBJECTIVES_EN IS NULL, "null",CONCAT("'",REPLACE(REPLACE(c.GENERAL_OBJECTIVES_EN,"\'","&quot;"),"\"","&quot;"),"'")),
                 IF(c.OPERACIONAL_OBJECTIVES_EN IS NULL, "null", CONCAT("'",REPLACE(REPLACE(c.OPERACIONAL_OBJECTIVES_EN,"\'","&quot;"),"\"","&quot;"),"'")),
                 IF(c.PROGRAM_EN IS NULL, "null", CONCAT("'",REPLACE(REPLACE(c.PROGRAM_EN,"\'","&quot;"),"\"","&quot;"),"'"))),                 
         ");") as ""
         from
           CURRICULAR_COURSE as cc inner join CURRICULAR_COURSE_EXECUTION_COURSE as ccec on cc.id_internal=ccec.key_curricular_course
           inner join EXECUTION_COURSE as ec on ec.id_internal=ccec.key_execution_course
           inner join CURRICULUM as c on c.key_execution_course=ec.id_internal;
select CONCAT("insert into EVALUATION_METHOD (",
         CONCAT_WS(",","KEY_CURRICULAR_COURSE",
         		"KEY_EXECUTION_COURSE",	
                         "EVALUATION_ELEMENTS" ,
                         "EVALUATION_ELEMENTS_EN"),") values (",
         CONCAT_WS(",",
                 cc.id_internal,
                 em.key_execution_course,
                 IF(em.EVALUATION_ELEMENTS IS NULL, "null", CONCAT("'", REPLACE(REPLACE(em.EVALUATION_ELEMENTS,"'","&quot;"),"\"","&quot;"),"'")),
                 IF(em.EVALUATION_ELEMENTS_EN IS NULL, "null", CONCAT("'",REPLACE(REPLACE(em.EVALUATION_ELEMENTS_EN,"\'","&quot;"),"\"","&quot;"),"'"))
                 ),");") as ""
         from
           CURRICULAR_COURSE as cc inner join CURRICULAR_COURSE_EXECUTION_COURSE as ccec on cc.id_internal=ccec.key_curricular_course
           inner join EXECUTION_COURSE as ec on ec.id_internal=ccec.key_execution_course
           inner join EVALUATION_METHOD as em on em.key_execution_course=ec.id_internal;           
select CONCAT("insert into SITE (key_execution_course) values (",ec.id_internal,");") as ""  from EXECUTION_COURSE ec left join SITE s on ec.id_internal = s.key_execution_Course where s.id_internal is null;
           
           