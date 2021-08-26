import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private ArrayList<Person> people = new ArrayList<>();
    private HashMap<Integer, Integer> father = new HashMap<>();//父节点
    private int count = 0;//并查集个数

    public MyNetwork() {
    }

    int find(Integer p) { //寻找根节点
        int tmp = p;
        while (true) {
            if (father.get(tmp) == tmp) {
                return tmp;
            } else {
                tmp = father.get(tmp);
            }
        }
    }

    void union(int p, int q) { //合并两个并查集
        int proot = find(p);
        int qroot = find(q);
        if (proot == qroot) {
            return;
        }
        father.put(proot, qroot);
        count--;
    }

    @Override
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            for (Person person : people) {
                if (person.getId() == id) {
                    return person;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person value : people) {
            if (value.equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.add(person);
        father.put(person.getId(), person.getId());
        count++;
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            if (id1 != id2) {
                ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value);
                ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value);
                int root1 = find(id1);
                int root2 = find(id2);
                if (root1 != root2) {
                    union(id1, id2);
                }
            }
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            return getPerson(id1).getName().compareTo(getPerson(id2).getName());
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            int num = 1;
            for (Person person : people) {
                if (compareName(id, person.getId()) > 0) {
                    num++;
                }
            }
            return num;
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            return find(id1) == find(id2);
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }

    @Override
    public int queryBlockSum() {
        return count;
    }

    boolean bfs(Person p1, Person p2, HashMap<Person, Boolean> visit) {
        if (p1.isLinked(p2)) {
            return true;
        } else {
            ArrayList<Person> acquaintance = ((MyPerson) p1).getAcquaintance();
            for (Person person : acquaintance) {
                if (!visit.get(person)) {
                    visit.put(person, true);
                    if (bfs(person, p2, visit)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
