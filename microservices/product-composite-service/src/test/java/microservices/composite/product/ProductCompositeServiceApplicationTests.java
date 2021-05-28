package microservices.composite.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProductCompositeServiceApplicationTests {

	@Test
	void savedUserHasRegistrationDate() {
		
		// Arrange
		String str = "";
		// Act
		
		// Assert
		assertThat(str).isNotNull();
		
	}
}

