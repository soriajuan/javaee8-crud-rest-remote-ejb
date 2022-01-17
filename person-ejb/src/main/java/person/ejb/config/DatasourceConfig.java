package person.ejb.config;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Startup
@Singleton
@DataSourceDefinition(name = "java:global/PersonDataSource", className = "org.h2.jdbcx.JdbcDataSource", url = "jdbc:h2:mem:test", user = "sa", password = "")
public class DatasourceConfig {

	@Resource(lookup = "java:global/PersonDataSource")
	private DataSource dataSource;

	@Produces
	public DataSource getDatasource() {
		return dataSource;
	}

}
