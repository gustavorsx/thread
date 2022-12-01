package TP5.NConsumidoresUmProdutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    // Remove um item
    public synchronized int Remove() {
        try {
            this.item = null;
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }
}

// --------------------------------------------------------
// Consumidor
class Consumidor extends Thread {

    int id;
    int delay;
    Buffer buffer;
    List<String> items;

    // Construtor
    Consumidor(int id, int delayTime, Buffer b, List<String> items) {
        this.id = id;
        this.delay = delayTime;
        this.buffer = b;
        this.items = items;
    }

    // Metodo executado pela thread
    public void run() {
        try {
            for (String item : items) {
                System.out.println(item);
            }
            this.buffer.Remove();
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
            this.buffer.Insere(this.id + ": " + item);
            sleep(this.delay);
        } catch (InterruptedException e) {
            return;
        }
    }

    public String getItem() {
        return this.item;
    }
}

// --------------------------------------------------------
// Classe principal
class NPC {

    static final int P = 10;
    static final int C = 5;

    public static void main(String[] args) throws InterruptedException {
        int i;
        Buffer bufferClient = new Buffer(); // Monitor
        List<String> items = new ArrayList<String>();
        Produtor[] prod = new Produtor[P]; // Produtores

        Random rand = new Random();
        LoremIpsum loremIpsum = new LoremIpsum();

        for (i = 0; i < P; i++) {
            Buffer buffer = new Buffer();
            String text = loremIpsum.getWords(rand.nextInt(50), rand.nextInt(50));
            prod[i] = new Produtor(i+1, 1000, buffer, text);
            prod[i].start();
        }
        
        for (int j = 0; j < P; j++) {
            prod[j].join();
            items.add(prod[j].buffer.item);
        }

        Consumidor cons = new Consumidor(i + 1, 1000, bufferClient, items);
        cons.start();
    }
}
