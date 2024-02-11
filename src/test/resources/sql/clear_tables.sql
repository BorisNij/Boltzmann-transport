truncate table students, groups, courses, student_course;

select setval('courses_course_id_seq', 1, false);
SELECT setval('groups_group_id_seq', 1, false);
SELECT setval('students_student_id_seq', 1, false);
