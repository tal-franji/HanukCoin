package il.ac.tau.cs.hanukcoin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 * Simple request-response server that uses non-blocking TCP API
 */
class ServerSimpleNonBlocking {
	protected static int acceptPort = 8080;
	private final ArrayList<ClientConnection> connections = new ArrayList<>();

	public static void main(String[] argv) {
		if (argv.length > 0) {
			// allow changing accept port
			acceptPort = Integer.parseInt(argv[0]);
		}
		ServerSimpleNonBlocking server = new ServerSimpleNonBlocking();
		try {
			server.runServer();
		} catch (InterruptedException e) {
			// Exit - Ctrl -C pressed
		}
	}

	public void runServer() throws InterruptedException {
		ServerSocketChannel acceptSocket = null;
		try {
			acceptSocket = ServerSocketChannel.open();
			acceptSocket.configureBlocking(false);  // non-blocking to allow loop to handle other things
			acceptSocket.socket().bind(new InetSocketAddress(acceptPort));
		} catch (IOException e) {
			System.out.printf("ERROR accepting at port %d%n", acceptPort);
			return;
		}

		while (true) {
			SocketChannel connectionSocket = null;
			try {
				connectionSocket = acceptSocket.accept();
				if (connectionSocket != null) {
					connections.add(new ClientConnection(connectionSocket));
				}
			} catch (IOException e) {
				System.out.printf("ERROR accept:\n  %s%n", e);
			}
			handleConnections();
			Thread.sleep(50);  // prevent tight loop
		}
	}

	/**
	 * go over all live connections hand see if there is data to handle.
	 */
	private void handleConnections() {
		for (ClientConnection connection : connections) {
			if (connection.isActive) {
				connection.handleHttp();
			}
			// TODO(franji): if !connection.isAlive - remove from connections[]
		}
	}

	class ClientConnection {
		private final SocketChannel connectionSocket;
		private ByteBuffer innerBuffer = null;
		private boolean isActive = true;

		public ClientConnection(SocketChannel connectionSocket) {
			this.connectionSocket = connectionSocket;
		}

		/**
		 * Attempt to read more bytes from the Channel
		 *
		 * @throws IOException
		 */
		protected void readMore() throws IOException {
			// See https://howtodoinjava.com/java7/nio/java-nio-2-0-working-with-buffers/#buffer_attributes
			if (innerBuffer == null) {
				innerBuffer = ByteBuffer.allocate(32 * 1024);
			} else {
				// append to end of buffer
				innerBuffer.compact();
				innerBuffer.position(innerBuffer.limit()).limit(innerBuffer.capacity());
			}
			connectionSocket.read(innerBuffer);
			innerBuffer.flip();
		}

		/**
		 * Try reading a full text line from what's in the buffer
		 *
		 * @return a line read (String) or null if no line in buffer
		 * @throws IOException
		 */
		protected String readLine() throws IOException {
			int start = innerBuffer.position();
			int end = innerBuffer.limit();
			int endOfLineIndex = -1;
			int nextLineIndex = -1;
			final int carriageReturn = '\r';
			final int newLine = '\n';

			for (int i = start; i < end; i++) {
				byte b = innerBuffer.get(i);
				if (b == carriageReturn || b == newLine) {
					endOfLineIndex = i;
					nextLineIndex = i + 1;
					if (b == carriageReturn) {
						// assume '\n' follows
						nextLineIndex += 1;
					}
					break;
				}
			}
			if (endOfLineIndex < 0) {
				return null;
			}
			innerBuffer.position(nextLineIndex);
			return new String(innerBuffer.array(), start, endOfLineIndex - start, StandardCharsets.UTF_8);
		}

		private void handleHttp() {
			try {
				boolean firstLine = true;
				while (true) {
					readMore();
					String line = readLine();
					if (line == null) {
						// note line == null and line == "" is not the same
						return;   // not enough data in buffer
					}
					if (firstLine) {
						firstLine = false;
						if (!line.startsWith("GET / ")) {
							connectionSocket.close();
							isActive = false;
							return;
						}
					}
					System.out.printf("Http header: %s%n", line);
					if (line.isEmpty()) {
						break;  // in HTTP empty line ends the header part
					}
				}
				sendHtml(); // send response - assume this is a "GET"
			} catch (IOException e) {
				throw new RuntimeException(e); ///e.printStackTrace();
			}
		}

		protected void sendHtml() throws IOException {
			String html = String.format("<html><body>I am alive<br/>" + "I am non-blocking server connection:%s" + "</body></html>\r\n", this);
			int contentLen = html.length();
			HashMap<String, String> header = new HashMap<>();
			header.put("Content-Length", Integer.valueOf(contentLen).toString());
			header.put("Content-Type", "text/html");
			header.put("Connection", "Closed");
			String responseLine = "HTTP/1.1 200 OK\r\n";
			String headerText = header.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\r\n"));
			String headerFull = responseLine + headerText + "\r\n\r\n";
			String response = headerFull + html;
			System.out.println(headerFull);
			connectionSocket.write(ByteBuffer.wrap(response.getBytes()));
		}
	}
}