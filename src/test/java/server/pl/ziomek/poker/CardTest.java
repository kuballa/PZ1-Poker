package server.pl.ziomek.poker;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void suit() {
        int i = 0;
        Card cd1 = new Card(1,2);
        Card cd2 = new Card(2,2);
        Card cd3 = new Card(3,2);
        Card cd4 = new Card(4,2);
        assertEquals(1,cd1.suit());
        i++;
        assertEquals(2,cd2.suit());
        i++;
        assertEquals(3,cd3.suit());
        i++;
        assertEquals(4,cd4.suit());
        i++;
        assertEquals(4,i);
    }

    @Test
    public void suitStr() {
        int i = 0;
        Card cd1 = new Card(1,2);
        Card cd2 = new Card(2,2);
        Card cd3 = new Card(3,2);
        Card cd4 = new Card(4,2);
        assertEquals("d",cd1.suitStr());
        i++;
        assertEquals("c",cd2.suitStr());
        i++;
        assertEquals("h",cd3.suitStr());
        i++;
        assertEquals("s",cd4.suitStr());
        i++;
        assertEquals(4,i);
    }

    @Test
    public void rank() {
        int i = 0;
        Card cd1 = new Card(2,6);
        Card cd2 = new Card(3,2);
        Card cd3 = new Card(4,9);
        Card cd4 = new Card(1,11);
        assertEquals(6,cd1.rank());
        i++;
        assertEquals(2,cd2.rank());
        i++;
        assertEquals(9,cd3.rank());
        i++;
        assertEquals(11,cd4.rank());
        i++;
        assertEquals(4,i);
    }

    @Test
    public void rankStr() {
        int i = 0;
        Card cd1 = new Card(2,6);
        Card cd2 = new Card(3,2);
        Card cd3 = new Card(4,9);
        Card cd4 = new Card(1,11);
        assertEquals("6",cd1.rankStr());
        i++;
        assertEquals("2",cd2.rankStr());
        i++;
        assertEquals("9",cd3.rankStr());
        i++;
        assertEquals("J",cd4.rankStr());
        i++;
        assertEquals(4,i);
    }

    @Test
    public void testToString() {
        int i = 0;
        Card cd1 = new Card(2,6);
        Card cd2 = new Card(3,2);
        Card cd3 = new Card(4,9);
        Card cd4 = new Card(1,11);
        assertEquals("6c",cd1.toString());
        i++;
        assertEquals("2h",cd2.toString());
        i++;
        assertEquals("9s",cd3.toString());
        i++;
        assertEquals("Jd",cd4.toString());
        i++;
        assertEquals(4,i);
    }
}