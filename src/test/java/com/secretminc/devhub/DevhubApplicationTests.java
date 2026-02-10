package com.secretminc.devhub;

import com.secretminc.devhub.domain.blogs.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
@ActiveProfiles("test")
class DevhubApplicationTests {

	@MockitoBean
	private BlogMapper blogMapper;


	@Test
	void contextLoads() {
	}

}
