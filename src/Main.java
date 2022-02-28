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
                try {

                    String path = checkInput(input);   // проверяем относительный ли путь к файлу и расширение файла
                    readFile(path);                 // считываем текст из файла 
                    showResults();                  // выводим результаты

                } catch (NotTxtException e){
                    System.out.println("Выбран нетекстовый файл!");
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден!");
                } catch (IOException e) {
                    System.out.println("Ошибка открытия файла!");
                } catch (NoSuchElementException e) {
                    System.out.println("Не найдено слов в файле!");
                }
            }
        }
        scan.close();
    }

    public static String checkInput(String input) throws NotTxtException {
        if (!input.endsWith(".txt"))
            throw new NotTxtException();
        if (!input.startsWith(":\\", 1))
            input = Paths.get("").toAbsolutePath() + "\\" + input;
//        System.out.println(input);
        return input;
    }

    private static void readFile(String path) throws IOException{
            FileReader reader = new FileReader(path);
            StringBuilder temp = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1){
//                System.out.println((char) c);
                if(        (0x40<c && c<0x5B)
                        || (0x60<c && c<0x7B))
                {
                    temp.append((char) c);   // если с - буква, то добавляем её в слово
                } else {
                    addToDictionary(temp.toString().toLowerCase());   // добавляем слово в словарь
                    temp.setLength(0);
                }
            }
            addToDictionary(temp.toString().toLowerCase());   // добавляем последнее слово в словарь
            reader.close();
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

    private static void showResults() throws NoSuchElementException{
        int maxCountWord;
        System.out.println("\nВ файле найдены слова:");
        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){
            System.out.println(entry.getKey() + " - " + entry.getValue() + " повторений"); // выводим все слова из словаря
        }

        maxCountWord = dictionary.values().stream()  // находим максимальное число повторений
                    .max(Integer::compareTo).get();

        System.out.println("\nЧаще всего встречается:");

        for (Map.Entry<String, Integer> entry : dictionary.entrySet()){  // выводим чаще встерающиеся слова
            if(entry.getValue().equals(maxCountWord))
                System.out.println(entry.getKey() + " - составляет " + maxCountWord*100/countWord + "% от всех слов");
        }
    }
}

class NotTxtException extends IOException{
}
