package be.froland.student.labmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository repository;

    @Test
    public void createStudent() {
        Student originalStudent = new Student();
        originalStudent.setFirstName("Stijn");
        originalStudent.setLastName("Van Den Enden");
        originalStudent.setEmailAddress("stijn@student.umons.ac.be");

        Student savedStudent = repository.save(originalStudent).block();
        assertThat(savedStudent).isNotNull()
            .extracting(Student::getId).isNotEmpty();

        String studentId = savedStudent.getId();

        Student foundStudent = repository.findById(studentId).block();
        assertThat(foundStudent.getId()).isEqualTo(studentId);
        assertThat(foundStudent.getFirstName()).isEqualTo("Stijn");
        assertThat(foundStudent.getLastName()).isEqualTo("Van Den Enden");
        assertThat(foundStudent.getEmailAddress()).isEqualTo("stijn@student.umons.ac.be");

        repository.deleteById(studentId).block();

        Student unexpectedStudent = repository.findById(studentId).block();
        assertThat(unexpectedStudent).isNull();
    }
}
