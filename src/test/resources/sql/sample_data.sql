insert into courses (course_name, course_description) values ('Course1', 'Description1');
insert into courses (course_name, course_description) values ('Course2', 'Description2');
insert into courses (course_name, course_description) values ('Course to delete', 'Description');

insert into groups (group_name) values ('BB-22');
insert into groups (group_name) values ('CC-33');
insert into groups (group_name) values ('Group to Delete');

insert into students (group_id, first_name, last_name) values ((select group_id from groups where group_name = 'BB-22'), 'Jane', 'Doe');
insert into students (group_id, first_name, last_name) values ((select group_id from groups where group_name = 'CC-33'), 'Student', 'ToRemove');
insert into students (group_id, first_name, last_name) values ((select group_id from groups where group_name = 'CC-33'), 'Student2', 'McStudent2');

insert into student_course (student_id, course_id) values ((select student_id from students where first_name = 'Jane' and last_name = 'Doe'),(select course_id from courses where course_name = 'Course1'));
insert into student_course (student_id, course_id) values ((select student_id from students where first_name = 'Jane' and last_name = 'Doe'),(select course_id from courses where course_name = 'Course2'));
insert into student_course (student_id, course_id) values ((select student_id from students where first_name = 'Student' and last_name = 'ToRemove'),(select course_id from courses where course_name = 'Course2'));
