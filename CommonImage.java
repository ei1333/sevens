
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CommonImage
{
    private Image[][] img = new Image[6][13];

    public CommonImage() throws IOException
    {
        for(int i = 0 ; i < 4 ; i++ ){
            for(int j = 0 ; j < 13 ; j++ ){
                img[i][j] = ImageIO.read(getClass().getResourceAsStream(String.format("image/%d.png",i + j * 4 + 1)));
            }
        }
        img[4][0] = ImageIO.read(getClass().getResourceAsStream("image/53.png"));
        img[4][1] = ImageIO.read(getClass().getResourceAsStream("image/b1fv.png"));
        img[4][2] = ImageIO.read(getClass().getResourceAsStream("image/b1fh.png"));


        img[4][3] = ImageIO.read(getClass().getResourceAsStream("image/button0.png")); //Start
        img[4][4] = ImageIO.read(getClass().getResourceAsStream("image/button0b.png"));


        img[4][5] = ImageIO.read(getClass().getResourceAsStream("image/button1.png"));
        img[4][6] = ImageIO.read(getClass().getResourceAsStream("image/button2.png"));
        img[4][7] = ImageIO.read(getClass().getResourceAsStream("image/button3.png"));
        img[4][8] = ImageIO.read(getClass().getResourceAsStream("image/button4.png"));
        img[4][9] = ImageIO.read(getClass().getResourceAsStream("image/button1b.png"));
        img[4][10] = ImageIO.read(getClass().getResourceAsStream("image/button2b.png"));
        img[4][11] = ImageIO.read(getClass().getResourceAsStream("image/button3b.png"));

        img[4][12] = ImageIO.read(getClass().getResourceAsStream("image/put.gif"));

        img[5][0] = ImageIO.read(getClass().getResourceAsStream("image/itii.png"));
        img[5][1] = ImageIO.read(getClass().getResourceAsStream("image/nii.png"));
        img[5][2] = ImageIO.read(getClass().getResourceAsStream("image/sani.png"));
        img[5][3] = ImageIO.read(getClass().getResourceAsStream("image/biri.png"));

        img[5][4] = ImageIO.read(getClass().getResourceAsStream("image/one.png"));
        img[5][5] = ImageIO.read(getClass().getResourceAsStream("image/two.png"));
        img[5][6] = ImageIO.read(getClass().getResourceAsStream("image/three.png"));
        img[5][7] = ImageIO.read(getClass().getResourceAsStream("image/lose.png"));

        img[5][9] = ImageIO.read(getClass().getResourceAsStream("image/ps0.png"));
        img[5][10] = ImageIO.read(getClass().getResourceAsStream("image/ps1.png"));
        img[5][11] = ImageIO.read(getClass().getResourceAsStream("image/ps2.png"));
        img[5][12] = ImageIO.read(getClass().getResourceAsStream("image/ps3.png"));

    }

	public Image getImg(int y, int x)
	{
		return img[y][x];
	}
}
