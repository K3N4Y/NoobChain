package main;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class NoobChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        blockchain.add(new Block("Hola yo soy el primer bloque", "0"));
        System.out.println("Intentando minar el bloque 1: ");
        blockchain.getFirst().mineBlock(difficulty);

        blockchain.add(new Block("Hola yo soy el segundo bloque", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Intentando minar el bloque 2: ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Hola yo soy el tercer bloque", blockchain.get(blockchain.size()-1).hash));
        System.out.println("Intentando minar el bloque 3: ");
        blockchain.get(2).mineBlock(difficulty);


        System.out.println("\nLa blockchain es valida: " + isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nEl bloque chaine: ");
        System.out.println(blockChainJson);

    }

    public static boolean isChainValid() {
        Block currenBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i ++) {
            currenBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // comparar el hash registrado con el calculado
            if (currenBlock.hash.equals(currenBlock.previousHash)) {
                System.out.println("El hash anterior no coincide");
                return false;
            }

            // comparar el hash anterior con el registrado
            if (!previousBlock.hash.equals(currenBlock.previousHash)) {
                System.out.println("El hash anterior no coincide");
                return false;
            }

            // revisar si el hash se resolvio
            if (!currenBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("Este bloque no ha sido minado");
            }
        }
        return true;
    }
}
