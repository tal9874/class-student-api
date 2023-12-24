package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);



	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate, ClassRoomRepository classRoomRepository, StudentRepository studentRepository) {
		return args -> {
			jdbcTemplate.execute("DROP TABLE IF EXISTS classroom cascade;\n" +
					"DROP TABLE IF EXISTS students cascade;\n" +
					"CREATE TABLE classroom (\n" +
					"id SERIAL PRIMARY KEY,\n" +
					"number_of_students int NOT NULL,\n" +
					"class_avg FLOAT NOT NULL,\n" +
					"type_class varchar(255) NOT NULL default ''\n" +
					");\n" +
					"CREATE TABLE students (\n" +
					"    id SERIAL PRIMARY KEY,\n" +
					"    first_name varchar(255) NOT NULL default '',\n" +
					"    last_name varchar(255) NOT NULL default '',\n" +
					"    avg_grade FLOAT NOT NULL,\n" +
					"    gender varchar(255) NOT NULL default '',\n" +
					"	 class_id int NOT NULL, \n" +
					"    FOREIGN KEY (class_id) REFERENCES classroom(id));");


			classRoomRepository.createClassRoom(new ClassRoom(0, 0, 0.0f, TypeClass.EXTERNAL));
			classRoomRepository.createClassRoom(new ClassRoom(0, 0, 0.0f, TypeClass.REGULAR));
			classRoomRepository.createClassRoom(new ClassRoom(0, 0, 0.0f, TypeClass.REGULAR));

			jdbcTemplate.execute("INSERT INTO students (first_name, last_name, avg_grade, gender, class_id) VALUES ('Tal', 'Abutbul', 87.2, 'MALE', 1)");
			jdbcTemplate.execute("INSERT INTO students (first_name, last_name, avg_grade, gender, class_id) VALUES ('Eden', 'Alon', 90.4, 'FEMALE', 3)");
			jdbcTemplate.execute("INSERT INTO students (first_name, last_name, avg_grade, gender, class_id) VALUES ('Alon', 'Levi', 84.4, 'MALE', 2)");
			jdbcTemplate.execute("INSERT INTO students (first_name, last_name, avg_grade, gender, class_id) VALUES ('Miri', 'Gloskin', 91.2, 'FEMALE', 2)");
			jdbcTemplate.execute("INSERT INTO students (first_name, last_name, avg_grade, gender, class_id) VALUES ('Artur', 'Israeli', 60.6, 'MALE', 1)");

			// avg by class (atgar)

			List <Student> studentList = studentRepository.getByClassId(1);
			Component root_for_avg_class = new Composite(studentList.get(0));
			for (Student student :studentList) {
				root_for_avg_class.addStudent(new Leaf(student));
			}
			System.out.println(root_for_avg_class.getAvg());

		};
	}

}
