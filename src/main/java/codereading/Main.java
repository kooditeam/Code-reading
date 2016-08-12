
package codereading;

import codereading.profile.DevProfile;
import codereading.profile.ProductionProfile;
import codereading.profile.TestProfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import({DevProfile.class, ProdProfile.class})
@Import({ProductionProfile.class, DevProfile.class, TestProfile.class})
@SpringBootApplication
public class Main {
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
