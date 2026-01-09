package pl.kkaczynski.votingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.kkaczynski")
public class VotingWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingWebApplication.class, args);
    }

}
