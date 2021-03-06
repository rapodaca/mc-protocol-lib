package ch.spacebase.mcprotocol.net.packet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.util.IOUtils;
import ch.spacebase.mcprotocol.util.Util;

public class PacketKeyRequest extends Packet {

	public String serverId;
	public byte[] pubKey;
	public byte[] verifyToken;
	
	public PacketKeyRequest() {
	}
	
	public PacketKeyRequest(String serverId, byte[] pubKey, byte[] verifyToken) {
		this.pubKey = pubKey;
		this.verifyToken = verifyToken;
	}
	
	public String getServerId() {
		return this.serverId;
	}
	
	public byte[] getPublicKey() {
		return this.pubKey;
	}
	
	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.serverId = IOUtils.readString(in);
		byte pubKey[] = new byte[in.readShort()];
		in.readFully(pubKey);
		this.pubKey = pubKey;
		
		byte verifyToken[] = new byte[in.readShort()];
		in.readFully(verifyToken);
		this.verifyToken = verifyToken;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.serverId);
		out.writeShort(this.pubKey.length);
		out.write(this.pubKey);
		out.writeShort(this.verifyToken.length);
		out.write(this.verifyToken);
	}

	@Override
	public void handleClient(Client conn) {
		PublicKey key = toKey(this.pubKey);
		SecretKey secret = generateKey();
		if(!this.serverId.equals("-")) {
			String encrypted = new BigInteger(Util.encrypt(this.serverId, key, secret)).toString(16);
			String response = joinServer(conn.getUsername(), conn.getSessionId(), encrypted);
			if(!response.equalsIgnoreCase("ok")) {
				Util.logger().severe("Failed to login to session.minecraft.net! (RESPONSE: \"" + response + "\")");
				conn.disconnect("Failed to login to session.minecraft.net!");
				return;
			}
		}
		
		conn.send(new PacketKeyResponse(encryptBytes(key, secret.getEncoded()), encryptBytes(key, this.verifyToken)));
		conn.setSecretKey(secret);
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
	}
	
	@Override
	public int getId() {
		return 253;
	}
	
	private static byte[] encryptBytes(PublicKey key, byte[] bytes) {
		try {
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(1, key);
			return cipher.doFinal(bytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static SecretKey generateKey() {
        CipherKeyGenerator gen = new CipherKeyGenerator();
        gen.init(new KeyGenerationParameters(new SecureRandom(), 128));
        return new SecretKeySpec(gen.generateKey(), "AES");
	}
	
	private static PublicKey toKey(byte[] key) {
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			return factory.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			Util.logger().warning("Failed to get public key from array!");
			e.printStackTrace();
			return null;
		} catch (InvalidKeySpecException e) {
			Util.logger().warning("Failed to get public key from array!");
			e.printStackTrace();
			return null;
		}
	}
	
	private static String joinServer(String user, String session, String key) {
        try {
            URL url = new URL("http://session.minecraft.net/game/joinserver.jsp?user=" + URLEncoder.encode(user, "UTF-8") + "&sessionId=" + URLEncoder.encode(session, "UTF-8") + "&serverId=" + URLEncoder.encode(key, "UTF-8"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = reader.readLine();
            reader.close();
            return response;
        } catch (IOException e) {
            return e.toString();
        }
	}
	
}
