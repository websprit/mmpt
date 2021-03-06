/*
The MIT License (MIT)

Copyright (c) 2014 Manni Wood

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.manniwood.mmpt.test;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TypeTest {

    private static Logger log = LoggerFactory.getLogger(IntegerArrayTest.class);
    private static final String MYBATIS_CONF_FILE = "mybatis/config.xml";
    private String tableCreateId = null;
    protected SqlSessionFactory sqlSessionFactory = null;
    protected SqlSession session = null;

    public TypeTest(String tableCreateId) {
        super();
        this.tableCreateId = tableCreateId;
    }

    @BeforeClass
    protected void initMybatis() {
        log.info("********* initializing sqlSessionFactory with conf file {} ******", MYBATIS_CONF_FILE);
        Reader myBatisConfReader = null;
        try {
            myBatisConfReader = Resources.getResourceAsReader(MYBATIS_CONF_FILE);
        } catch (IOException e) {
            throw new RuntimeException("problem trying to read mybatis config file: ", e);
        }
        if (myBatisConfReader == null) {
            throw new RuntimeException("mybatis conf reader is null");
        }

        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfReader);
        } catch (PersistenceException e) {
            throw new RuntimeException("problem trying to set up database connection: ", e);
        }
        if (sqlSessionFactory == null) {
            throw new RuntimeException("sqlSessionFactory is null");
        }

        session = sqlSessionFactory.openSession();
        session.insert(tableCreateId);
        session.commit(true);
    }

    @AfterClass
    protected void wrapUp() {
        session.close();  // org.apache.ibatis.executor.BaseExecutor does rollback if an exception is thrown
    }

}
