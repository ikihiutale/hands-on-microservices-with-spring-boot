package microservices.core.recommendation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RecommendationServiceApplicationTests {
	@Test
	void savedUserHasRegistrationDate() {
		
		// Arrange
		String str = "";
		// Act
		
		// Assert
		assertThat(str).isNotNull();
		
	}
}
