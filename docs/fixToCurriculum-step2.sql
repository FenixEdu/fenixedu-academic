
use ciapl;

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
                 c.key_execution_course,
                 IF(c.GENERAL_OBJECTIVES IS NULL, "null", CONCAT("'",c.GENERAL_OBJECTIVES,"'")),
                 IF(c.OPERACIONAL_OBJECTIVES IS NULL, "null", CONCAT("'",c.OPERACIONAL_OBJECTIVES,"'")),
                 IF(c.PROGRAM IS NULL, "null", CONCAT("'",c.PROGRAM,"'")),
                 IF(c.GENERAL_OBJECTIVES_EN IS NULL, "null",CONCAT("'",c.GENERAL_OBJECTIVES_EN,"'")),
                 IF(c.OPERACIONAL_OBJECTIVES_EN IS NULL, "null", CONCAT("'",c.OPERACIONAL_OBJECTIVES_EN,"'")),
                 IF(c.PROGRAM_EN IS NULL, "null", CONCAT("'",c.PROGRAM_EN,"'"))),                 
         ");") as a
         from
           curricular_course as cc inner join curricular_course_execution_course as ccec on cc.id_internal=ccec.key_curricular_course
           inner join execution_course as ec on ec.id_internal=ccec.key_execution_course
           inner join curriculum as c on c.key_execution_course=ec.id_internal;
           
     