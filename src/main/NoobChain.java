package main;

import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

// Clase principal que gestiona la blockchain
public class NoobChain {

    // Lista que almacena todos los bloques de la cadena
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    // Mapa que almacena las salidas de transacciones no gastadas (UTXO)
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    // Nivel de dificultad para el minado de bloques (cantidad de ceros al inicio del hash)
    public static int difficulty = 5;
    // Valor mínimo de transacción permitida
    public static float minimumTransaction = 0.1f;
    // Billetera A para pruebas
    public static Wallet walletA;
    // Billetera B para pruebas
    public static Wallet walletB;
    // Primera transacción que inicializa la blockchain
    public static Transaction genesisTransaction;

    // Método principal: punto de entrada de la aplicación
    public static void main(String[] args) {

        //Agregar el proveedor de seguridad BouncyCastle para operaciones criptográficas
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Configurar Bouncey Castle como proveedor de seguridad

        //Crear tres billeteras: A, B y una billetera base (coinbase) para emitir monedas iniciales
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //Crear la transacción genesis: la billetera base envía 100 monedas a la billetera A
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 //Firmar manualmente la transacción genesis
        genesisTransaction.transactionId = "0"; //Asignar manualmente el ID de la transacción a "0"
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId)); //Agregar manualmente la salida de la transacción
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //Almacenar la primera transacción en la lista de UTXO

        System.out.println("Creating and Mining Genesis block... ");
        // Crear el bloque genesis (primer bloque de la cadena)
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //===== PRUEBAS DE TRANSACCIONES =====
        // Crear el bloque 1
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        // Billetera A envía 40 monedas a la billetera B
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        // Crear el bloque 2
        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        // Billetera A intenta enviar más monedas de las que tiene (transacción inválida)
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        // Crear el bloque 3
        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        // Billetera B envía 20 monedas de vuelta a la billetera A
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
        addBlock(block3);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        // Verificar si la blockchain es válida
        isChainValid();
    }

    // Método que valida la integridad de toda la blockchain
    // Verifica: hashes, firmas, entradas/salidas y UTXOs
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        // Crear una cadena objetivo de ceros según el nivel de dificultad (ej: "00000" para dificultad 5)
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        // Lista temporal de UTXOs para validar transacciones en cada bloque
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //una lista de trabajo temporal de transacciones no gastadas en un estado de bloque determinado.
        // Agregar la salida de la transacción genesis a los UTXO temporales
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //Iterar a través de todos los bloques de la blockchain (empezando desde el bloque 1, saltando el genesis)
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            // Verificar que el hash almacenado coincida con el hash calculado
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            // Verificar que el hash anterior sea correcto
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            // Verificar que el bloque haya sido minado correctamente (comienza con los ceros requeridos)
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //Iterar a través de todas las transacciones del bloque:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                // Verificar que la firma digital de la transacción sea válida
                if(!currentTransaction.verifiySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                // Verificar que la suma de entradas sea igual a la suma de salidas
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                // Validar cada entrada de la transacción
                for(TransactionInput input: currentTransaction.inputs) {
                    // Buscar el UTXO referenciado en la lista temporal
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    // Verificar que el UTXO exista
                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    // Verificar que el valor del UTXO sea correcto
                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    // Remover el UTXO gasto de la lista temporal (ya no puede ser usado)
                    tempUTXOs.remove(input.transactionOutputId);
                }

                // Agregar las salidas de la transacción a los UTXO temporales (para usar en futuras transacciones)
                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                // Verificar que la primera salida vaya al destinatario correcto
                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                // Verificar que la segunda salida (cambio) vaya al remitente correcto
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    // Método que agrega un nuevo bloque a la blockchain
    // Primero mina el bloque (resuelve el problema de Proof of Work) y luego lo añade a la lista
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
