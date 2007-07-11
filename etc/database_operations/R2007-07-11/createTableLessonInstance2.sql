select concat('update SUMMARY set KEY_LESSON_INSTANCE = ' , ID_INTERNAL , ' where ID_INTERNAL = ' , KEY_SUMMARY , ';') as "" from LESSON_INSTANCE;
