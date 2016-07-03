package codereading.profile;

import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionProfile {

    @Bean
    public DataSource dataSource() {

        URI dbUri;
        try {
            String username = "username";
            String password = "password";
            String url = "jdbc:postgresql://localhost/dbname";
            String dbProperty = System.getenv("DATABASE_URL");
            if (dbProperty != null) {
                dbUri = new URI(dbProperty);

                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
                url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            }

            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            return basicDataSource;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
