package de.timfreiheit.plugin.buildtypelaunchericon

import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageProcessor {

    companion object {

        fun process(input: File, density: Density, buildType: String, output: File) {

            val originalLauncherIcon = ImageIO.read(input);

            val result = BufferedImage(originalLauncherIcon.width, originalLauncherIcon.height, BufferedImage.TYPE_INT_ARGB);
            val graphics2D = result.createGraphics();

            // draw original icon with clip
            val rect = RoundRectangle2D.Float();
            val arc = (24 * density.multiplier)
            rect.setRoundRect(0f, 0f, (result.width).toFloat(),(result.height).toFloat(), arc, arc)
            graphics2D.clip = rect;
            graphics2D.drawImage(originalLauncherIcon, null, 0, 0)
            graphics2D.clip = null

            val overlayImage = getOverlayImage(density, buildType);
            graphics2D.drawImage(overlayImage, null, 0, 0)

            ImageIO.write(result, "PNG", output);
        }

        fun getOverlayImage(density: Density, buildType: String): BufferedImage {
            val path = "res/$buildType/ic_launcher_${density.typeName}.png";
            return ImageIO.read(getResourceStream(path));
        }
    }
}