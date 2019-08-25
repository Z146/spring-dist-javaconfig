package com.zhou.service.impl;

import com.zhou.dao.StudentDao;
import com.zhou.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger logger = Logger.getLogger(StudentServiceImpl.class);
    private StudentDao studentDao;
    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Cacheable(value = "cache",key = "#id.toString()")
    public String testCache(Integer id){
        logger.info("执行了方法 testCache()....");
        studentDao.findOne(id);
        return "OK";
    }
}
