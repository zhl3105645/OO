import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private ArrayList<Integer> emojiIdList = new ArrayList<>();
    private HashMap<Integer, Integer> emojiHeatList = new HashMap<>();

    private HashMap<Integer, Integer> father = new HashMap<>();//父节点
    private int count = 0;//并查集个数

    private HashMap<Integer, ArrayList<Edge>> edges = new HashMap<>();

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

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public HashMap<Integer, Integer> getEmojiHeatList() {
        return emojiHeatList;
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            father.put(person.getId(), person.getId());
            count++;
            edges.put(person.getId(), new ArrayList<>());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value);
            ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value);
            edges.get(id1).add(new Edge(id2, value));
            edges.get(id2).add(new Edge(id1, value));
            int root1 = find(id1);
            int root2 = find(id2);
            if (root1 != root2) {
                union(id1, id2);
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
            for (Integer key : people.keySet()) {
                if (compareName(id, people.get(key).getId()) > 0) {
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

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.getOrDefault(id, null);
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getSize();
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getValueSum();
        }
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getAgeMean();
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getAgeVar();
        }
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId((((EmojiMessage) message).getEmojiId()))) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(), message);
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (messages.get(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (messages.get(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else if (messages.get(id).getType() == 0 &&
                (getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2())) &&
                !getMessage(id).getPerson1().equals(getMessage(id).getPerson2())) {
            getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id));
            Message message = getMessage(id);
            if (message instanceof RedEnvelopeMessage) {
                message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                emojiHeatList.put(emojiId, emojiHeatList.get(emojiId) + 1);
            }
            messages.remove(id);
        } else if (messages.get(id).getType() == 1 &&
                (getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            Message message = getMessage(id);
            for (Integer key : people.keySet()) {
                if (message.getGroup().hasPerson(people.get(key))) {
                    people.get(key).addSocialValue(message.getSocialValue());
                }
            }
            if (message instanceof RedEnvelopeMessage) {
                int money = ((RedEnvelopeMessage) message).getMoney();
                int i = money / message.getGroup().getSize();
                for (Integer key : people.keySet()) {
                    if (message.getGroup().hasPerson(people.get(key))
                            && !message.getPerson1().equals(people.get(key))) {
                        people.get(key).addMoney(i);
                        message.getPerson1().addMoney(-i);
                    }
                }
            } else if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                emojiHeatList.put(emojiId, emojiHeatList.get(emojiId) + 1);
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiHeatList.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojiIdList.add(id);
            emojiHeatList.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getMoney();
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (emojiHeatList.containsKey(id)) {
            return emojiHeatList.get(id);
        } else {
            throw new MyEmojiIdNotFoundException(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        ArrayList<Integer> coldEmoji = new ArrayList<>();
        for (Integer emojiId : emojiIdList) {
            if (emojiHeatList.get(emojiId) < limit) {
                coldEmoji.add(emojiId);
            }
        }
        for (Integer emojiId : coldEmoji) {
            emojiIdList.remove(emojiId);
            emojiHeatList.remove(emojiId);
        }
        ArrayList<Message> coldMessage = new ArrayList<>();
        for (Integer i : messages.keySet()) {
            Message message = messages.get(i);
            if (message instanceof EmojiMessage) {
                if (!containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                    coldMessage.add(message);
                }
            }
        }
        for (Message message : coldMessage) {
            messages.remove(message.getId());
        }
        return emojiIdList.size();
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        } else {
            boolean flag = false;
            try {
                flag = isCircle(getMessage(id).getPerson1().getId(),
                        getMessage(id).getPerson2().getId());
            } catch (PersonIdNotFoundException e) {
                e.printStackTrace();
            }
            if (!flag) {
                return -1;
            } else {
                Message message = getMessage(id);
                message.getPerson1().addSocialValue(message.getSocialValue());
                message.getPerson2().addSocialValue(message.getSocialValue());
                message.getPerson2().getMessages().add(0, message);
                if (message instanceof RedEnvelopeMessage) {
                    message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                    message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
                } else if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    emojiHeatList.put(emojiId, emojiHeatList.get(emojiId) + 1);
                }
                //最短路径
                int min = minPath(message.getPerson1().getId(), message.getPerson2().getId());
                messages.remove(id);
                return min;
            }
        }
    }

    public int minPath(int id1, int id2) {
        HashMap<Integer, Boolean> visit = new HashMap<>();
        HashMap<Integer, Integer> dis = new HashMap<>();
        final int inf = 0x0fffffff;
        for (Integer integer : people.keySet()) {
            visit.put(integer, false);
            dis.put(integer, inf);
        }
        PriorityQueue<Edge> queue = new PriorityQueue<>();
        queue.add(new Edge(id1, 0));
        dis.put(id1, 0);
        while (!queue.isEmpty()) {
            Edge tmp = queue.poll();
            int u = tmp.getTo();
            if (dis.get(u) < tmp.getValue()) {
                continue;
            }
            if (visit.get(u)) {
                continue;
            }
            visit.put(u, true);
            for (int i = 0; i < edges.get(u).size(); i++) {
                int next = edges.get(u).get(i).getTo();
                int value = edges.get(u).get(i).getValue();
                if (!visit.get(next) && dis.get(next) > dis.get(u) + value) {
                    dis.put(next, dis.get(u) + value);
                    queue.add(new Edge(next, dis.get(next)));
                }
            }
        }
        return dis.get(id2);
    }
}

