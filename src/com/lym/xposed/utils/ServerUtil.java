package com.lym.xposed.utils;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerUtil implements Runnable {
	public interface ServerListener {
		public void onConnect(Socket socket);

		public void onClose();
	}

	boolean isRunning;
	Thread serverThread;
	int port;
	ServerSocket server;
	ServerListener serverListener;

	public ServerUtil(int port, ServerListener serverListener) {
		this.port = port;
		this.serverListener = serverListener;
		startServer();
	}

	public void run() {
		try {
			server = new ServerSocket(port);
			try {
				while (isRunning) {
					Socket socket = server.accept();
					if (serverListener != null) {
						serverListener.onConnect(socket);
					}
				}

			} catch (Exception e) {
			}
		} catch (Exception e) {
		}
		try {
			server.close();
		} catch (Exception e) {
		}
		if (serverListener != null) {
			serverListener.onClose();
		}
		isRunning = false;
	}

	public void startServer() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		serverThread = new Thread(this);
		serverThread.start();
	}

	public void stopServer() {
		isRunning = false;
		try {
			serverThread.interrupt();
		} catch (Exception e) {
		}
		try {
			server.close();
		} catch (Exception e) {
		}
	}

}
