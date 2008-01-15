insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','assiduousnessManagers', 'assiduousnessManagers',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('deff78d6-eeeb-4dc2-a2b8-d73e860734a9');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','assiduousnessManagers', 'assiduousnessManagers',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('5a998369-be4c-4d46-adf4-e8f562ff082b');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','assiduousnessManagers', 'assiduousnessManagers',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('d15aff99-d251-40d5-b546-3d0a65ca43d5');


insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','payrollSectionStaff', 'payrollSectionStaff',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('70e4e7aa-3bf0-4933-8080-e4fb5346d700');



insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','assiduousnessSectionStaff || assiduousnessManagers', 'assiduousnessSectionStaff || assiduousnessManagers',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('11279eeb-6e02-4374-a255-fc140930f111');

insert into AVAILABILITY_POLICY(OJB_CONCRETE_CLASS, KEY_ROOT_DOMAIN_OBJECT,EXPRESSION,TARGET_GROUP,KEY_CONTENT,CONTENT_ID) select 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', '1','assiduousnessSectionStaff || assiduousnessManagers', 'assiduousnessSectionStaff || assiduousnessManagers',ID_INTERNAL , UUID() FROM CONTENT 
WHERE CONTENT_ID=md5('940d3b34-752c-4e56-8d35-c1dab4f954a8');

update CONTENT C, AVAILABILITY_POLICY AP SET C.KEY_AVAILABILITY_POLICY=AP.ID_INTERNAL WHERE AP.KEY_CONTENT=C.ID_INTERNAL AND C.KEY_AVAILABILITY_POLICY IS NULL;
