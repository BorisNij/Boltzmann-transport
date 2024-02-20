drop table IF EXISTS students, groups, courses, student_course CASCADE;

create table groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    CONSTRAINT unique_group_name UNIQUE (group_name)
);

create table students (
    student_id SERIAL PRIMARY KEY,
    group_id INT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id) ON delete SET NULL
);

create table courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    course_description TEXT,
    CONSTRAINT unique_course_name UNIQUE (course_name)
);

create table student_course (
    id SERIAL PRIMARY KEY,
    student_id int,
    course_id int,
    foreign key (student_id) references students (student_id) ON delete CASCADE,
    foreign key (course_id) references courses (course_id) ON delete CASCADE,
    CONSTRAINT unique_student_course UNIQUE (student_id, course_id)
);