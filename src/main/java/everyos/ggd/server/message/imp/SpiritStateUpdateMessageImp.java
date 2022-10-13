package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritStateUpdate;
import everyos.ggd.server.message.SpiritStateUpdateMessage;

public class SpiritStateUpdateMessageImp implements SpiritStateUpdateMessage {

	private final SpiritStateUpdate stateUpdate;

	public SpiritStateUpdateMessageImp(SpiritStateUpdate stateUpdate) {
		this.stateUpdate = stateUpdate;
	}
	
	@Override
	public int getType() {
		return Message.ENTITY_UPDATE;
	}
	
	@Override
	public SpiritStateUpdate getUpdate() {
		return this.stateUpdate;
	}

}
