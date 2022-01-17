package person.ejb.config;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class FlywayMigrator {

	@Inject
	private DataSource dataSource;

	@PostConstruct
	private void doMigration() {
		final Flyway flyway = Flyway.configure().dataSource(dataSource).baselineOnMigrate(true).load();
		flyway.migrate();
	}

}
