package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketServerPing extends Packet {
	
	public PacketServerPing() {
	}

	@Override
	public void read(DataInputStream in) throws IOException {
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 254;
	}
	
}
