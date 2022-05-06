package co.edu.uniajc.demo.service;

import co.edu.uniajc.demo.model.StudentModel;
import co.edu.uniajc.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentModel createStudent (StudentModel studentModel){
        return studentRepository.save(studentModel);
    }

    public StudentModel updateStudent (StudentModel studentModel){
        return studentRepository.save(studentModel);
    }

    public void deleteStudent (Long id){
        studentRepository.deleteById(id);
    }

    public List<StudentModel> findAllStudent (){
        return studentRepository.findAll();
    }

    public List<StudentModel> findAllStudentByName (String name){
        return studentRepository.findAllByNameContains(name);
    }

    public List<StudentModel> findAllAges (Integer age){
        return studentRepository.findAge(age);
    }

    public Optional<StudentModel> findById (Long id){
        return studentRepository.findById(id);
    }
}
