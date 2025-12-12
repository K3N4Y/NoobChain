# NoobChain

Una implementación educativa de una blockchain simple en Java que demuestra los conceptos fundamentales de tecnología de ledger distribuido, criptografía y transacciones digitales.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Arquitectura](#arquitectura)
- [Componentes](#componentes)
- [Cómo Funciona](#cómo-funciona)
- [Ejemplos](#ejemplos)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## 🎯 Descripción

NoobChain es una implementación educativa de una blockchain que implementa los conceptos clave de tecnología blockchain, incluyendo:

- **Proof of Work**: Algoritmo de minado basado en dificultad ajustable
- **Criptografía**: Firmas digitales ECDSA y hashes SHA-256
- **Transacciones**: Sistema completo de entrada/salida con UTXO (Unspent Transaction Output)
- **Validación**: Verificación integral de la integridad de la cadena
- **Billeteras**: Gestión de claves públicas y privadas

Este proyecto fue creado con propósitos educativos para entender cómo funcionan internamente los sistemas blockchain.

## ✨ Características

### Núcleo Blockchain
- ✅ Estructura de bloques con merkle root
- ✅ Algoritmo Proof of Work ajustable
- ✅ Validación de cadena completa
- ✅ Sistema de transacciones con firmas digitales

### Sistema de Transacciones
- ✅ UTXO (Unspent Transaction Output)
- ✅ Entrada y salida de transacciones
- ✅ Validación de firma ECDSA
- ✅ Verificación de integridad de datos

### Billeteras
- ✅ Generación de pares de claves ECDSA
- ✅ Cálculo de saldo desde UTXOs
- ✅ Envío seguro de fondos
- ✅ Gestión de cambio de transacciones

### Seguridad
- ✅ Hashing SHA-256
- ✅ Firmas digitales ECDSA
- ✅ Validación de merkle root
- ✅ Verificación de cadena de custodia

## 📦 Requisitos

- **Java**: JDK 11 o superior
- **Maven**: 3.6 o superior (para compilar)
- **BouncyCastle**: Librería criptográfica (incluida en pom.xml)
- **Gson**: Para serialización JSON (incluida en pom.xml)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/K3N4Y/NoobChain.git
cd NoobChain
```

### 2. Compilar el proyecto

```bash
mvn clean compile
```

### 3. Construir el proyecto

```bash
mvn package
```

## 💻 Uso

### Ejecutar la aplicación

```bash
mvn exec:java -Dexec.mainClass="main.NoobChain"
```

O compilar y ejecutar directamente:

```bash
javac -cp ".:target/classes:target/dependency/*" src/main/*.java
java -cp ".:target/classes:target/dependency/*" main.NoobChain
```

### Ejemplo de Salida

```
Creating and Mining Genesis block... 
Bloque mindado!!!00000abc123def...

WalletA's balance is: 100.0

WalletA is Attempting to send funds (40) to WalletB...
Transaction Successfully added to Block
Bloque mindado!!!00000xyz789ghi...

WalletA's balance is: 59.5
WalletB's balance is: 40.0

Blockchain is valid
```

## 🏗️ Arquitectura

### Diagrama de Componentes

```
┌─────────────────────────────────────────┐
│           NoobChain (Main)              │
│  - Gestiona blockchain                  │
│  - Valida cadena completa               │
│  - Coordina transacciones               │
└────────────┬──────────────────┬─────────┘
             │                  │
      ┌──────▼──────┐    ┌─────▼──────┐
      │   Wallet    │    │   Block    │
      │ - Billetera │    │ - Minado   │
      │ - Saldo     │    │ - Hash     │
      │ - Envío     │    │ - Merkle   │
      └──────┬──────┘    └─────┬──────┘
             │                  │
      ┌──────▼─────────────────▼──────┐
      │      Transaction              │
      │ - Entrada/Salida              │
      │ - Firma ECDSA                 │
      │ - Validación                  │
      └──────┬──────────────┬─────────┘
             │              │
      ┌──────▼──────┐  ┌───▼─────────┐
      │Transaction  │  │Transaction  │
      │  Input      │  │   Output    │
      └─────────────┘  └─────────────┘
```

## 📚 Componentes

### 1. **NoobChain.java**
Clase principal que gestiona toda la blockchain.

**Responsabilidades:**
- Almacenar lista de bloques
- Gestionar UTXOs globales
- Validar integridad de la cadena
- Coordinar transacciones

**Atributos principales:**
```java
public static ArrayList<Block> blockchain
public static HashMap<String, TransactionOutput> UTXOs
public static int difficulty
public static float minimumTransaction
```

### 2. **Block.java**
Representa un bloque individual en la cadena.

**Responsabilidades:**
- Almacenar transacciones
- Calcular hash del bloque
- Realizar minado (Proof of Work)
- Validar transacciones

**Características:**
- Hash SHA-256
- Merkle Root de transacciones
- Nonce para prueba de trabajo
- Timestamp

### 3. **Transaction.java**
Gestiona transacciones individuales.

**Responsabilidades:**
- Verificar firma digital
- Validar entrada/salida
- Procesar transacción
- Calcular ID de transacción

**Métodos clave:**
- `generateSignature()`: Firma la transacción
- `verifiySignature()`: Verifica la firma
- `processTransaction()`: Procesa y valida

### 4. **Wallet.java**
Gestiona billeteras y claves criptográficas.

**Responsabilidades:**
- Generar pares de claves ECDSA
- Calcular saldo
- Firmar transacciones
- Enviar fondos

**Métodos clave:**
- `generateKeyPair()`: Crea claves públicas/privadas
- `getBalance()`: Calcula saldo desde UTXOs
- `sendFunds()`: Crea y firma transacciones

### 5. **TransactionInput.java**
Representa la entrada de una transacción.

**Atributos:**
- `transactionOutputId`: ID del UTXO referenciado
- `UTXO`: Referencia al UTXO

### 6. **TransactionOutput.java**
Representa la salida de una transacción.

**Atributos:**
- `id`: Hash único del output
- `recipient`: Clave pública del destinatario
- `value`: Cantidad de monedas
- `parentTransactionId`: ID de la transacción padre

### 7. **StringUtil.java**
Utilidades criptográficas.

**Métodos principales:**
- `applySha256()`: Calcula SHA-256
- `applyECDSASig()`: Firma con ECDSA
- `verifyECDSASig()`: Verifica firma ECDSA
- `getStringFromKey()`: Codifica clave en Base64
- `getMerkleRoot()`: Calcula merkle root

## 🔄 Cómo Funciona

### 1. Inicialización
```
1. Se crean 3 billeteras (A, B, Coinbase)
2. Se crea transacción genesis: Coinbase → WalletA (100 monedas)
3. Se crea primer bloque y se agrega la transacción
4. Se mina el bloque
```

### 2. Transacción Normal
```
1. Usuario A crea transacción
2. Especifica entrada (UTXO actual) y salida (destinatario + cambio)
3. Firma la transacción con su clave privada
4. Se agrega a un bloque nuevo
5. El bloque se mina (Proof of Work)
6. Se agrega a la cadena
```

### 3. Validación de Cadena
```
1. Para cada bloque (excepto genesis):
   a. Verificar hash = calculateHash()
   b. Verificar previousHash correcto
   c. Verificar hash comienza con "0"*difficulty
   
2. Para cada transacción:
   a. Verificar firma digital válida
   b. Verificar entrada/salida balance
   c. Verificar UTXO existe
   d. Verificar UTXO no fue gastado
   e. Verificar destinatarios correctos
```

## 📖 Ejemplos

### Ejemplo 1: Crear una Billetera

```java
Wallet wallet = new Wallet();
System.out.println("Saldo: " + wallet.getBalance());
```

### Ejemplo 2: Enviar Fondos

```java
Transaction transaction = walletA.sendFunds(walletB.publicKey, 50f);
block.addTransaction(transaction);
```

### Ejemplo 3: Crear y Minar Bloque

```java
Block block = new Block(previousBlockHash);
block.addTransaction(transaction);
NoobChain.addBlock(block); // Mina y agrega
```

### Ejemplo 4: Validar Blockchain

```java
if (NoobChain.isChainValid()) {
    System.out.println("Blockchain válida");
} else {
    System.out.println("Blockchain comprometida");
}
```

## 🔐 Conceptos Criptográficos

### SHA-256
- Función hash criptográfica
- Genera hash de 256 bits
- Propiedad: imposible revertir
- Usada en: cálculo de hash de bloque

### ECDSA (Elliptic Curve Digital Signature Algorithm)
- Algoritmo de firma digital
- Clave pública: verifica firma
- Clave privada: crea firma
- Curva: prime192v1

### Merkle Root
- Árbol hash de todas las transacciones
- Cambio en cualquier transacción → cambio en merkle root
- Cambio en merkle root → cambio en hash de bloque

### UTXO (Unspent Transaction Output)
- Monedas disponibles no gastadas
- Se usa como entrada en nueva transacción
- Se reemplaza por nuevas salidas

## 🧪 Pruebas

El programa incluye pruebas integradas que verifican:

1. **Creación de bloques**: Bloques se crean con hash válido
2. **Minado**: Prueba de trabajo se calcula correctamente
3. **Transacciones**: Se procesan y validan correctamente
4. **Firmas**: Las firmas ECDSA se verifican
5. **Validación de cadena**: La integridad se mantiene

Ejecutar pruebas:
```bash
mvn test
```

## ⚙️ Configuración

### Ajustar Dificultad

```java
NoobChain.difficulty = 4; // Reduce tiempo de minado
NoobChain.difficulty = 6; // Aumenta dificultad
```

### Transacción Mínima

```java
NoobChain.minimumTransaction = 0.5f; // Moneda mínima
```

## 📊 Complejidad

| Operación | Complejidad |
|-----------|-------------|
| Crear transacción | O(n) - donde n es # UTXOs |
| Minar bloque | O(2^d) - donde d es dificultad |
| Validar cadena | O(n*m) - n bloques, m transacciones |
| Calcular balance | O(n) - n UTXOs |

## 🐛 Limitaciones y Consideraciones

- **No es producción**: Solo para educación
- **No es escalable**: Mantiene todo en memoria
- **Minado simple**: No implementa pool de minería
- **Dificultad fija**: No se ajusta automáticamente
- **Sin persistencia**: Datos se pierden al cerrar

## 🔮 Mejoras Futuras

- [ ] Persistencia en base de datos
- [ ] API REST
- [ ] Pool de minería
- [ ] Dificultad dinámica
- [ ] Interfaz gráfica
- [ ] Nodos distribuidos
- [ ] Smart contracts

## 📝 Licencia

Este proyecto está disponible bajo licencia MIT. Ver archivo LICENSE para detalles.

## 👨‍💻 Autor

**K3N4Y**

- GitHub: [@K3N4Y](https://github.com/K3N4Y)
- Repositorio: [NoobChain](https://github.com/K3N4Y/NoobChain)

## 📧 Contacto y Soporte

Para preguntas, sugerencias o reportar issues:

1. Abre un issue en GitHub
2. Envía un pull request con mejoras
3. Contacta al autor

## 📚 Recursos Adicionales

### Documentación
- [Bitcoin Whitepaper](https://bitcoin.org/bitcoin.pdf)
- [ECDSA Explained](https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm)
- [Merkle Trees](https://en.wikipedia.org/wiki/Merkle_tree)
- [Proof of Work](https://en.wikipedia.org/wiki/Proof_of_work)

### Herramientas
- [BouncyCastle Documentation](https://www.bouncycastle.org/)
- [Gson Documentation](https://github.com/google/gson)

---

**Nota**: Este proyecto es únicamente para propósitos educativos. No debe usarse en producción ni para aplicaciones reales de blockchain.

**Última actualización**: Diciembre 2025
