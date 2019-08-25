package com.zhou.controller;

import com.zhou.dao.StudentDao;
import com.zhou.domain.Student;
import com.zhou.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class HelloController {

    private static Logger logger = Logger.getLogger(HelloController.class);

    /**
     * 测试 spring 整合 thymeleaf
     * @param model 将信息添加到域中
     * @return home 视图
     */
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(Model model){
        model.addAttribute("hello", "jjjj");
        logger.info("执行了hello方法……");
        return "home";
    }


    private StudentDao studentDao;
    private StudentService service;
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    public HelloController(StudentDao studentDao, StudentService service, RedisTemplate<String, String> redisTemplate) {
        this.studentDao = studentDao;
        this.service = service;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 通过 ID 查找
     */
    @Cacheable(value = "student",key = "#id")
    @GetMapping("/student/{id}")
    @ResponseBody
    public Student findById(@PathVariable Integer id){
        logger.info("执行了方法 findById()....");
        return studentDao.findOne(id);
    }

    /**
     * 查找全部
     * @return 全部学生信息
     */
    @GetMapping("/student")
    @ResponseBody
    public List<Student> findAll(){
        return studentDao.findAll();
    }

    /**
     * 添加
     * @param student 学生对象
     * @return 成功的对象信息
     */
    @PostMapping("/student")
    @ResponseBody
    public Student save(Student student){
        return studentDao.save(student);
    }

    /**
     * 通过 ID 删除
     * @param id 传入要删除的id。
     * @return 成功返回 OK ，失败返回 “o , your id is null”。
     */

    @DeleteMapping("/student/{id}")
    @ResponseBody
    public String deleteById(@PathVariable Integer id){
        if (id != null) {
            studentDao.delete(id);
            return "OK";
        }
        return "o , your id is null";
    }

    /**
     * 删除全部
     * @return OK,delete all student message
     */
    @DeleteMapping("/student")
    @ResponseBody
    public String deleteAll(){
        studentDao.deleteAll();
        return "OK,delete all student message";
    }

    /**
     * 更新
     * @param id 被更新的学生id
     * @param name 要更新的名字
     * @return 原来的学生信息和更新后的学生信息
     */
    @PutMapping("/student")
    @ResponseBody
    public List<Student> update(Integer id, String name){
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        Student student1 = findById(id);
        Student student2 = studentDao.save(student);
        return Arrays.asList(student1, student2);
    }


    @GetMapping("/student/addRedis/{name}")
    @ResponseBody
    public String addRedisMessage(@PathVariable String name){
        redisTemplate.opsForValue().append("name",name);
        return "OK";
    }


    @GetMapping("/cache/{id}")
    @ResponseBody
    public String testCache(@PathVariable Integer id){
        //logger.info("执行了方法 testCache()....");
        service.testCache(id);
        return "OK";
    }
}
