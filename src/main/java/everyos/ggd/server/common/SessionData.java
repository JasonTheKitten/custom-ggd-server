package everyos.ggd.server.common;

public record SessionData(int matchId, int playerId, String authenticationKey) {
	
	@Override
	public String toString() {
		return
			matchId() + "/" +
			playerId() + "/" +
			authenticationKey();
	}

	public static SessionData fromString(String serialized) {
		String[] parts = serialized.split("/");
		return new SessionData(
			Integer.valueOf(parts[0]),
			Integer.valueOf(parts[1]),
			parts[2]);
	}
	
}
