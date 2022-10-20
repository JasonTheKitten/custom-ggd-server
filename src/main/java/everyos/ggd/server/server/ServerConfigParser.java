package everyos.ggd.server.server;

import java.util.Optional;

import everyos.api.getopts.ArgumentParser;
import everyos.api.getopts.Flag;
import everyos.api.getopts.FlagArgumentPairCollection;
import everyos.api.getopts.ParserFailedException;

public final class ServerConfigParser {
	
	private static final int HELP_FLAG_ID = 0;
	private static final int PORT_FLAG_ID = 1;
	private static final int CSSTORE_FLAG_ID = 2;
	private static final int CSSTORE_PASSWORD_FLAG_ID = 3;
	
	private ServerConfigParser() {}

	public static ServerConfig parse(String[] args) throws ParserFailedException {
		ArgumentParser parser = createParser();
		
		FlagArgumentPairCollection arguments = parser.parse(args);
		showHelpIfRequested(parser, arguments);
		
		int portId = getPortId(arguments);
		Optional<String> csStore = getCSStore(arguments);
		Optional<String> csStorePassword = getCSStorePassword(arguments);
			
		return new ServerConfig() {
			
			@Override
			public int getPortId() {
				return portId;
			}
			
			@Override
			public Optional<String> getCSStore() {
				return csStore;
			}

			@Override
			public Optional<String> getCSStorePassword() {
				return csStorePassword;
			}
			
		};
	}

	private static void showHelpIfRequested(ArgumentParser parser, FlagArgumentPairCollection arguments) throws ParserFailedException {
		if (arguments.get(HELP_FLAG_ID).length > 0) {
			parser.printHelpScreen();
			throw new ParserFailedException();
		}
	}

	private static int getPortId(FlagArgumentPairCollection arguments) throws ParserFailedException {
		if (arguments.get(PORT_FLAG_ID).length > 0) {
			return arguments
				.get(PORT_FLAG_ID)[0]
				.getArguments()[0]
				.read((inp, e) -> Integer.valueOf(inp));
		}
		
		return 8080;
	}
	
	private static Optional<String> getCSStore(FlagArgumentPairCollection arguments) throws ParserFailedException {
		if (arguments.get(CSSTORE_FLAG_ID).length > 0) {
			return arguments
				.get(CSSTORE_FLAG_ID)[0]
				.getArguments()[0]
				.read((inp, e) -> Optional.of(inp));
		}
		
		return Optional.empty();
	}
	
	private static Optional<String> getCSStorePassword(FlagArgumentPairCollection arguments) throws ParserFailedException {
		if (arguments.get(CSSTORE_PASSWORD_FLAG_ID).length > 0) {
			return arguments
				.get(CSSTORE_PASSWORD_FLAG_ID)[0]
				.getArguments()[0]
				.read((inp, e) -> Optional.of(inp));
		}
		
		return Optional.empty();
	}

	private static ArgumentParser createParser() {
		return ArgumentParser.createBuilder()
			.setFlags(
					Flag.createBuilder("port")
						.setID(PORT_FLAG_ID)
						.setAlias("p")
						.setNumberRequiredArguments(1)
						.setDescription("Set the port number")
						.setAllowDuplicates(false)
						.build(),
					Flag.createBuilder("csstore")
						.setID(CSSTORE_FLAG_ID)
						.setAlias("cs")
						.setNumberRequiredArguments(1)
						.setDescription("Set the certificate store")
						.setAllowDuplicates(false)
						.build(),
					Flag.createBuilder("cspass")
						.setID(CSSTORE_PASSWORD_FLAG_ID)
						.setAlias("csp")
						.setNumberRequiredArguments(1)
						.setDescription("Set the certificate store password")
						.setAllowDuplicates(false)
						.build(),
					Flag.createBuilder("help")
						.setID(HELP_FLAG_ID)
						.setAlias("h")
						.setDescription("Display this screen")
						.build()
				)
				.setAllowLooseArguments(false)
				.setHelpHeader("A custom Great Ghoul Duel server")
				.setErrorFooter("Re-run with only the \"--help\" flag to view the help screen")
				.setLogStream(System.out)
				.build();
	}

}
