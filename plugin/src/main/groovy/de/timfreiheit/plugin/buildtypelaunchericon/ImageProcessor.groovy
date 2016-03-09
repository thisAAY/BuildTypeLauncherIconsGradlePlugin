package de.timfreiheit.plugin.buildtypelaunchericon

import javax.imageio.ImageIO
import java.awt.*
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage

class ImageProcessor {

    public static void process(File input, Density density, String buildType,  File output) {

        BufferedImage originalLauncherIcon = ImageIO.read(input);

        BufferedImage result = new BufferedImage(originalLauncherIcon.getWidth(), originalLauncherIcon.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = result.createGraphics();

        // draw original icon with clip
        RoundRectangle2D rect = new RoundRectangle2D.Float();
        int arc = (int) (24 * density.multiplier)
        rect.setRoundRect(0,0,  (int) (result.getWidth()), (int) (result.getHeight()), arc, arc)
        graphics2D.setClip(rect);
        graphics2D.drawImage(originalLauncherIcon, null , 0, 0)
        graphics2D.setClip(null)

        BufferedImage overlayImage = getOverlayImage(density, buildType)
        graphics2D.drawImage(overlayImage, null , 0, 0)

        ImageIO.write(result, "PNG", output);
    }

    public static BufferedImage getOverlayImage(Density density, String buildType) {
        String path = "res/$buildType/ic_launcher_$density.typeName" + ".png";
        return ImageIO.read(BuildTypeLauncherIconTask.getResourceStream(path));
    }

}