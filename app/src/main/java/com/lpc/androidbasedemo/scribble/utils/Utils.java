package com.lpc.androidbasedemo.scribble.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

public class Utils {

    public static DisplayMetrics displayMetrics() {
        return ScribbleManager.getsInstance().getContext().getResources().getDisplayMetrics();
    }

    public static byte[] getUTF16Bytes(char[] chars) {//将字符转为字节(编码)
        Charset cs = Charset.forName("UTF-16BE");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    public static byte[] getUTF8Bytes(char[] chars) {//将字符转为字节(编码)
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    private char[] getChars(byte[] bytes) {//将字节转为字符(解码)
        Charset cs = Charset.forName("UTF-16BE");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    public static byte[] intToBytes(int value, ByteOrder mode) {
        byte[] src = new byte[4];
        if (mode == ByteOrder.LITTLE_ENDIAN) {
            src[3] = (byte) ((value >> 24) & 0xFF);
            src[2] = (byte) ((value >> 16) & 0xFF);
            src[1] = (byte) ((value >> 8) & 0xFF);
            src[0] = (byte) (value & 0xFF);
        } else {
            src[0] = (byte) ((value >> 24) & 0xFF);
            src[1] = (byte) ((value >> 16) & 0xFF);
            src[2] = (byte) ((value >> 8) & 0xFF);
            src[3] = (byte) (value & 0xFF);
        }
        return src;
    }

    public static int bytesToInt(byte[] src, int offset, ByteOrder mode) {
        int value;
        if (mode == ByteOrder.LITTLE_ENDIAN) {
            value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                    | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
        } else {
            value = (int) (((src[offset] & 0xFF) << 24)
                    | ((src[offset + 1] & 0xFF) << 16)
                    | ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
        }
        return value;
    }


    public static String ByteToHexString(byte[] b, int offset, int count) {
        String sHex = "0123456789ABCDEF";
        char[] cHex = sHex.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < count; i++) {
            sb.append(cHex[(b[i] >> 4) & 0x0f]);
            sb.append(cHex[(b[i] & 0x0f)]);
        }
        return sb.toString();
    }

    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getUTFStrFormBytes(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-16"));
    }

    /**
     * ip 整形类型转换成String
     *
     * @param ip
     * @return
     */
    public static String ipConvert(int ip) {
        byte[] bytes = BigInteger.valueOf(ip).toByteArray();
        InetAddress address = null;
        try {
            address = InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String h1 = address.getHostName();
        return h1;
    }
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    public static int getUniqueNumber(){
        Long time = System.currentTimeMillis();
        int id = (int)(time%1000000000);
        return id;
    }

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
