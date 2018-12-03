package Client;

import java.util.Arrays;

public class Criptografia {
    
    private static final long DELTA = 0x9e3779b9;
    private static final long[] KEY = { 78945677, 87678687, 234234, 234234 };
    
    public String encrypt(String mensagem) {

        int j;
        long text[], sum;

        sum = 0;
        text = StringToLong('e', mensagem);
        
        for (int n = 0; n < 32; n++) {
            sum += DELTA;

            for (int i = 0; i < text.length; i++) {
                j = i + 1;
                text[i] += ((text[j] << 4) + KEY[0]) ^ (text[j]+sum) ^ ((text[j] >> 5) + KEY[1]);
                text[j] += ((text[i] << 4) + KEY[2]) ^ (text[i]+sum) ^ ((text[i] >> 5) + KEY[3]);
                i++;
            }
        }
        return Arrays.toString(text);
    }

    public String decrypt(String mensagem) {

        int j;
        long sum = DELTA << 5;
        long text[] = StringToLong('d', mensagem);

        for (int n = 0; n < 32; n++) {
            for (int i = 0; i < text.length; i++) {
                j = i + 1;
                text[j] -= ((text[i] << 4) + KEY[2]) ^ (text[i]+sum) ^ ((text[i] >> 5) + KEY[3]);
                text[i] -= ((text[j] << 4) + KEY[0]) ^ (text[j]+sum) ^ ((text[j] >> 5) + KEY[1]);
                i++;
            }
            sum -= DELTA;
        }
        return LongToString(text);
    }
    
    private String LongToString(long text[]){
        
        String mensagem = "";
        
        for (int i = 0; i < text.length; i++) {
            mensagem += (char)text[i];
        }
        return mensagem;
    }
    
    private long[] StringToLong(char mode, String mensagem){
        
        long text[] = null;
        
        switch (mode) {
            case 'e':
                if (mensagem.length() % 2 == 0) { text = new long[mensagem.length()]; }
                else { text = new long[mensagem.length() + 1]; }
                for (int i = 0; i < mensagem.length(); i++) {
                    text[i] = mensagem.charAt(i);
                }
                break;
            case 'd':
                String msg[] = mensagem.substring(1, mensagem.length() - 1).split(",");
                if (msg.length % 2 == 0) { text = new long[msg.length]; }
                else { text = new long[msg.length + 1]; }

                for (int i = 0; i < msg.length; i++) {
                    if (!msg[i].contains("-")) {
                        msg[i] = "+" + msg[i].trim();
                }}

                for (int j = 0; j < msg.length; j++) {
                    text[j] = Long.valueOf(msg[j].trim());
                }
                break;
        }
        return text;
    }
}