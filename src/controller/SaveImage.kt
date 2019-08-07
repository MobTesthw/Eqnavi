package controller

import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

abstract class SaveImage {
    companion object{
        fun toDefault(image:WritableImage):String {
            val path = "D:\\1\\JavaFx\\"
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("YYYY_MM_DD__HH_mm_ss")
            val fileName = sdf.format(cal.time)

            val name = path + fileName.replace("\\.[a-zA-Z]{3,4}", "")
            val file = File("$name.png")

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file)
            } catch (exception: IOException) {
                // handle exception here
            }
            return name
        }
    }

}