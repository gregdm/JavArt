package com.gregdm.javart.service;

import com.gregdm.javart.JavartApp;
import com.gregdm.javart.domain.User;
import com.gregdm.javart.domain.art.Work;
import com.gregdm.javart.repository.UserRepository;
import com.gregdm.javart.service.art.WorkService;
import com.gregdm.javart.service.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JavartApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class WorkServiceIntTest {

    @Inject
    private WorkService workService;

    @Test
    public void assertRestTemplateIsOk() {
        assertThat(workService.get("199751")).isPresent();
    }
}
