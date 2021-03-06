package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketEntityHeadYaw extends Packet {

	public int entityId;
	public byte headYaw;
	
	public PacketEntityHeadYaw() {
	}
	
	public PacketEntityHeadYaw(int entityId, byte headYaw) {
		this.entityId = entityId;
		this.headYaw = headYaw;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.entityId = in.readInt();
		this.headYaw = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(this.entityId);
		out.writeByte(this.headYaw);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 35;
	}
	
}
