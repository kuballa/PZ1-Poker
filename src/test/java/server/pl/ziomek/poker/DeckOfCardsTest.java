package server.pl.ziomek.poker;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeckOfCardsTest {

    @Test
    public void testShuffle() {
        DeckOfCards dc = new DeckOfCards();
        DeckOfCards dc1 = new DeckOfCards();
        assertEquals(dc.toString(),dc1.toString());
        dc.shuffle(10);
        dc1.shuffle(10);
        assertNotEquals(dc.toString(),dc1.toString());
    }

    @Test
    public void testDeal() {
        DeckOfCards dc = new DeckOfCards();
        DeckOfCards dc1 = new DeckOfCards();

        assertEquals(dc.deal().toString(),dc1.deal().toString());
        dc.shuffle(100);
        dc1.shuffle(100);

        assertNotEquals(dc.deal().toString(),dc1.deal().toString());
        dc.shuffle(100);
        dc1.shuffle(100);

        assertNotEquals(dc.deal().toString(),dc1.deal().toString());
    }

    @Test
    public void testToString() {
        DeckOfCards dc = new DeckOfCards();
        DeckOfCards dc1 = new DeckOfCards();
        assertEquals(dc.toString(),dc1.toString());
        dc.shuffle(10);
        dc1.shuffle(10);
        for(int i = 0; i < 5; i++){
            assertNotEquals(dc.toString(),dc1.toString());
            dc.shuffle(10);
            dc1.shuffle(10);
        }
    }
}