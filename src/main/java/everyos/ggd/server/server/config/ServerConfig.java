package everyos.ggd.server.server.config;

import java.util.Optional;

public interface ServerConfig {

	int getPortId();

	Optional<String> getCSStore();

	Optional<String> getCSStorePassword();

	boolean isVerbose();

}
