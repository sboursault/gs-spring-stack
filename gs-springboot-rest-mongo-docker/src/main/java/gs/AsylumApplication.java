package gs;

import com.google.common.collect.Lists;
import gs.model.Aka;
import gs.model.Inmate;
import gs.repository.InmateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

/*
 * Sprint boot appplication
 */
@SpringBootApplication
public class AsylumApplication {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(AsylumApplication.class, args);
    }


    @Bean
    CommandLineRunner init(InmateRepository inmateRepository) {
        // load basic inmates in not existing
        return args -> {
            inmateRepository.save(Inmate.builder()
                    .id("penguin_1234")
                    .firstname("Oswald")
                    .lastname("Cobblepot")
                    .birthDate(LocalDate.of(1960, 05, 31))
                    .aka(Lists.newArrayList(Aka.builder().name("Penguin").build()))
                    .build());
            inmateRepository.save(Inmate.builder()
                    .id("joker_5555")
                    .firstname("???")
                    .lastname("???")
                    .aka(Lists.newArrayList(Aka.builder().name("Joker").build()))
                    .build());
            inmateRepository.save(Inmate.builder()
                    .id("poisonIvy_7777")
                    .firstname("Pamela")
                    .lastname("Isley")
                    .aka(Lists.newArrayList(Aka.builder().name("Poison Ivy").build()))
                    .build());
        };
    }

}
