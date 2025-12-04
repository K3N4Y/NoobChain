package main;

public class NoobChain {
    public static void main(String[] args) {
        Block genesisBlock = new Block("Hola yo soy el primer bloque", "0");
        System.out.println("Hash para el bloque 1: " + genesisBlock.hash);

        Block segundoBloque = new Block("Hola yo soy el segundo bloque", genesisBlock.hash);
        System.out.println("Hash para el bloque 2: " + segundoBloque.hash);

        Block tercerBloque = new Block("Hola yo soy el tercer bloque", segundoBloque.hash);
        System.out.println("Hash para el bloque 3: " + tercerBloque.hash);
    }
}
