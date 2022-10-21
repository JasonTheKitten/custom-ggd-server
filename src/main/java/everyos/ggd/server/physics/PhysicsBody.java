package everyos.ggd.server.physics;

public interface PhysicsBody {

	Position getCurrentPosition();
	
	void setCurrentPosition(Position position);
	
	void recordCurrentPosition(Position position);
	
	Velocity getCurrentVelocity();
	
	void setCurrentVelocity(Velocity velocity);
	
	Velocity getLastVelocity();
	
	void storeLastVelocity();
	
}
