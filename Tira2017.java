
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Scanner;

/*
   Teemu Soini
   Tira 2017 harjoitustyö, Hashtable toteutus
   30.1.2018
   424083
   Soini.Teemu.O@student.uta.fi
 */

class Node {
    String key;
    String value;
    Node next;

    //Rakentaja
    public Node(String key,String value) {
        this.key = key;
        this.value = value;
    }

    //Aseta seuraava
    public void setNext(Node next){
        this.next = next;
    }

    // Hae avain

    public String getKey() {
        return key;
    }
    // Hae value
    public String getValue(){
        return value;
    }

    // Aseta value

    public void setValue(String value) {
        this.value = value;
    }

    // Hae seuraava node

    public Node getNext(){
        return next;
    }


}

// hashmap luokka
class HashMap {

    private int maxsize = 10000;
    private ArrayList<Node> bucketArray;

    // Rakentaja, jossa alustetaan Arraylist null arvoilla koolle 9999
    public HashMap(){
        bucketArray = new ArrayList<>();
        for (int i =0;i<maxsize;i++) {
            bucketArray.add(null);
        }
    }

    // Tekee listan A ja B unionin, käyttäen arraylist operaatioita.
    public void union(HashMap setA, HashMap setB){
        setA.bucketArray.addAll(setB.bucketArray);
    }

    // palauttaa listan koon.
    public int size(){
        return bucketArray.size();
    }

    // tarkistaa onko listassa value
    public boolean contains(String value){
        if (value == null) {
            return false;
        }
        return bucketArray.contains(bucketArray.get(Integer.parseInt(value)));
    }
    // hakee avaimelle hashcode kautta indexin.
    public int getHashIndex(String key) {
        if (key !=null){
            int hashCode = key.hashCode();
            int index = hashCode % maxsize;
            return index;
        }
        return 0;
    }
    // lisää hashmapin arraylistaan noden, parametreina avain ja arvo.
    public void add(String key, String value) {
        // Haetaan hashindex ja sijoitetaan pääksi löydetty olio.
        int bucketIndex = getHashIndex(key);
        Node head = bucketArray.get(bucketIndex);
        //jos oli on null, niin asetetaan uusi node hashindexin kohdalle, arvona node luokka.
        if (head == null) {
            Node newNode = new Node(key,value);
            bucketArray.set(bucketIndex,newNode);
        }

        else {
            //Etsitään linkitettyjen nodejen pää, josta seuraava alkio on nolla.
            while (head.getNext() != null & head.getKey() != key) {
                head = head.getNext();
                // Jos paan avain vastaa parametria niin päivitetään arvo, jos ei niin laitetaan node päästä seuraavaan alkioon.
                if (head.getKey() == key) {
                    head.setValue(value);
                }
                else {
                    head.setNext(new Node(key,value));
                }
            }
        }
    }

    // Luokka, jolla poistetaan alkio ämpäristä.
    public void remove(String key) {
        int bucketIndex = getHashIndex(key);
        if (bucketArray.get(bucketIndex) != null) {
            Node prev = null;
            Node head = bucketArray.get(bucketIndex);
            while (head.getNext() != null && Integer.parseInt(head.getKey()) != Integer.parseInt(key)) {
                prev = head;
                head = head.getNext();
            }
            if (head.getKey().equals(key)){
                bucketArray.set(bucketIndex,head.getNext());
            }
            else {
                if (head.getNext() != null){
                    prev.setNext(head.getNext());
                }
            }
        }
    }

    // Hakee alkion arvon ja palauttaa sen String muodossa.
    public String get(int key) {
        int bucketIndex= getHashIndex(Integer.toString(key));
        // Palautetaan null jos lista on tyhjä tai pää on tyhjä.
        if (bucketArray.isEmpty()){
            return null;
        }
        else {
            Node head = bucketArray.get(bucketIndex);
            while (head != null) {
                if (head.getKey().equals(Integer.toString(key))){
                    return head.value;
                }
                head = head.getNext();
            }
        }
        return null;
    }

    // Tulostaa listan
    public void printHashtable() {
        System.out.println();
        for (int i= 0; i<bucketArray.size(); i++){
            System.out.println(bucketArray.get(i));
        }
    }
}

public class Tira2017
{
    //Alustetaan setA ja setB, jotka luetaan tiedostosta.
private HashMap setA = new HashMap();
private HashMap setB = new HashMap();

// Lukija joka populoi setA ja setB
private void readInput()
{

	int key = 1;
    String line;
	try {
		BufferedReader br = new BufferedReader( new FileReader("setA.txt"));

		while (br.ready()) {
			line = br.readLine();
			String[] part = line.split("\n ");
			setA.add(Integer.toString(key),part[0]);
            key++;
		}
	}
	catch(IOException e) {
		System.out.println("File not found.");
	}

	try {
		BufferedReader br = new BufferedReader( new FileReader("setB.txt"));
		key = 1;
		while (br.ready()) {
            line = br.readLine();
            String[] part = line.split("\n ");
            setB.add(Integer.toString(key),part[0]);
            key++;
		}
	}
	catch(IOException e) {
		System.out.println("File not found.");;
	}
}
// Kirjoittaja joka kirjoittaa tekstitiedoston, parametreinä Hashmap ja tiedoston nimi.
private void writeOutput(HashMap output, String filename)
{
    String outputrow;
    try {
		 BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		 for (int i=1;i< output.size();i++){
		     if (output.get(i) != null) {
                 outputrow = output.getHashIndex(Integer.toString(i)) + " " + output.get(i);
                 bw.write(outputrow);
                 bw.newLine();
             }
        }
        bw.close();
    }
    catch (IOException e) {
    System.err.format("IOException: %s%n", e);
}
  System.out.println("Writing file...");
}

// Unioni setA ja setB välillä.
private void mapUnion() {
    // Tehdään uusi setC taulu ja liitetään kaikki alkiot setA ja setB:stä siihen.
    HashMap setC = new HashMap();
    int counter = 1;
    for (int i =1; i<setA.size();i++) {
        if (setA.get(i) != null) {
            for (int j = 1; j < setB.size(); j++) {
                if (setB.get(j) != null) {
                    if (setA.get(i).equals(setB.get(j))){
                        counter++;
                    }
                }
            }
            setC.add(setA.get(i),Integer.toString(counter));
        }
        counter = 1;
    }
    for (int i =1; i<setB.size();i++) {
        if (setB.get(i) != null) {
            for (int j = 1; j < setA.size(); j++) {
                if (setA.get(j) != null) {
                    if (setB.get(i).equals(setA.get(j))){
                        counter++;
                    }
                }
            }
            setC.add(setB.get(i),Integer.toString(counter));
        }
        counter = 1;
    }
    writeOutput(setA,"testi.txt");
}

// Work in progress.
private void mapIntersection() {
    HashMap setC = new HashMap();

    for (int i = 1; i<setA.size();i++){
        if (setA.get(i) != null) {
            for (int j = i; j< setB.size(); j++) {
                if (setA.get(i).equals(setB.get(j))){
                    setC.add(setA.get(i),Integer.toString(i));
                }
            }
        }
    }

    writeOutput(setC, "and.txt");
}

private void logicalOr(){
    HashMap setC = new HashMap();

    // Tupla silmukka lisäämään listasta setA ja listasta setB ne alkiot jotka eivät ole samoja.
    for (int i = 1; i < setA.size();i++) {
        if (setA.get(i) != null) {
            for (int j = 1; j <setB.size();j++){
                if (!setA.get(i).equals(setB.get(j))){
                    setC.add(setA.get(i),Integer.toString(1));
                }
            }
        }

    }
    for (int i = 1; i < setB.size();i++) {
        if (setB.get(i) != null) {
            for (int j = 1; j <setA.size();j++){
                if (!setB.get(i).equals(setA.get(j))){
                    setC.add(setB.get(i),Integer.toString(2));
                }
            }
        }
    }
    writeOutput(setC,"xor.txt");
}

public static void main(String[] args)
	{
	    Tira2017 ht=new Tira2017();
        Scanner reader = new Scanner(System.in);

        System.out.println("Tira 2017 harjoitustyö.");
		// Kutsutaan readInput pupuloimaan listat.
        ht.readInput();

        //ohjelman pyörittäjä looppi ja interface.
        boolean jatketaan = true;
        System.out.println("Syötä s jos haluat suorittaa operaatiot, \nsyötä p jos haluat poistaa alkion listasta,\ntai syötä l lopettaaksesi.");
        System.out.println("Maksimi syöte on 9999");

        while (jatketaan) {
            System.out.println("Syötä komento:");
            String s = reader.next();
            if (s.equals("s")) {
                ht.mapUnion();
                ht.mapIntersection();
                ht.logicalOr();
                jatketaan = false;
            }
            if (s.equals("p")){
                System.out.println("Syötä poistettava alkion avain:");
                s = reader.next();
                System.out.println(("Syötä listan nimi josta alkio poistetaan:"));
                String lista = reader.next();
                if (lista.equals("setA")){
                    ht.setA.remove(s);
                    System.out.println("Avain poistettu.");
                }
                else if (lista.equals("setB")){
                    ht.setB.remove(s);
                    System.out.println("Avain poistettu.");
                }
                else {
                    System.out.println("Virheellinen syöte.");
                }
            }
            if (s.equals("l")) {
                jatketaan = false;
            }
        }
        System.out.println("Ohjelma lopetetaan.");
        reader.close();
	}

}