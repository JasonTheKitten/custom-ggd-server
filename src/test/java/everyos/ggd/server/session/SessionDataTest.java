package everyos.ggd.server.session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionDataTest {

	@Test
	@DisplayName("Can reconstruct serialized session data")
	public void canReconstructSerializedSessionData() {
		SessionData originalData = new SessionData(5, 7, "XKCD");
		String serialized = originalData.toString();
		SessionData reconstructed = SessionData.fromString(serialized);
		Assertions.assertEquals(originalData, reconstructed);
	}
	
}
