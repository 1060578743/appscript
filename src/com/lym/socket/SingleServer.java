package com.lym.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SingleServer implements Runnable {
	boolean isRunning;
	int port;
	ServerSocket server;
	Socket lastSocket;
	ServerListener serverListener;

	public static interface ServerListener {
		public void onConnect(Socket socket);

		public void onClose();
	}

	public SingleServer(int port, ServerListener serverListener) {
		this.port = port;
		this.serverListener = serverListener;
		startServer();
	}

	public void onConnect(Socket socket) {
		try {
			lastSocket.close();
		} catch (Exception e) {
		}
		lastSocket = socket;
		serverListener.onConnect(socket);
	}

	public void onClose() {
		serverListener.onClose();
	}

	public void run() {
		try {
			server = new ServerSocket(port);
			while (isRunning) {
				Socket socket = server.accept();
				onConnect(socket);
			}
		} catch (Exception e) {
		}
		try {
			server.close();
		} catch (Exception e) {
		}

		isRunning = false;
		onClose();
	}

	private void startServer() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		new Thread(this).start();
	}

	public void close() {
		if (!isRunning) {
			return;
		}
		isRunning = false;
		try {
			server.close();
		} catch (Exception e) {
		}
	}

}
