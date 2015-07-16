/**
 * Copyright @ 2010 Quan Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sourceforge.tess4j;

import net.sourceforge.vietocr.ImageHelper;
import net.sourceforge.vietocr.ImageIOHelper;
import com.recognition.software.jdeskew.ImageDeskew;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.imageio.IIOImage;
import org.junit.*;
import static org.junit.Assert.*;

public class TesseractTest {
    static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
    Tesseract instance;

    public TesseractTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = Tesseract.getInstance();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doOCR method, of class Tesseract.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_File() throws Exception {
        System.out.println("doOCR on a PNG image");
        File imageFile = new File("eurotext.png");
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String result = instance.doOCR(imageFile);
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }

    /**
     * Test of doOCR method, of class Tesseract.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_File_With_Configs() throws Exception {
        System.out.println("doOCR with \"digits\" configs");
        File imageFile = new File("eurotext.png");
        String expResult = "[-0123456789.\n ]+";
        List<String> configs = Arrays.asList("digits");
        instance.setConfigs(configs);
        String result = instance.doOCR(imageFile);
        System.out.println(result);
        assertTrue(result.matches(expResult));
        instance.setConfigs(null); // since Tesseract instance is a singleton, clear configs so the effects do not carry on into subsequent runs.
    }

    /**
     * Test of doOCR method, of class Tesseract.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_File_Rectangle() throws Exception {
        System.out.println("doOCR on a BMP image with bounding rectangle");
        File imageFile = new File("eurotext.bmp");
        Rectangle rect = new Rectangle(0, 0, 1024, 800); // define an equal or smaller region of interest on the image
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String result = instance.doOCR(imageFile, rect);
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }

    /**
     * Test of doOCR method, of class Tesseract.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_List_Rectangle() throws Exception {
        System.out.println("doOCR on a PDF document");
        File imageFile = new File("eurotext.pdf");
        List<IIOImage> imageList = ImageIOHelper.getIIOImageList(imageFile);
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String result = instance.doOCR(imageList, null);
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }

    /**
     * Test of doOCR method, of class Tesseract.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_BufferedImage() throws Exception {
        System.out.println("doOCR on a buffered image of a GIF");
        File imageFile = new File("eurotext.gif");
        BufferedImage bi = ImageIO.read(imageFile);
        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String result = instance.doOCR(bi);
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }

    /**
     * Test of deskew algorithm.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoOCR_SkewedImage() throws Exception {
        System.out.println("doOCR on a skewed PNG image");
        File imageFile = new File("eurotext_deskew.png");
        BufferedImage bi = ImageIO.read(imageFile);
        ImageDeskew id = new ImageDeskew(bi);
        double imageSkewAngle = id.getSkewAngle(); // determine skew angle
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        }

        String expResult = "The (quick) [brown] {fox} jumps!\nOver the $43,456.78 <lazy> #90 dog";
        String result = instance.doOCR(bi);
        System.out.println(result);
        assertEquals(expResult, result.substring(0, expResult.length()));
    }
}