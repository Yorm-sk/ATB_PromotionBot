package yorm.org;

import yorm.org.entity.Product;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        Object result = null;
        try (FileInputStream fis = new FileInputStream("object.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            result = ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
