package server.pl.ziomek.poker;

import org.junit.Test;

import static org.junit.Assert.*;

public class PokerTest {

    @Test
    public void valueHand() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(3,10);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,12);
        cd[3] = new Card(3,13);
        cd[4] = new Card(3,14);

        System.out.println(pk.valueHand(cd));

    }

    @Test
    public void valueStraightFlush() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(3,10);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,12);
        cd[3] = new Card(3,13);
        cd[4] = new Card(3,14);

        assertEquals(pk.valueStraightFlush(cd),pk.valueStraightFlush(cd));
    }

    @Test
    public void valueFlush() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(3,3);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,5);
        cd[3] = new Card(3,13);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueFlush(cd),pk.valueHand(cd));
    }

    @Test
    public void valueStraight() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,10);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,12);
        cd[3] = new Card(4,13);
        cd[4] = new Card(2,14);

        assertEquals(pk.valueStraight(cd),pk.valueHand(cd));
    }

    @Test
    public void valueFourOfAKind() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,3);
        cd[3] = new Card(3,3);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueFourOfAKind(cd),pk.valueHand(cd));
    }

    @Test
    public void valueFullHouse() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,3);
        cd[3] = new Card(2,7);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueFullHouse(cd),pk.valueHand(cd));
    }

    @Test
    public void valueSet() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,3);
        cd[3] = new Card(3,9);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueSet(cd),pk.valueHand(cd));
    }

    @Test
    public void valueTwoPairs() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,7);
        cd[3] = new Card(3,9);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueTwoPairs(cd),pk.valueHand(cd));
    }

    @Test
    public void valueOnePair() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,9);
        cd[3] = new Card(3,2);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueOnePair(cd),pk.valueHand(cd));
    }

    @Test
    public void valueHighCard() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,8);
        cd[1] = new Card(2,5);
        cd[2] = new Card(4,11);
        cd[3] = new Card(3,2);
        cd[4] = new Card(3,7);

        assertEquals(pk.valueHighCard(cd),pk.valueHand(cd));
    }

    @Test
    public void is4s() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,3);
        cd[3] = new Card(3,3);
        cd[4] = new Card(3,7);

        assertEquals(true,pk.is4s(cd));
    }

    @Test
    public void isFullHouse() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,13);
        cd[1] = new Card(2,13);
        cd[2] = new Card(4,14);
        cd[3] = new Card(3,14);
        cd[4] = new Card(3,13);

        assertEquals(true,pk.isFullHouse(cd));
    }

    @Test
    public void is3s() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,3);
        cd[3] = new Card(3,9);
        cd[4] = new Card(3,7);

        assertEquals(true,pk.is3s(cd));
    }

    @Test
    public void is22s() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,7);
        cd[3] = new Card(3,2);
        cd[4] = new Card(3,7);

        assertEquals(true,pk.is22s(cd));
    }

    @Test
    public void is2s() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,3);
        cd[1] = new Card(2,3);
        cd[2] = new Card(4,5);
        cd[3] = new Card(3,11);
        cd[4] = new Card(3,7);

        assertEquals(true,pk.is2s(cd));
    }

    @Test
    public void isFlush() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(3,3);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,5);
        cd[3] = new Card(3,13);
        cd[4] = new Card(3,7);

        assertEquals(true,pk.isFlush(cd));
    }

    @Test
    public void isStraight() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,10);
        cd[1] = new Card(3,11);
        cd[2] = new Card(3,12);
        cd[3] = new Card(4,13);
        cd[4] = new Card(2,14);

        assertEquals(true,pk.isStraight(cd));
    }
///////////////
    @Test
    public void sortByRank() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(1,13);
        cd[1] = new Card(3,5);
        cd[2] = new Card(3,2);
        cd[3] = new Card(4,4);
        cd[4] = new Card(2,12);
        Card[] cd1 = new Card[5];

        for(int i = 0; i < 5; i++){
            cd1[i] = cd[i];
        }

        pk.sortByRank(cd);
        for(int i = 0; i < 5; i++){
            assertNotEquals(cd1[i].rank(), cd[i].rank());
        }
    }

    @Test
    public void sortBySuit() {
        Card[] cd = new Card[5];
        Poker pk = new Poker();
        cd[0] = new Card(4,13);
        cd[1] = new Card(4,5);
        cd[2] = new Card(1,2);
        cd[3] = new Card(2,4);
        cd[4] = new Card(3,12);
        Card[] cd1 = new Card[5];

        for(int i = 0; i < 5; i++){
            cd1[i] = cd[i];
        }

        pk.sortBySuit(cd);
        for(int i = 0; i < 5; i++){
            assertNotEquals(cd1[i].suit(), cd[i].suit());
        }
    }
}