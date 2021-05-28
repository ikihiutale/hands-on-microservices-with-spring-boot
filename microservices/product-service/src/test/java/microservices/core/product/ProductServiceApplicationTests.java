package microservices.core.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductServiceApplicationTests {
	@Test
	void savedUserHasRegistrationDate() {
		
		// Arrange
		String str = "";
		// Act
		
		// Assert
		assertThat(str).isNotNull();
		
	}

}
