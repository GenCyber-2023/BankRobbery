package bank;

import static bank.CaesarCipher.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class CharSetStreamTest {
    private static final Charset UTF_16 = Charset.forName("UTF-16");

    public static byte[] encryptAndByteify(String plaintext, int shift) {
        String ciphertext = encrypt(plaintext, shift);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bout, true, UTF_16);
        writer.println(ciphertext);

        return bout.toByteArray();
    }

    public static String debyteifyAndDecrypt(byte[] data, int shift) 
            throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        InputStreamReader in = new InputStreamReader(bin, UTF_16);
        BufferedReader reader = new BufferedReader(in);

        String ciphertext = reader.readLine();
        String plaintext = decrypt(ciphertext, shift);
        return plaintext;
    }

    public static void main(String[] args) throws IOException {
        // SortedMap<String, Charset> charsets = Charset.availableCharsets();
        // for(String name : charsets.keySet()) {
        //     Charset charset = charsets.get(name);
        //     System.out.println(name + " : " + charset);
        // }
        Charset utf16 = Charset.forName("UTF-16");
        System.out.println(utf16);

        byte[] data = encryptAndByteify("unrecognized", 7);

        String plaintext = debyteifyAndDecrypt(data, 7);

        System.out.println(plaintext);

    }
}
