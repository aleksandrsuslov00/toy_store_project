import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }
}

public class ToyStore {

    private List<Toy> toys;

    public ToyStore() {
        toys = new ArrayList<>();
    }

    public void addToy(int id, String name, int quantity, double weight) {
        Toy toy = new Toy(id, name, quantity, weight);
        toys.add(toy);
    }

    public void updateWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    public Toy drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomNumber = new Random().nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomNumber <= cumulativeWeight) {
                Toy drawnToy = new Toy(toy.getId(), toy.getName(), 1, toy.getWeight());
                toy.decreaseQuantity();
                return drawnToy;
            }
        }
        return null;
    }

    public void saveToFile(Toy toy, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(toy.getId() + "." + '"' + toy.getName() + '"');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayToys() {
        System.out.println("Игрушки в магазине:");
        for (Toy toy : toys) {
            System.out.println("ID: " + toy.getId() +
                    ", Название: " + toy.getName() +
                    ", Количество: " + toy.getQuantity() +
                    ", Вес (частота выпадения в %): " + toy.getWeight());
        }
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Введите информацию о игрушках:");
            for (int i = 1; i <= 3; i++) {
                System.out.println("Игрушка #" + i);
                System.out.println("ID: " + i);
                int id = i;

                System.out.print("Название: ");
                String name = scanner.next();

                System.out.print("Количество: ");
                int quantity = scanner.nextInt();

                System.out.print("Вес (частота выпадения в %): ");
                double weight = scanner.nextDouble();

                toyStore.addToy(id, name, quantity, weight);
            }

            toyStore.displayToys(); //показать весь инвентарь

            System.out.println("Требуется изменить вес (частоту выпадения) какой-то игрушки? (y/n):");
            String answer = scanner.next();

            if ("y".equalsIgnoreCase(answer)) {
                System.out.println("Введите ID игрушки для изменения веса:");
                int toyIdToUpdate = scanner.nextInt();
                System.out.println("Введите новый вес (частоту выпадения) для игрушки:");
                double newWeight = scanner.nextDouble();
                toyStore.updateWeight(toyIdToUpdate, newWeight);
            }

            Toy drawnToy = toyStore.drawToy();
            if (drawnToy != null) {
                toyStore.saveToFile(drawnToy, "DrawResults.txt");
                System.out.println("Проведение розыгрыша...");
                System.out.println("Выиграна игрушка: " + drawnToy.getName() + ". Записано в файл.");
            } else {
                System.out.println("Розыгрыш не удался.");
            }
        } finally {
            scanner.close();
        }
    }
}