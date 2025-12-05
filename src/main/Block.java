package main;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private Long timestamp;
    private long nonce;

    // Constructor

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.nonce = 0L;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calcultedHash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timestamp)+
                        Long.toString(nonce)+
                        data
        );
        return calcultedHash;
    }

    // agregue la funcionalidad de minar
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Crea un string con dificultad * "0"
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Bloque mindado!!!" + hash);
    }
}
