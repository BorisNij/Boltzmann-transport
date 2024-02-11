drop table IF EXISTS students, groups, courses, student_course CASCADE;

create table groups (
    group_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    CONSTRAINT unique_group_name UNIQUE (group_name)
);

create table students (
    student_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    group_id INT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id) ON delete SET NULL,
    CONSTRAINT unique_student_group UNIQUE (first_name, last_name)
);

create table courses (
    course_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    course_description TEXT,
    CONSTRAINT unique_course_name UNIQUE (course_name)
);

create table student_course (
    student_id int,
    course_id int,
    primary key (student_id, course_id),
    foreign key (student_id) references students (student_id) ON DELETE CASCADE,
    foreign key (course_id) references courses (course_id) ON DELETE CASCADE,
    CONSTRAINT unique_student_course UNIQUE (student_id, course_id)
);