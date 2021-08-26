import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance = new HashMap<>();
    private HashMap<Integer, Integer> value = new HashMap<>();
    private int socialValue;
    private ArrayList<Message> messages = new ArrayList<>();
    private int money;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return (((Person) obj).getId() == id);
        } else {
            return false;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        } else {
            return acquaintance.containsKey(person.getId());
        }
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        this.socialValue = this.socialValue + num;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> result = new ArrayList<>();
        for (int i = 0; i < 4 && i < messages.size(); i++) {
            result.add(messages.get(i));
        }
        return result;
    }

    @Override
    public void addMoney(int num) {
        money = money + num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addAcquaintance(Person person, int value0) {
        acquaintance.put(person.getId(), person);
        value.put(person.getId(), value0);
    }
}
