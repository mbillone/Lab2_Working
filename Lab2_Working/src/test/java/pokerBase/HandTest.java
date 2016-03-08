package pokerBase;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class HandTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	private Hand SetHand(ArrayList<Card> setCards, Hand h) {
		Object t = null;

		try {
			// Load the Class into 'c'
			Class<?> c = Class.forName("pokerBase.Hand");
			// Create a new instance 't' from the no-arg Deck constructor
			t = c.newInstance();
			// Load 'msetCardsInHand' with the 'Draw' method (no args);
			Method msetCardsInHand = c.getDeclaredMethod("setCardsInHand", new Class[] { ArrayList.class });
			// Change the visibility of 'setCardsInHand' to true *Good Grief!*
			msetCardsInHand.setAccessible(true);
			// invoke 'msetCardsInHand'
			Object oDraw = msetCardsInHand.invoke(t, setCards);

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		h = (Hand) t;
		return h;

	}

	/**
	 * This test checks to see if a HandException is throw if the hand only has
	 * two cards.
	 * 
	 * @throws Exception
	 */
	@Test(expected = HandException.class)
	public void TestEvalShortHand() throws Exception {

		Hand h = new Hand();

		ArrayList<Card> ShortHand = new ArrayList<Card>();
		ShortHand.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ShortHand.add(new Card(eSuit.CLUBS, eRank.ACE, 0));

		h = SetHand(ShortHand, h);

		// This statement should throw a HandException
		h = Hand.EvaluateHand(h);
	}

	@Test
	public void TestRoyalFlush() {

		ArrayList<Card> RoyalFlush = new ArrayList<Card>();
		RoyalFlush.add(new Card(eSuit.HEARTS, eRank.ACE, 0));
		RoyalFlush.add(new Card(eSuit.HEARTS, eRank.KING, 0));
		RoyalFlush.add(new Card(eSuit.HEARTS, eRank.QUEEN, 0));
		RoyalFlush.add(new Card(eSuit.HEARTS, eRank.JACK, 0));
		RoyalFlush.add(new Card(eSuit.HEARTS, eRank.TEN, 0));

		Hand h = new Hand();
		h = SetHand(RoyalFlush, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandRoyalFlush(h, hs);
		boolean bExpectedHandPair = true;

		// Checks for Royal Flush?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// HIGH = Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());

	}

	@Test
	public void TestStraightFlush() {

		ArrayList<Card> StraightFlush = new ArrayList<Card>();
		StraightFlush.add(new Card(eSuit.HEARTS, eRank.SIX, 0));
		StraightFlush.add(new Card(eSuit.HEARTS, eRank.FIVE, 0));
		StraightFlush.add(new Card(eSuit.HEARTS, eRank.FOUR, 0));
		StraightFlush.add(new Card(eSuit.HEARTS, eRank.THREE, 0));
		StraightFlush.add(new Card(eSuit.HEARTS, eRank.TWO, 0));

		Hand h = new Hand();
		h = SetHand(StraightFlush, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandStraightFlush(h, hs);
		boolean bExpectedHandPair = true;

		// Checks for Straight Flush?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// Checks if high high = Ten?
		assertEquals(hs.getHiHand(), eRank.TEN.getiRankNbr());
	}

	@Test
	public void TestFourOfAKind() {

		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));

		Hand h = new Hand();
		h = SetHand(FourOfAKind, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;

		// Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind, bExpectedIsHandFourOfAKind);
		// Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// FOAK has one kicker. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// FOAK has one kicker. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestFourOfAKind1() {

		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));

		Hand h = new Hand();
		h = SetHand(FourOfAKind, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;

		// Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind, bExpectedIsHandFourOfAKind);
		// Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// FOAK has one kicker. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// FOAK has one kicker. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestFourOfAKindEval() {

		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FourOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));

		Hand h = new Hand();
		h = SetHand(FourOfAKind, h);

		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {
			e.printStackTrace();
			fail("TestFourOfAKindEval failed");
		}
		HandScore hs = h.getHandScore();
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;

		// Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind, bExpectedIsHandFourOfAKind);
		// Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// FOAK has one kicker. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// FOAK has one kicker. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestFullHouse() {

		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));

		Hand h = new Hand();
		h = SetHand(FullHouse, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandFullHouse = Hand.isHandFullHouse(h, hs);
		boolean bExpectedIsHandFullHouse = true;

		// Checks for full house?
		assertEquals(bActualIsHandFullHouse, bExpectedIsHandFullHouse);
		// High Card
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// kickers suit
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers ranks
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		// kickers suit
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers rank
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestFullHouse1() {

		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));

		Hand h = new Hand();
		h = SetHand(FullHouse, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandFullHouse = Hand.isHandFullHouse(h, hs);
		boolean bExpectedIsHandFullHouse = true;

		// Checks for full house?
		assertEquals(bActualIsHandFullHouse, bExpectedIsHandFullHouse);
		// High Card
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// kickers suit
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers ranks
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		// kickers suit
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers rank
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestHandFlush() {

		ArrayList<Card> HandFlush = new ArrayList<Card>();
		HandFlush.add(new Card(eSuit.CLUBS, eRank.FOUR, 0));
		HandFlush.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		HandFlush.add(new Card(eSuit.CLUBS, eRank.SEVEN, 0));
		HandFlush.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		HandFlush.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		

		Hand h = new Hand();
		h = SetHand(HandFlush, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandFlush(h, hs);
		boolean bExpectedHandPair = true;

		// Checks for Flush?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// High Card
		assertEquals(hs.getHiHand(), eRank.QUEEN.getiRankNbr());
		
	}
	
	@Test
	public void TestStraight(){
		
		ArrayList<Card> Straight = new ArrayList<Card>();
		Straight.add(new Card(eSuit.HEARTS, eRank.NINE, 0));
		Straight.add(new Card(eSuit.SPADES, eRank.EIGHT, 0));
		Straight.add(new Card(eSuit.DIAMONDS, eRank.SEVEN, 0));
		Straight.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		Straight.add(new Card(eSuit.HEARTS, eRank.FIVE, 0));

		Hand h = new Hand();
		h = SetHand(Straight, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandStraight(h, hs);
		boolean bExpectedHandPair = true;

		// Check for Straight?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// HIGH CARD
		assertEquals(hs.getHiHand(), eRank.NINE.getiRankNbr());
		
	}

	@Test
	public void TestThreeOfAKind() {

		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);
		// Was the three of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a Jack?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestThreeOfAKind1() {

		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);
		// Was the three of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a Jack?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestThreeOfAKind2() {

		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));

		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();

		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);
		// Was the three of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a Jack?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestThreeOfAKindEval() {

		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);

		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {
			e.printStackTrace();
			fail("TestThreeOfAKindEval failed");
		}
		HandScore hs = h.getHandScore();

		boolean bActualHandIsThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualHandIsThreeOfAKind, bExpectedIsHandThreeOfAKind);
		// Was the three of a kind an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// TOAK has two kickers. Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// TOAK has two kickers. Was it a Jack?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestTwoPair() {

		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPair.add(new Card(eSuit.SPADES, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		TwoPair.add(new Card(eSuit.SPADES, eRank.THREE, 0));

		Hand h = new Hand();
		h = SetHand(TwoPair, h);
		HandScore hs = new HandScore();

		boolean bHandActualTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedHandTwoPair = true;

		// Did this evaluate to a two pair?
		assertEquals(bHandActualTwoPair, bExpectedHandTwoPair);
		// Was the pair an Ace?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// Pair has one kicker. Was is a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Pair has one kicker. Was it a ACE?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
	}

	@Test
	public void TestTwoPair1() {

		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.SPADES, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		TwoPair.add(new Card(eSuit.SPADES, eRank.THREE, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));

		Hand h = new Hand();
		h = SetHand(TwoPair, h);
		HandScore hs = new HandScore();

		boolean bHandActualTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedHandTwoPair = true;

		// Checks for two pair?
		assertEquals(bHandActualTwoPair, bExpectedHandTwoPair);
		// High Card
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// Pair has one kicker. Was is a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Pair has one kicker. Was it a ACE?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.TWO);
	}

	@Test
	public void TestTwoPair2() {

		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.SPADES, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.FOUR, 0));
		TwoPair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		TwoPair.add(new Card(eSuit.SPADES, eRank.THREE, 0));

		Hand h = new Hand();
		h = SetHand(TwoPair, h);
		HandScore hs = new HandScore();

		boolean bHandActualTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedHandTwoPair = true;

		// Check for two pair?
		assertEquals(bHandActualTwoPair, bExpectedHandTwoPair);
		// Check high?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// kicker - Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kicker - Four?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.FOUR);
	}

	@Test
	public void TestPair() {

		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedHandPair = true;

		// Pair?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// High Card
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Queen?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.QUEEN);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Jack?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestPair1() {

		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedHandPair = true;

		// Pair?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// High Card?
		assertEquals(hs.getHiHand(), eRank.KING.getiRankNbr());
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Ace?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Queen?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.QUEEN);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Jack?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestPair2() {

		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedHandPair = true;

		// Pair?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// high card?
		assertEquals(hs.getHiHand(), eRank.QUEEN.getiRankNbr());
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Ace?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - King?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Jack?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.JACK);

	}

	@Test
	public void TestPair3() {

		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();

		boolean bActualHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedHandPair = true;

		// Pair?
		assertEquals(bActualHandPair, bExpectedHandPair);
		// high card?
		assertEquals(hs.getHiHand(), eRank.JACK.getiRankNbr());
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Ace?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - King?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		// kickers - Club?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// kickers - Queen?
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.QUEEN);

	}

}
