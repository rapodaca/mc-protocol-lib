package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketPlayerPosition extends Packet {

	public double x;
	public double y;
	public double stance;
	public double z;
	public boolean grounded;
	
	public PacketPlayerPosition() {
	}
	
	public PacketPlayerPosition(double x, double y, double stance, double z, boolean grounded) {
		this.x = x;
		this.y = y;
		this.stance = stance;
		this.z = z;
		this.grounded = grounded;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.x = in.readDouble();
		this.y = in.readDouble();
		this.stance = in.readDouble();
		this.z = in.readDouble();
		this.grounded = in.readBoolean();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeDouble(this.x);
		out.writeDouble(this.y);
		out.writeDouble(this.stance);
		out.writeDouble(this.z);
		out.writeBoolean(this.grounded);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 11;
	}
	
}
