package everyos.ggd.server.server;

import java.util.Optional;

public interface ServerConfig {

	int getPortId();

	Optional<String> getCSStore();

	Optional<String> getCSStorePassword();

}
