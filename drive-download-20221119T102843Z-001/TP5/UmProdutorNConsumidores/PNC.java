package TP5.UmProdutorNConsumidores;

import de.svenjacobs.loremipsum.LoremIpsum;

// Monitor
class Buffer {
    static final int N = 10; // tamanho do buffer
    int[] buffer = new int[N]; // reserva espaco para o buffer
    int count = 0, in = 0, out = 0; // variaveis compartilhadas
    String item;

    // Construtor
    Buffer() {
        for (int i = 0; i < N; i++) {
            buffer[i] = -1;
        }
    } // inicia o buffer

    // Insere um item
    public synchronized void Insere(String item) {
        try {
            this.item = item;
        } catch (Exception e) {
        }
    }
}

// --------------------------------------------------------
// Consumidor
class Consumidor extends Thread {

    int id;
    int delay;
    Buffer buffer;
    String item;

    // Construtor
    Consumidor(int id, int delayTime, Buffer b, String item) {
        this.id = id;
        this.delay = delayTime;
        this.buffer = b;
        this.item = item;
    }

    // Metodo executado pela thread
    public void run() {
        try {
            System.out.println(this.id + " " + item);
            sleep(this.delay); // ...simula o tempo para fazer algo com o item retirado

        } catch (InterruptedException e) {
            return;
        }
    }
}

// --------------------------------------------------------
// Produtor
class Produtor extends Thread {

    int id;
    int delay;
    Buffer buffer;
    private String item;

    // Construtor
    Produtor(int id, int delayTime, Buffer b, String item) {
        this.id = id;
        this.delay = delayTime;
        this.buffer = b;
        this.item = item;
    }

    // Metodo executado pelo thread
    public void run() {
        try {
            this.buffer.Insere(item); // simplificacao: insere o proprio ID
            sleep(this.delay);
        } catch (InterruptedException e) {
            return;
        }
    }

    public String getItem() {
        return this.buffer.item;
    }
}

// --------------------------------------------------------
// Classe principal
class PNC {

    static final int P = 1;
    static final int C = 5;

    public static void main(String[] args) throws InterruptedException {
        int i;
        Buffer buffer = new Buffer(); // Monitor
        Consumidor[] cons = new Consumidor[C]; // Consumidores

        LoremIpsum loremIpsum = new LoremIpsum();
        String text = loremIpsum.getWords(50);

        Produtor prod = new Produtor(1, 1000, buffer, text);
        prod.start();
        prod.join();

        for (i = 0; i < C; i++) {
            cons[i] = new Consumidor(i + 1, 1000, buffer, prod.getItem());
            cons[i].start();
        }
    }
}
