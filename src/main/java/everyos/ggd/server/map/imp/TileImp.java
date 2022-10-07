package everyos.ggd.server.map.imp;

import everyos.ggd.server.map.Tile;

public class TileImp implements Tile {
	
	private final boolean canGreenPass;
	private final boolean canPurplePass;
	private final boolean hasSpiritFlame;
	private final boolean hasMegaFlame;

	public TileImp(boolean canGreenPass, boolean canPurplePass, boolean hasSpiritFlame, boolean hasMegaFlame) {
		this.canGreenPass = canGreenPass;
		this.canPurplePass = canPurplePass;
		this.hasSpiritFlame = hasSpiritFlame;
		this.hasMegaFlame = hasMegaFlame;
	}
	
	@Override
	public boolean greenCanPass() {
		return this.canGreenPass;
	}

	@Override
	public boolean purpleCanPass() {
		return this.canPurplePass;
	}

	@Override
	public boolean hasSpiritFlame() {
		return this.hasSpiritFlame;
	}

	@Override
	public boolean hasMegaFlame() {
		return this.hasMegaFlame;
	}

}
