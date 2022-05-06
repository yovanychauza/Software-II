package co.edu.uniajc.demo.repository;

import co.edu.uniajc.demo.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, Long> {

    List<StudentModel> findAllByNameContains(String name);
    StudentModel getById(Long id);

    @Query(nativeQuery = true, value="SELECT" +
        "st_id, st_name, " +
        "st_last_name, " +
        "st_age," +
        "st_state" +
        "FROM student"+
        "WHERE st_age=: age")
        List<StudentModel> findAge (@Param(value = "age") Integer age);
        }
