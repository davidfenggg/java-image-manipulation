public class AdvancedManipulations {

  /**
     * Change the contrast of a picture.
     *
     * <p>Your job is to change the intensity of the colors in the picture. The
     * simplest method of changing contrast is as follows:
     *
     * <p>1. Find the average color intensity of the picture. a) Sum the values of all
     * the color components for each pixel. b) Divide the total by the number of
     * pixels times the number of components (3). 2. Subtract the average color
     * intensity from each color component of each pixel. Note that you could
     * underflow into negatives. This will make the average color intensity zero. 3.
     * Scale the intensity of each pixel's color components by multiplying them by
     * the "multiplier" parameter. Note that the multiplier is a double (a decimal
     * value like 1.2 or 0.6) and color values are ints between 0 and 255. 4. Add
     * the original average color intensity back to each component of each pixel. 5.
     * Clip the color values so that all color component values are between 0 and
     * 255. (This should be handled by the Pixel class anyway!)
     *
     * <p>Hint: You should use Math.round() before casting to an int for the average
     * color intensity and for the scaled RGB values. (I.e., in particular, the
     * average should be rounded to an int before being used for further
     * calculations..)
     *
     * @param pic        the original picture
     * @param multiplier the factor by which each color component of each pixel
     *                   should be scaled
     * @return the new adjusted picture
     * 
     */
    public static PixelPicture adjustContrast(PixelPicture pic, double multiplier) {

        int w = pic.getWidth();
        int h = pic.getHeight();

        // step 1 (average rgb with doubles)
        Pixel[][] bmp = pic.getBitmap();
        int total = 0;
        for (int col = 0; col < w; col++) {
            for (int row = 0; row < h; row++) {
                Pixel p = bmp[row][col];
                total = total + p.getBlue() + p.getGreen() + p.getRed();
            }
        }
        double dtot = (double) total;
        double dim3 = (double) (w * h * 3);
        double davg = dtot / dim3;
        int avg = (int) Math.round(davg);

        // get bitmap and iterate through row/col
        for (int col = 0; col < w; col++) {
            for (int row = 0; row < h; row++) {
                Pixel p = bmp[row][col];

                // get initial rgb
                int r = p.getRed();
                int g = p.getGreen();
                int b = p.getBlue();

                // step 2 (subtract avg)
                int subr = r - avg;
                int subg = g - avg;
                int subb = b - avg;

        // step 3 (scale/multiply)
                double multr = ((double) subr) * multiplier;
                double multg = ((double) subg) * multiplier;
                double multb = ((double) subb) * multiplier;
                int mr = (int) Math.round(multr);
                int mg = (int) Math.round(multg);
                int mb = (int) Math.round(multb);
                bmp[row][col] = new Pixel(mr + avg, mg + avg, mb + avg);
            }
        }
        return new PixelPicture(bmp);
    }

    /**
     * Reduce a picture to its most common colors.
     *
     * <p>You will need to make use of the ColorMap class to generate a map from Pixels
     * of a certain color to the frequency with which pixels of that color appear in
     * the image. If you go to the ColorMap class, you will notice that it does not
     * have an explicitly declared constructor. In those cases, Java provides a
     * default constructor, which you can call with no arguments
     * 
     * <p>ColorMap m = new ColorMap();
     * 
     * <p>Once you have generated your ColorMap, select your palette by retrieving the
     * pixels whose color appears in the picture with the highest frequency. Then
     * change each pixel in the picture to one with the closest matching color from
     * your palette.
     *
     * <p>Note that if there are two different colors that are the *same* minimal
     * distance from the given color, your code should select the most frequently
     * appearing one as the new color for the pixel. If both colors appear with the
     * same frequency, your code should select the one that appears *first* in the
     * output of the colormap's getSortedPixels.
     *
     * <p>Algorithms like this are widely used in image compression. GIFs in particular
     * compress the palette to no more than 255 colors. The variant we have
     * implemented here is a weak one, since it only counts color frequency by exact
     * match. Advanced palette reduction algorithms (known as "indexing" algorithms)
     * calculate color regions and distribute the palette over the regions. For
     * example, if our picture had a lot of shades of blue and a little bit of red,
     * our algorithm would likely choose a palette of all blue colors. An advanced
     * algorithm would recognize that blues look similar and distribute the palette
     * so that it would be possible to display red as well.
     *
     * @param pic       the original picture
     * @param numColors the maximum number of colors that can be used in the reduced
     *                  picture
     * @return the new reduced picture
     */
    public static PixelPicture reducePalette(PixelPicture pic, int numColors) {

        // off case
        if (numColors <= 0) {
            return pic;
        }

        // generate ColorMap
        ColorMap m = new ColorMap();
        Pixel[][] bmp = pic.getBitmap();
        int w = pic.getWidth();
        int h = pic.getHeight();

        // insert Pixels into ColorMap
        for (int col = 0; col < w; col++) {
            for (int row = 0; row < h; row++) {
                Pixel p = bmp[row][col];
                int v;
                if (!m.contains(p)) {
                    v = 0;
                } else {
                    v = m.getValue(p);
                }
                m.put(p, v + 1);
            }
        }

        // create palette
        Pixel[] colors = m.getSortedPixels();
        Pixel[] finalColors = new Pixel[numColors];
        for (int i = 0; i < numColors; i++) {
            finalColors[i] = colors[i];
        }

        // change colors
        for (int col = 0; col < w; col++) {
            for (int row = 0; row < h; row++) {
                Pixel p = bmp[row][col];
                Pixel closest = finalColors[0];
                for (int i = 0; i < numColors; i++) {
                    if (p.distance(finalColors[i]) < p.distance(closest)) {
                        closest = finalColors[i];
                    }
                }
                bmp[row][col] = closest;
            }
        }
    
        return new PixelPicture(bmp);
    }

    /**
     * Blur an image.
     *
     * <p>There are different blurring algorithms, but we'll use the simplest: box
     * blur. Box blurring works by averaging the box-shaped neighborhood around a
     * pixel. The size of the box is configurable by setting the radius, half the
     * length of a side of the box. In the simplest case - a radius of 1 - blurring
     * just takes the average around a pixel. For example, to blur around the pixel
     * at (1,1) with radius 1, we take the average value of the pixels of its
     * neighborhood: (0,0) though (2,2), including (1,1).
     *
     * <p>NOTE: when blurring a pixel, make sure that you access the pixels in its
     * neighborhood in the original image. You shouldn't use already "blurred"
     * pixels to calculate the blur.
     *
     * <p>Each Pixel is composed of 3 colors (red, green, and blue, or RGB) called
     * components. Each component should be averaged separately. For a radius R, we
     * set component c at location (x,y) according to the formula below:
     *
     * <p>c = sum(c of each pixel within radius R) / ((2R + 1) * (2R + 1))
     *
     * <p>Logically, it makes sense that this would be the formula for the average. We
     * sum the amount of each color in neighboring pixels and then divide by the
     * number of pixels we considered in the neighborhood.
     *
     * <p>Note that this equation disregards corner cases. When blurring (0,0) with
     * radius 1, we only need to consider the top-left corner, (0,0) through (1,1) -
     * we'll divide by 4 at the end, not 9. You'll have to be careful to only access
     * bitmaps inside of their bounds.
     *
     * <p>You can assume that you will not be given a radius less than 1.
     *
     * <p>Note: In computing the average, you should be doing double division. Use
     * Math.round() before casting to an int when computing the average. If you do
     * not follow these specifications, the values you compute will not pass our
     * tests.
     *
     * @param pic    The picture to be blurred.
     * @param radius The radius of the blurring box.
     * @return A blurred version of the original picture.
     */
    public static PixelPicture blur(PixelPicture pic, int radius) {

        Pixel[][] bmp = pic.getBitmap();
        int w = pic.getWidth();
        int h = pic.getHeight();
        Pixel[][] bmp2 = new Pixel[h][w];

        for (int col = 0; col < w; col++) {
            for (int row = 0; row < h; row++) {
                double numPixels = 0.0;
                double r = 0.0;
                double g = 0.0;
                double b = 0.0;
                for (int col2 = col - radius; col2 < col + radius + 1; col2++) {
                    for (int row2 = row - radius; row2 < row + radius + 1; row2++) {
                        if (col2 >= 0 && row2 >= 0 && col2 < w && row2 < h) {
                            numPixels += 1.0;
                            Pixel p = bmp[row2][col2];
                            r += (double) p.getRed();
                            g += (double) p.getGreen();
                            b += (double) p.getBlue();
                        }
                    }
                }
                int ravg = (int) Math.round(r / numPixels);
                int gavg = (int) Math.round(g / numPixels);
                int bavg = (int) Math.round(b / numPixels);
                bmp2[row][col] = new Pixel(ravg, gavg, bavg);
            }
        }

        return new PixelPicture(bmp2);
    }

    // You may want to add a static helper function here to help find the
    // average color around the pixel you are blurring.

    /**
     * Challenge Problem (this problem is worth 0 points): Flood pixels of the same
     * color with a different color.
     *
     * The name is short for flood fill, which is the familiar "paint bucket"
     * operation in graphics programs. In a paint program, the user clicks on a
     * point in the image. Every neighboring, similarly-colored point is then
     * "flooded" with the color the user selected.
     *
     * Suppose we want to flood color at (x,y). The simplest way to do flood fill is
     * as follows:
     *
     * 1. Let target be the color at (x,y). 2. Create a set of points Q containing
     * just the point (x,y). 3. Take the first point p out of Q. 4. Set the color at
     * p to color. 5. For each of p's non-diagonal neighbors - up, down, left, and
     * right - check to see if they have the same color as target. If they do, add
     * them to Q. 6. If Q is empty, stop. Otherwise, go to 3.
     *
     * This is a naive algorithm that can be made significantly faster if you wish
     * to try.
     *
     * For Q, you should use the provided IntQueue class. It works very much like
     * the queues we implemented in OCaml.
     *
     * @param pic The original picture to be flooded.
     * @param c   The pixel the user "clicked" (representing the color that should
     *            be flooded).
     * @param row The row of the point on which the user "clicked."
     * @param col The column of the point on which the user "clicked."
     * @return A new picture with the appropriate region flooded.
     */
    public static PixelPicture flood(PixelPicture pic, Pixel c, int row, int col) {
        return pic;
    }

}
