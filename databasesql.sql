
-- Create student table
CREATE TABLE tbl_student (
                             student_id SERIAL PRIMARY KEY,
                             full_name VARCHAR(100) NOT NULL,
                             email VARCHAR(100) UNIQUE NOT NULL,
                             password VARCHAR(100) NOT NULL,
                             registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create class table
CREATE TABLE tbl_class (
                           class_id SERIAL PRIMARY KEY,
                           student_id INTEGER REFERENCES tbl_student(student_id),
                           class_name VARCHAR(100) NOT NULL,
                           semester VARCHAR(20) NOT NULL,
                           UNIQUE(student_id, class_name, semester)
);

-- Create post table
CREATE TABLE tbl_post (
                          post_id SERIAL PRIMARY KEY,
                          student_id INTEGER REFERENCES tbl_student(student_id),
                          class_id INTEGER REFERENCES tbl_class(class_id),
                          week_number INTEGER NOT NULL,
                          topic VARCHAR(200) NOT NULL,
                          content TEXT NOT NULL,
                          post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add some indexes for better performance
CREATE INDEX idx_student_email ON tbl_student(email);
CREATE INDEX idx_post_class ON tbl_post(class_id);
CREATE INDEX idx_post_student ON tbl_post(student_id);
