package com.lym.xposed;

import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lym.xposed.utils.ServerUtil;
import com.lym.xposed.utils.ServerUtil.ServerListener;
import com.lym.xposed.utils.SocketUtil;

import de.robv.android.xposed.XposedBridge;

/**
 * 运行在脚本app上的脚本服务器，用于和topactivity通信
 * 
 * @author Administrator
 * 
 */
public class ScriptServer implements ServerListener {
	ServerUtil server;
	Socket lastSocket;
	ClientThread clientThread;

	public ScriptServer() {
		server = new ServerUtil(9999, this);
	}

	public void close() {
		server.stopServer();
	}

	public void sendMsg(String msg) {
		try {
			SocketUtil util = clientThread.util;
			util.sendLine(msg);
			System.out.println("msg:" + msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onReceiveMsg(String msg) {
		System.out.println(msg);
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(msg).getAsJsonObject().get("result");
		onResult(je);
	}

	public void onResult(JsonElement je) {
		System.out.println("result:" + je.getAsString());
	}

	private void closeClient() {
		if (lastSocket != null) {
			try {
				lastSocket.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onConnect(Socket socket) {
		closeClient();
		lastSocket = socket;
		new ClientThread(socket).start();
	}

	@Override
	public void onClose() {
		closeClient();
	}

	class ClientThread extends Thread {
		SocketUtil util;

		public ClientThread(Socket socket) {
			util = new SocketUtil(socket);

		}

		@Override
		public void run() {
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			json.addProperty("method", "getActivityName");
			String str = gson.toJson(json);
//			sendMsg(str);
			try {
				util.sendLine(str);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			String msg = null;
			try {
				while ((msg = util.readLine()) != null) {
					onReceiveMsg(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			util.close();
		}
	}
}
