package person.ejb.config;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.h2.tools.Server;

@Startup
@Singleton
public class DatabaseInitialization {

	private Server server;

	@PostConstruct
	private void start() {
		try {
			server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
			server.start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	private void stop() {
		if (server.isRunning(false)) {
			server.stop();
		}
	}

}
