package main;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private Long timestamp;

    // Constructor

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calcultedHash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timestamp) +
                        data
        );
        return calcultedHash;
    }
}
