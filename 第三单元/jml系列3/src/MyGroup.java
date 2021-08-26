import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people = new HashMap<>();
    private int ageSum;
    private int age2Sum;

    public MyGroup(int id) {
        this.id = id;
        this.ageSum = 0;
        this.age2Sum = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return (((Group) obj).getId() == id);
        } else {
            return false;
        }
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
        ageSum += person.getAge();
        age2Sum += (person.getAge() * person.getAge());
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person person : people.values()) {
            for (Person person1 : ((MyPerson) person).getAcquaintance().values()) {
                if (hasPerson(person1)) {
                    sum += person.queryValue(person1);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            return 0;
        } else {
            return (age2Sum - 2 * ageSum * getAgeMean() +
                    people.size() * getAgeMean() * getAgeMean()) / people.size();
        }
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        ageSum -= person.getAge();
        age2Sum -= (person.getAge() * person.getAge());
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
