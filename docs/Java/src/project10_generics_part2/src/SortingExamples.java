import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortingExamples
{
    public static void main(String[] args)
    {
        Person modi = new Person("Narendra Modi", 65);
        Person gandhi = new Person("Rahul Gandhi", 45);
        Person george = new Person("George Able", 5);

        List<Person> people = new ArrayList<>();

        people.add(modi);
        people.add(gandhi);
        people.add(george);

        people.sort(Person.BY_AGE);
        System.out.println(people);

    }
}
