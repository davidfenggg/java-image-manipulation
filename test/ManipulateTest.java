import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


/* DO NOT EDIT this file, we will not see your changes. 
 * Add your own JUnit test cases to ImageTest.java */




/**
 * Tests some image manipulations.
 * 
 * Note that these tests are not comprehensive. Passing these tests is not
 * enough to consider this assignment complete. Be sure to test with the GUI
 * and write some of your own tests in PennstagramTest.
 */
public class ManipulateTest {

    /*
     * Methods to generate test cases.
     */

    public static PixelPicture smallSquare() {
        return new PixelPicture(new Pixel[][] {
            {Pixel.BLACK, Pixel.BLUE},
            {Pixel.RED,   Pixel.GREEN}
        });
    }

    public static PixelPicture smallSquareBorder() { 
        return new PixelPicture(new Pixel[][] {
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE},
            {Pixel.WHITE, Pixel.BLACK, Pixel.BLUE,  Pixel.WHITE},
            {Pixel.WHITE, Pixel.RED,   Pixel.GREEN, Pixel.WHITE}, 
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE}
        });
    }

    public static PixelPicture threeStripes(
            int r0, int g0, int b0,
            int r1, int g1, int b1,
            int r2, int g2, int b2) {
        Pixel [][] bmp = new Pixel[256][256];
        for (int row = 0; row < 256; row++) {
            for (int col = 0; col < 256; col++) {
                if (col > 200) {
                    bmp[row][col] = new Pixel(r0,g0,b0);
                } else if (col > 100) {
                    bmp[row][col] = new Pixel(r1,g1,b1);
                } else {
                    bmp[row][col] = new Pixel(r2,g2,b2);
                }
            }
        }
        return new PixelPicture(bmp);
    }

    public static PixelPicture testNewPic() {
        Pixel [][] bmp = new Pixel[256][256];
        for (int row = 0; row < 256; row++) {
            for (int col = 0; col < 256; col++) {
                bmp[row][col] = new Pixel(row, row + col, row ^ col);
            }
        }
        return new PixelPicture(bmp);
    }


    public static PixelPicture testNewPicCCW() {
        Pixel [][] bmp = new Pixel[256][256];
        for (int row = 0; row < 256; row++) {
            for (int col = 0; col < 256; col++) {
                bmp[255 - col][row] = new Pixel(row, row + col, row ^ col);
            }
        }
        return new PixelPicture(bmp);
    }


    public static PixelPicture doubleSmallSquareBorder() {
        return new PixelPicture(new Pixel[][] {
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE},
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE},
            {Pixel.WHITE, Pixel.WHITE, Pixel.BLACK, Pixel.BLUE,  Pixel.WHITE, Pixel.WHITE},
            {Pixel.WHITE, Pixel.WHITE, Pixel.RED,   Pixel.GREEN, Pixel.WHITE, Pixel.WHITE}, 
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE},
            {Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE, Pixel.WHITE}
        });
    }

    /* ------ CW rotation, provided code ------- */

    @Test 
    public void rotateCwSmall() {
        assertEquals(
                0,
                PixelPicture.diff(
                        new PixelPicture(new Pixel[][] {
                            {Pixel.RED, Pixel.BLACK},
                            {Pixel.GREEN,Pixel.BLUE}
                        }),
                        SimpleManipulations.rotateCW(smallSquare())
                ),
                "rotateCW 2x2 image"
        );
    }

    @Test 
    public void borderSmall() {
        PixelPicture p = SimpleManipulations.border(smallSquare(), 1, Pixel.WHITE);
        assertEquals(
                0,
                PixelPicture.diff(p, smallSquareBorder()),
                "Pixel.WHITE Border on Small image"
        );
    }

    @Test 
    public void luminositySmall() {
        assertTrue(
                9 >= //Allow off by 1 for each non-black color component         
                PixelPicture.diff(
                        new PixelPicture(new Pixel[][] {
                            {Pixel.BLACK,    new Pixel(29,29,29)},
                            {new Pixel(76,76,76), new Pixel(149,149,149)}
                        }),
                        SimpleManipulations.grayScaleLuminosity(smallSquare())
                ),
                "GrayScaleLuminosity on 2x2 image"
        );

    }

    @Test 
    public void averageSmall() {
        assertTrue(
                9 >= //Allow off by 1
                PixelPicture.diff(
                        new PixelPicture(new Pixel[][] {
                            {new Pixel(0,0,0),    new Pixel(85,85,85)},
                            {new Pixel(85,85,85),new Pixel(85,85,85)}
                        }),
                        SimpleManipulations.grayScaleAverage(smallSquare())
                ),
                "GrayScaleAverage on 2x2 image"
        );
    }

    @Test 
    public void colorInvertSmall() {
        assertTrue(
                9 >= //Allow off by 1
                PixelPicture.diff(new PixelPicture(new Pixel[][] {
                        {new Pixel(255,255,255), new Pixel(255,255,0)},
                        {new Pixel(0,255,255),new Pixel(255,0,255)}
                    }),
                    SimpleManipulations.invertColors(smallSquare())
                ),
                "ColorInvert on 2x2 image"
        );
    }

    @Test 
    public void colorScaleRed() {
        assertEquals(
                0, 
                PixelPicture.diff(new PixelPicture(new Pixel[][] {
                    {Pixel.BLACK, Pixel.BLACK},
                    {Pixel.RED, Pixel.BLACK}
                }),
                SimpleManipulations.scaleColors(smallSquare(),1.0,0.0,0.0)),
                "Only keep red component of each pixel"
        );    
    }

    /*  ---------------- alpha-Blend -------------------- */

    @Test 
    public void blendSmall() {
        assertTrue(
                3 >=
                PixelPicture.diff(new PixelPicture(new Pixel [][] {
                        {Pixel.BLACK, new Pixel(0,0,128)},
                        {new Pixel(128,0,0), new Pixel(0,128,0)}
                    }),
                    SimpleManipulations.alphaBlend(0.5, smallSquare(), 
                        new PixelPicture(new Pixel[][] {
                            { Pixel.BLACK, Pixel.BLACK },
                            { Pixel.BLACK, Pixel.BLACK } 
                        }
                    ))
                ),
                "blend on 2x2 with black"
        );
    }

    /*  ---------------- Contrast -------------------- */

    @Test 
    public void contrastSmall() {
        assertTrue(
                36 >= // Lots of rounding error...
                PixelPicture.diff(
                        new PixelPicture(new Pixel [][] {
                            {new Pixel(32,32,32),  new Pixel(32,32,160)},
                            {new Pixel(160,32,32), new Pixel(32,160,32)}
                        }),
                        AdvancedManipulations.adjustContrast(smallSquare(), 0.5)
                ),
                "contrast on 2x2"
        );
    }

    /*  ---------------- ReducePalette -------------------- */



    @Test 
    public void paletteTrivial() {
        PixelPicture s = threeStripes(10,10,10,255,255,255,0,0,0);
        assertEquals(
                0,
                PixelPicture.diff(
                        threeStripes(10,10,10,255,255,255,0,0,0),
                        AdvancedManipulations.reducePalette(s, 3)
                ),
                "same size palette"
        );
    }

    @Test public void paletteInteresting() {
        PixelPicture s = threeStripes(0,0,0,255,255,255,10,10,10);
        assertEquals(
                0,
                PixelPicture.diff(
                        threeStripes(10,10,10,255,255,255,10,10,10),
                        AdvancedManipulations.reducePalette(s, 2)
                ),
                "3 to 2 palette"
        );
    }




    /*--------------------Blur-------------------*/

    @Test 
    public void blurSmall() {
        assertTrue(
                12 >= //Off by one okay 
                PixelPicture.diff(
                        new PixelPicture(new Pixel[][] {
                            {new Pixel(63,63,63), new Pixel(63,63,63)},
                            {new Pixel(63,63,63),new Pixel(63,63,63)}
                        }),
                        AdvancedManipulations.blur(smallSquare(),1)
                ),
                "Small Blur"
        );
    }
}
