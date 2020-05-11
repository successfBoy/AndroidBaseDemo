package com.lpc.androidbasedemo.scribble;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by reweber on 12/20/14.
 */
public class Client {

    private String host;
    private int port;
    private AsyncSocket mSocket;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        setup();
    }

    private void setup() {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                System.out.println("[Client] New Connection " + socket.toString());
                mSocket = socket;
                handleConnectCompleted(ex, socket);
            }
        });
    }

    private void handleConnectCompleted(Exception ex, final AsyncSocket socket) {
        if (ex != null) throw new RuntimeException(ex);

   /*     Util.writeAll(socket, "Hello Server".getBytes(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully wrote message");
            }
        });
*/

        ByteBuffer b = ByteBuffer.allocate(32);
//		b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
        b.putInt(32);
        b.putInt(1);
        b.putInt(1);
        b.putInt(20);
        b.putInt(6);
        b.putInt(1);
        b.putInt(2);
        b.putInt(9);

        byte[] result = b.array();
        sendData(result);

        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                int cmdid = bb.skip(4).peekInt();
                System.out.println("[Client] Received Message " + new String(bb.getAllByteArray()));
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully end connection");
            }
        });
    }

    public void sendData(byte[] bytes) {


        Util.writeAll(mSocket, bytes, new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Client] Successfully wrote message");
            }
        });
    }

    public interface ConnnectListener {
        void connectSuccess();
    }
}

