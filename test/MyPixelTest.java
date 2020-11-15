import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Use this file to test your implementation of Pixel.
 * 
 * We will manually grade this file to give you feedback about the completeness
 * of your test cases.
 */

public class MyPixelTest {

    /*
     * Remember, UNIT tests should ideally have one point of failure. Below we give
     * you an example of a unit test for the Pixel constructor that takes in three
     * ints as arguments. We use the getRed(), getGreen(), and getBlue() methods to
     * check that the values were set correctly. This one test does not
     * comprehensively test all of Pixel so you must add your own.
     *
     * You might want to look into assertEquals, assertTrue, assertFalse, and
     * assertArrayEquals at the following:
     * http://junit.sourceforge.net/javadoc/org/junit/Assert.html
     *
     * Note, if you want to add global variables so that you can reuse Pixels in
     * multiple tests, feel free to do so.
     */

    @Test
    public void testConstructInBounds() {
        Pixel p = new Pixel(40, 50, 60);
        assertEquals(40, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(60, p.getBlue());
    }

    /* ADD YOUR OWN TESTS BELOW */

    /**
     * Constructor tests
     */
    @Test
    public void testArrayParam() {
        Pixel p = new Pixel(new int[] { 10, 20, 30 });
        assertEquals(10, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(30, p.getBlue());
    }

    @Test
    public void testNegativeValueArray() {
        Pixel p = new Pixel(new int[] { -10, 20, 30 });
        assertEquals(0, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(30, p.getBlue());
    }

    @Test
    public void testNegativeValueInts() {
        Pixel p = new Pixel(-10, 20, 30);
        assertEquals(0, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(30, p.getBlue());
    }

    @Test
    public void testHighValueArray() {
        Pixel p = new Pixel(new int[] { 10, 20, 400 });
        assertEquals(10, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(255, p.getBlue());
    }

    @Test
    public void testHighValueInts() {
        Pixel p = new Pixel(10, 20, 400);
        assertEquals(10, p.getRed());
        assertEquals(20, p.getGreen());
        assertEquals(255, p.getBlue());
    }

    @Test
    public void testNullArray() {
        Pixel p = new Pixel(null);
        assertEquals(0, p.getRed());
        assertEquals(0, p.getGreen());
        assertEquals(0, p.getBlue());
    }

    @Test
    public void testZeroArray() {
        Pixel p = new Pixel(new int[] {});
        assertEquals(0, p.getRed());
        assertEquals(0, p.getGreen());
        assertEquals(0, p.getBlue());
    }

    @Test
    public void testSmallArray() {
        Pixel p = new Pixel(new int[] { 10, 30 });
        assertEquals(10, p.getRed());
        assertEquals(30, p.getGreen());
        assertEquals(0, p.getBlue());
    }

    @Test
    public void testSmallerArray() {
        Pixel p = new Pixel(new int[] { 10 });
        assertEquals(10, p.getRed());
        assertEquals(0, p.getGreen());
        assertEquals(0, p.getBlue());
    }

    /**
     * getComponents Tests
     */

    @Test
    public void testGetComponentsEncap() {
        Pixel p = new Pixel(new int[] { 40, 20, 50 });
        int[] rgb = p.getComponents();
        rgb[0] = 0;
        assertNotEquals(rgb[0], p.getRed());
    }

    @Test
    public void testGetComponentsVals() {
        Pixel p = new Pixel(new int[] { 40, 20, 50 });
        int[] rgb = p.getComponents();
        assertEquals(rgb[0], p.getRed());
        assertEquals(rgb[1], p.getGreen());
        assertEquals(rgb[2], p.getBlue());
    }

    /**
     * distance Tests
     */

    @Test
    public void testDistanceNull() {
        Pixel p = new Pixel(new int[] { 40, 20, 50 });
        assertEquals(-1, p.distance(null));
    }

    @Test
    public void testDistance() {
        Pixel p = new Pixel(new int[] { 0, 0, 0 });
        Pixel p2 = new Pixel(new int[] { 40, 20, 50 });
        assertEquals(110, p.distance(p2));
    }

    @Test
    public void testSameDistance() {
        Pixel p = new Pixel(new int[] { 40, 20, 50 });
        Pixel p2 = new Pixel(new int[] { 40, 20, 50 });
        assertEquals(0, p.distance(p2));
    }

    /**
     * toString Tests
     */

    @Test
    public void toStringTest() {
        Pixel p = new Pixel(new int[] { 20, 50, 110 });
        assertEquals("(20, 50, 110)", p.toString());
    }

    /**
     * equals Tests
     */

    @Test
    public void notEquals() {
        Pixel p = new Pixel(new int[] { 0, 0, 0 });
        Pixel p2 = new Pixel(new int[] { 40, 20, 50 });
        assertFalse(p.equals(p2));
    }

    @Test
    public void equalsTrue() {
        Pixel p = new Pixel(new int[] { 40, 20, 50 });
        Pixel p2 = new Pixel(new int[] { 40, 20, 50 });
        assertTrue(p.equals(p2));
    }

    @Test
    public void notEqualsOffOne() {
        Pixel p = new Pixel(new int[] { 40, 21, 50 });
        Pixel p2 = new Pixel(new int[] { 40, 20, 50 });
        assertFalse(p.equals(p2));
    }
}
