package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public class PacketEnchantItem extends Packet {

	public byte id;
	public byte enchantment;
	
	public PacketEnchantItem() {
	}
	
	public PacketEnchantItem(byte id, byte enchantment) {
		this.id = id;
		this.enchantment = enchantment;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.id = in.readByte();
		this.enchantment = in.readByte();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeByte(this.id);
		out.writeByte(this.enchantment);
	}

	@Override
	public void handleClient(Client conn) {
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 108;
	}
	
}
