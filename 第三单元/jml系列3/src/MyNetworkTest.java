import com.oocourse.spec3.main.Message;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MyNetworkTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Rule
    public ExpectedException ee = ExpectedException.none();

    @org.junit.Test
    public void testAll() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "A", 10);
        MyPerson person2 = new MyPerson(2, "B", 10);
        MyPerson person3 = new MyPerson(3, "C", 10);
        MyPerson person4 = new MyPerson(4, "D", 10);
        MyPerson person5 = new MyPerson(5, "E", 10);
        MyPerson person6 = new MyPerson(6, "F", 10);
        MyPerson person7 = new MyPerson(7, "G", 10);
        MyPerson person8 = new MyPerson(8, "H", 10);

        myNetwork.addPerson(person1);
        myNetwork.addPerson(person2);
        myNetwork.addPerson(person3);
        myNetwork.addPerson(person4);
        myNetwork.addPerson(person5);
        myNetwork.addPerson(person6);
        myNetwork.addPerson(person7);
        myNetwork.addPerson(person8);

        myNetwork.addRelation(1, 2, 10);
        myNetwork.addRelation(1, 6, 30);
        myNetwork.addRelation(2, 3, 20);
        myNetwork.addRelation(2, 4, 30);
        myNetwork.addRelation(4, 6, 40);
        myNetwork.addRelation(4, 5, 5);
        myNetwork.addRelation(4, 8, 30);
        myNetwork.addRelation(5, 6, 1);
        myNetwork.addRelation(5, 7, 10);
        myNetwork.addRelation(7, 8, 20);

        myNetwork.storeEmojiId(1);
        myNetwork.storeEmojiId(2);
        myNetwork.storeEmojiId(3);
        myNetwork.storeEmojiId(4);

        MyMessage message1 = new MyMessage(1, 10, person1, person4);
        MyMessage message2 = new MyEmojiMessage(2, 1, person1, person2);
        MyMessage message3 = new MyEmojiMessage(3, 2, person2, person3);
        MyMessage message4 = new MyEmojiMessage(4, 3, person1, person6);
        MyMessage message5 = new MyEmojiMessage(5, 2, person2, person4);
        MyMessage message6 = new MyEmojiMessage(6, 4, person4, person5);
        MyMessage message7 = new MyEmojiMessage(7, 4, person4, person5);


        myNetwork.addMessage(message1);
        myNetwork.addMessage(message2);
        myNetwork.addMessage(message3);
        myNetwork.addMessage(message4);
        myNetwork.addMessage(message5);
        myNetwork.addMessage(message6);
        myNetwork.addMessage(message7);

        myNetwork.sendMessage(2);
        myNetwork.sendMessage(3);
        myNetwork.sendMessage(4);
        myNetwork.sendMessage(5);
        myNetwork.deleteColdEmoji(2);
    }

    @org.junit.Test
    public void testContains() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertFalse(myNetwork.contains(1));
        myNetwork.addPerson(new MyPerson(1, "jack", 10));
        assertTrue(myNetwork.contains(1));
        System.out.println("contain is ok!");
    }

    @org.junit.Test
    public void testGetPerson() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertNull(myNetwork.getPerson(1));
        MyPerson myPerson = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(myPerson);
        assertEquals(myPerson, myNetwork.getPerson(1));
        System.out.println("getPerson is ok!");
    }

    @org.junit.Test
    public void testAddPerson() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person);
        ee.expect(MyEqualPersonIdException.class);
        myNetwork.addPerson(person);
    }

    @org.junit.Test
    public void testAddRelationCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.addRelation(1, 2, 100);
    }

    @org.junit.Test
    public void testAddRelationCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.addRelation(1, 2, 100);
    }

    @org.junit.Test
    public void testAddRelationCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);

        myNetwork.addRelation(1, 2, 100);
    }

    @org.junit.Test
    public void testAddRelationCase4() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 12);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);

        ee.expect(MyEqualRelationException.class);
        myNetwork.addRelation(1, 2, 100);
    }

    @org.junit.Test
    public void testQueryValueCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.queryValue(1, 2);
    }

    @org.junit.Test
    public void testQueryValueCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);

        myNetwork.queryValue(1, 1);
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.queryValue(1, 2);
    }

    @org.junit.Test
    public void testQueryValueCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);

        myNetwork.queryValue(1, 1);
        myNetwork.queryValue(2, 2);
        ee.expect(MyRelationNotFoundException.class);
        myNetwork.queryValue(1, 2);
    }

    @org.junit.Test
    public void testQueryValueCase4() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);

        myNetwork.queryValue(1, 1);
        myNetwork.queryValue(2, 2);
        myNetwork.queryValue(1, 2);
    }

    @org.junit.Test
    public void testCompareNameCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.compareName(1, 2);
    }

    @org.junit.Test
    public void testCompareNameCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);

        myNetwork.compareName(1, 1);
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.compareName(1, 2);
    }

    @org.junit.Test
    public void testCompareNameCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);

        myNetwork.compareName(1, 1);
        myNetwork.compareName(2, 2);
        myNetwork.compareName(1, 2);
    }

    @org.junit.Test
    public void testQueryPeopleSum() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertEquals(0, myNetwork.queryPeopleSum());
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        assertEquals(1, myNetwork.queryPeopleSum());
        MyPerson person2 = new MyPerson(2, "Tim", 15);
        myNetwork.addPerson(person2);
        assertEquals(2, myNetwork.queryPeopleSum());
    }

    @org.junit.Test
    public void testQueryNameRankCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.queryNameRank(1);
    }

    @org.junit.Test
    public void testQueryNameRankCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        assertEquals(1, myNetwork.queryNameRank(1));
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);
        assertEquals(1, myNetwork.queryNameRank(1));
        assertEquals(2, myNetwork.queryNameRank(2));
        MyPerson person3 = new MyPerson(3, "Kate", 13);
        myNetwork.addPerson(person3);
        assertEquals(1, myNetwork.queryNameRank(1));
        assertEquals(2, myNetwork.queryNameRank(3));
        assertEquals(3, myNetwork.queryNameRank(2));
    }

    @org.junit.Test
    public void testIsCircleCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.isCircle(1, 2);
    }

    @org.junit.Test
    public void testIsCircleCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);

        assertTrue(myNetwork.isCircle(1, 1));
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.isCircle(1, 2);
    }

    @org.junit.Test
    public void testIsCircleCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);

        assertTrue(myNetwork.isCircle(1, 1));
        assertTrue(myNetwork.isCircle(2, 2));
        assertFalse(myNetwork.isCircle(1, 2));
        myNetwork.addRelation(1, 2, 100);
        assertTrue(myNetwork.isCircle(1, 2));
    }

    @org.junit.Test
    public void testQueryBlockSum() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        MyPerson person1 = new MyPerson(1, "Jack", 10);
        myNetwork.addPerson(person1);
        assertEquals(1, myNetwork.queryBlockSum());

        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);
        assertEquals(2, myNetwork.queryBlockSum());
        myNetwork.addRelation(1, 2, 100);
        assertEquals(1, myNetwork.queryBlockSum());
    }

    @org.junit.Test
    public void testAddGroup() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group = new MyGroup(1);
        myNetwork.addGroup(group);
        assertTrue(myNetwork.getGroups().containsKey(1));

        ee.expect(MyEqualGroupIdException.class);
        myNetwork.addGroup(group);
    }

    @org.junit.Test
    public void testGetGroup() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertNull(myNetwork.getGroup(1));
        MyGroup group = new MyGroup(1);
        myNetwork.addGroup(group);
        assertEquals(group, myNetwork.getGroup(1));
        assertNull(myNetwork.getGroup(2));
    }

    @org.junit.Test
    public void testAddToGroupCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();

        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.addToGroup(1, 2);
    }

    @org.junit.Test
    public void testAddToGroupCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup myGroup = new MyGroup(2);
        myNetwork.addGroup(myGroup);

        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.addToGroup(1, 2);
    }

    @org.junit.Test
    public void testAddToGroupCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup myGroup = new MyGroup(2);
        myNetwork.addGroup(myGroup);
        MyPerson myPerson = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(myPerson);
        myNetwork.addToGroup(1, 2);
        assertTrue(myGroup.hasPerson(myPerson));
    }

    @org.junit.Test
    public void testAddToGroupCase4() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup myGroup = new MyGroup(2);
        myNetwork.addGroup(myGroup);
        MyPerson myPerson = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(myPerson);
        myNetwork.addToGroup(1, 2);
        assertTrue(myGroup.hasPerson(myPerson));

        ee.expect(MyEqualPersonIdException.class);
        myNetwork.addToGroup(1, 2);
    }

    @org.junit.Test
    public void testAddToGroupCase5() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        for (int i = 0; i < 1111; i++) {
            MyPerson myPerson = new MyPerson(i, "Same", 10);
            myNetwork.addPerson(myPerson);
            myNetwork.addToGroup(i, 1);
            assertTrue(myGroup.hasPerson(myPerson));
        }
        MyPerson myPerson = new MyPerson(1111, "Same", 10);
        myNetwork.addPerson(myPerson);
        myNetwork.addToGroup(1111, 1);
        assertFalse(myGroup.hasPerson(myPerson));
    }

    @org.junit.Test
    public void testQueryGroupSum() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertEquals(0, myNetwork.queryGroupSum());
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        assertEquals(1, myNetwork.queryGroupSum());
        MyGroup group2 = new MyGroup(2);
        myNetwork.addGroup(group2);
        assertEquals(2, myNetwork.queryGroupSum());
    }

    @org.junit.Test
    public void testQueryGroupPeopleSum() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        myNetwork.addToGroup(1, 1);
        assertEquals(1, myNetwork.queryGroupPeopleSum(1));
        MyPerson person2 = new MyPerson(2, "jack", 10);
        myNetwork.addPerson(person2);
        myNetwork.addToGroup(2, 1);
        assertEquals(2, myNetwork.queryGroupPeopleSum(1));
        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.queryGroupPeopleSum(2);
    }

    @org.junit.Test
    public void testQueryGroupValueSum() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 10);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        assertEquals(0, myNetwork.queryGroupValueSum(1));
        myNetwork.addToGroup(1, 1);
        assertEquals(0, myNetwork.queryGroupValueSum(1));
        myNetwork.addToGroup(2, 1);
        assertEquals(200, myNetwork.queryGroupValueSum(1));
        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.queryGroupValueSum(2);
    }

    @org.junit.Test
    public void testQueryGroupAgeMean() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        assertEquals(0, myNetwork.queryGroupAgeMean(1));
        myNetwork.addToGroup(1, 1);
        assertEquals(10, myNetwork.queryGroupAgeMean(1));
        myNetwork.addToGroup(2, 1);
        assertEquals(12, myNetwork.queryGroupAgeMean(1));
        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.queryGroupAgeMean(2);
    }

    @org.junit.Test
    public void testQueryGroupAgeVar() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        assertEquals(0, myNetwork.queryGroupAgeVar(1));
        myNetwork.addToGroup(1, 1);
        assertEquals(0, myNetwork.queryGroupAgeVar(1));
        myNetwork.addToGroup(2, 1);
        assertEquals(6, myNetwork.queryGroupAgeVar(1));
        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.queryGroupAgeVar(2);
    }

    @org.junit.Test
    public void testDelFromGroupCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        ee.expect(MyGroupIdNotFoundException.class);
        myNetwork.delFromGroup(1, 1);
    }

    @org.junit.Test
    public void testDelFromGroupCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.delFromGroup(1, 1);
    }

    @org.junit.Test
    public void testDelFromGroupCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        ee.expect(MyEqualPersonIdException.class);
        myNetwork.delFromGroup(1, 1);
    }

    @org.junit.Test
    public void testDelFromGroupCase4() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyGroup group1 = new MyGroup(1);
        myNetwork.addGroup(group1);
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        myNetwork.addToGroup(1, 1);
        assertTrue(group1.hasPerson(person1));
        myNetwork.delFromGroup(1, 1);
        assertFalse(group1.hasPerson(person1));
    }

    @org.junit.Test
    public void testContainsMessage() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        assertFalse(myNetwork.containsMessage(1));
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        assertTrue(myNetwork.containsMessage(1));
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        MyMessage message2 = new MyMessage(2, 5, person2, myGroup);
        myNetwork.addMessage(message2);
        assertTrue(myNetwork.containsMessage(2));
    }

    @org.junit.Test
    public void testAddMessageCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyMessage message1 = new MyMessage(1, 10, person1, person1);
        ee.expect(MyEqualPersonIdException.class);
        myNetwork.addMessage(message1);
    }

    @org.junit.Test
    public void testAddMessageCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        assertFalse(myNetwork.containsMessage(1));
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        assertTrue(myNetwork.containsMessage(1));
        MyMessage message2 = new MyMessage(2, 5, person2, myGroup);
        myNetwork.addMessage(message2);
        assertTrue(myNetwork.containsMessage(2));
        ee.expect(MyEqualMessageIdException.class);
        myNetwork.addMessage(message2);
    }

    @org.junit.Test
    public void testAddMessageCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "Tom", 15);
        myNetwork.addPerson(person2);
        MyMessage message1 = new MyEmojiMessage(1, 10, person1, person1);
        ee.expect(MyEmojiIdNotFoundException.class);
        myNetwork.addMessage(message1);
    }

    @org.junit.Test
    public void testGetMessage() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        assertNull(myNetwork.getMessage(1));
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        MyMessage message2 = new MyMessage(2, 5, person2, myGroup);
        myNetwork.addMessage(message2);
        assertEquals(message1, myNetwork.getMessage(1));
        assertEquals(message2, myNetwork.getMessage(2));
    }

    @org.junit.Test
    public void testSendMessageCase1() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        ee.expect(MyMessageIdNotFoundException.class);
        myNetwork.sendMessage(1);
    }

    @org.junit.Test
    public void testSendMessageCase2() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        ee.expect(MyRelationNotFoundException.class);
        myNetwork.sendMessage(1);
    }

    @org.junit.Test
    public void testSendMessageCase3() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        MyMessage message2 = new MyMessage(2, 5, person1, myGroup);
        myNetwork.addMessage(message2);
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.sendMessage(2);
    }

    @org.junit.Test
    public void testSendMessageCase4() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        myNetwork.addRelation(1, 2, 100);
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        myNetwork.sendMessage(1);
        assertEquals(10, person1.getSocialValue());
        assertEquals(10, person2.getSocialValue());
        assertFalse(myNetwork.containsMessage(1));
        assertTrue(person2.getMessages().contains(message1));
        MyMessage message2 = new MyRedEnvelopeMessage(2, 20, person1, person2);
        myNetwork.addMessage(message2);
        myNetwork.sendMessage(2);
        assertEquals(-20, person1.getMoney());
        assertEquals(20, person2.getMoney());
        assertEquals(110, person1.getSocialValue());
        assertEquals(110, person2.getSocialValue());
        assertFalse(myNetwork.containsMessage(2));
        assertTrue(person2.getMessages().contains(message2));
        myNetwork.storeEmojiId(1);
        MyMessage message3 = new MyEmojiMessage(3, 1, person2, person1);
        myNetwork.addMessage(message3);
        myNetwork.sendMessage(3);
        assertEquals(111, person1.getSocialValue());
        assertEquals(111, person2.getSocialValue());
        assertFalse(myNetwork.containsMessage(3));
        assertTrue(person1.getMessages().contains(message3));
        assertEquals(1, (long)myNetwork.getEmojiHeatList().get(1));
    }

    @org.junit.Test
    public void testSendMessageCase5() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        myNetwork.addToGroup(1, 1);
        MyMessage message2 = new MyMessage(2, 5, person1, myGroup);
        myNetwork.addMessage(message2);
        myNetwork.sendMessage(2);
        assertEquals(5, person1.getSocialValue());
        assertFalse(myNetwork.containsMessage(2));
    }

    @org.junit.Test
    public void testQuerySocialValue() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        MyPerson person3 = new MyPerson(3, "jack", 20);
        myNetwork.addPerson(person3);
        myNetwork.addRelation(1, 2, 100);
        myNetwork.addRelation(1, 3, 100);
        MyGroup myGroup = new MyGroup(1);
        myNetwork.addGroup(myGroup);
        myNetwork.addToGroup(1, 1);
        myNetwork.addToGroup(2, 1);
        myNetwork.addToGroup(3, 1);
        MyMessage message1 = new MyMessage(1, 10, person1, person2);
        myNetwork.addMessage(message1);
        MyMessage message2 = new MyMessage(2, 5, person1, myGroup);
        myNetwork.addMessage(message2);
        MyMessage message3 = new MyMessage(3, 20, person3, person1);
        myNetwork.addMessage(message3);
        myNetwork.sendMessage(1);
        assertEquals(10, myNetwork.querySocialValue(1));
        assertEquals(10, myNetwork.querySocialValue(2));
        assertEquals(0, myNetwork.querySocialValue(3));
        myNetwork.sendMessage(2);
        assertEquals(15, myNetwork.querySocialValue(1));
        assertEquals(15, myNetwork.querySocialValue(2));
        assertEquals(5, myNetwork.querySocialValue(3));
        myNetwork.sendMessage(3);
        assertEquals(35, myNetwork.querySocialValue(1));
        assertEquals(15, myNetwork.querySocialValue(2));
        assertEquals(25, myNetwork.querySocialValue(3));
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.querySocialValue(4);
    }

    @org.junit.Test
    public void testQueryReceivedMessages() throws Exception {
        MyNetwork myNetwork = new MyNetwork();
        MyPerson person1 = new MyPerson(1, "jack", 10);
        myNetwork.addPerson(person1);
        MyPerson person2 = new MyPerson(2, "jack", 15);
        myNetwork.addPerson(person2);
        MyPerson person3 = new MyPerson(3, "jack", 20);
        myNetwork.addPerson(person3);
        MyPerson person4 = new MyPerson(4, "jack", 25);
        myNetwork.addPerson(person4);
        MyPerson person5 = new MyPerson(5, "jack", 30);
        myNetwork.addPerson(person5);
        MyPerson person6 = new MyPerson(6, "jack", 30);
        myNetwork.addPerson(person6);

        myNetwork.addRelation(1, 2, 10);
        myNetwork.addRelation(1, 3, 10);
        myNetwork.addRelation(1, 4, 10);
        myNetwork.addRelation(1, 5, 10);
        myNetwork.addRelation(1, 6, 10);

        MyMessage message1 = new MyMessage(1, 5, person2, person1);
        MyMessage message2 = new MyMessage(2, 5, person3, person1);
        MyMessage message3 = new MyMessage(3, 5, person4, person1);
        MyMessage message4 = new MyMessage(4, 5, person5, person1);
        MyMessage message5 = new MyMessage(5, 5, person6, person1);
        myNetwork.addMessage(message1);
        myNetwork.addMessage(message2);
        myNetwork.addMessage(message3);
        myNetwork.addMessage(message4);
        myNetwork.addMessage(message5);

        List<Message> result = new ArrayList<>();
        myNetwork.sendMessage(1);
        result.add(0, message1);
        assertEquals(result, myNetwork.queryReceivedMessages(1));
        myNetwork.sendMessage(2);
        result.add(0, message2);
        assertEquals(result, myNetwork.queryReceivedMessages(1));
        myNetwork.sendMessage(3);
        result.add(0, message3);
        assertEquals(result, myNetwork.queryReceivedMessages(1));
        myNetwork.sendMessage(4);
        result.add(0, message4);
        assertEquals(result, myNetwork.queryReceivedMessages(1));
        myNetwork.sendMessage(5);
        result.add(0, message5);
        result.remove(4);
        assertEquals(result, myNetwork.queryReceivedMessages(1));
        ee.expect(MyPersonIdNotFoundException.class);
        myNetwork.queryReceivedMessages(10);
    }
}