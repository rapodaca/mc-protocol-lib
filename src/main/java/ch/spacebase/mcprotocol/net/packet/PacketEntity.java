package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketEntity extends Packet {

	public int entityId;
	
	public PacketEntity() {
	}
	
	public PacketEntity(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 30;
	}
	
}
