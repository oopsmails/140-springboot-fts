package com.oopsmails.lucenesearch.dao;

import com.oopsmails.lucenesearch.dao.impl.SecurityDaoFileImpl;
import com.oopsmails.lucenesearch.model.Security;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;

//@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
        SecurityDaoFileImpl.class})
@TestPropertySource(properties = {
        //        "albert.api.security.file.location=/data/security/" // This is C:/data/security, working
                "albert.api.security.file.location=./data/security/" // This is C:\oopsmails\140-springboot-fts\springboot-lucene, i.e, project root, working

})
@Slf4j
public class SecurityDaoFileImplTest {

    @Autowired
    private SecurityDao securityDao;

    @Before("")
    public void setupMock() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_getFromFile() {
        List<Security> result = securityDao.getAllInstitutions();

        assertTrue("size should > 0", result.size() > 0); // 0L is hardcoded
        //        assertEquals("size should match.", 0L, retId); // 0L is hardcoded
    }

}
