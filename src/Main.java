import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Sergey Nesterov
 */
public class Main {

    private static Map <String, Integer> dictionary = new TreeMap<>();
    private static int countWord;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("\nВведите путь к файлу или 'exit' - для закрытия программы!");
            if (scan.hasNext()){
                String input = scan.nextLine();
                if (input.equals("exit")) break;
                String path = checkInput(input);   // проверяем относительный ли путь к файлу
                if (!readFile(path)) continue;      // считываем текст из файла (при исключении возвращается false)
                showResults();                  // выводим результаты
            }
        }
        scan.close();
    }

    public static String checkInput(String input){
        if (!input.startsWith(":\\", 1))
            input = Paths.get("").toAbsolutePath() + "\\" + input;
//        System.out.println(input);
        return input;
    }

    private static boolean readFile(String path) {
        try (FileReader reader = new FileReader(path)){
            StringBuilder temp = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1){
//                System.out.println((char) c);
                if(        (0x2F<c && c<0x3A)
                        || (0x41<c && c<0x5B)
                        || (0x60<c && c<0x7B))
                {
                    temp.append((char) c);   // если с - буква или цифра, то добавляем его в слово
                } else {
                    addToDictionary(temp.toString());   // добавляем слово в словарь
                    temp.setLength(0);
                }
            }
            addToDictionary(temp.toString());   // добавляем последнее слово в словарь


        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
            return false;
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла!");
            return false;
        }
        return true;
    }

    private static void addToDictionary(String word) {
        if (word.isEmpty()) return;
        countWord++;
        if(dictionary.containsKey(word)){
            dictionary.put(word, dictionary.get(word) + 1);
        } else {
            dictionary.put(word, 1);
        }
    }

    private static void showResults() {
        int maxCountWord;
        System.out.println("\nВ файле найдены слова:");
        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){
            System.out.println(entry.getKey() + " - " + entry.getValue() + " повторений"); // выводим все слова из словаря
        }

        try {
             maxCountWord = dictionary.values().stream()  // находим максимальное число повторений
                    .max(Integer::compareTo).get();
        }catch (NoSuchElementException ex){
            System.out.println("Не найдено слов в файле!");
            return;
        }

        System.out.println("\nЧаще всего встречается:");

        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){  // выводим чаще встерающиеся слова
            if(entry.getValue().equals(maxCountWord))
                System.out.println(entry.getKey() + " - составляет " + maxCountWord*100/countWord + "% от всех слов");
        }
    }
}
