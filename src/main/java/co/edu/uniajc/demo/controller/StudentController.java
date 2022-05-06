package co.edu.uniajc.demo.controller;

import co.edu.uniajc.demo.exception.StudentRequestException;
import co.edu.uniajc.demo.model.StudentModel;
import co.edu.uniajc.demo.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@Api("Students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = "/save")
    @ApiOperation(value="Insert Student", response = StudentModel.class)
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Something Went Wrong"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public StudentModel saveStudent(@RequestBody StudentModel studentModel){
        return studentService.createStudent(studentModel);
    }

    @PutMapping(path = "/update")
    @ApiOperation(value="Update Student", response = StudentModel.class)
    public StudentModel updateStudent(@RequestBody StudentModel studentModel){
        return studentService.updateStudent(studentModel);
    }

    @DeleteMapping(path="/delete")
    @ApiOperation(value="Delete Student by Id", response = StudentModel.class)
    public void deleteStudent (@RequestParam(name = "id") Long id){
        studentService.deleteStudent(id);
    }

    @GetMapping(path="/all")
    @ApiOperation(value="Find All Student", response = StudentModel.class)
    public List<StudentModel> findAllStudent(){
        return studentService.findAllStudent();
    }

    @GetMapping(path="/all/name")
    @ApiOperation(value="Find Student by Name", response = StudentModel.class)
    public List<StudentModel> findAllStudentByName(@RequestParam(name ="name") String name){
        return studentService.findAllStudentByName(name);
    }

    @GetMapping(path="/all/age")
    @ApiOperation(value="Find all Students", response = StudentModel.class)
    public List<StudentModel> findAllStudentByAge(@RequestParam(name ="age") Integer age){
        return studentService.findAllAges(age);
    }

    @GetMapping(path="/id")
    @ApiOperation(value="Find Student by Id", response = StudentModel.class)
    @ApiResponses( value = {
            @ApiResponse(code = 400, message = "Something Went Wrong"),
            @ApiResponse(code = 404, message = "No se encontro"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<StudentModel> findByid(@RequestParam(name ="id") Long idStudent) throws  Exception{
        return ResponseEntity.ok(studentService.findById(idStudent).
                orElseThrow(() -> new StudentRequestException("No se encontro"))
        );
    }

}
