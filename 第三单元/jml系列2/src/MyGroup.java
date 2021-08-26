import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people = new HashMap<>();
    private int valueSum;
    private int ageMean;

    public MyGroup(int id) {
        this.id = id;
        this.valueSum = 0;
        this.ageMean = 0;
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
        for (Integer key : people.keySet()) {
            if (people.get(key).isLinked(person)) {
                valueSum = valueSum + 2 * people.get(key).queryValue(person);
            }
        }
        people.put(person.getId(), person);
        getAgeMean(); //update the ageMean
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person person : people.values()) {
            for (Person person1 : people.values()) {
                if (person.isLinked(person1)) {
                    sum = sum + person.queryValue(person1);
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            this.ageMean = 0;
            return 0;
        }
        int ageSum = 0;
        for (Integer key : people.keySet()) {
            ageSum += people.get(key).getAge();
        }
        this.ageMean = ageSum / people.size();
        return ageMean;
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            return 0;
        }
        int tmp = 0;
        for (Integer key : people.keySet()) {
            tmp += (people.get(key).getAge() - ageMean) * (people.get(key).getAge() - ageMean);
        }
        return (tmp / people.size());
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        for (Integer key : people.keySet()) {
            if (people.get(key).isLinked(person)) {
                valueSum = valueSum - 2 * people.get(key).queryValue(person);
            }
        }
        getAgeMean();//update the ageMean
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
