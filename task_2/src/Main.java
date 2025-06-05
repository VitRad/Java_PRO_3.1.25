import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
        int third = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();
        System.out.println(third);

        //Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9,
        // в отличие от прошлой задачи здесь разные 10 считает за одно число)
        int thirdUnic = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();
        System.out.println(thirdUnic);

        // Имеется список объектов типа Сотрудник (имя, возраст, должность),
        // необходимо получить список имен 3 самых старших сотрудников с должностью «Инженер»,
        // в порядке убывания возраста
        List<String> names = Stream.of(
                        new Employee("Олег", 67, "Инженер"),
                        new Employee("Василий", 52, "Инженер"),
                        new Employee("Петр", 19, "Инженер"),
                        new Employee("Александр", 33, "Инженер"),
                        new Employee("Семен", 28, "Слесарь"),
                        new Employee("Николай", 41, "Инженер")
                ).filter(e -> e.position().equalsIgnoreCase("инженер"))
                .sorted(Comparator.comparingInt(Employee::age).reversed())
                .map(Employee::name)
                .limit(3)
                .toList();
        System.out.println(names);

        // Имеется список объектов типа Сотрудник (имя, возраст, должность),
        // посчитайте средний возраст сотрудников с должностью «Инженер»
        double medianAge = Stream.of(
                        new Employee("Олег", 67, "Инженер"),
                        new Employee("Василий", 52, "Инженер"),
                        new Employee("Петр", 19, "Инженер"),
                        new Employee("Александр", 33, "Инженер"),
                        new Employee("Семен", 28, "Слесарь"),
                        new Employee("Николай", 41, "Инженер")
                ).filter(e -> e.position().equalsIgnoreCase("инженер"))
                .mapToInt(Employee::age)
                .average()
                .orElseThrow();
        System.out.println("Средний возраст инженеров: " + medianAge);

        // Найдите в списке слов самое длинное
        String longestWord = Stream.of("абвгд",
                        "абвгдеёжз",
                        "абвгдеёжзик",
                        "абвгдеёжзиклмн",
                        "абвгдеёжзиклмноп",
                        "абвгдеёжзиклмнопрст")
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
        System.out.println("Самое длинное слово: " + longestWord);

        //Имеется строка с набором слов в нижнем регистре, разделенных пробелом.
        // Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
        Map<String, Long> words = Stream.of(("одно два три четыре два четыре три четыре три четыре").split(" "))
                .map(Objects::toString)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        System.out.println("words: " + words);

        //Отпечатайте в консоль строки из списка в порядке увеличения длины слова,
        //если слова имеют одинаковую длины, то должен быть сохранен алфавитный порядок
        Stream.of("абвгд",
                        "абвгдеёжз",
                        "зжёедгвба",
                        "абвгдеёжзик",
                        "кизжёедгвба",
                        "абвгдеёжзиклмн",
                        "абвгдеёжзиклмноп",
                        "абвгдеёжзиклмнопрст",
                        "тсрпонмлкизжёедгвба")
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);

        //Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом,
        //найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
        String maxLengthWord = Stream.of("aa aa aaa aaa aaaa", "bb bbb bbb bbbbb bbbbb", "c c ccccc ccccc cc")
                .flatMap(s -> Stream.of(s.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
        System.out.println(maxLengthWord);
    }
}