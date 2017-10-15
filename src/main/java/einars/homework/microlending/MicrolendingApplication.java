package einars.homework.microlending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MicrolendingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicrolendingApplication.class, args);
	}
	

}
