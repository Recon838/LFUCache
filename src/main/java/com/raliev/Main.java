package com.raliev;

import java.io.Console;

/**
 * Необходимо реализовать Last Frequently Used Cache (LFU).
 *
 * LFU должен работать как Map, т.е. должен быть generic,
 * должен иметь возможность задать типы ключа и значения при объявлении.
 *
 * Необходимо реализовать как минимум 4 метода
 * 1. put (k, v) - вставка
 * 2. get (k) - чтение значения
 * 3. contains (k) - проверка на наличие ключа
 * 4. size() - кол-во элементов в кеше.
 *
 * У каждого ключа k есть счетчик. При каждом обращении к ключу k, его счетчик увеличивается на 1.
 * Все остальные ключи уменьшаются на 1. Если у ключа счетчик равен нулю, элемент удаляется из кеша.
 *
 * Можно использовать любые стандартные структуры данных.
 * Реализовать в виде консольной программы с возможностью вносить пары ключ, значение и с возможностью печати кеша.
 */
public class Main {

    private static final String PUT_COMMAND = "put";
    private static final String GET_COMMAND = "get";
    private static final String PRINT_COMMAND = "print";

    private static final String CONTAINS_COMMAND = "contains";

    private static final String SIZE_COMMAND = "size";

    private static final String EXIT_COMMAND = "exit";

    private static final Console console = System.console();

    private static final Cache<String, String> cache = new LFUCache<>();

    /**
     * 1. maven clean install
     * 2. run: "java -jar cache.jar"
     */
    public static void main(String[] args) {
        if (console == null) {
            System.out.println("No console: non-interactive mode!");
            System.exit(0);
        }

        console.printf("Available commands: '%s', '%s', '%s', '%s', '%s', '%s'\n",
                PUT_COMMAND, GET_COMMAND, PRINT_COMMAND, CONTAINS_COMMAND, SIZE_COMMAND, EXIT_COMMAND);

        String currentCommand;
        do {
            console.printf("\nEnter command:\n> ");
            currentCommand = console.readLine();
            switch (currentCommand) {
                case PUT_COMMAND -> put();
                case GET_COMMAND -> get();
                case PRINT_COMMAND -> print();
                case CONTAINS_COMMAND -> contains();
                case SIZE_COMMAND -> size();
                case EXIT_COMMAND -> {
                }
                default -> console.printf("incorrect input, try again");
            }
        } while (!EXIT_COMMAND.equals(currentCommand));
        System.exit(0);
    }

    private static void put() {
        console.printf("enter key: ");
        String key = console.readLine();
        console.printf("enter value: ");
        String value = console.readLine();

        cache.put(key, value);
    }

    private static void get() {
        console.printf("enter key: ");
        String key = console.readLine();
        String value = cache.get(key).orElse("null");
        console.printf("the value: %s\n", value);
    }

    private static void print() {
        console.printf("cache: %s", cache.toString());
    }

    private static void contains() {
        console.printf("enter key: ");
        String key = console.readLine();
        console.printf("contains: %b\n", cache.contains(key));
    }

    private static void size() {
        console.printf("size: %d\n", cache.size());
    }
}
